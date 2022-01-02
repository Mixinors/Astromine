package com.github.mixinors.astromine.common.provider.config.tiered;

import com.github.mixinors.astromine.common.config.entry.tiered.TankConfig;
import com.github.mixinors.astromine.common.provider.FluidStorageSizeProvider;
import com.github.mixinors.astromine.common.provider.SpeedProvider;

public interface TankConfigProvider extends TieredConfigProvider<TankConfig>, SpeedProvider, FluidStorageSizeProvider {
	@Override
	default long getFluidStorageSize() {
		return getConfig().getFluidStorageSize(getMachineTier());
	}

	@Override
	default double getSpeed() {
		return getConfig().getSpeed(getMachineTier());
	}
}
