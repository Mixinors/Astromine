package com.github.mixinors.astromine.common.provider.config.tiered;

import com.github.mixinors.astromine.common.config.entry.tiered.FluidStorageMachineConfig;
import com.github.mixinors.astromine.common.provider.FluidStorageSizeProvider;

public interface FluidStorageMachineConfigProvider extends MachineConfigProvider<FluidStorageMachineConfig>, FluidStorageSizeProvider {
	@Override
	default long getFluidStorageSize() {
		return getConfig().getFluidStorageSize(getMachineTier());
	}
}
