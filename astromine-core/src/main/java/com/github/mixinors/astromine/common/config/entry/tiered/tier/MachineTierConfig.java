package com.github.mixinors.astromine.common.config.entry.tiered.tier;

import com.github.mixinors.astromine.common.provider.EnergyStorageSizeProvider;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class MachineTierConfig extends SpeedModifierTierConfig implements EnergyStorageSizeProvider {
	@Comment("The maximum energy able to be stored by this tier of the machine.")
	public long energyStorageSize;

	public MachineTierConfig(double speedModifier, long energyStorageSize) {
		super(speedModifier);
		this.energyStorageSize = energyStorageSize;
	}

	@Override
	public long getEnergyStorageSize() {
		return energyStorageSize;
	}
}
