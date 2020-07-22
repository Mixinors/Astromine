package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.common.registry.GravityRegistry;

public class AstromineGravities {
	public static void initialize() {
		GravityRegistry.INSTANCE.register(AstromineDimensions.EARTH_SPACE_REGISTRY_KEY, AstromineConfig.get().spaceGravity);
		GravityRegistry.INSTANCE.register(AstromineDimensions.MOON_REGISTRY_KEY, AstromineConfig.get().moonGravity);
		GravityRegistry.INSTANCE.register(AstromineDimensions.MARS_REGISTRY_KEY, AstromineConfig.get().marsGravity);
	}
}
