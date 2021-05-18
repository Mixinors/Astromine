package com.github.mixinors.astromine.common.component.general.base;

import com.github.mixinors.astromine.common.component.AutoSyncedComponent;
import com.github.mixinors.astromine.common.volume.fluid.FluidVolume;

/**
 * A {@link SimpleDirectionalFluidComponent} that synchronizes itself
 * automatically.
 */
class SimpleAutoSyncedDirectionalFluidComponent extends SimpleDirectionalFluidComponent implements AutoSyncedComponent {
	/** Instantiates a {@link SimpleDirectionalFluidComponent}. */
	public <V> SimpleAutoSyncedDirectionalFluidComponent(V v, int size) {
		super(v, size);
	}
	
	/** Instantiates a {@link SimpleDirectionalFluidComponent}. */
	public <V> SimpleAutoSyncedDirectionalFluidComponent(V v, FluidVolume... volumes) {
		super(v, volumes);
	}
}
