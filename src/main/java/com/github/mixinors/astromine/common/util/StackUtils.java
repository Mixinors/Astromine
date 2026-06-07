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

package com.github.mixinors.astromine.common.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class StackUtils {
	private static final String ITEM_KEY = "item";
	private static final String ID_KEY = "id";
	private static final String COUNT_KEY = "count";
	
	public static boolean areItemsAndTagsEqual(ItemStack left, ItemStack right) {
		return ItemStack.matches(left, right);
	}
	
	public static ItemStack fromJson(JsonElement jsonElement) {
		if (!jsonElement.isJsonObject()) {
			if (jsonElement.isJsonPrimitive()) {
				var primitive = jsonElement.getAsJsonPrimitive();
				
				if (primitive.isString()) {
					return new ItemStack(BuiltInRegistries.ITEM.get(ResourceLocation.parse(primitive.getAsString())));
				} else {
					return ItemStack.EMPTY;
				}
			} else {
				return ItemStack.EMPTY;
			}
		} else {
			var object = jsonElement.getAsJsonObject();
			var itemKey = object.has(ITEM_KEY) ? ITEM_KEY : ID_KEY;
			var item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(object.get(itemKey).getAsString()));
			var count = object.has(COUNT_KEY) ? object.get(COUNT_KEY).getAsInt() : 1;
			
			return new ItemStack(item, count);
		}
	}
	
	public static JsonElement toJson(ItemStack stack) {
		var object = new JsonObject();
		
		object.addProperty(ITEM_KEY, BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());
		object.addProperty(COUNT_KEY, stack.getCount());
		
		return object;
	}
	
	public static ItemStack fromPacket(FriendlyByteBuf buffer) {
		return ItemStack.OPTIONAL_STREAM_CODEC.decode((RegistryFriendlyByteBuf) buffer);
	}
	
	public static void toPacket(FriendlyByteBuf buffer, ItemStack stack) {
		ItemStack.OPTIONAL_STREAM_CODEC.encode((RegistryFriendlyByteBuf) buffer, stack);
	}
}
