package com.github.chainmailstudios.astromine.datagen.generator.recipe.onetime.base;

import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.datagen.generator.recipe.RecipeGenerator;

public abstract class OneTimeRecipeGenerator implements RecipeGenerator {
	public final Identifier output;
	public final int outputCount;
	public Identifier recipeId;

	public OneTimeRecipeGenerator(Identifier output, int outputCount) {
		this.output = output;
		this.outputCount = outputCount;
		this.recipeId = output;
	}

	public Identifier getRecipeId() {
		return recipeId;
	}

	public OneTimeRecipeGenerator setRecipeId(Identifier recipeId) {
		this.recipeId = recipeId;
		return this;
	}
}
