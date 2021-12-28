package com.github.mixinors.astromine.common.config.tiered.provider;

import com.github.mixinors.astromine.common.config.DefaultConfigValues;
import com.github.mixinors.astromine.common.util.tier.MachineTier;

public interface DefaultedTieredEnergyStorageProvider extends TieredEnergyStorageProvider {
	default long getDefaultEnergyStorage(MachineTier tier) {
		return switch(tier) {
			case PRIMITIVE -> DefaultConfigValues.PRIMITIVE_BATTERY_PACK_ENERGY;
			case BASIC -> DefaultConfigValues.BASIC_BATTERY_PACK_ENERGY;
			case ADVANCED -> DefaultConfigValues.ADVANCED_BATTERY_PACK_ENERGY;
			case ELITE -> DefaultConfigValues.ELITE_BATTERY_PACK_ENERGY;
			case CREATIVE -> Long.MAX_VALUE;
		};
	}
}
