package com.github.chainmailstudios.astromine.datagen.generator.recipe.onetime.base;

import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;

public abstract class SimpleProcessingRecipeGenerator extends OneTimeRecipeGenerator {
	public final Ingredient input;
	public final int inputCount;
	public final int time;

	public SimpleProcessingRecipeGenerator(Ingredient input, int inputCount, ItemConvertible output, int outputCount, int time) {
		super(output, outputCount);
		this.input = input;
		this.inputCount = inputCount;
		this.time = time;
	}
}
