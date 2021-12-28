package com.github.mixinors.astromine.common.config.tiered;

import com.github.mixinors.astromine.common.config.tiered.provider.DefaultedTieredFluidStorageProvider;
import com.github.mixinors.astromine.common.config.tiered.provider.DefaultedTieredSpeedProvider;
import com.github.mixinors.astromine.common.config.tiered.tier.TankTierConfig;
import com.github.mixinors.astromine.common.util.tier.MachineTier;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class TankTieredConfig extends TieredConfig<TankTierConfig> implements DefaultedTieredSpeedProvider, DefaultedTieredFluidStorageProvider {
	@Comment("The base speed of the tank.")
	public double speed = 10d;

	@Override
	public double getBaseSpeed() {
		return speed;
	}

	@Override
	public double getSpeedModifier(MachineTier tier) {
		return getTierConfig(tier).getSpeedModifier();
	}

	@Override
	public long getFluidStorage(MachineTier tier) {
		return getTierConfig(tier).getFluidStorage();
	}

	@Override
	public TankTierConfig createTierConfig(MachineTier tier) {
		return new TankTierConfig(getDefaultSpeedModifier(tier), getDefaultFluidStorage(tier));
	}
}
