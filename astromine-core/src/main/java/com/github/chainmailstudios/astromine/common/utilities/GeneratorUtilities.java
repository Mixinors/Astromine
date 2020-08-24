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

import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;

import com.google.gson.JsonObject;
import java.util.function.Consumer;

public class GeneratorUtilities {
	public static final class Providers {
		public static DefaultedRecipeJsonProvider createProvider(RecipeSerializer<?> type, Identifier id, Consumer<JsonObject> serializer) {
			return new DefaultedRecipeJsonProvider(type, id) {
				@Override
				public void serialize(JsonObject json) {
					serializer.accept(json);
				}
			};
		}

		private static abstract class DefaultedRecipeJsonProvider implements RecipeJsonProvider {
			private final RecipeSerializer<?> type;
			private final Identifier id;

			public DefaultedRecipeJsonProvider(RecipeSerializer<?> type, Identifier id) {
				this.type = type;
				this.id = id;
			}

			@Override
			public Identifier getRecipeId() {
				return id;
			}

			@Override
			public RecipeSerializer<?> getSerializer() {
				return type;
			}

			@Override
			public JsonObject toAdvancementJson() {
				return null;
			}

			@Override
			public Identifier getAdvancementId() {
				return null;
			}
		}
	}
}
