package com.github.chainmailstudios.astromine.datagen.generator.recipe.set.base;

import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;

public abstract class EnergyProcessingSetRecipeGenerator extends SimpleProcessingSetRecipeGenerator {
	public final int energyConsumed;

	public EnergyProcessingSetRecipeGenerator(MaterialItemType input, int inputCount, MaterialItemType output, int outputCount, int time, int energyConsumed) {
		super(input, inputCount, output, outputCount, time);
		this.energyConsumed = energyConsumed;
	}
}
