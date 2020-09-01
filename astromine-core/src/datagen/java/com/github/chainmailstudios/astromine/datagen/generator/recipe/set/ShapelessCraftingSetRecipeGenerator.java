package com.github.chainmailstudios.astromine.datagen.generator.recipe.set;

import net.minecraft.advancement.criterion.ImpossibleCriterion;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonFactory;

import com.github.chainmailstudios.astromine.datagen.generator.recipe.set.base.CraftingSetRecipeGenerator;
import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;
import com.github.chainmailstudios.astromine.datagen.material.MaterialSet;
import me.shedaniel.cloth.api.datagen.v1.RecipeData;

public class ShapelessCraftingSetRecipeGenerator extends CraftingSetRecipeGenerator {
	public ShapelessCraftingSetRecipeGenerator(MaterialItemType input, MaterialItemType output, int outputCount) {
		super(input, output, outputCount);
	}

	public ShapelessCraftingSetRecipeGenerator(MaterialItemType input, MaterialItemType output) {
		this(input, output, 1);
	}

	@Override
	public String getRecipeSuffix() {
		return "_from_" + input.getName();
	}

	@Override
	public void generate(RecipeData recipes, MaterialSet set) {
		ShapelessRecipeJsonFactory
				.create(set.getItem(output), outputCount)
				.criterion("impossible", new ImpossibleCriterion.Conditions())
				.input(set.getIngredient(input))
				.offerTo(recipes, getRecipeId(set));
	}

	@Override
	public String getGeneratorName() {
		return "shapeless";
	}
}
