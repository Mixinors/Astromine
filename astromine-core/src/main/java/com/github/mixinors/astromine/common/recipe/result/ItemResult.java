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

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;

public record ItemResult(ItemVariant variant, int count) {
	public static final ItemResult EMPTY = new ItemResult(ItemVariant.blank(), 0);

	public ItemStack toStack() {
		return variant.toStack(count);
	}

	public boolean equalsAndFitsIn(SingleSlotStorage<ItemVariant> storage) {
		return storage.getCapacity() - storage.getAmount() >= count && storage.getResource().equals(variant);
	}

	public static JsonObject toJson(ItemResult result) {
		JsonObject jsonObject = new JsonObject();

		jsonObject.addProperty("item", Registry.ITEM.getId(result.variant.getItem()).toString());
		jsonObject.addProperty("count", result.count);

		return jsonObject;
	}

	public static ItemResult fromJson(JsonElement jsonElement) {
		if (!jsonElement.isJsonObject()) {
			Identifier variantId = new Identifier(jsonElement.getAsString());
			Item variantItem = Registry.ITEM.get(variantId);

			ItemVariant variant = ItemVariant.of(variantItem);

			return new ItemResult(variant, 1);
		} else {
			JsonObject jsonObject = jsonElement.getAsJsonObject();

			Identifier variantId = new Identifier(jsonObject.get("item").getAsString());
			Item variantItem = Registry.ITEM.get(variantId);

			ItemVariant variant = ItemVariant.of(variantItem);

			var variantCount = 1;
			
			if (jsonObject.has("count")) {
				variantCount = jsonObject.get("count").getAsInt();
			}

			return new ItemResult(variant, variantCount);
		}
	}

	public static void toPacket(PacketByteBuf buf, ItemResult result) {
		buf.writeString(Registry.ITEM.getId(result.variant.getItem()).toString());
		buf.writeLong(result.count);
	}

	public static ItemResult fromPacket(PacketByteBuf buf) {
		Identifier variantId = new Identifier(buf.readString());
		Item variantItem = Registry.ITEM.get(variantId);

		ItemVariant variant = ItemVariant.of(variantItem);

		int variantAmount = buf.readInt();

		return new ItemResult(variant, variantAmount);
	}
}
