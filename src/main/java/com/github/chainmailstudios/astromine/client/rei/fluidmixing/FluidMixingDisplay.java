package com.github.chainmailstudios.astromine.client.rei.fluidmixing;

import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.client.rei.AstromineREIPlugin;
import com.github.chainmailstudios.astromine.common.recipe.FluidMixingRecipe;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;

public class FluidMixingDisplay extends AbstractFluidMixingDisplay {
	public FluidMixingDisplay(FluidMixingRecipe recipe) {
		super(
				recipe.getEnergyConsumed(),
				new FluidVolume(recipe.getFirstInputFluid(), recipe.getFirstInputAmount()),
				new FluidVolume(recipe.getSecondInputFluid(), recipe.getSecondInputAmount()),
				new FluidVolume(recipe.getOutputFluid(), recipe.getOutputAmount()),
				recipe.getId()
		);
	}

	@Override
	public Identifier getRecipeCategory() {
		return AstromineREIPlugin.FLUID_MIXING;
	}
}
