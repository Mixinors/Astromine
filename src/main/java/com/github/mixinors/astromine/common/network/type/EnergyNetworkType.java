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
import com.github.mixinors.astromine.common.transfer.storage.LongEnergyStorage;
import it.unimi.dsi.fastutil.objects.Reference2LongMap;
import it.unimi.dsi.fastutil.objects.Reference2LongOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;

@SuppressWarnings("UnstableApiUsage")
public abstract class EnergyNetworkType extends NetworkType<IEnergyStorage> {
	@Override
	public IEnergyStorage find(Level world, BlockPos pos, @Nullable Direction direction) {
		return world.getCapability(Capabilities.EnergyStorage.BLOCK, pos, direction);
	}
	
	private void move(Reference2LongMap<IEnergyStorage> extractableStorages, Reference2LongMap<IEnergyStorage> insertableStorages) {
		record EnergyPair(
				long maxAmount,
				IEnergyStorage their
		) {}
		
		for (var extractableEntry : extractableStorages.reference2LongEntrySet()) {
			var extractableStorage = extractableEntry.getKey();
			var extractedAmount = extractableEntry.getLongValue();
			
			var pairs = new ArrayList<EnergyPair>();
			
			var offering = 0L;
			var requesting = 0L;
			
			for (var insertableEntry : insertableStorages.reference2LongEntrySet()) {
				var insertableStorage = insertableEntry.getKey();
				var insertedAmount = insertableEntry.getLongValue();
				
				if (extractableStorage == insertableStorage) {
					continue;
				}
				
				var availableToExtract = Math.min(LongEnergyStorage.getAmount(extractableStorage), getTransferRate() - extractedAmount);
				var availableToInsert = Math.min(LongEnergyStorage.getCapacity(insertableStorage) - LongEnergyStorage.getAmount(insertableStorage), getTransferRate() - insertedAmount);
				
				// Skip if nothing can be extracted or inserted.
				if (availableToExtract == 0L || availableToInsert == 0L) {
					continue;
				}
				
				if (availableToExtract > 0L) {
					availableToExtract = LongEnergyStorage.extract(extractableStorage, availableToExtract, true);
				}
				
				if (availableToInsert > 0L) {
					availableToInsert = LongEnergyStorage.insert(insertableStorage, availableToInsert, true);
				}
				
				var availableToMove = Math.min(availableToExtract, availableToInsert);
				
				if (availableToMove > 0L) {
					offering = Math.max(offering, LongEnergyStorage.getAmount(extractableStorage));
					
					requesting += availableToMove;
					
					pairs.add(new EnergyPair(availableToMove, insertableStorage));
				}
			}
			
			pairs.sort(Comparator.comparingLong(EnergyPair::maxAmount));
			
			for (var pair : pairs) {
				var move = (long) Math.ceil(pair.maxAmount * Mth.clamp(requesting <= 0.0D ? 0.0D : (double) offering / requesting, 0.0D, 1.0D));
				
				var moved = LongEnergyStorage.move(extractableStorage, pair.their, move);
				
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
	}
	
	private void moveBetweenBuffers(Reference2LongMap<IEnergyStorage> bufferStorages) {
		var initialAmounts = new Reference2LongOpenHashMap<IEnergyStorage>();
		var extractedAmounts = new Reference2LongOpenHashMap<IEnergyStorage>();
		var insertedAmounts = new Reference2LongOpenHashMap<IEnergyStorage>();
		
		for (var entry : bufferStorages.reference2LongEntrySet()) {
			initialAmounts.put(entry.getKey(), LongEnergyStorage.getAmount(entry.getKey()));
			extractedAmounts.put(entry.getKey(), 0L);
			insertedAmounts.put(entry.getKey(), entry.getLongValue());
		}
		
		for (var sourceEntry : extractedAmounts.reference2LongEntrySet()) {
			var source = sourceEntry.getKey();
			var extractedAmount = sourceEntry.getLongValue();
			
			for (var destinationEntry : insertedAmounts.reference2LongEntrySet()) {
				var destination = destinationEntry.getKey();
				
				if (source == destination) {
					continue;
				}
				
				var sourceAmount = initialAmounts.getLong(source);
				var destinationAmount = initialAmounts.getLong(destination);
				
				if (sourceAmount <= destinationAmount) {
					continue;
				}
				
				var targetMove = Math.max(1L, (sourceAmount - destinationAmount) / 2L);
				var availableToExtract = Math.min(Math.min(targetMove, LongEnergyStorage.getAmount(source)), getTransferRate() - extractedAmount);
				var availableToInsert = Math.min(LongEnergyStorage.getCapacity(destination) - LongEnergyStorage.getAmount(destination), getTransferRate() - destinationEntry.getLongValue());
				var availableToMove = Math.min(availableToExtract, availableToInsert);
				
				if (availableToMove <= 0L) {
					continue;
				}
				
				availableToMove = LongEnergyStorage.extract(source, availableToMove, true);
				availableToMove = LongEnergyStorage.insert(destination, availableToMove, true);
				
				var moved = LongEnergyStorage.move(source, destination, availableToMove);
				
				extractedAmount += moved;
				destinationEntry.setValue(destinationEntry.getLongValue() + moved);
				
				if (extractedAmount >= getTransferRate()) {
					break;
				}
			}
			
			sourceEntry.setValue(extractedAmount);
		}
	}
	
	@Override
	public void tick(Network<IEnergyStorage> network) {
		var world = network.getWorld();
		
		var extractableStorages = new Reference2LongOpenHashMap<IEnergyStorage>();
		var bufferStorages = new Reference2LongOpenHashMap<IEnergyStorage>();
		var insertableStorages = new Reference2LongOpenHashMap<IEnergyStorage>();
		
		for (var member : network.getMembers()) {
			var storage = find(world, member.blockPos(), member.direction());
			
			if (storage == null) {
				continue;
			} else {
				if (storage.canReceive() && storage.canExtract()) {
					bufferStorages.put(storage, 0L);
				} else if (storage.canReceive()) {
					insertableStorages.put(storage, 0L);
				} else if (storage.canExtract()) {
					extractableStorages.put(storage, 0L);
				}
			}
		}
		
		move(extractableStorages, insertableStorages);
		move(extractableStorages, bufferStorages);
		
		// Clean the map as we are now using it to store the extracted
		// amount rather than the inserted amount.
		var cleanBufferStorages = new Reference2LongOpenHashMap<IEnergyStorage>();
		
		for (var entry : bufferStorages.reference2LongEntrySet()) {
			cleanBufferStorages.put(entry.getKey(), 0L);
		}
		
		move(cleanBufferStorages, insertableStorages);
		moveBetweenBuffers(bufferStorages);
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
