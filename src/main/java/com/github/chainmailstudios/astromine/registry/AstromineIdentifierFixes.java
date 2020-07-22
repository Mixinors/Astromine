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
		IdentifierFixRegistry.INSTANCE.register("tin_haxe", "tin_mattock");
		IdentifierFixRegistry.INSTANCE.register("copper_haxe", "copper_mattock");
		IdentifierFixRegistry.INSTANCE.register("bronze_haxe", "bronze_mattock");
		IdentifierFixRegistry.INSTANCE.register("steel_haxe", "steel_mattock");
		IdentifierFixRegistry.INSTANCE.register("metite_haxe", "metite_mattock");
		IdentifierFixRegistry.INSTANCE.register("asterite_haxe", "asterite_mattock");
		IdentifierFixRegistry.INSTANCE.register("stellum_haxe", "stellum_mattock");
		IdentifierFixRegistry.INSTANCE.register("galaxium_haxe", "galaxium_mattock");
		IdentifierFixRegistry.INSTANCE.register("univite_haxe", "univite_mattock");
		IdentifierFixRegistry.INSTANCE.register("tin_shaxe", "tin_mining_tool");
		IdentifierFixRegistry.INSTANCE.register("copper_shaxe", "copper_mining_tool");
		IdentifierFixRegistry.INSTANCE.register("bronze_shaxe", "bronze_mining_tool");
		IdentifierFixRegistry.INSTANCE.register("steel_shaxe", "steel_mining_tool");
		IdentifierFixRegistry.INSTANCE.register("metite_shaxe", "metite_mining_tool");
		IdentifierFixRegistry.INSTANCE.register("asterite_shaxe", "asterite_mining_tool");
		IdentifierFixRegistry.INSTANCE.register("stellum_shaxe", "stellum_mining_tool");
		IdentifierFixRegistry.INSTANCE.register("galaxium_shaxe", "galaxium_mining_tool");
		IdentifierFixRegistry.INSTANCE.register("univite_shaxe", "univite_mining_tool");
	}
}
