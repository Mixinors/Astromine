package com.github.mixinors.astromine.common.config.entry.tiered;

import com.github.mixinors.astromine.common.config.entry.provider.tiered.DefaultedTieredEnergyStorageSizeProvider;
import com.github.mixinors.astromine.common.config.entry.tiered.tier.MachineTierConfig;
import com.github.mixinors.astromine.common.util.tier.MachineTier;

public abstract class MachineConfig<T extends MachineTierConfig> extends SpeedTieredConfig<T> implements DefaultedTieredEnergyStorageSizeProvider {
	@Override
	public long getEnergyStorageSize(MachineTier tier) {
		return getTierConfig(tier).getEnergyStorageSize();
	}
}
