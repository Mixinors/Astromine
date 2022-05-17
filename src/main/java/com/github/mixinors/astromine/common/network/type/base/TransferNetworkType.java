/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.mixinors.astromine.common.network.type.base;

import com.github.mixinors.astromine.common.network.Network;
import com.google.common.collect.AbstractIterator;
import it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;

@SuppressWarnings("UnstableApiUsage")
public abstract class TransferNetworkType<T> extends NetworkType<Storage<T>> {
	protected void move(Network<?> network, Long2ObjectSortedMap<Storage<T>> extractableStorages, Long2ObjectSortedMap<Storage<T>> insertableStorages) {
		record TransactionPair<T>(
				long maxAmount,
				long posLong,
				StorageView<T> our,
				Storage<T> their
		) {}
		
		var worldTicks = network.getWorld().getServer().getTicks();
		
		try (var transaction = Transaction.openOuter()) {
			for (var extractableStorage : extractableStorages.values()) {
				var transacted = 0L;
				
				for (var extractableView : extractableStorage.iterable(transaction)) {
					var pairs = new ArrayList<TransactionPair<T>>();
					
					var offering = 0L;
					var requesting = 0L;
					
					if (extractableView.isResourceBlank()) {
						continue;
					}
					var resource = extractableView.getResource();
					
					var availableToExtract = Math.min(getTransferRate() - transacted, extractableView.getAmount());
					var availableToInsert = 0L;
					
					// check how much can be extracted
					try (var extractionTestTransaction = transaction.openNested()) {
						availableToExtract = extractableView.extract(resource, availableToExtract, extractionTestTransaction);
					}
					
					if (availableToExtract <= 0) {
						continue;
					}
					
					for (var insertableStorageEntry : insertableStorages.long2ObjectEntrySet()) {
						var pos = insertableStorageEntry.getLongKey();
						
						var insertableStorage = insertableStorageEntry.getValue();
						
						try (var insertionTestTransaction = transaction.openNested()) {
							availableToInsert = insertableStorage.insert(resource, availableToExtract, insertionTestTransaction);
						}
						
						if (availableToExtract == 0L || availableToInsert == 0L) {
							continue;
						}
						
						var availableToMove = Math.min(availableToExtract, availableToInsert);
						
						offering = Math.min(getTransferRate() - transacted, Math.max(offering, extractableView.getAmount()));
						
						requesting += availableToMove;
						
						pairs.add(new TransactionPair<>(availableToMove, pos, extractableView, insertableStorage));
					}
					
					if (offering != 0 && !pairs.isEmpty()) {
						var needsRoundRobin = offering != 0.0D && (getTransferRate() - transacted) / (requesting / Math.min(1.0D, offering)) <= 0.0D;
						
						Iterable<TransactionPair<T>> iterable;
						
						if (needsRoundRobin) {
							pairs.sort(Comparator.comparingLong(TransactionPair::posLong));
							
							var skip = worldTicks % pairs.size();
							
							iterable = () -> new AbstractIterator<>() {
								int cursor = 0;
								
								@Nullable
								@Override
								protected TransactionPair<T> computeNext() {
									var index = cursor++ + skip;
									
									if (cursor > pairs.size()) {
										return endOfData();
									} else {
										return pairs.get(index % pairs.size());
									}
								}
							};
						} else {
							pairs.sort(Comparator.comparingLong(TransactionPair::maxAmount));
							
							iterable = pairs;
						}
						
						for (var pair : iterable) {
							var move = (long) Math.ceil(pair.maxAmount * MathHelper.clamp(requesting <= 0.0D ? 0.0D : (double) offering / requesting, 0.0D, 1.0D));
							
							var moved = move(resource, pair.our, pair.their, move, transaction);
							
							transacted += moved;
							
							if (transacted >= getTransferRate()) {
								break;
							}
						}
					}
					
					if (transacted >= getTransferRate()) {
						break;
					}
				}
			}
			
			transaction.commit();
		}
	}
	
	private static <T> long move(T resource, @Nullable StorageView<T> from, @Nullable Storage<T> to, long maxAmount, @Nullable TransactionContext transaction) {
		if (from == null || to == null) {
			return 0L;
		}
		
		StoragePreconditions.notNegative(maxAmount);
		
		// Simulate extraction first.
		long maxExtracted;
		
		try (var extractionTestTransaction = Transaction.openNested(transaction)) {
			maxExtracted = from.extract(resource, maxAmount, extractionTestTransaction);
		}
		
		try (var moveTransaction = Transaction.openNested(transaction)) {
			// Then insert what can be extracted.
			var accepted = to.insert(resource, maxExtracted, moveTransaction);
			
			// Extract for real.
			if (from.extract(resource, accepted, moveTransaction) == accepted) {
				// Commit if the amounts match.
				moveTransaction.commit();
				
				return accepted;
			}
		}
		
		return 0;
	}
}
