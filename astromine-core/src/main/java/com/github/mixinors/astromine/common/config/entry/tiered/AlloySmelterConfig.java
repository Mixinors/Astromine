package com.github.mixinors.astromine.common.config.entry.tiered;

import com.github.mixinors.astromine.common.util.tier.MachineTier;

public class AlloySmelterConfig extends SimpleMachineConfig {
	@Override
	public long getDefaultEnergyStorageSize(MachineTier tier) {
		return super.getDefaultEnergyStorageSize(tier) * 3;
	}
}
