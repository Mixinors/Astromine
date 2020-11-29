package com.github.chainmailstudios.astromine.technologies.datagen;

import com.github.chainmailstudios.astromine.datagen.generator.loottable.LootTableGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.loottable.onetime.DropSelfLootTableGenerator;
import com.github.chainmailstudios.astromine.datagen.registry.AstromineLootTableGenerators;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlocks;

public class AstromineTechnologiesLootTableGenerators extends AstromineLootTableGenerators {
	public final LootTableGenerator DROP_SELF = register(new DropSelfLootTableGenerator(
			AstromineTechnologiesBlocks.HOLOGRAPHIC_BRIDGE_PROJECTOR,
			AstromineTechnologiesBlocks.HOLOGRAPHIC_BRIDGE_INVISIBLE_BLOCK,

			AstromineTechnologiesBlocks.VENT,

			AstromineTechnologiesBlocks.PRIMITIVE_TANK,
			AstromineTechnologiesBlocks.BASIC_TANK,
			AstromineTechnologiesBlocks.ADVANCED_TANK,
			AstromineTechnologiesBlocks.ELITE_TANK,
			AstromineTechnologiesBlocks.CREATIVE_TANK,

			AstromineTechnologiesBlocks.PRIMITIVE_SOLID_GENERATOR,
			AstromineTechnologiesBlocks.BASIC_SOLID_GENERATOR,
			AstromineTechnologiesBlocks.ADVANCED_SOLID_GENERATOR,
			AstromineTechnologiesBlocks.ELITE_SOLID_GENERATOR,

			AstromineTechnologiesBlocks.PRIMITIVE_FLUID_GENERATOR,
			AstromineTechnologiesBlocks.BASIC_FLUID_GENERATOR,
			AstromineTechnologiesBlocks.ADVANCED_FLUID_GENERATOR,
			AstromineTechnologiesBlocks.ELITE_FLUID_GENERATOR,

			AstromineTechnologiesBlocks.PRIMITIVE_ELECTRIC_FURNACE,
			AstromineTechnologiesBlocks.BASIC_ELECTRIC_FURNACE,
			AstromineTechnologiesBlocks.ADVANCED_ELECTRIC_FURNACE,
			AstromineTechnologiesBlocks.ELITE_ELECTRIC_FURNACE,

			AstromineTechnologiesBlocks.PRIMITIVE_ALLOY_SMELTER,
			AstromineTechnologiesBlocks.BASIC_ALLOY_SMELTER,
			AstromineTechnologiesBlocks.ADVANCED_ALLOY_SMELTER,
			AstromineTechnologiesBlocks.ELITE_ALLOY_SMELTER,

			AstromineTechnologiesBlocks.PRIMITIVE_TRITURATOR,
			AstromineTechnologiesBlocks.BASIC_TRITURATOR,
			AstromineTechnologiesBlocks.ADVANCED_TRITURATOR,
			AstromineTechnologiesBlocks.ELITE_TRITURATOR,

			AstromineTechnologiesBlocks.PRIMITIVE_PRESS,
			AstromineTechnologiesBlocks.BASIC_PRESS,
			AstromineTechnologiesBlocks.ADVANCED_PRESS,
			AstromineTechnologiesBlocks.ELITE_PRESS,

			AstromineTechnologiesBlocks.PRIMITIVE_WIRE_MILL,
			AstromineTechnologiesBlocks.BASIC_WIRE_MILL,
			AstromineTechnologiesBlocks.ADVANCED_WIRE_MILL,
			AstromineTechnologiesBlocks.ELITE_WIRE_MILL,

			AstromineTechnologiesBlocks.PRIMITIVE_ELECTROLYZER,
			AstromineTechnologiesBlocks.BASIC_ELECTROLYZER,
			AstromineTechnologiesBlocks.ADVANCED_ELECTROLYZER,
			AstromineTechnologiesBlocks.ELITE_ELECTROLYZER,

			AstromineTechnologiesBlocks.PRIMITIVE_REFINERY,
			AstromineTechnologiesBlocks.BASIC_REFINERY,
			AstromineTechnologiesBlocks.ADVANCED_REFINERY,
			AstromineTechnologiesBlocks.ELITE_REFINERY,

			AstromineTechnologiesBlocks.PRIMITIVE_FLUID_MIXER,
			AstromineTechnologiesBlocks.BASIC_FLUID_MIXER,
			AstromineTechnologiesBlocks.ADVANCED_FLUID_MIXER,
			AstromineTechnologiesBlocks.ELITE_FLUID_MIXER,

			AstromineTechnologiesBlocks.PRIMITIVE_SOLIDIFIER,
			AstromineTechnologiesBlocks.BASIC_SOLIDIFIER,
			AstromineTechnologiesBlocks.ADVANCED_SOLIDIFIER,
			AstromineTechnologiesBlocks.ELITE_SOLIDIFIER,

			AstromineTechnologiesBlocks.PRIMITIVE_MELTER,
			AstromineTechnologiesBlocks.BASIC_MELTER,
			AstromineTechnologiesBlocks.ADVANCED_MELTER,
			AstromineTechnologiesBlocks.ELITE_MELTER,

			AstromineTechnologiesBlocks.PRIMITIVE_BUFFER,
			AstromineTechnologiesBlocks.BASIC_BUFFER,
			AstromineTechnologiesBlocks.ADVANCED_BUFFER,
			AstromineTechnologiesBlocks.ELITE_BUFFER,
			AstromineTechnologiesBlocks.CREATIVE_BUFFER,

			AstromineTechnologiesBlocks.FLUID_COLLECTOR,
			AstromineTechnologiesBlocks.FLUID_PLACER,
			AstromineTechnologiesBlocks.BLOCK_BREAKER,
			AstromineTechnologiesBlocks.BLOCK_PLACER,

			AstromineTechnologiesBlocks.NUCLEAR_WARHEAD,

			AstromineTechnologiesBlocks.PRIMITIVE_CAPACITOR,
			AstromineTechnologiesBlocks.BASIC_CAPACITOR,
			AstromineTechnologiesBlocks.ADVANCED_CAPACITOR,
			AstromineTechnologiesBlocks.ELITE_CAPACITOR,
			AstromineTechnologiesBlocks.CREATIVE_CAPACITOR,

			AstromineTechnologiesBlocks.AIRLOCK));
}
