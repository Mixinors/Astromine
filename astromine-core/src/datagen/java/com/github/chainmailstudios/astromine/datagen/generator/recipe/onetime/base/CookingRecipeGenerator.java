package com.github.chainmailstudios.astromine.datagen.generator.recipe.onetime.base;

import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;

public abstract class CookingRecipeGenerator extends SimpleProcessingRecipeGenerator {
	public final float experience;

	public CookingRecipeGenerator(Ingredient input, ItemConvertible output, int time, float experience) {
		super(input, 1, output, 1, time);
		this.experience = experience;
	}

	public CookingRecipeGenerator(Ingredient input, ItemConvertible output, int time) {
		this(input, output, time, 0.1f);
	}

	@Override
	public String getGeneratorName() {
		return "cooking";
	}
}
