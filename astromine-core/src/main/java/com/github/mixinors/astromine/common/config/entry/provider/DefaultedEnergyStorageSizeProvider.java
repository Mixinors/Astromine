package com.github.mixinors.astromine.common.config.entry.provider;

import com.github.mixinors.astromine.common.config.DefaultConfigValues;
import com.github.mixinors.astromine.common.provider.EnergyStorageSizeProvider;

public interface DefaultedEnergyStorageSizeProvider extends EnergyStorageSizeProvider {
	default long getDefaultEnergyStorageSize() {
		return DefaultConfigValues.UTILITY_ENERGY;
	}
}
