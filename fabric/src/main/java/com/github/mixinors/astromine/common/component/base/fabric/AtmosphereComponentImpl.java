package com.github.mixinors.astromine.common.component.base.fabric;

import com.github.mixinors.astromine.common.component.base.AtmosphereComponent;
import com.github.mixinors.astromine.registry.common.fabric.AMComponentsImpl;

public interface AtmosphereComponentImpl {
	static <V> AtmosphereComponent fromPost(V v) {
		if (v != null && AMComponentsImpl.ATMOSPHERE.isProvidedBy(v)) {
			return (AtmosphereComponent) AMComponentsImpl.ATMOSPHERE.get(v).peek();
		}
		
		return null;
	}
}
