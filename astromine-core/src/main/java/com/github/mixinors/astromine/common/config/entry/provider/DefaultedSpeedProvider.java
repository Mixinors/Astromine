package com.github.mixinors.astromine.common.config.entry.provider;

import com.github.mixinors.astromine.common.config.DefaultConfigValues;
import com.github.mixinors.astromine.common.provider.SpeedProvider;

public interface DefaultedSpeedProvider extends SpeedProvider {
	default double getDefaultSpeed() {
		return DefaultConfigValues.UTILITY_DELAY;
	}
}
