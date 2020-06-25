package com.github.chainmailstudios.astromine.registry;

import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.registry.Registry;

import com.github.chainmailstudios.astromine.recipe.LiquidGeneratingRecipe;
import com.github.chainmailstudios.astromine.recipe.SortingRecipe;

public class AstromineRecipeSerializers {
	public static final RecipeSerializer<SortingRecipe> SORTING = Registry.register(
			Registry.RECIPE_SERIALIZER,
			SortingRecipe.Serializer.ID,
			SortingRecipe.Serializer.INSTANCE);
	
	public static final RecipeSerializer<LiquidGeneratingRecipe> LIQUID_GENERATING = Registry.register(
			Registry.RECIPE_SERIALIZER,
			LiquidGeneratingRecipe.Serializer.ID,
			LiquidGeneratingRecipe.Serializer.INSTANCE);

	public static void initialize() {
		// Unused.
	}
}
