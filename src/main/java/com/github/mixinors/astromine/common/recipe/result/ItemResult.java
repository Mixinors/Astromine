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

import com.github.mixinors.astromine.common.transfer.storage.SimpleItemVariantStorage;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public record ItemResult(
		ItemStack stack
) {
	private static final String ITEM_KEY = "item";
	private static final String COUNT_KEY = "count";
	
	public static final ItemResult EMPTY = new ItemResult(ItemStack.EMPTY);
	
	public ItemStack toStack() {
		return stack.copy();
	}
	
	public int count() {
		return stack.getCount();
	}
	
	public boolean equalsAndFitsIn(SimpleItemVariantStorage storage) {
		return equalsAndFitsIn(storage, false);
	}
	
	public boolean equalsAndFitsIn(SimpleItemVariantStorage storage, boolean ignoreMaxCount) {
		var storedStack = storage.getResource();
		var storedAmount = storage.getAmount();
		var capacity = storage.getCapacity(stack);
		var maxStackSize = stack.getMaxStackSize();
		
		return storedAmount + stack.getCount() <= capacity
				&& (storedAmount + stack.getCount() <= maxStackSize || ignoreMaxCount)
				&& (storedStack.isEmpty() || ItemStack.isSameItemSameComponents(storedStack, stack));
	}
	
	public static JsonObject toJson(ItemResult result) {
		var jsonObject = new JsonObject();
		
		jsonObject.addProperty(ITEM_KEY, BuiltInRegistries.ITEM.getKey(result.stack.getItem()).toString());
		jsonObject.addProperty(COUNT_KEY, result.stack.getCount());
		
		return jsonObject;
	}
	
	public static ItemResult fromJson(JsonElement jsonElement) {
		if (!jsonElement.isJsonObject()) {
			var item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(jsonElement.getAsString()));
			
			return new ItemResult(new ItemStack(item));
		}
		
		var jsonObject = jsonElement.getAsJsonObject();
		var item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(jsonObject.get(ITEM_KEY).getAsString()));
		var count = jsonObject.has(COUNT_KEY) ? jsonObject.get(COUNT_KEY).getAsInt() : 1;
		
		return new ItemResult(new ItemStack(item, count));
	}
	
	public static void toPacket(FriendlyByteBuf buf, ItemResult result) {
		buf.writeUtf(BuiltInRegistries.ITEM.getKey(result.stack.getItem()).toString());
		buf.writeInt(result.stack.getCount());
	}
	
	public static ItemResult fromPacket(FriendlyByteBuf buf) {
		var item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(buf.readUtf()));
		var amount = buf.readInt();
		
		return new ItemResult(new ItemStack(item, amount));
	}
}
