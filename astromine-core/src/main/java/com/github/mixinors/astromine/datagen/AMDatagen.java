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
import com.github.mixinors.astromine.datagen.provider.AMModelProvider;
import com.github.mixinors.astromine.datagen.provider.AMRecipeProvider;
import com.github.mixinors.astromine.datagen.provider.AMTagProviders;
import com.github.mixinors.astromine.registry.common.AMFluids;

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
	}
}
