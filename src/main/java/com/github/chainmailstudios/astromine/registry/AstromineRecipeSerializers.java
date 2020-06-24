package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.recipe.SortingRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.registry.Registry;

public class AstromineRecipeSerializers {
	public static final RecipeSerializer<SortingRecipe> SORTING = Registry.register(
			Registry.RECIPE_SERIALIZER,
			SortingRecipe.Serializer.ID,
			SortingRecipe.Serializer.INSTANCE);

	public static void initialize() {
		// Unused.
	}
}
