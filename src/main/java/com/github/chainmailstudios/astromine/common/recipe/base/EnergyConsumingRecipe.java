package com.github.chainmailstudios.astromine.common.recipe.base;

import net.minecraft.inventory.Inventory;

public interface EnergyConsumingRecipe<C extends Inventory> extends AstromineRecipe<C> {
    double getEnergyConsumed();
}
