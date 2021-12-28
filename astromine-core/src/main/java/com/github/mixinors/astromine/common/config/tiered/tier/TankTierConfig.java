package com.github.mixinors.astromine.common.config.tiered.tier;

import com.github.mixinors.astromine.common.config.tiered.tier.provider.FluidStorageProvider;
import com.github.mixinors.astromine.common.config.tiered.tier.provider.SpeedModifierProvider;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class TankTierConfig extends TierConfig implements SpeedModifierProvider, FluidStorageProvider {
	@Comment("A modifier for this tier applied to the machine's base speed.")
	public double speedModifier;
	@Comment("The maximum fluid able to be stored by this tier of the machine.")
	public long fluidStorage;

	public TankTierConfig(double speedModifier, long fluidStorage) {
		this.speedModifier = speedModifier;
		this.fluidStorage = fluidStorage;
	}

	@Override
	public double getSpeedModifier() {
		return speedModifier;
	}

	@Override
	public long getFluidStorage() {
		return fluidStorage;
	}
}
