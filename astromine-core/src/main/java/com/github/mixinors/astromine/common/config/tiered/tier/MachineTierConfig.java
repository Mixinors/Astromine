package com.github.mixinors.astromine.common.config.tiered.tier;

import com.github.mixinors.astromine.common.config.tiered.tier.provider.EnergyStorageProvider;
import com.github.mixinors.astromine.common.config.tiered.tier.provider.SpeedModifierProvider;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class MachineTierConfig extends TierConfig implements SpeedModifierProvider, EnergyStorageProvider {
	@Comment("A modifier for this tier applied to the machine's base speed.")
	public double speedModifier;
	@Comment("The maximum energy able to be stored by this tier of the machine.")
	public long energyStorage;

	public MachineTierConfig(double speedModifier, long energyStorage) {
		this.speedModifier = speedModifier;
		this.energyStorage = energyStorage;
	}

	@Override
	public double getSpeedModifier() {
		return speedModifier;
	}

	@Override
	public long getEnergyStorage() {
		return energyStorage;
	}
}
