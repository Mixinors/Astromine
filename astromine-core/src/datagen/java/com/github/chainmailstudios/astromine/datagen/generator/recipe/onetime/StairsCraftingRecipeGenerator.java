package com.github.chainmailstudios.astromine.datagen.generator.recipe.onetime;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class StairsCraftingRecipeGenerator extends ShapedCraftingRecipeGenerator {
	public StairsCraftingRecipeGenerator(ItemLike output, Ingredient base) {
		super(output, 4, "#  ", "## ", "###");
		this.addIngredient('#', base);
	}
}
