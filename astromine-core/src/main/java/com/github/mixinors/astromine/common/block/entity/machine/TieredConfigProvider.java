package com.github.mixinors.astromine.common.block.entity.machine;

import com.github.mixinors.astromine.common.config.tiered.TieredConfig;

public interface TieredConfigProvider<T extends TieredConfig<?>> extends TierProvider {
	T getTieredConfig();
}
