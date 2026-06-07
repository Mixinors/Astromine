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

package com.github.mixinors.astromine.common.recipe.result;

import com.github.mixinors.astromine.common.transfer.storage.SimpleFluidVariantStorage;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;

public record FluidResult(
		FluidStack stack
) {
	private static final String FLUID_KEY = "fluid";
	private static final String AMOUNT_KEY = "amount";
	
	public static final FluidResult EMPTY = new FluidResult(FluidStack.EMPTY);
	
	public FluidStack toStack() {
		return stack.copy();
	}
	
	public long amount() {
		return stack.getAmount();
	}
	
	public boolean equalsAndFitsIn(SimpleFluidVariantStorage storage) {
		var storedStack = storage.getResource();
		var storedAmount = storage.getAmount();
		
		return storage.getCapacity() - storedAmount >= amount()
				&& (storedStack.isEmpty() || FluidStack.isSameFluidSameComponents(storedStack, stack));
	}
	
	public static JsonObject toJson(FluidResult result) {
		var jsonObject = new JsonObject();
		
		jsonObject.addProperty(FLUID_KEY, BuiltInRegistries.FLUID.getKey(result.stack.getFluid()).toString());
		jsonObject.addProperty(AMOUNT_KEY, result.stack.getAmount());
		
		return jsonObject;
	}
	
	public static FluidResult fromJson(JsonElement jsonElement) {
		if (!jsonElement.isJsonObject()) {
			var fluid = BuiltInRegistries.FLUID.get(ResourceLocation.parse(jsonElement.getAsString()));
			
			return new FluidResult(new FluidStack(fluid, FluidType.BUCKET_VOLUME));
		}
		
		var jsonObject = jsonElement.getAsJsonObject();
		var fluid = BuiltInRegistries.FLUID.get(ResourceLocation.parse(jsonObject.get(FLUID_KEY).getAsString()));
		var amount = jsonObject.has(AMOUNT_KEY) ? jsonObject.get(AMOUNT_KEY).getAsLong() : FluidType.BUCKET_VOLUME;
		
		if (fluid == Fluids.EMPTY || amount <= 0L) {
			return EMPTY;
		}
		
		return new FluidResult(new FluidStack(fluid, (int) Math.min(amount, Integer.MAX_VALUE)));
	}
	
	public static void toPacket(FriendlyByteBuf buf, FluidResult result) {
		buf.writeUtf(BuiltInRegistries.FLUID.getKey(result.stack.getFluid()).toString());
		buf.writeLong(result.stack.getAmount());
	}
	
	public static FluidResult fromPacket(FriendlyByteBuf buf) {
		var fluid = BuiltInRegistries.FLUID.get(ResourceLocation.parse(buf.readUtf()));
		var amount = buf.readLong();
		
		if (fluid == Fluids.EMPTY || amount <= 0L) {
			return EMPTY;
		}
		
		return new FluidResult(new FluidStack(fluid, (int) Math.min(amount, Integer.MAX_VALUE)));
	}
}
