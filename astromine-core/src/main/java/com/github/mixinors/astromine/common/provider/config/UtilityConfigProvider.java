package com.github.mixinors.astromine.common.provider.config;

import com.github.mixinors.astromine.common.config.entry.utility.UtilityConfig;
import com.github.mixinors.astromine.common.provider.EnergyConsumedProvider;
import com.github.mixinors.astromine.common.provider.EnergyStorageSizeProvider;
import com.github.mixinors.astromine.common.provider.SpeedProvider;

public interface UtilityConfigProvider<T extends UtilityConfig> extends ConfigProvider<T>, SpeedProvider, EnergyStorageSizeProvider, EnergyConsumedProvider {
	@Override
	default long getEnergyStorageSize() {
		return getConfig().getEnergyStorageSize();
	}

	@Override
	default long getEnergyConsumed() {
		return getConfig().getEnergyConsumed();
	}

	@Override
	default double getSpeed() {
		return getConfig().getSpeed();
	}
}
