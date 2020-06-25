package com.github.chainmailstudios.astromine.client.rei.electricalsmelting;

import com.github.chainmailstudios.astromine.client.rei.AstromineREIPlugin;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import me.shedaniel.rei.plugin.cooking.DefaultCookingDisplay;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ElectricalSmeltingDisplay extends DefaultCookingDisplay {
	private final Fraction energyRequired;

	static {
		getFuel().clear();
	}

	public ElectricalSmeltingDisplay(AbstractCookingRecipe recipe) {
		super(recipe);
		this.energyRequired = Fraction.simplify(new Fraction(recipe.getCookTime(), 4 * 10 * 20));
	}

	@Override
	public Identifier getRecipeCategory() {
		return AstromineREIPlugin.ELECTRICAL_SMELTING;
	}

	public Fraction getEnergyRequired() {
		return energyRequired;
	}
}
