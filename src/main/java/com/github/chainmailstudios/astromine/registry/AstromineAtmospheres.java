package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.common.atmosphere.AtmosphereRegistry;

public class AstromineAtmospheres {
	public static void initialize() {
		AtmosphereRegistry.INSTANCE.register(AstromineDimensions.EARTH_SPACE_REGISTRY_KEY, true);
	}
}
