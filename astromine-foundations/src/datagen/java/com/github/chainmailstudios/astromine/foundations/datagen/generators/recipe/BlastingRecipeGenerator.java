package com.github.chainmailstudios.astromine.foundations.datagen.generators.recipe;

import net.minecraft.advancement.criterion.ImpossibleCriterion;
import net.minecraft.data.server.recipe.CookingRecipeJsonFactory;

import com.github.chainmailstudios.astromine.foundations.datagen.MaterialItemType;
import com.github.chainmailstudios.astromine.foundations.datagen.MaterialSet;
import me.shedaniel.cloth.api.datagen.v1.RecipeData;

public class BlastingRecipeGenerator extends CookingRecipeGenerator {
	public BlastingRecipeGenerator(MaterialItemType input, MaterialItemType output, int time, float experience) {
		super(input, output, time, experience);
	}

	public BlastingRecipeGenerator(MaterialItemType input, MaterialItemType output, float experience) {
		this(input, output, 100, experience);
	}

	public BlastingRecipeGenerator(MaterialItemType input, MaterialItemType output) {
		this(input, output, 100, 0.1f);
	}

	@Override
	public void generateRecipe(RecipeData recipes, MaterialSet set) {
		CookingRecipeJsonFactory
				.createBlasting(
						set.getIngredient(input),
						set.getItem(output),
						experience,
						time)
				.criterion("impossible", new ImpossibleCriterion.Conditions())
				.offerTo(recipes, getRecipeId(set));
	}

	@Override
	public String getRecipeName(MaterialSet set) {
		return set.getItemIdPath(output) + "_from_blasting_" + input.getName();
	}
}
