package com.github.chainmailstudios.astromine.common.recipe.base;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;

public interface EnergyGeneratingRecipe<C extends Inventory> extends AstromineRecipe<C> {
    double getEnergyGenerated();
}
