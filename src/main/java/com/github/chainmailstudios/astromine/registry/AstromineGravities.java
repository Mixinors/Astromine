package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.common.dimension.EarthSpaceDimensionType;
import com.github.chainmailstudios.astromine.common.registry.GravityRegistry;

public class AstromineGravities {
	public static void initialize() {
		GravityRegistry.INSTANCE.register(EarthSpaceDimensionType.EARTH_SPACE_REGISTRY_KEY, AstromineConfig.get().spaceGravity);
	}
}
