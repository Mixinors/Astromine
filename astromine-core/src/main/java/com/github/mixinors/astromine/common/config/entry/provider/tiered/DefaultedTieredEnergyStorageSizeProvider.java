package com.github.mixinors.astromine.common.config.entry.provider.tiered;

import com.github.mixinors.astromine.common.config.DefaultConfigValues;
import com.github.mixinors.astromine.common.provider.tiered.TieredEnergyStorageSizeProvider;
import com.github.mixinors.astromine.common.util.tier.MachineTier;

public interface DefaultedTieredEnergyStorageSizeProvider extends TieredEnergyStorageSizeProvider {
	default long getDefaultEnergyStorageSize(MachineTier tier) {
		return switch(tier) {
			case PRIMITIVE -> DefaultConfigValues.PRIMITIVE_BATTERY_PACK_ENERGY;
			case BASIC -> DefaultConfigValues.BASIC_BATTERY_PACK_ENERGY;
			case ADVANCED -> DefaultConfigValues.ADVANCED_BATTERY_PACK_ENERGY;
			case ELITE -> DefaultConfigValues.ELITE_BATTERY_PACK_ENERGY;
			case CREATIVE -> Long.MAX_VALUE;
		};
	}
}
