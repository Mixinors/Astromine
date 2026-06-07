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

import net.minecraft.util.Mth;
import net.neoforged.neoforge.fluids.FluidStack;

public class SimpleFluidVariantStorage {
	private final long capacity;
	private final int slot;
	private final SimpleFluidStorage outerStorage;
	
	private FluidStack stack = FluidStack.EMPTY;
	
	public SimpleFluidVariantStorage(long capacity, int slot, SimpleFluidStorage outerStorage) {
		this.capacity = Math.max(0L, capacity);
		this.slot = slot;
		this.outerStorage = outerStorage;
	}
	
	public void setAmount(long amount) {
		if (stack.isEmpty()) {
			return;
		}
		
		var clampedAmount = clampAmount(amount);
		
		if (clampedAmount <= 0) {
			stack = FluidStack.EMPTY;
		} else {
			stack = stack.copyWithAmount(clampedAmount);
		}
		
		notifyOuterStorage();
	}
	
	public void setResource(FluidStack stack) {
		if (stack.isEmpty()) {
			this.stack = FluidStack.EMPTY;
			notifyOuterStorage();
			return;
		}
		
		this.stack = stack.copyWithAmount(clampAmount(stack.getAmount()));
		notifyOuterStorage();
	}
	
	public int getSlot() {
		return slot;
	}
	
	public long getCapacity() {
		return capacity;
	}
	
	public boolean isResourceBlank() {
		return stack.isEmpty();
	}
	
	public FluidStack getResource() {
		return stack;
	}
	
	public long getAmount() {
		return stack.getAmount();
	}
	
	public long insert(FluidStack insertedStack, long maxAmount, boolean force, boolean simulate) {
		if (insertedStack.isEmpty() || maxAmount <= 0L) {
			return 0L;
		}
		
		if (outerStorage != null && !outerStorage.canInsert(insertedStack, slot) && !force) {
			return 0L;
		}
		
		if (!stack.isEmpty() && !FluidStack.isSameFluidSameComponents(stack, insertedStack)) {
			return 0L;
		}
		
		var inserted = Math.min(maxAmount, capacity - getAmount());
		
		if (inserted <= 0L) {
			return 0L;
		}
		
		if (!simulate) {
			if (stack.isEmpty()) {
				stack = insertedStack.copyWithAmount(clampAmount(inserted));
			} else {
				stack = stack.copyWithAmount(clampAmount(getAmount() + inserted));
			}
			
			notifyOuterStorage();
		}
		
		return inserted;
	}
	
	public long extract(FluidStack extractedStack, long maxAmount, boolean force, boolean simulate) {
		if (extractedStack.isEmpty() || maxAmount <= 0L || stack.isEmpty()) {
			return 0L;
		}
		
		if (outerStorage != null && !outerStorage.canExtract(stack, slot) && !force) {
			return 0L;
		}
		
		if (!FluidStack.isSameFluidSameComponents(stack, extractedStack)) {
			return 0L;
		}
		
		var extracted = Math.min(maxAmount, getAmount());
		
		if (extracted <= 0L) {
			return 0L;
		}
		
		if (!simulate) {
			var remaining = getAmount() - extracted;
			
			stack = remaining <= 0L ? FluidStack.EMPTY : stack.copyWithAmount(clampAmount(remaining));
			
			notifyOuterStorage();
		}
		
		return extracted;
	}
	
	private void notifyOuterStorage() {
		if (outerStorage != null) {
			outerStorage.notifyListeners();
			outerStorage.incrementVersion();
		}
	}
	
	private int clampAmount(long amount) {
		return (int) Mth.clamp(amount, 0L, Math.min(capacity, Integer.MAX_VALUE));
	}
}
