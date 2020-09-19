package com.github.chainmailstudios.astromine.datagen.generator.recipe.set;

import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;

public class Crafting2x2SetRecipeGenerator extends ShapedCraftingSetRecipeGenerator {
	public Crafting2x2SetRecipeGenerator(MaterialItemType input, MaterialItemType output) {
		super(input, output, "I#", "##");
	}

	@Override
	public String getRecipeSuffix() {
		return "_from_" + input.getName();
	}
}
