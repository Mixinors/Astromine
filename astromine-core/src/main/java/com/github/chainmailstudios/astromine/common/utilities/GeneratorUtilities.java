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

import com.google.gson.JsonObject;
import java.util.function.Consumer;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class GeneratorUtilities {
	public static final class Providers {
		/** Returns a default implementation of a {@link FinishedRecipe}. */
		public static DefaultedRecipeJsonProvider createProvider(RecipeSerializer<?> type, ResourceLocation id, Consumer<JsonObject> serializer) {
			return new DefaultedRecipeJsonProvider(type, id) {
				@Override
				public void serializeRecipeData(JsonObject json) {
					serializer.accept(json);
				}
			};
		}

		/** A class responsible for populating a recipe JSON. */
		private static abstract class DefaultedRecipeJsonProvider implements FinishedRecipe {
			private final RecipeSerializer<?> type;
			private final ResourceLocation id;

			public DefaultedRecipeJsonProvider(RecipeSerializer<?> type, ResourceLocation id) {
				this.type = type;
				this.id = id;
			}

			@Override
			public ResourceLocation getId() {
				return id;
			}

			@Override
			public RecipeSerializer<?> getType() {
				return type;
			}

			@Override
			public JsonObject serializeAdvancement() {
				return null;
			}

			@Override
			public ResourceLocation getAdvancementId() {
				return null;
			}
		}
	}
}
