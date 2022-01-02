package com.github.mixinors.astromine.common.provider.tiered;

import com.github.mixinors.astromine.common.util.tier.MachineTier;

public interface TieredFluidStorageSizeProvider {
	long getFluidStorageSize(MachineTier tier);
}
