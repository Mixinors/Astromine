package com.github.chainmailstudios.astromine.foundations.registry;

import com.github.chainmailstudios.astromine.foundations.common.recipe.AltarRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.registry.Registry;

public class AstromineFoundationsRecipeSerializers {
	public static final RecipeSerializer<AltarRecipe> ALLOY_SMELTING = Registry.register(Registry.RECIPE_SERIALIZER, AltarRecipe.Serializer.ID, AltarRecipe.Serializer.INSTANCE);

	public static void initialize() {

	}
}
