package com.github.mixinors.astromine.common.config.entry.tiered;

import com.github.mixinors.astromine.common.config.entry.tiered.tier.MachineTierConfig;
import com.github.mixinors.astromine.common.util.tier.MachineTier;

public class SimpleMachineConfig extends com.github.mixinors.astromine.common.config.entry.tiered.MachineConfig<MachineTierConfig> {
	@Override
	public MachineTierConfig createTierConfig(MachineTier tier) {
		return new MachineTierConfig(getDefaultSpeedModifier(tier), getDefaultEnergyStorageSize(tier));
	}
}
