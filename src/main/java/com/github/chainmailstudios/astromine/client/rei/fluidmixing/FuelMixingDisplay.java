package com.github.chainmailstudios.astromine.client.rei.fluidmixing;

import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.client.rei.AstromineREIPlugin;
import com.github.chainmailstudios.astromine.common.recipe.FuelMixingRecipe;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;

public class FuelMixingDisplay extends AbstractFluidMixingDisplay {
	public FuelMixingDisplay(FuelMixingRecipe recipe) {
		super(
				recipe.getEnergyConsumed().copy(),
				new FluidVolume(recipe.getFirstInputFluid(), recipe.getFirstInputAmount()),
				new FluidVolume(recipe.getOutputFluid(), recipe.getOutputAmount()),
				recipe.getId()
		);
	}

	@Override
	public Identifier getRecipeCategory() {
		return AstromineREIPlugin.FUEL_MIXING;
	}
}
