package com.github.mixinors.astromine.datagen;

import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import com.github.mixinors.astromine.common.fluid.ExtendedFluid;
import com.github.mixinors.astromine.datagen.family.block.AMBlockFamilies;
import com.github.mixinors.astromine.datagen.family.material.MaterialFamilies;
import com.github.mixinors.astromine.datagen.family.material.variant.BlockVariant;
import com.github.mixinors.astromine.datagen.family.material.variant.ItemVariant;
import com.github.mixinors.astromine.datagen.provider.AMBlockLootTableProvider;
import com.github.mixinors.astromine.datagen.provider.AMModelProvider;
import com.github.mixinors.astromine.datagen.provider.AMRecipeProvider;
import com.github.mixinors.astromine.datagen.provider.AMTagProviders;
import com.github.mixinors.astromine.registry.common.AMBlocks;
import com.github.mixinors.astromine.registry.common.AMFluids;

import net.minecraft.block.Block;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class AMDatagen implements DataGeneratorEntrypoint {
	public static final List<ExtendedFluid> FLUIDS = List.of(
			AMFluids.OIL,
			AMFluids.FUEL,
			AMFluids.BIOMASS,
			AMFluids.OXYGEN,
			AMFluids.HYDROGEN
	);

	public static List<BlockVariant> ORE_VARIANTS = List.of(
			BlockVariant.STONE_ORE,
			BlockVariant.DEEPSLATE_ORE,
			BlockVariant.NETHER_ORE,
			BlockVariant.METEOR_ORE,
			BlockVariant.ASTEROID_ORE
	);

	public static final List<ItemVariant> ARMOR_VARIANTS = List.of(
			ItemVariant.HELMET,
			ItemVariant.CHESTPLATE,
			ItemVariant.LEGGINGS,
			ItemVariant.BOOTS
	);

	public static final List<ItemVariant> TOOL_VARIANTS = List.of(
			ItemVariant.PICKAXE,
			ItemVariant.AXE,
			ItemVariant.SHOVEL,
			ItemVariant.SWORD,
			ItemVariant.HOE
	);

	public static final List<ItemVariant> EQUIPMENT_VARIANTS = new ImmutableList.Builder<ItemVariant>().add(
			ItemVariant.HORSE_ARMOR
	).addAll(ARMOR_VARIANTS).addAll(TOOL_VARIANTS).build();

	public static final List<ItemVariant> CLUSTER_VARIANTS = List.of(
			ItemVariant.METEOR_CLUSTER,
			ItemVariant.ASTEROID_CLUSTER
	);

	public static final Set<Block> MACHINES = Set.of(
			AMBlocks.PRIMITIVE_TANK.get(),
			AMBlocks.BASIC_TANK.get(),
			AMBlocks.ADVANCED_TANK.get(),
			AMBlocks.ELITE_TANK.get(),
			AMBlocks.CREATIVE_TANK.get(),

			AMBlocks.PRIMITIVE_SOLID_GENERATOR.get(),
			AMBlocks.BASIC_SOLID_GENERATOR.get(),
			AMBlocks.ADVANCED_SOLID_GENERATOR.get(),
			AMBlocks.ELITE_SOLID_GENERATOR.get(),

			AMBlocks.PRIMITIVE_LIQUID_GENERATOR.get(),
			AMBlocks.BASIC_LIQUID_GENERATOR.get(),
			AMBlocks.ADVANCED_LIQUID_GENERATOR.get(),
			AMBlocks.ELITE_LIQUID_GENERATOR.get(),

			AMBlocks.PRIMITIVE_ELECTRIC_FURNACE.get(),
			AMBlocks.BASIC_ELECTRIC_FURNACE.get(),
			AMBlocks.ADVANCED_ELECTRIC_FURNACE.get(),
			AMBlocks.ELITE_ELECTRIC_FURNACE.get(),

			AMBlocks.PRIMITIVE_ALLOY_SMELTER.get(),
			AMBlocks.BASIC_ALLOY_SMELTER.get(),
			AMBlocks.ADVANCED_ALLOY_SMELTER.get(),
			AMBlocks.ELITE_ALLOY_SMELTER.get(),

			AMBlocks.PRIMITIVE_TRITURATOR.get(),
			AMBlocks.BASIC_TRITURATOR.get(),
			AMBlocks.ADVANCED_TRITURATOR.get(),
			AMBlocks.ELITE_TRITURATOR.get(),

			AMBlocks.PRIMITIVE_PRESSER.get(),
			AMBlocks.BASIC_PRESSER.get(),
			AMBlocks.ADVANCED_PRESSER.get(),
			AMBlocks.ELITE_PRESSER.get(),

			AMBlocks.PRIMITIVE_WIREMILL.get(),
			AMBlocks.BASIC_WIREMILL.get(),
			AMBlocks.ADVANCED_WIREMILL.get(),
			AMBlocks.ELITE_WIREMILL.get(),

			AMBlocks.PRIMITIVE_ELECTROLYZER.get(),
			AMBlocks.BASIC_ELECTROLYZER.get(),
			AMBlocks.ADVANCED_ELECTROLYZER.get(),
			AMBlocks.ELITE_ELECTROLYZER.get(),

			AMBlocks.PRIMITIVE_REFINERY.get(),
			AMBlocks.BASIC_REFINERY.get(),
			AMBlocks.ADVANCED_REFINERY.get(),
			AMBlocks.ELITE_REFINERY.get(),

			AMBlocks.PRIMITIVE_FLUID_MIXER.get(),
			AMBlocks.BASIC_FLUID_MIXER.get(),
			AMBlocks.ADVANCED_FLUID_MIXER.get(),
			AMBlocks.ELITE_FLUID_MIXER.get(),

			AMBlocks.PRIMITIVE_SOLIDIFIER.get(),
			AMBlocks.BASIC_SOLIDIFIER.get(),
			AMBlocks.ADVANCED_SOLIDIFIER.get(),
			AMBlocks.ELITE_SOLIDIFIER.get(),

			AMBlocks.PRIMITIVE_MELTER.get(),
			AMBlocks.BASIC_MELTER.get(),
			AMBlocks.ADVANCED_MELTER.get(),
			AMBlocks.ELITE_MELTER.get(),

			AMBlocks.PRIMITIVE_BUFFER.get(),
			AMBlocks.BASIC_BUFFER.get(),
			AMBlocks.ADVANCED_BUFFER.get(),
			AMBlocks.ELITE_BUFFER.get(),
			AMBlocks.CREATIVE_BUFFER.get(),

			AMBlocks.PRIMITIVE_CAPACITOR.get(),
			AMBlocks.BASIC_CAPACITOR.get(),
			AMBlocks.ADVANCED_CAPACITOR.get(),
			AMBlocks.ELITE_CAPACITOR.get(),
			AMBlocks.CREATIVE_CAPACITOR.get(),

			AMBlocks.VENT.get(),

			AMBlocks.FLUID_EXTRACTOR.get(),
			AMBlocks.FLUID_INSERTER.get(),

			AMBlocks.BLOCK_BREAKER.get(),
			AMBlocks.BLOCK_PLACER.get()
	);

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator dataGenerator) {
		AMBlockFamilies.init();
		MaterialFamilies.init();
		dataGenerator.addProvider(AMModelProvider::new);
		dataGenerator.addProvider(AMRecipeProvider::new);
		AMTagProviders.AMBlockTagProvider blockTagProvider = new AMTagProviders.AMBlockTagProvider(dataGenerator);
		dataGenerator.addProvider(blockTagProvider);
		dataGenerator.addProvider(new AMTagProviders.AMItemTagProvider(dataGenerator, blockTagProvider));
		dataGenerator.addProvider(AMTagProviders.AMFluidTagProvider::new);
		dataGenerator.addProvider(AMBlockLootTableProvider::new);
	}
}
