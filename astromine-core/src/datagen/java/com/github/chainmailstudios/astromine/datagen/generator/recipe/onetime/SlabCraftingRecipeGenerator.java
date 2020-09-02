package com.github.chainmailstudios.astromine.datagen.generator.recipe.onetime;

import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;

public class SlabCraftingRecipeGenerator extends ShapedCraftingRecipeGenerator {
	public SlabCraftingRecipeGenerator(ItemConvertible output, Ingredient base) {
		super(output, 6, "###");
		this.addIngredient('#', base);
	}
}
