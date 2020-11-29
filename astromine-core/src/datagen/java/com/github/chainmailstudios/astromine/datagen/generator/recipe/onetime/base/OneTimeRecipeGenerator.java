package com.github.chainmailstudios.astromine.datagen.generator.recipe.onetime.base;

import net.minecraft.item.ItemConvertible;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import com.github.chainmailstudios.astromine.datagen.generator.recipe.RecipeGenerator;

public abstract class OneTimeRecipeGenerator implements RecipeGenerator {
	public final ItemConvertible output;
	public final int outputCount;
	public ResourceLocation recipeId;

	public OneTimeRecipeGenerator(ItemConvertible output, int outputCount) {
		this.output = output;
		this.outputCount = outputCount;
		this.recipeId = Registry.ITEM.getId(output.asItem());
	}

	public ResourceLocation getRecipeId() {
		return recipeId;
	}
}
