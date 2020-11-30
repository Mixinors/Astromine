package com.github.chainmailstudios.astromine.datagen.generator.recipe.set;

import com.github.chainmailstudios.astromine.datagen.generator.recipe.set.base.CookingSetRecipeGenerator;
import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;
import com.github.chainmailstudios.astromine.datagen.material.MaterialSet;
import me.shedaniel.cloth.api.datagen.v1.RecipeData;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;

public class BlastingSetRecipeGenerator extends CookingSetRecipeGenerator {
	public BlastingSetRecipeGenerator(MaterialItemType input, MaterialItemType output, int time, float experience) {
		super(input, output, time, experience);
	}

	public BlastingSetRecipeGenerator(MaterialItemType input, MaterialItemType output, float experience) {
		this(input, output, 100, experience);
	}

	public BlastingSetRecipeGenerator(MaterialItemType input, MaterialItemType output) {
		this(input, output, 100, 0.1f);
	}

	@Override
	public void generate(RecipeData recipes, MaterialSet set) {
		SimpleCookingRecipeBuilder
			.blasting(
				set.getIngredient(input),
				set.getItem(output),
				experience,
				time)
			.unlockedBy("impossible", new ImpossibleTrigger.TriggerInstance())
			.save(recipes, getRecipeId(set));
	}

	@Override
	public String getRecipeName(MaterialSet set) {
		return set.getItemIdPath(output) + "_from_blasting_" + input.getName();
	}
}
