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

			AstromineTechnologiesBlocks.PRIMITIVE_LIQUID_GENERATOR,
			AstromineTechnologiesBlocks.BASIC_LIQUID_GENERATOR,
			AstromineTechnologiesBlocks.ADVANCED_LIQUID_GENERATOR,
			AstromineTechnologiesBlocks.ELITE_LIQUID_GENERATOR,

			AstromineTechnologiesBlocks.PRIMITIVE_ELECTRIC_SMELTER,
			AstromineTechnologiesBlocks.BASIC_ELECTRIC_SMELTER,
			AstromineTechnologiesBlocks.ADVANCED_ELECTRIC_SMELTER,
			AstromineTechnologiesBlocks.ELITE_ELECTRIC_SMELTER,

			AstromineTechnologiesBlocks.PRIMITIVE_ALLOY_SMELTER,
			AstromineTechnologiesBlocks.BASIC_ALLOY_SMELTER,
			AstromineTechnologiesBlocks.ADVANCED_ALLOY_SMELTER,
			AstromineTechnologiesBlocks.ELITE_ALLOY_SMELTER,

			AstromineTechnologiesBlocks.PRIMITIVE_TRITURATOR,
			AstromineTechnologiesBlocks.BASIC_TRITURATOR,
			AstromineTechnologiesBlocks.ADVANCED_TRITURATOR,
			AstromineTechnologiesBlocks.ELITE_TRITURATOR,

			AstromineTechnologiesBlocks.PRIMITIVE_PRESSER,
			AstromineTechnologiesBlocks.BASIC_PRESSER,
			AstromineTechnologiesBlocks.ADVANCED_PRESSER,
			AstromineTechnologiesBlocks.ELITE_PRESSER,

			AstromineTechnologiesBlocks.PRIMITIVE_WIREMILL,
			AstromineTechnologiesBlocks.BASIC_WIREMILL,
			AstromineTechnologiesBlocks.ADVANCED_WIREMILL,
			AstromineTechnologiesBlocks.ELITE_WIREMILL,

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

			AstromineTechnologiesBlocks.PRIMITIVE_BUFFER,
			AstromineTechnologiesBlocks.BASIC_BUFFER,
			AstromineTechnologiesBlocks.ADVANCED_BUFFER,
			AstromineTechnologiesBlocks.ELITE_BUFFER,
			AstromineTechnologiesBlocks.CREATIVE_BUFFER,

			AstromineTechnologiesBlocks.FLUID_EXTRACTOR,
			AstromineTechnologiesBlocks.FLUID_INSERTER,
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
