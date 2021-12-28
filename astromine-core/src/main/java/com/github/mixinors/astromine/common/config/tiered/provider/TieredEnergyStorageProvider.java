package com.github.mixinors.astromine.common.config.tiered.provider;

import com.github.mixinors.astromine.common.util.tier.MachineTier;

public interface TieredEnergyStorageProvider {
	long getEnergyStorage(MachineTier tier);
}
