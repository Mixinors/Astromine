package com.github.chainmailstudios.astromine.datagen.generator.recipe.onetime.base;

import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;

public abstract class EnergyProcessingRecipeGenerator extends SimpleProcessingRecipeGenerator {
	public final int energyConsumed;

	public EnergyProcessingRecipeGenerator(Ingredient input, int inputCount, Identifier output, int outputCount, int time, int energyConsumed) {
		super(input, inputCount, output, outputCount, time);
		this.energyConsumed = energyConsumed;
	}
}
