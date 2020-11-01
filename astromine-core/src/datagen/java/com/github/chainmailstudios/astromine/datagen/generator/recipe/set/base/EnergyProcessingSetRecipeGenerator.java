package com.github.chainmailstudios.astromine.datagen.generator.recipe.set.base;

import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;

public abstract class EnergyProcessingSetRecipeGenerator extends SimpleProcessingSetRecipeGenerator {
	public final int energy;

	public EnergyProcessingSetRecipeGenerator(MaterialItemType input, int inputCount, MaterialItemType output, int outputCount, int time, int energy) {
		super(input, inputCount, output, outputCount, time);
		this.energy = energy;
	}
}
