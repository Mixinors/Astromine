package com.github.chainmailstudios.astromine.datagen.generator.recipe.onetime.base;

import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;

public abstract class SimpleProcessingRecipeGenerator extends OneTimeRecipeGenerator {
	public final Ingredient input;
	public final int inputCount;
	public final int time;

	public SimpleProcessingRecipeGenerator(Ingredient input, int inputCount, Identifier output, int outputCount, int time) {
		super(output, outputCount);
		this.input = input;
		this.inputCount = inputCount;
		this.time = time;
	}
}
