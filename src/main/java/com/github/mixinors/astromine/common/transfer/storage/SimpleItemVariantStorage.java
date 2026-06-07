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

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public class SimpleItemVariantStorage {
	private final Container inventory;
	private final int slot;
	
	private SimpleItemStorage outerStorage = null;
	
	public SimpleItemVariantStorage(Container inventory, int slot) {
		this.inventory = inventory;
		this.slot = slot;
	}
	
	public ItemStack getStack() {
		return inventory.getItem(slot);
	}
	
	public void setStack(ItemStack stack) {
		inventory.setItem(slot, stack);
	}
	
	public int getCapacity(ItemStack stack) {
		return stack.isEmpty() ? inventory.getMaxStackSize() : Math.min(inventory.getMaxStackSize(), stack.getMaxStackSize());
	}
	
	public boolean isResourceBlank() {
		return getStack().isEmpty();
	}
	
	public ItemStack getResource() {
		return getStack();
	}
	
	public long getAmount() {
		return getStack().getCount();
	}
	
	public long getCapacity() {
		return getCapacity(getStack());
	}
	
	public SimpleItemStorage getOuterStorage() {
		return outerStorage;
	}
	
	public void setOuterStorage(SimpleItemStorage outerStorage) {
		this.outerStorage = outerStorage;
	}
	
	public int getSlot() {
		return slot;
	}
	
	public int insert(ItemStack stack, int maxAmount, boolean force, boolean simulate) {
		if (stack.isEmpty() || maxAmount <= 0) {
			return 0;
		}
		
		var existingStack = getStack();
		
		if (!existingStack.isEmpty() && !ItemStack.isSameItemSameComponents(existingStack, stack)) {
			return 0;
		}
		
		if (outerStorage != null && !outerStorage.canInsert(stack, slot) && !force) {
			return 0;
		}
		
		var inserted = Math.min(maxAmount, getCapacity(stack) - existingStack.getCount());
		
		if (inserted > 0 && !simulate) {
			if (existingStack.isEmpty()) {
				var insertedStack = stack.copy();
				insertedStack.setCount(inserted);
				setStack(insertedStack);
			} else {
				existingStack.grow(inserted);
				setStack(existingStack);
			}
		}
		
		return inserted;
	}
	
	public int extract(ItemStack stack, int maxAmount, boolean force, boolean simulate) {
		if (stack.isEmpty() || maxAmount <= 0) {
			return 0;
		}
		
		var existingStack = getStack();
		
		if (existingStack.isEmpty() || !ItemStack.isSameItemSameComponents(existingStack, stack)) {
			return 0;
		}
		
		if (outerStorage != null && !outerStorage.canExtract(existingStack, slot) && !force) {
			return 0;
		}
		
		var extracted = Math.min(existingStack.getCount(), maxAmount);
		
		if (extracted > 0 && !simulate) {
			existingStack.shrink(extracted);
			setStack(existingStack);
		}
		
		return extracted;
	}
}
