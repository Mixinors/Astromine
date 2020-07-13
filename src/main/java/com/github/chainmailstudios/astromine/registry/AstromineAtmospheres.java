package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.common.atmosphere.AtmosphereRegistry;
import com.github.chainmailstudios.astromine.common.dimension.EarthSpaceDimensionType;

public class AstromineAtmospheres {
	public static void initialize() {
		AtmosphereRegistry.INSTANCE.register(EarthSpaceDimensionType.EARTH_SPACE_REGISTRY_KEY, true);
	}
}
