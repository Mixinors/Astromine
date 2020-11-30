package com.github.chainmailstudios.astromine.foundations.datagen.generators.recipe;

import com.github.chainmailstudios.astromine.datagen.generator.recipe.set.ShapedCraftingSetRecipeGenerator;
import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class AppleCraftingRecipeGenerator extends ShapedCraftingSetRecipeGenerator {
	public AppleCraftingRecipeGenerator() {
		super(MaterialItemType.INGOT, MaterialItemType.APPLE, "###", "#A#", "###");
		this.addIngredient('A', Ingredient.of(Items.APPLE));
	}
}
