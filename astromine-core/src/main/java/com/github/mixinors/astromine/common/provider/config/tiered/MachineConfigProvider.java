package com.github.mixinors.astromine.common.provider.config.tiered;

import com.github.mixinors.astromine.common.config.entry.tiered.MachineConfig;
import com.github.mixinors.astromine.common.provider.EnergyStorageSizeProvider;
import com.github.mixinors.astromine.common.provider.SpeedProvider;

public interface MachineConfigProvider<T extends MachineConfig<?>> extends TieredConfigProvider<T>, SpeedProvider, EnergyStorageSizeProvider {
	@Override
	default long getEnergyStorageSize() {
		return getConfig().getEnergyStorageSize(getMachineTier());
	}

	@Override
	default double getSpeed() {
		return getConfig().getSpeed(getMachineTier());
	}
}
