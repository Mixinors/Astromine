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

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

public class SimpleFluidItemStorage implements IFluidHandlerItem {
	public static final String FLUID_ID_KEY = "Id";
	public static final String AMOUNT_KEY = "Amount";
	
	private final ItemStack stack;
	private final FluidStorageItem item;
	
	public SimpleFluidItemStorage(ItemStack stack, FluidStorageItem item) {
		this.stack = stack;
		this.item = item;
	}
	
	public static CompoundTag writeFluid(FluidStack stack) {
		var tag = new CompoundTag();
		
		if (!stack.isEmpty()) {
			tag.putString(FLUID_ID_KEY, BuiltInRegistries.FLUID.getKey(stack.getFluid()).toString());
			tag.putInt(AMOUNT_KEY, stack.getAmount());
		}
		
		return tag;
	}
	
	public static FluidStack readFluid(CompoundTag tag) {
		if (!tag.contains(FLUID_ID_KEY)) {
			return FluidStack.EMPTY;
		}
		
		var fluid = BuiltInRegistries.FLUID.get(ResourceLocation.parse(tag.getString(FLUID_ID_KEY)));
		
		return fluid == Fluids.EMPTY ? FluidStack.EMPTY : new FluidStack(fluid, tag.getInt(AMOUNT_KEY));
	}
	
	public FluidStack getResource() {
		return item.getStoredFluid(stack);
	}
	
	@Override
	public ItemStack getContainer() {
		return stack;
	}
	
	@Override
	public int getTanks() {
		return 1;
	}
	
	@Override
	public FluidStack getFluidInTank(int tank) {
		return tank == 0 ? getResource() : FluidStack.EMPTY;
	}
	
	@Override
	public int getTankCapacity(int tank) {
		return tank == 0 ? (int) Math.min(item.getFluidCapacity(), Integer.MAX_VALUE) : 0;
	}
	
	@Override
	public boolean isFluidValid(int tank, FluidStack stack) {
		var stored = getResource();
		
		return tank == 0 && (stored.isEmpty() || FluidStack.isSameFluidSameComponents(stored, stack));
	}
	
	@Override
	public int fill(FluidStack resource, FluidAction action) {
		if (resource.isEmpty() || !isFluidValid(0, resource)) {
			return 0;
		}
		
		var stored = getResource();
		var inserted = Math.min(resource.getAmount(), getTankCapacity(0) - stored.getAmount());
		
		if (inserted <= 0) {
			return 0;
		}
		
		if (action.execute()) {
			var updated = stored.isEmpty() ? resource.copyWithAmount(inserted) : stored.copyWithAmount(stored.getAmount() + inserted);
			item.setStoredFluid(stack, updated);
		}
		
		return inserted;
	}
	
	@Override
	public FluidStack drain(FluidStack resource, FluidAction action) {
		var stored = getResource();
		
		if (resource.isEmpty() || stored.isEmpty() || !FluidStack.isSameFluidSameComponents(stored, resource)) {
			return FluidStack.EMPTY;
		}
		
		return drain(resource.getAmount(), action);
	}
	
	@Override
	public FluidStack drain(int maxDrain, FluidAction action) {
		var stored = getResource();
		
		if (stored.isEmpty() || maxDrain <= 0) {
			return FluidStack.EMPTY;
		}
		
		var drained = Math.min(maxDrain, stored.getAmount());
		var result = stored.copyWithAmount(drained);
		
		if (action.execute()) {
			var remaining = stored.getAmount() - drained;
			item.setStoredFluid(stack, remaining <= 0 ? FluidStack.EMPTY : stored.copyWithAmount(remaining));
		}
		
		return result;
	}
}
