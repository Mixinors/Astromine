package com.github.chainmailstudios.astromine.discoveries.registry;

import com.github.chainmailstudios.astromine.common.registry.AtmosphereRegistry;
import com.github.chainmailstudios.astromine.registry.AstromineAtmospheres;
import com.github.chainmailstudios.astromine.registry.AstromineDimensions;

public class AstromineDiscoveriesAtmospheres extends AstromineAtmospheres {
	public static void initialize() {
		AtmosphereRegistry.INSTANCE.register(AstromineDiscoveriesDimensions.EARTH_SPACE_WORLD, true);
		AtmosphereRegistry.INSTANCE.register(AstromineDiscoveriesDimensions.MOON_WORLD, true);
		AtmosphereRegistry.INSTANCE.register(AstromineDiscoveriesDimensions.MARS_WORLD, true);
		AtmosphereRegistry.INSTANCE.register(AstromineDiscoveriesDimensions.VULCAN_WORLD, true);
	}
}
