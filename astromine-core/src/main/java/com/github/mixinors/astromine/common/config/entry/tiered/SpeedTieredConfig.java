package com.github.mixinors.astromine.common.config.entry.tiered;

import com.github.mixinors.astromine.common.config.entry.provider.tiered.DefaultedTieredSpeedProvider;
import com.github.mixinors.astromine.common.config.entry.tiered.tier.SpeedModifierTierConfig;
import com.github.mixinors.astromine.common.util.tier.MachineTier;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public abstract class SpeedTieredConfig<T extends SpeedModifierTierConfig> extends TieredConfig<T> implements DefaultedTieredSpeedProvider {
	@Comment("The base speed of the machine.")
	public double baseSpeed = getDefaultBaseSpeed();

	@Override
	public double getBaseSpeed() {
		return baseSpeed;
	}

	@Override
	public double getSpeedModifier(MachineTier tier) {
		return getTierConfig(tier).getSpeedModifier();
	}
}
