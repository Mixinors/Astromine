package com.github.chainmailstudios.astromine.client.rei.electricsmelting;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.client.rei.AstromineREIPlugin;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import me.shedaniel.rei.plugin.cooking.DefaultCookingDisplay;

@Environment(EnvType.CLIENT)
public class ElectricSmeltingDisplay extends DefaultCookingDisplay {
	private final Fraction energyRequired;

	static {
		getFuel().clear();
	}

	public ElectricSmeltingDisplay(AbstractCookingRecipe recipe) {
		super(recipe);
		this.energyRequired = Fraction.simplify(new Fraction(recipe.getCookTime(), 4 * 10 * 20));
	}

	@Override
	public Identifier getRecipeCategory() {
		return AstromineREIPlugin.ELECTRIC_SMELTING;
	}

	public Fraction getEnergyRequired() {
		return energyRequired;
	}
}
