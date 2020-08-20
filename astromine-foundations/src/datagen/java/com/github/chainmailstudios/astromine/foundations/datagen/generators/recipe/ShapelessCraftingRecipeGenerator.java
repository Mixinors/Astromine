package com.github.chainmailstudios.astromine.foundations.datagen.generators.recipe;

import com.github.chainmailstudios.astromine.foundations.datagen.MaterialItemType;
import com.github.chainmailstudios.astromine.foundations.datagen.MaterialSet;
import me.shedaniel.cloth.api.datagen.v1.RecipeData;
import net.minecraft.advancement.criterion.ImpossibleCriterion;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonFactory;

public class ShapelessCraftingRecipeGenerator extends CraftingRecipeGenerator {
	public ShapelessCraftingRecipeGenerator(MaterialItemType input, MaterialItemType output, int outputCount) {
		super(input, output, outputCount);
	}

	public ShapelessCraftingRecipeGenerator(MaterialItemType input, MaterialItemType output) {
		this(input, output, 1);
	}

	@Override
	public String getRecipeSuffix() {
		return "_from_" + input.getName();
	}

	@Override
	public void generateRecipe(RecipeData recipes, MaterialSet set) {
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
