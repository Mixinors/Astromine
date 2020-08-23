package com.github.chainmailstudios.astromine.datagen.registry;

import com.github.chainmailstudios.astromine.datagen.generator.recipe.onetime.base.OneTimeRecipeGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.recipe.set.base.SetRecipeGenerator;
import com.github.chainmailstudios.astromine.datagen.material.MaterialSet;
import me.shedaniel.cloth.api.datagen.v1.LootTableData;
import me.shedaniel.cloth.api.datagen.v1.RecipeData;

import java.util.ArrayList;
import java.util.List;

public class AstromineRecipeGenerators {
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
		AstromineMaterialSets.getMaterialSets().forEach((set) -> {
			generateSetRecipes(recipes, set);
		});
		generateOneTimeRecipes(recipes);
	}

	private void generateSetRecipes(RecipeData recipes, MaterialSet set) {
		SET_GENERATORS.forEach((generator) -> {
			try {
				if (set.shouldGenerate(generator)) {
					generator.generate(recipes, set);
					System.out.println("generated recipe " + generator.getRecipeId(set));
				}
			} catch (Exception e) {
				System.out.println("oh fuck recipe bronked for " + set.getName() + " with generator " + generator.getString());
				System.out.println(e.getMessage());
			}
		});
	}

	private void generateOneTimeRecipes(RecipeData recipes) {
		ONE_TIME_GENERATORS.forEach((generator) -> generator.generate(recipes));
	}
}
