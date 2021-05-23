package com.github.mixinors.astromine.common.component.base.fabric;

import com.github.mixinors.astromine.common.component.base.NetworkComponent;
import com.github.mixinors.astromine.registry.common.fabric.AMComponentsImpl;

public interface NetworkComponentImpl {
	static <V> NetworkComponent fromPost(V v) {
		if (v != null && AMComponentsImpl.NETWORK.isProvidedBy(v)) {
			return (NetworkComponent) AMComponentsImpl.NETWORK.get(v).peek();
		}
		
		return null;
	}
}
