package com.github.chainmailstudios.astromine.datagen.generator.recipe.onetime.base;

import com.github.chainmailstudios.astromine.datagen.generator.recipe.RecipeGenerator;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;

public abstract class OneTimeRecipeGenerator implements RecipeGenerator {
	public final ItemLike output;
	public final int outputCount;
	public ResourceLocation recipeId;

	public OneTimeRecipeGenerator(ItemLike output, int outputCount) {
		this.output = output;
		this.outputCount = outputCount;
		this.recipeId = Registry.ITEM.getKey(output.asItem());
	}

	public ResourceLocation getRecipeId() {
		return recipeId;
	}
}
