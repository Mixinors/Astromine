package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.common.registry.IdentifierFixRegistry;

public class AstromineIdentifierFixes {
	public static void initialize() {
		IdentifierFixRegistry.INSTANCE.register("space", "earth_space");
		//TODO: update this at some point
//		IdentifierFixRegistry.INSTANCE.register("asteroid_belt", "earth_space");
		IdentifierFixRegistry.INSTANCE.register("fuel_mixer", "advanced_fluid_mixer");
		IdentifierFixRegistry.INSTANCE.register("fuel_mixing", "fluid_mixing");
		IdentifierFixRegistry.INSTANCE.register("sorter", "advanced_triturator");
		IdentifierFixRegistry.INSTANCE.register("sorting", "triturating");
		IdentifierFixRegistry.INSTANCE.register("machine_chassis", "advanced_machine_chassis");
		IdentifierFixRegistry.INSTANCE.register("solid_generator", "advanced_solid_generator");
		IdentifierFixRegistry.INSTANCE.register("liquid_generator", "advanced_liquid_generator");
		IdentifierFixRegistry.INSTANCE.register("electric_smelter", "advanced_electric_smelter");
		IdentifierFixRegistry.INSTANCE.register("alloy_smelter", "advanced_alloy_smelter");
		IdentifierFixRegistry.INSTANCE.register("triturator", "advanced_triturator");
		IdentifierFixRegistry.INSTANCE.register("presser", "advanced_presser");
		IdentifierFixRegistry.INSTANCE.register("electrolyzer", "advanced_electrolyzer");
		IdentifierFixRegistry.INSTANCE.register("fluid_mixer", "advanced_fluid_mixer");
	}
}
