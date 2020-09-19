package com.github.chainmailstudios.astromine.foundations.datagen.generators.recipe;

import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;

import com.github.chainmailstudios.astromine.datagen.generator.recipe.set.ShapedCraftingSetRecipeGenerator;
import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;

public class AppleCraftingRecipeGenerator extends ShapedCraftingSetRecipeGenerator {
	public AppleCraftingRecipeGenerator() {
		super(MaterialItemType.INGOT, MaterialItemType.APPLE, "###", "#A#", "###");
		this.addIngredient('A', Ingredient.ofItems(Items.APPLE));
	}
}
