package com.github.mixinors.astromine.common.config.entry.provider;

import com.github.mixinors.astromine.common.config.DefaultConfigValues;
import com.github.mixinors.astromine.common.provider.EnergyConsumedProvider;
import com.github.mixinors.astromine.common.provider.SpeedProvider;

public interface DefaultedEnergyConsumedProvider extends EnergyConsumedProvider {
	default long getDefaultEnergyConsumed() {
		return DefaultConfigValues.UTILITY_ENERGY_CONSUMED;
	}
}