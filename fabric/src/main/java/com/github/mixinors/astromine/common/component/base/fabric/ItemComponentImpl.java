package com.github.mixinors.astromine.common.component.base.fabric;

import com.github.mixinors.astromine.common.component.base.ItemComponent;
import com.github.mixinors.astromine.registry.common.fabric.AMComponentsImpl;

public interface ItemComponentImpl {
	static <V> ItemComponent fromPost(V v) {
		if (v != null && AMComponentsImpl.ITEM.isProvidedBy(v)) {
			return (ItemComponent) AMComponentsImpl.ITEM.get(v).peek();
		}
		
		return null;
	}
}
