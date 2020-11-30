package com.github.chainmailstudios.astromine.datagen.generator.recipe.onetime.base;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public abstract class EnergyProcessingRecipeGenerator extends SimpleProcessingRecipeGenerator {
	public final int energy;

	public EnergyProcessingRecipeGenerator(Ingredient input, int inputCount, ItemLike output, int outputCount, int time, int energy) {
		super(input, inputCount, output, outputCount, time);
		this.energy = energy;
	}
}
