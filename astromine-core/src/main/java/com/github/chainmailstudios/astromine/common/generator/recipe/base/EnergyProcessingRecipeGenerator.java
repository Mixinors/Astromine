package com.github.chainmailstudios.astromine.common.generator.recipe.base;

import com.github.chainmailstudios.astromine.common.generator.material.MaterialItemType;

public abstract class EnergyProcessingRecipeGenerator extends SimpleProcessingRecipeGenerator {
	public final int energyConsumed;

	public EnergyProcessingRecipeGenerator(MaterialItemType input, int inputCount, MaterialItemType output, int outputCount, int time, int energyConsumed) {
		super(input, inputCount, output, outputCount, time);
		this.energyConsumed = energyConsumed;
	}
}
