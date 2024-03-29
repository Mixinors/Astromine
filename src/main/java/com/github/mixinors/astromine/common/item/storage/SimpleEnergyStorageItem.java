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
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import team.reborn.energy.api.EnergyStorage;

public class SimpleEnergyStorageItem extends Item implements EnergyStorageItem {
	private final long capacity;
	
	public SimpleEnergyStorageItem(Item.Settings settings, long capacity) {
		super(settings);
		
		this.capacity = capacity;
	}
	
	@Override
	public long getEnergyCapacity() {
		return capacity;
	}
	
	@Override
	public int getItemBarStep(ItemStack stack) {
		if (getEnergyCapacity() == 0L) {
			return 0;
		}
		
		var energyStorage = EnergyStorage.ITEM.find(stack, ContainerItemContext.withInitial(stack));
		
		return (int) (13.0F * ((float) energyStorage.getAmount() / (float) getEnergyCapacity()));
	}
	
	@Override
	public boolean isItemBarVisible(ItemStack stack) {
		return true;
	}
	
	@Override
	public int getItemBarColor(ItemStack stack) {
		return 0xACE379;
	}
	
	@Override
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
		super.appendStacks(group, stacks);
		
		if (this.isIn(group)) {
			var stack = new ItemStack(this);
			
			setStoredEnergy(stack, getEnergyCapacity());
			
			stacks.add(stack);
		}
	}
}
