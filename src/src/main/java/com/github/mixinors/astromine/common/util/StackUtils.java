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
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class StackUtils {
	private static final String ITEM_KEY = "item";
	private static final String COUNT_KEY = "count";
	
	public static boolean areItemsAndTagsEqual(ItemStack left, ItemStack right) {
		return ItemStack.areItemsEqual(left, right) && ItemStack.areNbtEqual(left, right);
	}
	
	public static ItemStack fromJson(JsonElement jsonElement) {
		if (!jsonElement.isJsonObject()) {
			if (jsonElement.isJsonPrimitive()) {
				var primitive = jsonElement.getAsJsonPrimitive();
				
				if (primitive.isString()) {
					return new ItemStack(Registry.ITEM.get(new Identifier(primitive.getAsString())));
				} else {
					return ItemStack.EMPTY;
				}
			} else {
				return ItemStack.EMPTY;
			}
		} else {
			return ShapedRecipe.outputFromJson(jsonElement.getAsJsonObject());
		}
	}
	
	public static JsonElement toJson(ItemStack stack) {
		var object = new JsonObject();
		
		object.addProperty(ITEM_KEY, Registry.ITEM.getId(stack.getItem()).toString());
		object.addProperty(COUNT_KEY, stack.getCount());
		
		return object;
	}
	
	public static ItemStack fromPacket(PacketByteBuf buffer) {
		return buffer.readItemStack();
	}
	
	public static void toPacket(PacketByteBuf buffer, ItemStack stack) {
		buffer.writeItemStack(stack);
	}
}
