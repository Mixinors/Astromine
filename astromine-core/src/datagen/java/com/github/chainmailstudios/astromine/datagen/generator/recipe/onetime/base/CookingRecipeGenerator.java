package com.github.chainmailstudios.astromine.datagen.generator.recipe.onetime.base;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public abstract class CookingRecipeGenerator extends SimpleProcessingRecipeGenerator {
	public final float experience;

	public CookingRecipeGenerator(Ingredient input, ItemLike output, int time, float experience) {
		super(input, 1, output, 1, time);
		this.experience = experience;
	}

	public CookingRecipeGenerator(Ingredient input, ItemLike output, int time) {
		this(input, output, time, 0.1f);
	}

	@Override
	public String getGeneratorName() {
		return "cooking";
	}
}
