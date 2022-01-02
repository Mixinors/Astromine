package com.github.mixinors.astromine.common.config.entry.provider.tiered;

import com.github.mixinors.astromine.common.config.DefaultConfigValues;
import com.github.mixinors.astromine.common.provider.tiered.TieredSpeedProvider;
import com.github.mixinors.astromine.common.util.tier.MachineTier;

public interface DefaultedTieredSpeedProvider extends TieredSpeedProvider {
	default double getDefaultBaseSpeed() {
		return DefaultConfigValues.BASE_SPEED;
	}

	default double getDefaultSpeedModifier(MachineTier tier) {
		return switch(tier) {
			case PRIMITIVE -> DefaultConfigValues.PRIMITIVE_SPEED_MODIFIER;
			case BASIC -> DefaultConfigValues.BASIC_SPEED_MODIFIER;
			case ADVANCED -> DefaultConfigValues.ADVANCED_SPEED_MODIFIER;
			case ELITE -> DefaultConfigValues.ELITE_SPEED_MODIFIER;
			case CREATIVE -> Double.MAX_VALUE;
		};
	}
}
