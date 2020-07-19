package com.github.chainmailstudios.astromine.client.rei.electricsmelting;

import com.github.chainmailstudios.astromine.client.rei.AstromineREIPlugin;
import me.shedaniel.rei.plugin.cooking.DefaultCookingDisplay;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ElectricSmeltingDisplay extends DefaultCookingDisplay {
	private final double energyRequired;

	static {
		getFuel().clear();
	}

	public ElectricSmeltingDisplay(AbstractCookingRecipe recipe) {
		super(recipe);
		this.energyRequired = recipe.getCookTime() / 3 * 15;
	}

	@Override
	public Identifier getRecipeCategory() {
		return AstromineREIPlugin.ELECTRIC_SMELTING;
	}

	public double getEnergyRequired() {
		return energyRequired;
	}
}
