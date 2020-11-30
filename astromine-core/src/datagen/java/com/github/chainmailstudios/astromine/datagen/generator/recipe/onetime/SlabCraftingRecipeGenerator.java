package com.github.chainmailstudios.astromine.datagen.generator.recipe.onetime;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class SlabCraftingRecipeGenerator extends ShapedCraftingRecipeGenerator {
	public SlabCraftingRecipeGenerator(ItemLike output, Ingredient base) {
		super(output, 6, "###");
		this.addIngredient('#', base);
	}
}
