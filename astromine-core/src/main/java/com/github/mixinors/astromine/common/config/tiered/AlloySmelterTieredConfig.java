package com.github.mixinors.astromine.common.config.tiered;

import com.github.mixinors.astromine.common.util.tier.MachineTier;

public class AlloySmelterTieredConfig extends SimpleMachineTieredConfig {
	@Override
	public long getDefaultEnergyStorage(MachineTier tier) {
		return super.getDefaultEnergyStorage(tier) * 3;
	}
}
