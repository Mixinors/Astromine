package com.github.chainmailstudios.astromine.technologies.datagen;

import com.github.chainmailstudios.astromine.common.utilities.type.MachineType;
import com.github.chainmailstudios.astromine.datagen.generator.modelstate.ModelStateGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.modelstate.onetime.GenericItemModelStateGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.modelstate.onetime.HandheldItemModelStateGenerator;
import com.github.chainmailstudios.astromine.datagen.registry.AstromineModelStateGenerators;
import com.github.chainmailstudios.astromine.technologies.datagen.generators.modelstate.BufferModelStateGenerator;
import com.github.chainmailstudios.astromine.technologies.datagen.generators.modelstate.TieredFacingModelStateGenerator;
import com.github.chainmailstudios.astromine.technologies.datagen.generators.modelstate.MachineModelStateGenerator;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlocks;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesItems;

public class AstromineTechnologiesModelStateGenerators extends AstromineModelStateGenerators {
	public final ModelStateGenerator PRIMITIVE_CAPACITOR = register(new TieredFacingModelStateGenerator(MachineType.PRIMITIVE, AstromineTechnologiesBlocks.PRIMITIVE_CAPACITOR));
	public final ModelStateGenerator BASIC_CAPACITOR = register(new TieredFacingModelStateGenerator(MachineType.BASIC, AstromineTechnologiesBlocks.BASIC_CAPACITOR));
	public final ModelStateGenerator ELITE_CAPACITOR = register(new TieredFacingModelStateGenerator(MachineType.ELITE, AstromineTechnologiesBlocks.ELITE_CAPACITOR));

	public final ModelStateGenerator ADVANCED_STUFF = register(new TieredFacingModelStateGenerator(MachineType.ADVANCED,
			AstromineTechnologiesBlocks.ADVANCED_CAPACITOR,
			AstromineTechnologiesBlocks.CREATIVE_CAPACITOR,
			AstromineTechnologiesBlocks.TANK,
			AstromineTechnologiesBlocks.CREATIVE_TANK,
			AstromineTechnologiesBlocks.HOLOGRAPHIC_BRIDGE_PROJECTOR
	));

	public final ModelStateGenerator BUFFERS = register(new BufferModelStateGenerator(MachineType.ADVANCED,
			AstromineTechnologiesBlocks.BASIC_BUFFER,
			AstromineTechnologiesBlocks.ADVANCED_BUFFER,
			AstromineTechnologiesBlocks.ELITE_BUFFER,
			AstromineTechnologiesBlocks.CREATIVE_BUFFER
	));

	public final ModelStateGenerator PRIMITIVE_MACHINES = register(new MachineModelStateGenerator(MachineType.PRIMITIVE,
			AstromineTechnologiesBlocks.PRIMITIVE_ALLOY_SMELTER,
			AstromineTechnologiesBlocks.PRIMITIVE_ELECTRIC_SMELTER,
			AstromineTechnologiesBlocks.PRIMITIVE_ELECTROLYZER,
			AstromineTechnologiesBlocks.PRIMITIVE_FLUID_MIXER,
			AstromineTechnologiesBlocks.PRIMITIVE_LIQUID_GENERATOR,
			AstromineTechnologiesBlocks.PRIMITIVE_PRESSER,
			AstromineTechnologiesBlocks.PRIMITIVE_SOLID_GENERATOR,
			AstromineTechnologiesBlocks.PRIMITIVE_TRITURATOR
	));
	public final ModelStateGenerator BASIC_MACHINES = register(new MachineModelStateGenerator(MachineType.BASIC,
			AstromineTechnologiesBlocks.BASIC_ALLOY_SMELTER,
			AstromineTechnologiesBlocks.BASIC_ELECTRIC_SMELTER,
			AstromineTechnologiesBlocks.BASIC_ELECTROLYZER,
			AstromineTechnologiesBlocks.BASIC_FLUID_MIXER,
			AstromineTechnologiesBlocks.BASIC_LIQUID_GENERATOR,
			AstromineTechnologiesBlocks.BASIC_PRESSER,
			AstromineTechnologiesBlocks.BASIC_SOLID_GENERATOR,
			AstromineTechnologiesBlocks.BASIC_TRITURATOR
	));
	public final ModelStateGenerator ADVANCED_MACHINES = register(new MachineModelStateGenerator(MachineType.ADVANCED,
			AstromineTechnologiesBlocks.ADVANCED_ALLOY_SMELTER,
			AstromineTechnologiesBlocks.ADVANCED_ELECTRIC_SMELTER,
			AstromineTechnologiesBlocks.ADVANCED_ELECTROLYZER,
			AstromineTechnologiesBlocks.ADVANCED_FLUID_MIXER,
			AstromineTechnologiesBlocks.ADVANCED_LIQUID_GENERATOR,
			AstromineTechnologiesBlocks.ADVANCED_PRESSER,
			AstromineTechnologiesBlocks.ADVANCED_SOLID_GENERATOR,
			AstromineTechnologiesBlocks.ADVANCED_TRITURATOR,
			AstromineTechnologiesBlocks.FLUID_INSERTER,
			AstromineTechnologiesBlocks.FLUID_EXTRACTOR,
			AstromineTechnologiesBlocks.BLOCK_BREAKER,
			AstromineTechnologiesBlocks.BLOCK_PLACER
	));
	public final ModelStateGenerator ELITE_MACHINES = register(new MachineModelStateGenerator(MachineType.ELITE,
			AstromineTechnologiesBlocks.ELITE_ALLOY_SMELTER,
			AstromineTechnologiesBlocks.ELITE_ELECTRIC_SMELTER,
			AstromineTechnologiesBlocks.ELITE_ELECTROLYZER,
			AstromineTechnologiesBlocks.ELITE_FLUID_MIXER,
			AstromineTechnologiesBlocks.ELITE_LIQUID_GENERATOR,
			AstromineTechnologiesBlocks.ELITE_PRESSER,
			AstromineTechnologiesBlocks.ELITE_SOLID_GENERATOR,
			AstromineTechnologiesBlocks.ELITE_TRITURATOR
	));

	public final ModelStateGenerator MACHINE_CHASSIS = register(new GenericItemModelStateGenerator(
			AstromineTechnologiesItems.PRIMITIVE_MACHINE_CHASSIS,
			AstromineTechnologiesItems.BASIC_MACHINE_CHASSIS,
			AstromineTechnologiesItems.ADVANCED_MACHINE_CHASSIS,
			AstromineTechnologiesItems.ELITE_MACHINE_CHASSIS
	));

	public final ModelStateGenerator CANISTERS = register(new GenericItemModelStateGenerator(
			AstromineTechnologiesItems.GAS_CANISTER,
			AstromineTechnologiesItems.PRESSURIZED_GAS_CANISTER
	));

	public final ModelStateGenerator CIRCUITS = register(new GenericItemModelStateGenerator(
			AstromineTechnologiesItems.BASIC_CIRCUIT,
			AstromineTechnologiesItems.ADVANCED_CIRCUIT,
			AstromineTechnologiesItems.ELITE_CIRCUIT
	));

	public final ModelStateGenerator BATTERIES = register(new GenericItemModelStateGenerator(
			AstromineTechnologiesItems.BASIC_BATTERY,
			AstromineTechnologiesItems.ADVANCED_BATTERY,
			AstromineTechnologiesItems.ELITE_BATTERY,
			AstromineTechnologiesItems.CREATIVE_BATTERY
	));

	public final ModelStateGenerator DRILLS = register(new HandheldItemModelStateGenerator(
			AstromineTechnologiesItems.BASIC_DRILL,
			AstromineTechnologiesItems.ADVANCED_DRILL,
			AstromineTechnologiesItems.ELITE_DRILL
	));
}
