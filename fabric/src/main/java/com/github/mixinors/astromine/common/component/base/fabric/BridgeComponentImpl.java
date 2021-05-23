package com.github.mixinors.astromine.common.component.base.fabric;

import com.github.mixinors.astromine.common.component.base.BridgeComponent;
import com.github.mixinors.astromine.registry.common.fabric.AMComponentsImpl;

public interface BridgeComponentImpl {
	static <V> BridgeComponent fromPost(V v) {
		if (v != null && AMComponentsImpl.BRIDGE.isProvidedBy(v)) {
			return (BridgeComponent) AMComponentsImpl.BRIDGE.get(v).peek();
		}
		
		return null;
	}
}
