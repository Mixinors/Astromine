package com.github.chainmailstudios.astromine.foundations.datagen.generators.recipe;

import com.github.chainmailstudios.astromine.datagen.generator.recipe.set.ShapedCraftingSetRecipeGenerator;
import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;

public class WrenchCraftingRecipeGenerator extends ShapedCraftingSetRecipeGenerator {
	public WrenchCraftingRecipeGenerator() {
		super(MaterialItemType.INGOT, MaterialItemType.WRENCH, "# #", " G ", " P ");
		this.addType('G', MaterialItemType.GEAR);
		this.addType('P', MaterialItemType.PLATE);
	}
}
