package com.github.chainmailstudios.astromine.foundations.datagen.generators.recipe;

import com.github.chainmailstudios.astromine.common.generator.material.MaterialItemType;
import com.github.chainmailstudios.astromine.common.generator.material.MaterialSet;
import me.shedaniel.cloth.api.datagen.v1.RecipeData;
import net.minecraft.advancement.criterion.ImpossibleCriterion;
import net.minecraft.data.server.recipe.CookingRecipeJsonFactory;

public class SmeltingRecipeGenerator extends CookingRecipeGenerator {
	public SmeltingRecipeGenerator(MaterialItemType input, MaterialItemType output, int time, float experience) {
		super(input, output, time, experience);
	}

	public SmeltingRecipeGenerator(MaterialItemType input, MaterialItemType output, float experience) {
		this(input, output, 200, experience);
	}

	public SmeltingRecipeGenerator(MaterialItemType input, MaterialItemType output) {
		this(input, output, 200, 0.1f);
	}

	@Override
	public void generate(RecipeData recipes, MaterialSet set) {
		CookingRecipeJsonFactory
			.createSmelting(
				set.getIngredient(input),
				set.getItem(output),
				experience,
				time)
			.criterion("impossible", new ImpossibleCriterion.Conditions())
			.offerTo(recipes, getRecipeId(set));
	}

	@Override
	public String getRecipeName(MaterialSet set) {
		return set.getItemIdPath(output) + "_from_smelting_" + input.getName();
	}
}
