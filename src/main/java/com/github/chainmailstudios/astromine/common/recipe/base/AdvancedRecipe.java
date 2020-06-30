package com.github.chainmailstudios.astromine.common.recipe.base;

import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.Recipe;

public interface AdvancedRecipe<C extends Inventory> extends Recipe<C> {
	/**
	 * Whenever the recipe is ticked, the associated object
	 * should be updated.
	 */
	<T extends Timed> void tick(T t);
}
