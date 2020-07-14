package com.github.chainmailstudios.astromine.registry;

import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.registry.Registry;

import com.github.chainmailstudios.astromine.common.recipe.ElectrolyzingRecipe;
import com.github.chainmailstudios.astromine.common.recipe.FluidMixingRecipe;
import com.github.chainmailstudios.astromine.common.recipe.LiquidGeneratingRecipe;
import com.github.chainmailstudios.astromine.common.recipe.SolidGeneratingRecipe;
import com.github.chainmailstudios.astromine.common.recipe.TrituratingRecipe;

public class AstromineRecipeSerializers {
	public static final RecipeSerializer<TrituratingRecipe> TRITURATING = Registry.register(
			Registry.RECIPE_SERIALIZER,
			TrituratingRecipe.Serializer.ID,
			TrituratingRecipe.Serializer.INSTANCE);
	
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
