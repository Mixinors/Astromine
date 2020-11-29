/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
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

package com.github.chainmailstudios.astromine.common.utilities;

import com.github.chainmailstudios.astromine.common.recipe.ingredient.FluidIngredient;
import com.github.chainmailstudios.astromine.common.recipe.ingredient.ItemIngredient;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import com.google.gson.JsonElement;

public class IngredientUtilities {
	/** Deserializes an {@link Ingredient} from a {@link JsonElement}. */
	public static Ingredient fromIngredientJson(JsonElement jsonElement) {
		return Ingredient.fromJson(jsonElement);
	}

	/** Deserializes an {@link Ingredient} from a {@link ByteBuf}. */
	public static Ingredient fromIngredientPacket(FriendlyByteBuf buffer) {
		return Ingredient.fromNetwork(buffer);
	}

	/** Serializes an {@link Ingredient} to a {@link ByteBuf}. */
	public static void toIngredientPacket(FriendlyByteBuf buffer, Ingredient ingredient) {
		ingredient.toNetwork(buffer);
	}

	/** Returns an {@link ItemStack} from the specified {@link Ingredient}
	 * which is compatible with the given {@link ItemStack}.
	 *
	 * This method is already implemented in {@link ItemIngredient} and
	 * {@link FluidIngredient}, but not present in the default {@link Ingredient}.
	 * */
	public static ItemStack testMatching(Ingredient input, ItemStack stack) {
		if (stack != null) {
			if (input.getItems().length != 0) {
				for (ItemStack matching : input.getItems()) {
					if (matching.getItem() == stack.getItem()) {
						return matching;
					}
				}
			}
		}

		return ItemStack.EMPTY;
	}
}
