package com.github.mixinors.astromine.common.config.tiered;

import com.github.mixinors.astromine.common.config.tiered.tier.MachineTierConfig;
import com.github.mixinors.astromine.common.util.tier.MachineTier;

public class SimpleMachineTieredConfig extends MachineTieredConfig<MachineTierConfig> {
	@Override
	public MachineTierConfig createTierConfig(MachineTier tier) {
		return new MachineTierConfig(getDefaultSpeedModifier(tier), getDefaultEnergyStorage(tier));
	}
}
