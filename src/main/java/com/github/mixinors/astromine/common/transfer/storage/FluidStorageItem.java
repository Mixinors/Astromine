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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.fluids.FluidStack;

public interface FluidStorageItem {
	String FLUID_KEY = "Fluid";
	String AMOUNT_KEY = "Amount";
	
	long getFluidCapacity();
	
	default FluidStack getStoredFluid(ItemStack stack) {
		var tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
		
		return SimpleFluidItemStorage.readFluid(tag.getCompound(FLUID_KEY));
	}
	
	default void setStoredFluid(ItemStack stack, FluidStack fluid) {
		var tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
		
		if (fluid.isEmpty()) {
			tag.remove(FLUID_KEY);
		} else {
			tag.put(FLUID_KEY, SimpleFluidItemStorage.writeFluid(fluid.copyWithAmount((int) Math.min(fluid.getAmount(), getFluidCapacity()))));
		}
		
		stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
	}
	
	default SimpleFluidItemStorage createFluidStorage(ItemStack stack) {
		return new SimpleFluidItemStorage(stack, this);
	}
}
