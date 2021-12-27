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

package com.github.mixinors.astromine.common.recipe.result;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import dev.architectury.fluid.FluidStack;

import net.minecraft.fluid.Fluid;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;

public record FluidResult(FluidVariant variant, long amount) {
	public static final FluidResult EMPTY = new FluidResult(FluidVariant.blank(), 0);

	public FluidStack toStack() {
		return FluidStack.create(variant.getFluid(), amount, variant.copyNbt());
	}

	public boolean equalsAndFitsIn(SingleSlotStorage<FluidVariant> storage) {
		return storage.getCapacity() - storage.getAmount() >= amount && storage.getResource().equals(variant);
	}

	public static JsonObject toJson(FluidResult result) {
		JsonObject jsonObject = new JsonObject();

		jsonObject.addProperty("fluid", Registry.FLUID.getId(result.variant.getFluid()).toString());
		jsonObject.addProperty("amount", result.amount);

		return jsonObject;
	}

	public static FluidResult fromJson(JsonElement jsonElement) {
		if (!jsonElement.isJsonObject()) {
			Identifier variantId = new Identifier(jsonElement.getAsString());
			Fluid variantFluid = Registry.FLUID.get(variantId);

			FluidVariant variant = FluidVariant.of(variantFluid);

			return new FluidResult(variant, FluidConstants.BUCKET);
		} else {
			JsonObject jsonObject = jsonElement.getAsJsonObject();

			Identifier variantId = new Identifier(jsonObject.get("fluid").getAsString());
			Fluid variantFluid = Registry.FLUID.get(variantId);

			FluidVariant variant = FluidVariant.of(variantFluid);
			
			if (jsonObject.has("amount")) {
				int variantAmount = jsonObject.get("amount").getAsInt();
				
				return new FluidResult(variant, variantAmount);
			} else {
				return new FluidResult(variant, FluidConstants.BUCKET);
			}
		}
	}

	public static void toPacket(PacketByteBuf buf, FluidResult result) {
		buf.writeString(Registry.FLUID.getId(result.variant.getFluid()).toString());
		buf.writeLong(result.amount);
	}

	public static FluidResult fromPacket(PacketByteBuf buf) {
		Identifier variantId = new Identifier(buf.readString());
		Fluid variantFluid = Registry.FLUID.get(variantId);

		FluidVariant variant = FluidVariant.of(variantFluid);

		long variantAmount = buf.readLong();

		return new FluidResult(variant, variantAmount);
	}
}
