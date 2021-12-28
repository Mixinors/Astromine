package com.github.mixinors.astromine.common.block.entity.machine;

import com.github.mixinors.astromine.common.config.tiered.MachineTieredConfig;

public interface MachineConfigProvider<T extends MachineTieredConfig<?>> extends TieredConfigProvider<T>, SpeedProvider, EnergyStorageSizeProvider {
	@Override
	default long getEnergyStorageSize() {
		return getTieredConfig().getEnergyStorage(getMachineTier());
	}

	@Override
	default double getSpeed() {
		return getTieredConfig().getSpeed(getMachineTier());
	}
}
