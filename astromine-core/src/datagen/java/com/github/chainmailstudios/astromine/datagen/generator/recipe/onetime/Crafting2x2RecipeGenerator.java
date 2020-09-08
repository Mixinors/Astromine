package com.github.chainmailstudios.astromine.datagen.generator.recipe.onetime;

import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;

public class Crafting2x2RecipeGenerator extends ShapedCraftingRecipeGenerator {
	public Crafting2x2RecipeGenerator(ItemConvertible output, Ingredient input) {
		super(output, "##", "##");
		this.addIngredient('#', input);
	}
}
