package com.github.chainmailstudios.astromine.datagen.generator.recipe.set;

import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;

public class Crafting3x3SetRecipeGenerator extends ShapedCraftingSetRecipeGenerator {
	public Crafting3x3SetRecipeGenerator(MaterialItemType input, MaterialItemType output) {
		super(input, output, "I##", "###", "###");
	}

	@Override
	public String getRecipeSuffix() {
		return "_from_" + input.getName();
	}
}
