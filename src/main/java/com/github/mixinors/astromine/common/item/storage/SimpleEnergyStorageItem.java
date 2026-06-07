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

import com.github.mixinors.astromine.common.transfer.storage.EnergyStorageItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class SimpleEnergyStorageItem extends Item implements EnergyStorageItem {
	private final long capacity;
	
	public SimpleEnergyStorageItem(Item.Properties settings, long capacity) {
		super(settings);
		
		this.capacity = capacity;
	}
	
	@Override
	public long getEnergyCapacity() {
		return capacity;
	}
	
	@Override
	public int getBarWidth(ItemStack stack) {
		if (getEnergyCapacity() == 0L) {
			return 0;
		}
		
		return (int) (13.0F * ((float) getStoredEnergy(stack) / (float) getEnergyCapacity()));
	}
	
	@Override
	public boolean isBarVisible(ItemStack stack) {
		return true;
	}
	
	@Override
	public int getBarColor(ItemStack stack) {
		return 0xACE379;
	}
}
