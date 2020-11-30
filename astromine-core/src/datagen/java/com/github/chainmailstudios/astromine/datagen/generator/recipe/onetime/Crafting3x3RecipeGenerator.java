package com.github.chainmailstudios.astromine.datagen.generator.recipe.onetime;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class Crafting3x3RecipeGenerator extends ShapedCraftingRecipeGenerator {
	public Crafting3x3RecipeGenerator(ItemLike output, Ingredient input) {
		super(output, "###", "###", "###");
		this.addIngredient('#', input);
	}
}
