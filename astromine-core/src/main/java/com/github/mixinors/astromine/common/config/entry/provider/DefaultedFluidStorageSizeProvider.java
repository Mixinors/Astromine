package com.github.mixinors.astromine.common.config.entry.provider;

import com.github.mixinors.astromine.common.config.DefaultConfigValues;
import com.github.mixinors.astromine.common.provider.FluidStorageSizeProvider;

public interface DefaultedFluidStorageSizeProvider extends FluidStorageSizeProvider {
	default double getDefaultFluidStorageSize() {
		return DefaultConfigValues.UTILITY_FLUID;
	}
}
