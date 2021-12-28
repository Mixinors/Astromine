package com.github.mixinors.astromine.common.block.entity.machine;

import com.github.mixinors.astromine.common.config.tiered.FluidStorageMachineTieredConfig;

public interface FluidStorageMachineConfigProvider extends MachineConfigProvider<FluidStorageMachineTieredConfig>, FluidStorageSizeProvider {
	@Override
	default long getFluidStorageSize() {
		return getTieredConfig().getFluidStorage(getMachineTier());
	}
}
