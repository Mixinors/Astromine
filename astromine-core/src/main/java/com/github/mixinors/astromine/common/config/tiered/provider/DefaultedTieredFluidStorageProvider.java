package com.github.mixinors.astromine.common.config.tiered.provider;

import com.github.mixinors.astromine.common.config.DefaultConfigValues;
import com.github.mixinors.astromine.common.util.tier.MachineTier;

public interface DefaultedTieredFluidStorageProvider extends TieredFluidStorageProvider {
	default long getDefaultFluidStorage(MachineTier tier) {
		return switch(tier) {
			case PRIMITIVE -> DefaultConfigValues.PRIMITIVE_FLUID_STORAGE;
			case BASIC -> DefaultConfigValues.BASIC_FLUID_STORAGE;
			case ADVANCED -> DefaultConfigValues.ADVANCED_FLUID_STORAGE;
			case ELITE -> DefaultConfigValues.ELITE_FLUID_STORAGE;
			case CREATIVE -> Long.MAX_VALUE;
		};
	}
}
