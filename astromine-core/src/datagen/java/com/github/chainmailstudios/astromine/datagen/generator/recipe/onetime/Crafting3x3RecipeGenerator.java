package com.github.chainmailstudios.astromine.datagen.generator.recipe.onetime;

import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;

public class Crafting3x3RecipeGenerator extends ShapedCraftingRecipeGenerator {
	public Crafting3x3RecipeGenerator(Identifier output, Ingredient input) {
		super(output, "###", "###", "###");
		this.addIngredient('#', input);
	}
}
