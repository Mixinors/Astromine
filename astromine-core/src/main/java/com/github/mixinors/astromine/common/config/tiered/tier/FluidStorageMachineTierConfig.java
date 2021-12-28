package com.github.mixinors.astromine.common.config.tiered.tier;

import com.github.mixinors.astromine.common.config.tiered.tier.provider.FluidStorageProvider;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class FluidStorageMachineTierConfig extends MachineTierConfig implements FluidStorageProvider {
	@Comment("The maximum fluid able to be stored by this tier of the machine.")
	public long fluidStorage;

	public FluidStorageMachineTierConfig(double speedModifier, long energyStorage, long fluidStorage) {
		super(speedModifier, energyStorage);
		this.fluidStorage = fluidStorage;
	}

	@Override
	public long getFluidStorage() {
		return fluidStorage;
	}
}
