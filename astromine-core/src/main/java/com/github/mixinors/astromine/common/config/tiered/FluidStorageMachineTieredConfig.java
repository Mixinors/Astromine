package com.github.mixinors.astromine.common.config.tiered;

import com.github.mixinors.astromine.common.config.DefaultConfigValues;
import com.github.mixinors.astromine.common.config.tiered.provider.DefaultedTieredFluidStorageProvider;
import com.github.mixinors.astromine.common.config.tiered.tier.FluidStorageMachineTierConfig;
import com.github.mixinors.astromine.common.util.tier.MachineTier;

public class FluidStorageMachineTieredConfig extends MachineTieredConfig<FluidStorageMachineTierConfig> implements DefaultedTieredFluidStorageProvider {
	@Override
	public FluidStorageMachineTierConfig createTierConfig(MachineTier tier) {
		return new FluidStorageMachineTierConfig(getDefaultSpeedModifier(tier), getDefaultEnergyStorage(tier), getDefaultFluidStorage(tier));
	}

	@Override
	public long getFluidStorage(MachineTier tier) {
		return getTierConfig(tier).getFluidStorage();
	}
}
