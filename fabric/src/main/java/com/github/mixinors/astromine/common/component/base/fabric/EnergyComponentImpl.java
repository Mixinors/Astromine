package com.github.mixinors.astromine.common.component.base.fabric;

import com.github.mixinors.astromine.common.component.base.AtmosphereComponent;
import com.github.mixinors.astromine.common.component.base.EnergyComponent;
import com.github.mixinors.astromine.registry.common.fabric.AMComponentsImpl;
import me.shedaniel.architectury.ExpectPlatform;

public interface EnergyComponentImpl {
	static <V> EnergyComponent fromPost(V v) {
		if (v != null && AMComponentsImpl.ENERGY.isProvidedBy(v)) {
			return (EnergyComponent) AMComponentsImpl.ENERGY.get(v).peek();
		}
		
		return null;
	}
}
