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

package com.github.mixinors.astromine.common.transfer.storage;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public interface EnergyStorageItem {
	String ENERGY_KEY = "Energy";
	
	long getEnergyCapacity();
	
	default long getEnergyMaxInput() {
		return getEnergyCapacity();
	}
	
	default long getEnergyMaxOutput() {
		return getEnergyCapacity();
	}
	
	default long getStoredEnergy(ItemStack stack) {
		return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getLong(ENERGY_KEY);
	}
	
	default void setStoredEnergy(ItemStack stack, long amount) {
		var tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
		
		tag.putLong(ENERGY_KEY, Math.clamp(amount, 0L, getEnergyCapacity()));
		
		stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
	}
	
	default boolean tryUseEnergy(ItemStack stack, long amount) {
		if (getStoredEnergy(stack) < amount) {
			return false;
		}
		
		setStoredEnergy(stack, getStoredEnergy(stack) - amount);
		
		return true;
	}
	
	default LongEnergyStorage createEnergyStorage(ItemStack stack) {
		return new StackBackedEnergyStorage(stack, this);
	}
	
	class StackBackedEnergyStorage extends LongEnergyStorage {
		private final ItemStack stack;
		private final EnergyStorageItem item;
		
		public StackBackedEnergyStorage(ItemStack stack, EnergyStorageItem item) {
			super(item.getEnergyCapacity(), item.getEnergyMaxInput(), item.getEnergyMaxOutput(), item.getStoredEnergy(stack));
			
			this.stack = stack;
			this.item = item;
		}
		
		@Override
		public long insert(long maxAmount, boolean simulate) {
			amount = item.getStoredEnergy(stack);
			
			var inserted = super.insert(maxAmount, simulate);
			
			if (!simulate) {
				item.setStoredEnergy(stack, amount);
			}
			
			return inserted;
		}
		
		@Override
		public long extract(long maxAmount, boolean simulate) {
			amount = item.getStoredEnergy(stack);
			
			var extracted = super.extract(maxAmount, simulate);
			
			if (!simulate) {
				item.setStoredEnergy(stack, amount);
			}
			
			return extracted;
		}
		
		@Override
		public long getAmount() {
			return item.getStoredEnergy(stack);
		}
	}
}
