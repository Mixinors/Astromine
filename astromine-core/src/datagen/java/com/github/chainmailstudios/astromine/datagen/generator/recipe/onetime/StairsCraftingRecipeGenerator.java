package com.github.chainmailstudios.astromine.datagen.generator.recipe.onetime;

import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;

public class StairsCraftingRecipeGenerator extends ShapedCraftingRecipeGenerator {
	public StairsCraftingRecipeGenerator(ItemConvertible output, Ingredient base) {
		super(output, 4, "#  ", "## ", "###");
		this.addIngredient('#', base);
	}
}
