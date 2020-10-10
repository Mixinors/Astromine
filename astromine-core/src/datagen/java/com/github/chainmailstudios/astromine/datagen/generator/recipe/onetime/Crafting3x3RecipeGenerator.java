package com.github.chainmailstudios.astromine.datagen.generator.recipe.onetime;

import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;

public class Crafting3x3RecipeGenerator extends ShapedCraftingRecipeGenerator {
	public Crafting3x3RecipeGenerator(ItemConvertible output, Ingredient input) {
		super(output, "###", "###", "###");
		this.addIngredient('#', input);
	}
}
