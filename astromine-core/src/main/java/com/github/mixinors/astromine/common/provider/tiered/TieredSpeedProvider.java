package com.github.mixinors.astromine.common.provider.tiered;

import com.github.mixinors.astromine.common.util.tier.MachineTier;

public interface TieredSpeedProvider {
	double getBaseSpeed();
	double getSpeedModifier(MachineTier tier);

	default double getSpeed(MachineTier tier) {
		if(getBaseSpeed() == Double.MAX_VALUE) return Double.MAX_VALUE;
		return getBaseSpeed() * getSpeedModifier(tier);
	}
}
