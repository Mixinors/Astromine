package com.github.mixinors.astromine.registry.common;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.body.Body;
import com.github.mixinors.astromine.common.registry.base.RegistryEntry;

public class AMBodies {
	public RegistryEntry<Body> SUN = AMRegistries.BODY.getEntry(AMCommon.id("sun"));

	public RegistryEntry<Body> EARTH = AMRegistries.BODY.getEntry(AMCommon.id("earth"));
	public RegistryEntry<Body> MOON = AMRegistries.BODY.getEntry(AMCommon.id("moon"));
	
	public static void init() {

	}
}
