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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import com.github.mixinors.astromine.common.recipe.ingredient.FluidIngredient;
import com.github.mixinors.astromine.common.recipe.ingredient.ItemIngredient;
import io.netty.buffer.ByteBuf;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;

public class IngredientUtils {
	private static final String COUNT_KEY = "count";
	
	public static Ingredient fromIngredientJson(JsonElement jsonElement) {
		return Ingredient.fromJson(jsonElement);
	}
	
	public static Ingredient fromIngredientPacket(PacketByteBuf buffer) {
		return Ingredient.fromPacket(buffer);
	}
	
	public static void toIngredientPacket(PacketByteBuf buffer, Ingredient ingredient) {
		ingredient.write(buffer);
	}
	
	public static ItemStack testMatching(Ingredient input, ItemStack stack) {
		if (stack != null) {
			if (input.matchingStacks.length != 0) {
				for (var matching : input.matchingStacks) {
					if (matching.getItem() == stack.getItem()) {
						return matching;
					}
				}
			}
		}

		return ItemStack.EMPTY;
	}

	public static JsonElement toJsonWithCount(Ingredient ingredient, int count) {
		if (ingredient.entries.length == 1) {
			var entryObject = ingredient.entries[0].toJson();
			entryObject.addProperty(COUNT_KEY, count);
			
			return entryObject;
		}
		
		var jsonArray = new JsonArray();
		
		for (var entry : ingredient.entries) {
			var entryObject = entry.toJson();
			entryObject.addProperty(COUNT_KEY, count);
			
			jsonArray.add(entryObject);
		}
		
		return jsonArray;
	}
}
