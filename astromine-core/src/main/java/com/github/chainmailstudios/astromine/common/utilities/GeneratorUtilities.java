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
