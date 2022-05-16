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

package com.github.mixinors.astromine.common.network.type;

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
public sealed interface TransferNetworkType<T> extends NetworkType<Storage<T>> permits FluidNetworkType, ItemNetworkType {
	default void move(Network<?> network, Long2ObjectSortedMap<Storage<T>> extractableStorages, Long2ObjectSortedMap<Storage<T>> insertableStorages) {
		record TransactionPair<T>(
				long maxAmount,
				long posLong,
				StorageView<T> our,
				Storage<T> their
		) {}
		
		var worldTickCount = network.getWorld().getServer().getTicks();
		
		try (var transaction = Transaction.openOuter()) {
			for (var extractableStorage : extractableStorages.values()) {
				var transacted = 0L;
				
				for (var extractableView : extractableStorage.iterable(transaction)) {
					var list = new ArrayList<TransactionPair<T>>();
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
						var posLong = insertableStorageEntry.getLongKey();
						
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
						
						list.add(new TransactionPair<>(availableToMove, posLong, extractableView, insertableStorage));
					}
					
					if (offering != 0 && !list.isEmpty()) {
						var needsRoundRobin = offering != 0 && (getTransferRate() - transacted) / (requesting / offering) <= 0;
						Iterable<TransactionPair<T>> iterable;
						
						if (needsRoundRobin) {
							list.sort(Comparator.comparingLong(TransactionPair::posLong));
							var skip = worldTickCount % list.size();
							
							iterable = () -> new AbstractIterator<>() {
								int cursor = 0;
								
								@Nullable
								@Override
								protected TransactionPair<T> computeNext() {
									var index = cursor++ + skip;
									
									if (cursor > list.size()) {
										return endOfData();
									} else {
										return list.get(index % list.size());
									}
								}
							};
						} else {
							list.sort(Comparator.comparingLong(TransactionPair::maxAmount));
							
							iterable = list;
						}
						
						for (var pair : iterable) {
							var move = (long) Math.ceil(pair.maxAmount * MathHelper.clamp(requesting <= 0 ? 0.0 : (double) offering / requesting, 0.0, 1.0));
							
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
			return 0;
		}
		
		StoragePreconditions.notNegative(maxAmount);
		
		// Simulate extraction first.
		long maxExtracted;
		
		try (Transaction extractionTestTransaction = Transaction.openNested(transaction)) {
			maxExtracted = from.extract(resource, maxAmount, extractionTestTransaction);
		}
		
		try (Transaction moveTransaction = Transaction.openNested(transaction)) {
			// Then insert what can be extracted.
			long accepted = to.insert(resource, maxExtracted, moveTransaction);
			
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
