package com.github.mixinors.astromine.common.component.base.fabric;

import com.github.mixinors.astromine.common.component.base.FluidComponent;
import com.github.mixinors.astromine.registry.common.fabric.AMComponentsImpl;

public interface FluidComponentImpl {
	static <V> FluidComponent fromPost(V v) {
		if (v != null && AMComponentsImpl.FLUID.isProvidedBy(v)) {
			return (FluidComponent) AMComponentsImpl.FLUID.get(v).peek();
		}
		
		return null;
	}
}
