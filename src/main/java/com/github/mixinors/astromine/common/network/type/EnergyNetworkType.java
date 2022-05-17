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

import com.github.mixinors.astromine.common.config.AMConfig;
import com.github.mixinors.astromine.common.network.Network;
import com.github.mixinors.astromine.common.network.type.base.NetworkType;
import it.unimi.dsi.fastutil.objects.Reference2LongMap;
import it.unimi.dsi.fastutil.objects.Reference2LongOpenHashMap;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.EnergyStorageUtil;

import java.util.ArrayList;
import java.util.Comparator;

@SuppressWarnings("UnstableApiUsage")
public abstract class EnergyNetworkType extends NetworkType<EnergyStorage> {
	@Override
	public EnergyStorage find(World world, BlockPos pos, @Nullable Direction direction) {
		return EnergyStorage.SIDED.find(world, pos, direction);
	}
	
	private void move(Reference2LongMap<EnergyStorage> extractableStorages, Reference2LongMap<EnergyStorage> insertableStorages) {
		record EnergyPair(
				long maxAmount,
				EnergyStorage their
		) {}
		
		try (var transaction = Transaction.openOuter()) {
			for (var extractableEntry : extractableStorages.reference2LongEntrySet()) {
				var extractableStorage = extractableEntry.getKey();
				var extractedAmount = extractableEntry.getLongValue();
				
				var pairs = new ArrayList<EnergyPair>();
				
				var offering = 0L;
				var requesting = 0L;
				
				for (var insertableEntry : insertableStorages.reference2LongEntrySet()) {
					var insertableStorage = insertableEntry.getKey();
					var insertedAmount = insertableEntry.getLongValue();
					
					var availableToExtract = Math.min(extractableStorage.getAmount(), getTransferRate() - extractedAmount);
					var availableToInsert = Math.min(insertableStorage.getCapacity() - insertableStorage.getAmount(), getTransferRate() - insertedAmount);
					
					// Skip if nothing can be extracted or inserted.
					if (availableToExtract == 0L || availableToInsert == 0L) {
						continue;
					}
					
					if (availableToExtract > 0L) {
						try (var extractionTestTransaction = Transaction.openNested(transaction)) {
							availableToExtract = extractableStorage.extract(availableToExtract, extractionTestTransaction);
						}
					}
					
					if (availableToInsert > 0L) {
						try (var insertionTestTransaction = Transaction.openNested(transaction)) {
							availableToInsert = insertableStorage.insert(availableToInsert, insertionTestTransaction);
						}
					}
					
					var availableToMove = Math.min(availableToExtract, availableToInsert);
					
					if (availableToMove > 0L) {
						offering = Math.max(offering, extractableStorage.getAmount());
						
						requesting += availableToMove;
						
						pairs.add(new EnergyPair(availableToMove, insertableStorage));
					}
				}
				
				pairs.sort(Comparator.comparingLong(EnergyPair::maxAmount));
				
				for (var pair : pairs) {
					var move = (long) Math.ceil(pair.maxAmount * MathHelper.clamp(requesting <= 0.0D ? 0.0D : (double) offering / Math.min(1.0D, requesting), 0.0D, 1.0D));
					
					var moved = EnergyStorageUtil.move(extractableStorage, pair.their, move, transaction);
					
					insertableStorages.put(pair.their, insertableStorages.getLong(pair.their) + moved);
					
					extractedAmount += moved;
					
					if (extractedAmount >= getTransferRate()) {
						break;
					}
				}
				
				extractableStorages.put(extractableStorage, extractedAmount);
				
				if (extractedAmount >= getTransferRate()) {
					break;
				}
			}
			
			transaction.commit();
		}
	}
	
	@Override
	public void tick(Network<EnergyStorage> network) {
		var world = network.getWorld();
		
		var extractableStorages = new Reference2LongOpenHashMap<EnergyStorage>();
		var bufferStorages = new Reference2LongOpenHashMap<EnergyStorage>();
		var insertableStorages = new Reference2LongOpenHashMap<EnergyStorage>();

		var toRemove = new ArrayList<Network.Member>();
		
		for (var member : network.getMembers()) {
			var storage = find(world, member.blockPos(), member.direction());
			
			if (storage == null) {
				toRemove.add(member);
				
				world.getBlockState(member.blockPos()).neighborUpdate(world, member.blockPos(), world.getBlockState(member.blockPos()).getBlock(), member.blockPos(), false);
			} else {
				if (storage.supportsInsertion() && storage.supportsExtraction()) {
					bufferStorages.put(storage, 0L);
				} else if (storage.supportsInsertion()) {
					insertableStorages.put(storage, 0L);
				} else if (storage.supportsExtraction()) {
					extractableStorages.put(storage, 0L);
				}
			}
		}
		
		network.getMembers().removeAll(toRemove);
		
		move(extractableStorages, insertableStorages);
		move(extractableStorages, bufferStorages);
		
		// Clean the map as we are now using it to store the extracted
		// amount rather than the inserted amount.
		var cleanBufferStorages = new Reference2LongOpenHashMap<EnergyStorage>();
		
		for (var entry : bufferStorages.reference2LongEntrySet()) {
			cleanBufferStorages.put(entry.getKey(), 0L);
		}
		
		move(cleanBufferStorages, insertableStorages);
	}
	
	@Override
	public boolean hasSiding() {
		return false;
	}
	
	@Override
	public boolean hasFiltering() {
		return false;
	}
	
	public static final class Primitive extends EnergyNetworkType {
		@Override
		public long getTransferRate() {
			return AMConfig.get().networks.primitiveEnergyNetwork.transferRate;
		}
	}
	
	public static final class Basic extends EnergyNetworkType {
		@Override
		public long getTransferRate() {
			return AMConfig.get().networks.basicEnergyNetwork.transferRate;
		}
	}
	
	public static final class Advanced extends EnergyNetworkType {
		@Override
		public long getTransferRate() {
			return AMConfig.get().networks.advancedEnergyNetwork.transferRate;
		}
	}
	
	public static final class Elite extends EnergyNetworkType {
		@Override
		public long getTransferRate() {
			return AMConfig.get().networks.eliteEnergyNetwork.transferRate;
		}
	}
}
