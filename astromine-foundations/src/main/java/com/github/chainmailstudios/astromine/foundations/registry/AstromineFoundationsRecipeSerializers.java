package com.github.chainmailstudios.astromine.foundations.registry;

import com.github.chainmailstudios.astromine.foundations.common.recipe.WireCuttingRecipe;
import com.github.chainmailstudios.astromine.registry.AstromineRecipeSerializers;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.registry.Registry;

public class AstromineFoundationsRecipeSerializers extends AstromineRecipeSerializers {
	public static final RecipeSerializer<WireCuttingRecipe> WIRE_CUTTING = Registry.register(Registry.RECIPE_SERIALIZER, WireCuttingRecipe.Serializer.ID, WireCuttingRecipe.Serializer.INSTANCE);
	
	public static void initialize() {

	}
}
