package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.common.registry.IdentifierFixRegistry;

public class AstromineIdentifierFixes {
	public static void initialize() {
		IdentifierFixRegistry.INSTANCE.register("space", "earth_space");
		IdentifierFixRegistry.INSTANCE.register("fuel_mixer", "fluid_mixer");
		IdentifierFixRegistry.INSTANCE.register("fuel_mixing", "fluid_mixing");
	}
}
