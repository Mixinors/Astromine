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

package com.github.mixinors.astromine.common.item.storage;

import com.github.mixinors.astromine.common.transfer.storage.FluidStorageItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class SimpleFluidStorageItem extends Item implements FluidStorageItem {
	private final long capacity;
	
	public SimpleFluidStorageItem(Item.Properties settings, long capacity) {
		super(settings);
		
		this.capacity = capacity;
	}
	
	@Override
	public long getFluidCapacity() {
		return capacity;
	}
	
	@Override
	public int getBarWidth(ItemStack stack) {
		if (getFluidCapacity() == 0L) {
			return 0;
		}
		
		var fluid = getStoredFluid(stack);
		var totalAmount = fluid.getAmount();
		var totalCapacity = Math.max(1L, getFluidCapacity());
		
		return (int) (13.0F * ((float) totalAmount / (float) totalCapacity));
	}
	
	@Override
	public boolean isBarVisible(ItemStack stack) {
		return true;
	}
	
	@Override
	public int getBarColor(ItemStack stack) {
		var fluid = getStoredFluid(stack);
		
		return fluid.isEmpty() ? 0xFFFFFF : 0x3F76E4;
	}
}
