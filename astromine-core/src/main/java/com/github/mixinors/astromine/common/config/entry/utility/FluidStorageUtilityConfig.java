package com.github.mixinors.astromine.common.config.entry.utility;

import com.github.mixinors.astromine.common.config.entry.provider.DefaultedFluidStorageSizeProvider;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class FluidStorageUtilityConfig extends UtilityConfig implements DefaultedFluidStorageSizeProvider {
	@Comment("The maximum fluid able to be stored by this utility.")
	public long fluidStorageSize;

	@Override
	public long getFluidStorageSize() {
		return fluidStorageSize;
	}
}
