package com.github.chainmailstudios.astromine.datagen.generator.recipe.onetime;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class Crafting2x2RecipeGenerator extends ShapedCraftingRecipeGenerator {
	public Crafting2x2RecipeGenerator(ItemLike output, Ingredient input) {
		this(output, input, 1);
	}

	public Crafting2x2RecipeGenerator(ItemLike output, Ingredient input, int outputCount) {
		super(output, outputCount, "##", "##");
		this.addIngredient('#', input);
	}
}
