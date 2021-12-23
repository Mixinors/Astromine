package com.github.mixinors.astromine.datagen.recipe;

import com.github.mixinors.astromine.common.recipe.TrituratingRecipe;

import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;

public class TrituratingRecipeJsonFactory extends SimpleMachineRecipeJsonFactory<TrituratingRecipe> {
	protected TrituratingRecipeJsonFactory(Ingredient input, ItemConvertible output, int outputCount, int processingTime, int energy) {
		super(input, output, outputCount, processingTime, energy, TrituratingRecipe.Serializer.INSTANCE);
	}

	@Override
	public String getName() {
		return "triturating";
	}
}
