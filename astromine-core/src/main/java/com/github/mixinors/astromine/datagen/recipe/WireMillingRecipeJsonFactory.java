package com.github.mixinors.astromine.datagen.recipe;

import com.github.mixinors.astromine.common.recipe.PressingRecipe;
import com.github.mixinors.astromine.common.recipe.WireMillingRecipe;

import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;

public class WireMillingRecipeJsonFactory extends MachineRecipeJsonFactory<WireMillingRecipe> {
	protected WireMillingRecipeJsonFactory(Ingredient input, ItemConvertible output, int outputCount, int processingTime, int energy, WireMillingRecipe.Serializer serializer) {
		super(input, output, outputCount, processingTime, energy, serializer);
	}

	@Override
	public String getName() {
		return "wire_milling";
	}
}
