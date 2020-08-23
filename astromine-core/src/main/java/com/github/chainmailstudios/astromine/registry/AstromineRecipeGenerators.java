package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.common.generator.entrypoint.RecipeGeneratorInitializer;
import com.github.chainmailstudios.astromine.common.generator.entrypoint.TagGeneratorInitializer;
import com.github.chainmailstudios.astromine.common.generator.material.MaterialSet;
import com.github.chainmailstudios.astromine.common.generator.recipe.RecipeGenerator;
import me.shedaniel.cloth.api.datagen.v1.RecipeData;
import net.fabricmc.loader.api.FabricLoader;

import java.util.ArrayList;
import java.util.List;
public class AstromineRecipeGenerators {
	private static final List<RecipeGenerator> GENERATORS = new ArrayList<>();

	public static RecipeGenerator register(RecipeGenerator generator) {
		GENERATORS.add(generator);
		return generator;
	}

	public static void generateRecipes(RecipeData recipes, MaterialSet set) {
		GENERATORS.forEach((generator) -> {
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

	public static void initialize() {

	}
}
