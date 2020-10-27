package com.github.chainmailstudios.astromine.technologies.datagen;

import com.github.chainmailstudios.astromine.common.utilities.tier.MachineTier;
import com.github.chainmailstudios.astromine.datagen.generator.modelstate.ModelStateGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.modelstate.onetime.GenericItemModelGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.modelstate.onetime.HandheldItemModelGenerator;
import com.github.chainmailstudios.astromine.datagen.registry.AstromineModelStateGenerators;
import com.github.chainmailstudios.astromine.technologies.datagen.generators.modelstate.BufferModelStateGenerator;
import com.github.chainmailstudios.astromine.technologies.datagen.generators.modelstate.MachineModelStateGenerator;
import com.github.chainmailstudios.astromine.technologies.datagen.generators.modelstate.TieredFacingModelStateGenerator;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlocks;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesItems;

public class AstromineTechnologiesModelStateGenerators extends AstromineModelStateGenerators {
	public final ModelStateGenerator MISCELLANEOUS = register(new TieredFacingModelStateGenerator(MachineTier.ADVANCED,
			AstromineTechnologiesBlocks.HOLOGRAPHIC_BRIDGE_PROJECTOR
	));

	public final ModelStateGenerator PRIMITIVE_BUFFER = register(new BufferModelStateGenerator(MachineTier.PRIMITIVE,
			AstromineTechnologiesBlocks.PRIMITIVE_BUFFER));

	public final ModelStateGenerator BASIC_BUFFER = register(new BufferModelStateGenerator(MachineTier.BASIC,
			AstromineTechnologiesBlocks.BASIC_BUFFER));

	public final ModelStateGenerator ADVANCED_BUFFER = register(new BufferModelStateGenerator(MachineTier.ADVANCED,
			AstromineTechnologiesBlocks.ADVANCED_BUFFER));

	public final ModelStateGenerator ELITE_BUFFER = register(new BufferModelStateGenerator(MachineTier.ELITE,
			AstromineTechnologiesBlocks.ELITE_BUFFER));

	public final ModelStateGenerator CREATIVE_BUFFER = register(new BufferModelStateGenerator(MachineTier.CREATIVE,
			AstromineTechnologiesBlocks.CREATIVE_BUFFER));

	public final ModelStateGenerator PRIMITIVE_SPECIAL_MACHINES = register(new TieredFacingModelStateGenerator(MachineTier.PRIMITIVE,
			AstromineTechnologiesBlocks.PRIMITIVE_TANK,
			AstromineTechnologiesBlocks.PRIMITIVE_CAPACITOR
	));

	public final ModelStateGenerator BASIC_SPECIAL_MACHINES = register(new TieredFacingModelStateGenerator(MachineTier.BASIC,
			AstromineTechnologiesBlocks.BASIC_TANK,
			AstromineTechnologiesBlocks.BASIC_CAPACITOR
	));

	public final ModelStateGenerator ADVANCED_SPECIAL_MACHINES = register(new TieredFacingModelStateGenerator(MachineTier.ADVANCED,
			AstromineTechnologiesBlocks.ADVANCED_TANK,
			AstromineTechnologiesBlocks.ADVANCED_CAPACITOR
	));

	public final ModelStateGenerator ELITE_SPECIAL_MACHINES = register(new TieredFacingModelStateGenerator(MachineTier.ELITE,
			AstromineTechnologiesBlocks.ELITE_TANK,
			AstromineTechnologiesBlocks.ELITE_CAPACITOR
	));

	public final ModelStateGenerator CREATIVE_SPECIAL_MACHINES = register(new TieredFacingModelStateGenerator(MachineTier.CREATIVE,
			AstromineTechnologiesBlocks.CREATIVE_TANK,
			AstromineTechnologiesBlocks.CREATIVE_CAPACITOR
	));


	public final ModelStateGenerator PRIMITIVE_MACHINES = register(new MachineModelStateGenerator(MachineTier.PRIMITIVE,
			AstromineTechnologiesBlocks.PRIMITIVE_ALLOY_SMELTER,
			AstromineTechnologiesBlocks.PRIMITIVE_ELECTRIC_SMELTER,
			AstromineTechnologiesBlocks.PRIMITIVE_ELECTROLYZER,
			AstromineTechnologiesBlocks.PRIMITIVE_REFINERY,
			AstromineTechnologiesBlocks.PRIMITIVE_FLUID_MIXER,
			AstromineTechnologiesBlocks.PRIMITIVE_LIQUID_GENERATOR,
			AstromineTechnologiesBlocks.PRIMITIVE_PRESSER,
			AstromineTechnologiesBlocks.PRIMITIVE_SOLID_GENERATOR,
			AstromineTechnologiesBlocks.PRIMITIVE_TRITURATOR,
			AstromineTechnologiesBlocks.PRIMITIVE_WIREMILL
	));
	public final ModelStateGenerator BASIC_MACHINES = register(new MachineModelStateGenerator(MachineTier.BASIC,
			AstromineTechnologiesBlocks.BASIC_ALLOY_SMELTER,
			AstromineTechnologiesBlocks.BASIC_ELECTRIC_SMELTER,
			AstromineTechnologiesBlocks.BASIC_ELECTROLYZER,
			AstromineTechnologiesBlocks.BASIC_REFINERY,
			AstromineTechnologiesBlocks.BASIC_FLUID_MIXER,
			AstromineTechnologiesBlocks.BASIC_LIQUID_GENERATOR,
			AstromineTechnologiesBlocks.BASIC_PRESSER,
			AstromineTechnologiesBlocks.BASIC_SOLID_GENERATOR,
			AstromineTechnologiesBlocks.BASIC_TRITURATOR,
			AstromineTechnologiesBlocks.BASIC_WIREMILL
	));
	public final ModelStateGenerator ADVANCED_MACHINES = register(new MachineModelStateGenerator(MachineTier.ADVANCED,
			AstromineTechnologiesBlocks.ADVANCED_ALLOY_SMELTER,
			AstromineTechnologiesBlocks.ADVANCED_ELECTRIC_SMELTER,
			AstromineTechnologiesBlocks.ADVANCED_ELECTROLYZER,
			AstromineTechnologiesBlocks.ADVANCED_REFINERY,
			AstromineTechnologiesBlocks.ADVANCED_FLUID_MIXER,
			AstromineTechnologiesBlocks.ADVANCED_LIQUID_GENERATOR,
			AstromineTechnologiesBlocks.ADVANCED_PRESSER,
			AstromineTechnologiesBlocks.ADVANCED_SOLID_GENERATOR,
			AstromineTechnologiesBlocks.ADVANCED_TRITURATOR,
			AstromineTechnologiesBlocks.ADVANCED_WIREMILL,
			AstromineTechnologiesBlocks.FLUID_INSERTER,
			AstromineTechnologiesBlocks.FLUID_EXTRACTOR,
			AstromineTechnologiesBlocks.BLOCK_BREAKER,
			AstromineTechnologiesBlocks.BLOCK_PLACER
	));
	public final ModelStateGenerator ELITE_MACHINES = register(new MachineModelStateGenerator(MachineTier.ELITE,
			AstromineTechnologiesBlocks.ELITE_ALLOY_SMELTER,
			AstromineTechnologiesBlocks.ELITE_ELECTRIC_SMELTER,
			AstromineTechnologiesBlocks.ELITE_ELECTROLYZER,
			AstromineTechnologiesBlocks.ELITE_REFINERY,
			AstromineTechnologiesBlocks.ELITE_FLUID_MIXER,
			AstromineTechnologiesBlocks.ELITE_LIQUID_GENERATOR,
			AstromineTechnologiesBlocks.ELITE_PRESSER,
			AstromineTechnologiesBlocks.ELITE_SOLID_GENERATOR,
			AstromineTechnologiesBlocks.ELITE_TRITURATOR,
			AstromineTechnologiesBlocks.ELITE_WIREMILL
	));

	public final ModelStateGenerator MACHINE_CHASSIS = register(new GenericItemModelGenerator(
			AstromineTechnologiesItems.PRIMITIVE_MACHINE_CHASSIS,
			AstromineTechnologiesItems.BASIC_MACHINE_CHASSIS,
			AstromineTechnologiesItems.ADVANCED_MACHINE_CHASSIS,
			AstromineTechnologiesItems.ELITE_MACHINE_CHASSIS
	));

	public final ModelStateGenerator UPGRADE_KIT = register(new GenericItemModelGenerator(
			AstromineTechnologiesItems.BASIC_MACHINE_UPGRADE_KIT,
			AstromineTechnologiesItems.ADVANCED_MACHINE_UPGRADE_KIT,
			AstromineTechnologiesItems.ELITE_MACHINE_UPGRADE_KIT
	));

	public final ModelStateGenerator CANISTERS = register(new GenericItemModelGenerator(
			AstromineTechnologiesItems.GAS_CANISTER,
			AstromineTechnologiesItems.PRESSURIZED_GAS_CANISTER
	));

	public final ModelStateGenerator CIRCUITS = register(new GenericItemModelGenerator(
			AstromineTechnologiesItems.BASIC_CIRCUIT,
			AstromineTechnologiesItems.ADVANCED_CIRCUIT,
			AstromineTechnologiesItems.ELITE_CIRCUIT
	));

	public final ModelStateGenerator BATTERIES = register(new GenericItemModelGenerator(
			AstromineTechnologiesItems.BASIC_BATTERY,
			AstromineTechnologiesItems.ADVANCED_BATTERY,
			AstromineTechnologiesItems.ELITE_BATTERY,
			AstromineTechnologiesItems.CREATIVE_BATTERY
	));

	public final ModelStateGenerator DRILLS = register(new HandheldItemModelGenerator(
			AstromineTechnologiesItems.BASIC_DRILL,
			AstromineTechnologiesItems.ADVANCED_DRILL,
			AstromineTechnologiesItems.ELITE_DRILL
	));
}
