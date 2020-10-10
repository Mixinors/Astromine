package com.github.chainmailstudios.astromine.datagen.generator.recipe.onetime;

import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;

public class WallCraftingRecipeGenerator extends ShapedCraftingRecipeGenerator {
	public WallCraftingRecipeGenerator(ItemConvertible output, Ingredient base) {
		super(output, 6, "###", "###");
		this.addIngredient('#', base);
	}
}
