package com.github.chainmailstudios.astromine.foundations.datagen.generators.recipe;

import com.github.chainmailstudios.astromine.foundations.datagen.MaterialItemType;
import com.github.chainmailstudios.astromine.foundations.datagen.MaterialSet;

public abstract class SimpleProcessingRecipeGenerator implements RecipeGenerator {
	public final MaterialItemType input;
	public final int inputCount;
	public final MaterialItemType output;
	public final int outputCount;
	public final int time;

	public SimpleProcessingRecipeGenerator(MaterialItemType input, int inputCount, MaterialItemType output, int outputCount, int time) {
		this.input = input;
		this.inputCount = inputCount;
		this.output = output;
		this.outputCount = outputCount;
		this.time = time;
	}

	@Override
	public boolean shouldGenerate(MaterialSet set) {
		return set.hasType(input) && set.hasType(output);
	}

	@Override
	public MaterialItemType getOutput() {
		return output;
	}
}
