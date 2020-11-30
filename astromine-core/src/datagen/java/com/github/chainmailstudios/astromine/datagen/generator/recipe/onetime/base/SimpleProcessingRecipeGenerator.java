package com.github.chainmailstudios.astromine.datagen.generator.recipe.onetime.base;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public abstract class SimpleProcessingRecipeGenerator extends OneTimeRecipeGenerator {
	public final Ingredient input;
	public final int inputCount;
	public final int time;

	public SimpleProcessingRecipeGenerator(Ingredient input, int inputCount, ItemLike output, int outputCount, int time) {
		super(output, outputCount);
		this.input = input;
		this.inputCount = inputCount;
		this.time = time;
	}
}
