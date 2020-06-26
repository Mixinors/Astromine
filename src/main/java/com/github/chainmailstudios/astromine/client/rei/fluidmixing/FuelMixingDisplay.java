package com.github.chainmailstudios.astromine.client.rei.fluidmixing;

import com.github.chainmailstudios.astromine.client.rei.AstromineREIPlugin;
import com.github.chainmailstudios.astromine.common.recipe.FuelMixingRecipe;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import net.minecraft.util.Identifier;

public class FuelMixingDisplay extends AbstractFluidMixingDisplay {
	public FuelMixingDisplay(FuelMixingRecipe recipe) {
		super(
				recipe.getEnergyConsumed().copy(),
				new FluidVolume(recipe.getInputFluid(), recipe.getInputAmount()),
				new FluidVolume(recipe.getOutputFluid(), recipe.getOutputAmount()),
				recipe.getId()
		);
	}

	@Override
	public Identifier getRecipeCategory() {
		return AstromineREIPlugin.FUEL_MIXING;
	}
}
