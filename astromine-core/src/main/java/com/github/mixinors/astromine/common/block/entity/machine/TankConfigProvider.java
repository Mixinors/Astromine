package com.github.mixinors.astromine.common.block.entity.machine;

import com.github.mixinors.astromine.common.config.tiered.TankTieredConfig;

public interface TankConfigProvider extends TieredConfigProvider<TankTieredConfig>, SpeedProvider, FluidStorageSizeProvider {
	@Override
	default long getFluidStorageSize() {
		return getTieredConfig().getFluidStorage(getMachineTier());
	}

	@Override
	default double getSpeed() {
		return getTieredConfig().getSpeed(getMachineTier());
	}
}
