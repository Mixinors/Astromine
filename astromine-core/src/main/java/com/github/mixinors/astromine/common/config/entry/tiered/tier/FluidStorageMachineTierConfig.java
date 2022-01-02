package com.github.mixinors.astromine.common.config.entry.tiered.tier;

import com.github.mixinors.astromine.common.provider.FluidStorageSizeProvider;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class FluidStorageMachineTierConfig extends MachineTierConfig implements FluidStorageSizeProvider {
	@Comment("The maximum fluid able to be stored by this tier of the machine.")
	public long fluidStorageSize;

	public FluidStorageMachineTierConfig(double speedModifier, long energyStorage, long fluidStorageSize) {
		super(speedModifier, energyStorage);
		this.fluidStorageSize = fluidStorageSize;
	}

	@Override
	public long getFluidStorageSize() {
		return fluidStorageSize;
	}
}
