package com.github.mixinors.astromine.common.config.entry.tiered;

import com.github.mixinors.astromine.common.config.DefaultConfigValues;
import com.github.mixinors.astromine.common.util.tier.MachineTier;

public class CapacitorConfig extends SimpleMachineConfig {
	@Override
	public double getDefaultBaseSpeed() {
		return DefaultConfigValues.CAPACITOR_BASE_SPEED;
	}

	@Override
	public long getDefaultEnergyStorageSize(MachineTier tier) {
		if(tier == MachineTier.CREATIVE) return super.getDefaultEnergyStorageSize(tier);
		return super.getDefaultEnergyStorageSize(tier) * 4;
	}
}
