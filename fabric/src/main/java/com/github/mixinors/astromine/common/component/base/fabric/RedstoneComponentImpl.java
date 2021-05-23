package com.github.mixinors.astromine.common.component.base.fabric;

import com.github.mixinors.astromine.common.component.base.RedstoneComponent;
import com.github.mixinors.astromine.registry.common.fabric.AMComponentsImpl;

public interface RedstoneComponentImpl {
	static <V> RedstoneComponent fromPost(V v) {
		if (v != null && AMComponentsImpl.REDSTONE.isProvidedBy(v)) {
			return (RedstoneComponent) AMComponentsImpl.REDSTONE.get(v).peek();
		}
		
		return null;
	}
}
