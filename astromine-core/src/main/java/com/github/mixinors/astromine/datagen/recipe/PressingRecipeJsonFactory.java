package com.github.mixinors.astromine.datagen.recipe;

import com.github.mixinors.astromine.common.recipe.PressingRecipe;

import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;

public class PressingRecipeJsonFactory extends SimpleMachineRecipeJsonFactory<PressingRecipe> {
	protected PressingRecipeJsonFactory(Ingredient input, ItemConvertible output, int outputCount, int processingTime, int energy) {
		super(input, output, outputCount, processingTime, energy, PressingRecipe.Serializer.INSTANCE);
	}

	@Override
	public String getName() {
		return "pressing";
	}
}
