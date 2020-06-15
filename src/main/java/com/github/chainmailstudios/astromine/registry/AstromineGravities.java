package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.common.registry.GravityRegistry;

public class AstromineGravities {
	public static void initialize() {
		GravityRegistry.INSTANCE.register(AstromineDimensionTypes.REGISTRY_KEY, 0.01D);
	}
}
