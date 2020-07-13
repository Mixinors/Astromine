package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.common.recipe.*;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.registry.Registry;

public class AstromineRecipeSerializers {
	public static final RecipeSerializer<SortingRecipe> SORTING = Registry.register(
			Registry.RECIPE_SERIALIZER,
			SortingRecipe.Serializer.ID,
			SortingRecipe.Serializer.INSTANCE);
	
	public static final RecipeSerializer<LiquidGeneratingRecipe> LIQUID_GENERATING = Registry.register(
			Registry.RECIPE_SERIALIZER,
			LiquidGeneratingRecipe.Serializer.ID,
			LiquidGeneratingRecipe.Serializer.INSTANCE);

	public static final RecipeSerializer<ElectrolyzingRecipe> ELECTROLYZER = Registry.register(
			Registry.RECIPE_SERIALIZER,
			ElectrolyzingRecipe.Serializer.ID,
			ElectrolyzingRecipe.Serializer.INSTANCE);

	public static final RecipeSerializer<FluidMixingRecipe> FLUID_MIXER = Registry.register(
			Registry.RECIPE_SERIALIZER,
			FluidMixingRecipe.Serializer.ID,
			FluidMixingRecipe.Serializer.INSTANCE);

	public static final RecipeSerializer<SolidGeneratingRecipe> SOLID_GENERATING = Registry.register(
			Registry.RECIPE_SERIALIZER,
			SolidGeneratingRecipe.Serializer.ID,
			SolidGeneratingRecipe.Serializer.INSTANCE);

	public static void initialize() {
		// Unused.
	}
}
