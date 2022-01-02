package com.github.mixinors.astromine.common.provider.config;

import com.github.mixinors.astromine.common.config.entry.utility.FluidStorageUtilityConfig;
import com.github.mixinors.astromine.common.provider.FluidStorageSizeProvider;

public interface FluidStorageUtilityConfigProvider extends UtilityConfigProvider<FluidStorageUtilityConfig>, FluidStorageSizeProvider {
	@Override
	default long getFluidStorageSize() {
		return getConfig().getFluidStorageSize();
	}
}
