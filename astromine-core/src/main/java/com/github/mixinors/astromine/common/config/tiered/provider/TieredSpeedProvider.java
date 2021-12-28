package com.github.mixinors.astromine.common.config.tiered.provider;

import com.github.mixinors.astromine.common.util.tier.MachineTier;

public interface TieredSpeedProvider {
	double getBaseSpeed();
	double getSpeedModifier(MachineTier tier);

	default double getSpeed(MachineTier tier) {
		return getBaseSpeed() * getSpeedModifier(tier);
	}
}
