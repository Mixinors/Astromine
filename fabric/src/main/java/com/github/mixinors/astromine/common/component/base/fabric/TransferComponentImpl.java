package com.github.mixinors.astromine.common.component.base.fabric;

import com.github.mixinors.astromine.common.component.base.TransferComponent;
import com.github.mixinors.astromine.registry.common.fabric.AMComponentsImpl;

public interface TransferComponentImpl {
	static <V> TransferComponent fromPost(V v) {
		if (v != null && AMComponentsImpl.TRANSFER.isProvidedBy(v)) {
			return (TransferComponent) AMComponentsImpl.TRANSFER.get(v).peek();
		}
		
		return null;
	}
}
