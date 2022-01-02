package com.github.mixinors.astromine.common.provider.config;

import com.github.mixinors.astromine.common.config.entry.AMConfigEntry;

public interface ConfigProvider<T extends AMConfigEntry> {
	T getConfig();
}
