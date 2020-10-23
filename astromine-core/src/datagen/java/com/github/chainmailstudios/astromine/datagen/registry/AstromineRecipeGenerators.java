package com.github.chainmailstudios.astromine.datagen.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.datagen.generator.recipe.onetime.base.OneTimeRecipeGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.recipe.set.base.SetRecipeGenerator;
import com.github.chainmailstudios.astromine.datagen.material.MaterialSet;
import me.shedaniel.cloth.api.datagen.v1.RecipeData;

import java.util.ArrayList;
import java.util.List;

public abstract class AstromineRecipeGenerators {
	private final List<SetRecipeGenerator> SET_GENERATORS = new ArrayList<>();
	private final List<OneTimeRecipeGenerator> ONE_TIME_GENERATORS = new ArrayList<>();

	public SetRecipeGenerator register(SetRecipeGenerator generator) {
		SET_GENERATORS.add(generator);
		return generator;
	}

	public OneTimeRecipeGenerator register(OneTimeRecipeGenerator generator) {
		ONE_TIME_GENERATORS.add(generator);
		return generator;
	}

	public void generateRecipes(RecipeData recipes) {
		AstromineMaterialSets.getMaterialSets().forEach((set) -> generateSetRecipes(recipes, set));
		generateOneTimeRecipes(recipes);
	}

	private void generateSetRecipes(RecipeData recipes, MaterialSet set) {
		SET_GENERATORS.forEach((generator) -> {
			try {
				if (set.shouldGenerate(generator)) {
					generator.generate(recipes, set);
					AstromineCommon.LOGGER.info("Recipe generation of " + set.getName() + " succeeded, with generator " + generator.getGeneratorName() + ".");
				}
			} catch (Exception exception) {
				AstromineCommon.LOGGER.error("Recipe generation of " + set.getName() + " failed, with generator " + generator.getGeneratorName() + ".");
				AstromineCommon.LOGGER.error(exception.getMessage());
			}
		});
	}

	private void generateOneTimeRecipes(RecipeData recipes) {
		ONE_TIME_GENERATORS.forEach((generator) -> {
			try {
				generator.generate(recipes);
			} catch (Exception exception) {
				AstromineCommon.LOGGER.error("Recipe generation failed, with generator " + generator.getGeneratorName() + ".");
				AstromineCommon.LOGGER.error(exception.getMessage());
			}
		});
	}
}
