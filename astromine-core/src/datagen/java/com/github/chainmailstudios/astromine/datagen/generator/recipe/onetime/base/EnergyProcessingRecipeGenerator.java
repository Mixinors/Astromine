package com.github.chainmailstudios.astromine.datagen.generator.recipe.onetime.base;

import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;

public abstract class EnergyProcessingRecipeGenerator extends SimpleProcessingRecipeGenerator {
	public final int energy;

	public EnergyProcessingRecipeGenerator(Ingredient input, int inputCount, ItemConvertible output, int outputCount, int time, int energy) {
		super(input, inputCount, output, outputCount, time);
		this.energy = energy;
	}
}
