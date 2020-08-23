package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.generator.material.MaterialSet;
import com.github.chainmailstudios.astromine.registry.AstromineRecipeGenerators;
import com.github.chainmailstudios.astromine.registry.AstromineTagGenerators;
import me.shedaniel.cloth.api.datagen.v1.RecipeData;
import me.shedaniel.cloth.api.datagen.v1.TagData;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class AstromineMaterialSets {
	public static final List<MaterialSet> MATERIAL_SETS = new ArrayList<>();

	public static MaterialSet register(MaterialSet set) {
		MATERIAL_SETS.add(set);
		System.out.println("registered material " + set.getName());
		return set;
	}

	public static void generateRecipes(RecipeData recipes) {
		MATERIAL_SETS.forEach((set) -> {
			try {
				AstromineRecipeGenerators.generateRecipes(recipes, set);
				System.out.println("generated recipes for " + set.getName());
			} catch (Exception e) {
				System.out.println("oh fuck recipe *big* bronked for " + set.getName());
				System.out.println(e.getMessage());
			}
		});
	}

	public static void generateTags(TagData tags) {
		MATERIAL_SETS.forEach((set) -> {
			try {
				AstromineTagGenerators.generateSetTags(tags, set);
				set.generateTags(tags);
				System.out.println("generated tags for " + set.getName());
			} catch (Exception e) {
				System.out.println("oh fuck tag bronked for " + set.getName());
				System.out.println(e.getMessage());
			}
		});
	}

	public static void initialize() {

	}
}
