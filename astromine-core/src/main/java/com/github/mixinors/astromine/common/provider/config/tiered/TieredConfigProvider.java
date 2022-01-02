package com.github.mixinors.astromine.common.provider.config.tiered;

import com.github.mixinors.astromine.common.config.entry.tiered.TieredConfig;
import com.github.mixinors.astromine.common.provider.TierProvider;
import com.github.mixinors.astromine.common.provider.config.ConfigProvider;

public interface TieredConfigProvider<T extends TieredConfig<?>> extends ConfigProvider<T>, TierProvider {
}
