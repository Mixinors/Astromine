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

package com.github.mixinors.astromine.common.comparator;

import com.github.mixinors.astromine.common.block.entity.base.ExtendedBlockEntity;
import com.github.mixinors.astromine.common.transfer.storage.LongEnergyStorage;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ComparatorBlockEntity;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

/**
 * A handler of {@link ComparatorBlockEntity} output levels.
 */
public class ComparatorOutput {
	public static int forItems(BlockEntity blockEntity) {
		if (blockEntity instanceof ExtendedBlockEntity extendedBlockEntity) {
			return forItems(extendedBlockEntity.getItemStorage());
		}
		return 0;
	}
	
	public static int forFluids(BlockEntity blockEntity) {
		if (blockEntity instanceof ExtendedBlockEntity extendedBlockEntity) {
			return forFluids(extendedBlockEntity.getFluidStorage());
		}
		return 0;
	}
	
	public static int forEnergy(BlockEntity blockEntity) {
		if (blockEntity instanceof ExtendedBlockEntity extendedBlockEntity) {
			return forEnergy(extendedBlockEntity.getEnergyStorage());
		}
		return 0;
	}
	
	/**
	 * Returns the output level for an {@link IEnergyStorage}.
	 */
	public static int forEnergy(@Nullable IEnergyStorage storage) {
		if (storage == null) {
			return 0;
		}
		
		var amount = LongEnergyStorage.getAmount(storage);
		var capacity = LongEnergyStorage.getCapacity(storage);
		
		if (amount <= 0L || capacity <= 0L) {
			return 0;
		}
		
		return 1 + (int) ((double) amount / (double) capacity * 14.0D);
	}
	
	public static int forItems(@Nullable IItemHandler storage) {
		if (storage == null || storage.getSlots() <= 0) {
			return 0;
		}
		
		var occupiedSlots = 0;
		var fullness = 0.0D;
		
		for (var slot = 0; slot < storage.getSlots(); ++slot) {
			var stack = storage.getStackInSlot(slot);
			
			if (!stack.isEmpty()) {
				fullness += (double) stack.getCount() / (double) Math.min(storage.getSlotLimit(slot), stack.getMaxStackSize());
				++occupiedSlots;
			}
		}
		
		return occupiedSlots == 0 ? 0 : 1 + (int) (fullness / storage.getSlots() * 14.0D);
	}
	
	public static int forFluids(@Nullable IFluidHandler storage) {
		if (storage == null || storage.getTanks() <= 0) {
			return 0;
		}
		
		var occupiedTanks = 0;
		var fullness = 0.0D;
		
		for (var tank = 0; tank < storage.getTanks(); ++tank) {
			var stack = storage.getFluidInTank(tank);
			var capacity = storage.getTankCapacity(tank);
			
			if (!stack.isEmpty() && capacity > 0) {
				fullness += (double) stack.getAmount() / (double) capacity;
				++occupiedTanks;
			}
		}
		
		return occupiedTanks == 0 ? 0 : 1 + (int) (fullness / storage.getTanks() * 14.0D);
	}
}
