package com.github.mixinors.astromine.common.recipe.base;

import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.Recipe;

public interface TimeBasedRecipe extends Recipe<Inventory> {
	int getTime();
}
