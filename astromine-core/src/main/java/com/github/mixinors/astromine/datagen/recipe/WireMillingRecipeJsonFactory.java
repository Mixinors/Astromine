package com.github.mixinors.astromine.datagen.recipe;

import com.github.mixinors.astromine.common.recipe.WireMillingRecipe;

import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;

public class WireMillingRecipeJsonFactory extends SimpleMachineRecipeJsonFactory<WireMillingRecipe> {
	protected WireMillingRecipeJsonFactory(Ingredient input, ItemConvertible output, int outputCount, int processingTime, int energy) {
		super(input, output, outputCount, processingTime, energy, WireMillingRecipe.Serializer.INSTANCE);
	}

	@Override
	public String getName() {
		return "wire_milling";
	}
}
