package com.github.chainmailstudios.astromine.foundations.datagen.generators.recipe;

import com.github.chainmailstudios.astromine.foundations.datagen.MaterialItemType;

public abstract class EnergyProcessingRecipeGenerator extends SimpleProcessingRecipeGenerator {
	public final int energyConsumed;

	public EnergyProcessingRecipeGenerator(MaterialItemType input, int inputCount, MaterialItemType output, int outputCount, int time, int energyConsumed) {
		super(input, inputCount, output, outputCount, time);
		this.energyConsumed = energyConsumed;
	}
}
