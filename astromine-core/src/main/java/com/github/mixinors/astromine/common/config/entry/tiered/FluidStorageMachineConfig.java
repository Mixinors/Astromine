package com.github.mixinors.astromine.common.config.entry.tiered;

import com.github.mixinors.astromine.common.config.entry.provider.tiered.DefaultedTieredFluidStorageSizeProvider;
import com.github.mixinors.astromine.common.config.entry.tiered.tier.FluidStorageMachineTierConfig;
import com.github.mixinors.astromine.common.util.tier.MachineTier;

public class FluidStorageMachineConfig extends MachineConfig<FluidStorageMachineTierConfig> implements DefaultedTieredFluidStorageSizeProvider {
	@Override
	public FluidStorageMachineTierConfig createTierConfig(MachineTier tier) {
		return new FluidStorageMachineTierConfig(getDefaultSpeedModifier(tier), getDefaultEnergyStorageSize(tier), getDefaultFluidStorageSize(tier));
	}

	@Override
	public long getFluidStorageSize(MachineTier tier) {
		return getTierConfig(tier).getFluidStorageSize();
	}
}
