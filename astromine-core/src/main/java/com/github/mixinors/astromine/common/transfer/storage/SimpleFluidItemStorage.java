/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantItemStorage;

public class SimpleFluidItemStorage extends SingleVariantItemStorage<FluidVariant> {
	public static final String FLUID_KEY = "fluid";
	public static final String VARIANT_KEY = "variant";
	public static final String AMOUNT_KEY = "amount";

	private final long capacity;

	public SimpleFluidItemStorage(ContainerItemContext context, long capacity) {
		super(context);
		this.capacity = capacity;
	}

	@Override
	protected FluidVariant getBlankResource() {
		return FluidVariant.blank();
	}

	@Override
	protected FluidVariant getResource(ItemVariant currentVariant) {
		var stack = currentVariant.toStack();
		var nbt = stack.getNbt();
		if(nbt == null || !nbt.contains(FLUID_KEY, NbtElement.COMPOUND_TYPE)) return getBlankResource();
		var fluidNbt = nbt.getCompound(FLUID_KEY);
		if(!fluidNbt.contains(VARIANT_KEY, NbtElement.COMPOUND_TYPE)) return getBlankResource();
		return FluidVariant.fromNbt(fluidNbt.getCompound(VARIANT_KEY));
	}

	@Override
	protected long getAmount(ItemVariant currentVariant) {
		var stack = currentVariant.toStack();
		var nbt = stack.getNbt();
		if(nbt == null || !nbt.contains(FLUID_KEY, NbtElement.COMPOUND_TYPE)) return 0;
		var fluidNbt = nbt.getCompound(FLUID_KEY);
		if(!fluidNbt.contains(AMOUNT_KEY, NbtElement.LONG_TYPE)) return 0;
		return fluidNbt.getLong(AMOUNT_KEY);
	}

	@Override
	protected long getCapacity(FluidVariant variant) {
		return capacity;
	}

	@Override
	protected ItemVariant getUpdatedVariant(ItemVariant currentVariant, FluidVariant newResource, long newAmount) {
		var stack = currentVariant.toStack();
		var nbt = stack.getOrCreateNbt();
		var fluidNbt = new NbtCompound();
		fluidNbt.put(VARIANT_KEY, newResource.toNbt());
		fluidNbt.putLong(AMOUNT_KEY, newAmount);
		nbt.put(FLUID_KEY, fluidNbt);
		return ItemVariant.of(stack);
	}
}
