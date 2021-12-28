package com.github.mixinors.astromine.common.config.tiered;

import com.github.mixinors.astromine.common.config.DefaultConfigValues;
import com.github.mixinors.astromine.common.config.tiered.provider.DefaultedTieredEnergyStorageProvider;
import com.github.mixinors.astromine.common.config.tiered.provider.DefaultedTieredSpeedProvider;
import com.github.mixinors.astromine.common.config.tiered.tier.MachineTierConfig;
import com.github.mixinors.astromine.common.util.tier.MachineTier;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public abstract class MachineTieredConfig<T extends MachineTierConfig> extends TieredConfig<T> implements DefaultedTieredSpeedProvider, DefaultedTieredEnergyStorageProvider {
	@Comment("The base speed of the machine.")
	public double speed = 1.0d;

	@Override
	public double getBaseSpeed() {
		return speed;
	}

	@Override
	public double getSpeedModifier(MachineTier tier) {
		return getTierConfig(tier).getSpeedModifier();
	}

	@Override
	public long getEnergyStorage(MachineTier tier) {
		return getTierConfig(tier).getEnergyStorage();
	}
}
