package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.common.registry.GravityRegistry;
import com.github.chainmailstudios.astromine.world.AstromineDimensionTypes;

public class AstromineGravities {
	public static void initialize() {
		GravityRegistry.INSTANCE.register(AstromineDimensionTypes.KEY_ID, 0.01D);
	}
}
