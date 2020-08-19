package com.github.chainmailstudios.astromine.foundations.datagen.generators.recipe;

import com.github.chainmailstudios.astromine.foundations.datagen.MaterialItemType;

public class Crafting3x3RecipeGenerator extends ShapedCraftingRecipeGenerator {
	public Crafting3x3RecipeGenerator(MaterialItemType input, MaterialItemType output) {
		super(input, output, "###", "###", "###");
	}

	@Override
	public String getRecipeSuffix() {
		return "_from_"+input.getName();
	}
}
