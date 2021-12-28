package com.github.mixinors.astromine.common.config.tiered;

import com.github.mixinors.astromine.common.util.tier.MachineTier;

public class CapacitorTieredConfig extends SimpleMachineTieredConfig {
	@Override
	public double getBaseSpeed() {
		return 10D;
	}

	@Override
	public long getDefaultEnergyStorage(MachineTier tier) {
		if(tier == MachineTier.CREATIVE) return super.getDefaultEnergyStorage(tier);
		return super.getDefaultEnergyStorage(tier) * 4;
	}
}
