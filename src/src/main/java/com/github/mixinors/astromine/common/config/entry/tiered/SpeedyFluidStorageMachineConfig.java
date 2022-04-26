package com.github.mixinors.astromine.common.config.entry.tiered;

import com.github.mixinors.astromine.common.config.DefaultConfigValues;

public class SpeedyFluidStorageMachineConfig extends FluidStorageMachineConfig {
	@Override
	public double getDefaultBaseSpeed() {
		return DefaultConfigValues.FLUID_RECIPE_MACHINE_BASE_SPEED;
	}
}
