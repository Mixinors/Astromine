package com.github.chainmailstudios.astromine.common.recipe.base;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;

public interface AstromineRecipe<C extends Inventory> extends Recipe<C> {
    @Override
    ItemStack getRecipeKindIcon();
}
