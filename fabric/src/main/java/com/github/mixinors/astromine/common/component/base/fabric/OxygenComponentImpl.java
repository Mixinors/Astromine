package com.github.mixinors.astromine.common.component.base.fabric;

import com.github.mixinors.astromine.common.component.base.OxygenComponent;
import com.github.mixinors.astromine.registry.common.fabric.AMComponentsImpl;

public interface OxygenComponentImpl {
	static <V> OxygenComponent fromPost(V v) {
		if (v != null && AMComponentsImpl.OXYGEN.isProvidedBy(v)) {
			return (OxygenComponent) AMComponentsImpl.OXYGEN.get(v).peek();
		}
		
		return null;
	}
}
