package com.github.mixinors.astromine.common.config.entry.tiered;

import com.github.mixinors.astromine.common.config.DefaultConfigValues;
import com.github.mixinors.astromine.common.config.entry.provider.tiered.DefaultedTieredFluidStorageSizeProvider;
import com.github.mixinors.astromine.common.config.entry.tiered.tier.TankTierConfig;
import com.github.mixinors.astromine.common.util.tier.MachineTier;

public class TankConfig extends SpeedTieredConfig<TankTierConfig> implements DefaultedTieredFluidStorageSizeProvider {
	@Override
	public double getDefaultBaseSpeed() {
		return DefaultConfigValues.TANK_BASE_SPEED;
	}

	@Override
	public long getFluidStorageSize(MachineTier tier) {
		return getTierConfig(tier).getFluidStorageSize();
	}

	@Override
	public TankTierConfig createTierConfig(MachineTier tier) {
		return new TankTierConfig(getDefaultSpeedModifier(tier), getDefaultFluidStorageSize(tier));
	}
}
