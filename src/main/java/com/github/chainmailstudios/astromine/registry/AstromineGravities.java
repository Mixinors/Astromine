package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.common.registry.GravityRegistry;

public class AstromineGravities {
	public static void initialize() {
		GravityRegistry.INSTANCE.register(AstromineDimensionTypes.SPACE_REGISTRY_KEY, AstromineConfig.get().spaceGravity);
	}
}
