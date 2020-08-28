package com.github.chainmailstudios.astromine.discoveries.registry;

import com.github.chainmailstudios.astromine.common.registry.GravityRegistry;
import com.github.chainmailstudios.astromine.registry.AstromineConfig;
import com.github.chainmailstudios.astromine.registry.AstromineDimensions;

public class AstromineDiscoveriesGravities {
	public static void initialize() {
		GravityRegistry.INSTANCE.register(AstromineDiscoveriesDimensions.EARTH_SPACE_WORLD, AstromineConfig.get().spaceGravity);
		GravityRegistry.INSTANCE.register(AstromineDiscoveriesDimensions.MOON_WORLD, AstromineConfig.get().moonGravity);
		GravityRegistry.INSTANCE.register(AstromineDiscoveriesDimensions.MARS_WORLD, AstromineConfig.get().marsGravity);
	}
}
