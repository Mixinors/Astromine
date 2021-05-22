package com.github.mixinors.astromine.cardinalcomponents.common.component.base;

import com.github.mixinors.astromine.cardinalcomponents.common.component.Component;
import com.github.mixinors.astromine.techreborn.common.volume.fluid.FluidVolume;

public class FluidComponentSyncedDirectionalImpl extends FluidComponentDirectionalImpl implements Component.Synced {
	<V> FluidComponentSyncedDirectionalImpl(V v, int size) {
		super(v, size);
	}
	
	<V> FluidComponentSyncedDirectionalImpl(V v, FluidVolume... volumes) {
		super(v, volumes);
	}
}
