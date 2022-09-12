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
import com.github.mixinors.astromine.common.transfer.storage.SimpleFluidItemStorage;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SimpleFluidStorageItem extends Item implements FluidStorageItem {
	private final long capacity;
	
	public SimpleFluidStorageItem(Item.Settings settings, long capacity) {
		super(settings);
		
		this.capacity = capacity;
	}
	
	@Override
	public long getFluidCapacity() {
		return capacity;
	}
	
	@Override
	public int getItemBarStep(ItemStack stack) {
		if (getFluidCapacity() == 0L) {
			return 0;
		}
		
		var fluidStorages = FluidStorage.ITEM.find(stack, ContainerItemContext.withInitial(stack));
		
		var totalAmount = 0L;
		var totalCapacity = 0L;
		
		try (var transaction = Transaction.openOuter()) {
			for (var fluidStorage : fluidStorages) {
				totalAmount += fluidStorage.getAmount();
				totalCapacity += fluidStorage.getCapacity();
			}
			
			transaction.abort();
		}
		
		totalCapacity = totalCapacity == 0L ? 1L : totalCapacity;
		
		return (int) (13.0F * ((float) totalAmount / (float) totalCapacity));
	}
	
	@Override
	public boolean isItemBarVisible(ItemStack stack) {
		return true;
	}
	
	@Override
	public int getItemBarColor(ItemStack stack) {
		var fluidStorage = (SimpleFluidItemStorage) FluidStorage.ITEM.find(stack, ContainerItemContext.withInitial(stack));
		
		return FluidVariantRendering.getColor(fluidStorage.getResource());
	}
}
