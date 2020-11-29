package com.github.chainmailstudios.astromine.technologies.datagen;

import com.github.chainmailstudios.astromine.datagen.generator.modelstate.ModelStateGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.modelstate.onetime.GenericItemModelGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.modelstate.onetime.HandheldItemModelGenerator;
import com.github.chainmailstudios.astromine.datagen.registry.AstromineModelStateGenerators;
import com.github.chainmailstudios.astromine.technologies.datagen.generators.modelstate.MachineModelStateGenerator;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlocks;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesItems;

public class AstromineTechnologiesModelStateGenerators extends AstromineModelStateGenerators {
	public final ModelStateGenerator PRIMITIVE_MACHINES = register(new MachineModelStateGenerator(
			AstromineTechnologiesBlocks.PRIMITIVE_ALLOY_SMELTER,
			AstromineTechnologiesBlocks.PRIMITIVE_ELECTRIC_FURNACE,
			AstromineTechnologiesBlocks.PRIMITIVE_ELECTROLYZER,
			AstromineTechnologiesBlocks.PRIMITIVE_REFINERY,
			AstromineTechnologiesBlocks.PRIMITIVE_FLUID_MIXER,
			AstromineTechnologiesBlocks.PRIMITIVE_FLUID_GENERATOR,
			AstromineTechnologiesBlocks.PRIMITIVE_PRESS,
			AstromineTechnologiesBlocks.PRIMITIVE_SOLID_GENERATOR,
			AstromineTechnologiesBlocks.PRIMITIVE_TRITURATOR,
			AstromineTechnologiesBlocks.PRIMITIVE_WIRE_MILL,
			AstromineTechnologiesBlocks.PRIMITIVE_SOLIDIFIER,
			AstromineTechnologiesBlocks.PRIMITIVE_MELTER,
			AstromineTechnologiesBlocks.PRIMITIVE_BUFFER,
			AstromineTechnologiesBlocks.PRIMITIVE_CAPACITOR,
			AstromineTechnologiesBlocks.PRIMITIVE_TANK
	));

	public final ModelStateGenerator BASIC_MACHINES = register(new MachineModelStateGenerator(
			AstromineTechnologiesBlocks.BASIC_ALLOY_SMELTER,
			AstromineTechnologiesBlocks.BASIC_ELECTRIC_FURNACE,
			AstromineTechnologiesBlocks.BASIC_ELECTROLYZER,
			AstromineTechnologiesBlocks.BASIC_REFINERY,
			AstromineTechnologiesBlocks.BASIC_FLUID_MIXER,
			AstromineTechnologiesBlocks.BASIC_FLUID_GENERATOR,
			AstromineTechnologiesBlocks.BASIC_PRESS,
			AstromineTechnologiesBlocks.BASIC_SOLID_GENERATOR,
			AstromineTechnologiesBlocks.BASIC_TRITURATOR,
			AstromineTechnologiesBlocks.BASIC_WIRE_MILL,
			AstromineTechnologiesBlocks.BASIC_SOLIDIFIER,
			AstromineTechnologiesBlocks.BASIC_MELTER,
			AstromineTechnologiesBlocks.BASIC_BUFFER,
			AstromineTechnologiesBlocks.BASIC_CAPACITOR,
			AstromineTechnologiesBlocks.BASIC_TANK
			));

	public final ModelStateGenerator ADVANCED_MACHINES = register(new MachineModelStateGenerator(
			AstromineTechnologiesBlocks.ADVANCED_ALLOY_SMELTER,
			AstromineTechnologiesBlocks.ADVANCED_ELECTRIC_FURNACE,
			AstromineTechnologiesBlocks.ADVANCED_ELECTROLYZER,
			AstromineTechnologiesBlocks.ADVANCED_REFINERY,
			AstromineTechnologiesBlocks.ADVANCED_FLUID_MIXER,
			AstromineTechnologiesBlocks.ADVANCED_FLUID_GENERATOR,
			AstromineTechnologiesBlocks.ADVANCED_PRESS,
			AstromineTechnologiesBlocks.ADVANCED_SOLID_GENERATOR,
			AstromineTechnologiesBlocks.ADVANCED_TRITURATOR,
			AstromineTechnologiesBlocks.ADVANCED_WIRE_MILL,
			AstromineTechnologiesBlocks.ADVANCED_SOLIDIFIER,
			AstromineTechnologiesBlocks.ADVANCED_MELTER,
			AstromineTechnologiesBlocks.ADVANCED_BUFFER,
			AstromineTechnologiesBlocks.ADVANCED_CAPACITOR,
			AstromineTechnologiesBlocks.ADVANCED_TANK
	));

	public final ModelStateGenerator ELITE_MACHINES = register(new MachineModelStateGenerator(
			AstromineTechnologiesBlocks.ELITE_ALLOY_SMELTER,
			AstromineTechnologiesBlocks.ELITE_ELECTRIC_FURNACE,
			AstromineTechnologiesBlocks.ELITE_ELECTROLYZER,
			AstromineTechnologiesBlocks.ELITE_REFINERY,
			AstromineTechnologiesBlocks.ELITE_FLUID_MIXER,
			AstromineTechnologiesBlocks.ELITE_FLUID_GENERATOR,
			AstromineTechnologiesBlocks.ELITE_PRESS,
			AstromineTechnologiesBlocks.ELITE_SOLID_GENERATOR,
			AstromineTechnologiesBlocks.ELITE_TRITURATOR,
			AstromineTechnologiesBlocks.ELITE_WIRE_MILL,
			AstromineTechnologiesBlocks.ELITE_SOLIDIFIER,
			AstromineTechnologiesBlocks.ELITE_MELTER,
			AstromineTechnologiesBlocks.ELITE_BUFFER,
			AstromineTechnologiesBlocks.ELITE_CAPACITOR,
			AstromineTechnologiesBlocks.ELITE_TANK
	));

	public final ModelStateGenerator CREATIVE_MACHINES = register(new MachineModelStateGenerator(
			AstromineTechnologiesBlocks.CREATIVE_BUFFER,
			AstromineTechnologiesBlocks.CREATIVE_CAPACITOR,
			AstromineTechnologiesBlocks.CREATIVE_TANK
	));

	public final ModelStateGenerator MACHINE_CHASSIS = register(new GenericItemModelGenerator(
			AstromineTechnologiesItems.MACHINE_CHASSIS
	));

	public final ModelStateGenerator UPGRADE_KIT = register(new GenericItemModelGenerator(
			AstromineTechnologiesItems.BASIC_MACHINE_UPGRADE_KIT,
			AstromineTechnologiesItems.ADVANCED_MACHINE_UPGRADE_KIT,
			AstromineTechnologiesItems.ELITE_MACHINE_UPGRADE_KIT
	));

	public final ModelStateGenerator CANISTERS = register(new GenericItemModelGenerator(
			AstromineTechnologiesItems.LARGE_PORTABLE_TANK,
			AstromineTechnologiesItems.PORTABLE_TANK
	));

	public final ModelStateGenerator CIRCUITS = register(new GenericItemModelGenerator(
			AstromineTechnologiesItems.PRIMITIVE_CIRCUIT,
			AstromineTechnologiesItems.BASIC_CIRCUIT,
			AstromineTechnologiesItems.ADVANCED_CIRCUIT,
			AstromineTechnologiesItems.ELITE_CIRCUIT
	));

	public final ModelStateGenerator PLATING = register(new GenericItemModelGenerator(
			AstromineTechnologiesItems.PRIMITIVE_PLATING,
			AstromineTechnologiesItems.BASIC_PLATING,
			AstromineTechnologiesItems.ADVANCED_PLATING,
			AstromineTechnologiesItems.ELITE_PLATING
	));

	public final ModelStateGenerator BATTERIES = register(new GenericItemModelGenerator(
			AstromineTechnologiesItems.PRIMITIVE_BATTERY,
			AstromineTechnologiesItems.BASIC_BATTERY,
			AstromineTechnologiesItems.ADVANCED_BATTERY,
			AstromineTechnologiesItems.ELITE_BATTERY,
			AstromineTechnologiesItems.CREATIVE_BATTERY
	));

	public final ModelStateGenerator BATTERY_PACKS = register(new GenericItemModelGenerator(
			AstromineTechnologiesItems.PRIMITIVE_BATTERY_PACK,
			AstromineTechnologiesItems.BASIC_BATTERY_PACK,
			AstromineTechnologiesItems.ADVANCED_BATTERY_PACK,
			AstromineTechnologiesItems.ELITE_BATTERY_PACK,
			AstromineTechnologiesItems.CREATIVE_BATTERY_PACK
	));

	public final ModelStateGenerator DRILLS = register(new HandheldItemModelGenerator(
			AstromineTechnologiesItems.PRIMITIVE_DRILL,
			AstromineTechnologiesItems.BASIC_DRILL,
			AstromineTechnologiesItems.ADVANCED_DRILL,
			AstromineTechnologiesItems.ELITE_DRILL
	));

	public final ModelStateGenerator DRILL_HEADS = register(new HandheldItemModelGenerator(
			AstromineTechnologiesItems.PRIMITIVE_DRILL_BASE,
			AstromineTechnologiesItems.BASIC_DRILL_BASE,
			AstromineTechnologiesItems.ADVANCED_DRILL_BASE,
			AstromineTechnologiesItems.ELITE_DRILL_BASE
	));

	public final ModelStateGenerator DRILL_HEAD = register(new HandheldItemModelGenerator(
			AstromineTechnologiesItems.DRILL_HEAD
	));

	public final ModelStateGenerator HOLOGRAPHIC_CONNECTOR = register(new HandheldItemModelGenerator(
			AstromineTechnologiesItems.HOLOGRAPHIC_CONNECTOR
	));
}
