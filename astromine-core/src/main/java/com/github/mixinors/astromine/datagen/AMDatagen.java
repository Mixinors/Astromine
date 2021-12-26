/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.mixinors.astromine.datagen;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import com.github.mixinors.astromine.common.fluid.ExtendedFluid;
import com.github.mixinors.astromine.datagen.family.block.AMBlockFamilies;
import com.github.mixinors.astromine.datagen.family.material.MaterialFamilies;
import com.github.mixinors.astromine.datagen.family.material.variant.BlockVariant;
import com.github.mixinors.astromine.datagen.family.material.variant.ItemVariant;
import com.github.mixinors.astromine.datagen.provider.AMBlockLootTableProvider;
import com.github.mixinors.astromine.datagen.provider.AMModelProvider;
import com.github.mixinors.astromine.datagen.provider.AMRecipeProvider;
import com.github.mixinors.astromine.datagen.provider.tag.AMBlockTagProvider;
import com.github.mixinors.astromine.datagen.provider.tag.AMFluidTagProvider;
import com.github.mixinors.astromine.datagen.provider.tag.AMItemTagProvider;
import com.github.mixinors.astromine.registry.common.AMBlocks;
import com.github.mixinors.astromine.registry.common.AMFluids;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.fabricmc.yarn.constants.MiningLevels;

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
			ItemVariant.METEOR_ORE_CLUSTER,
			ItemVariant.ASTEROID_ORE_CLUSTER
	);

	public static final List<Block> PRIMITIVE_MACHINES = List.of(
			AMBlocks.PRIMITIVE_TANK.get(),
			AMBlocks.PRIMITIVE_SOLID_GENERATOR.get(),
			AMBlocks.PRIMITIVE_FLUID_GENERATOR.get(),
			AMBlocks.PRIMITIVE_ELECTRIC_FURNACE.get(),
			AMBlocks.PRIMITIVE_ALLOY_SMELTER.get(),
			AMBlocks.PRIMITIVE_TRITURATOR.get(),
			AMBlocks.PRIMITIVE_PRESSER.get(),
			AMBlocks.PRIMITIVE_WIRE_MILL.get(),
			AMBlocks.PRIMITIVE_ELECTROLYZER.get(),
			AMBlocks.PRIMITIVE_REFINERY.get(),
			AMBlocks.PRIMITIVE_FLUID_MIXER.get(),
			AMBlocks.PRIMITIVE_SOLIDIFIER.get(),
			AMBlocks.PRIMITIVE_MELTER.get(),
			AMBlocks.PRIMITIVE_BUFFER.get(),
			AMBlocks.PRIMITIVE_CAPACITOR.get()
	);

	public static final List<Block> BASIC_MACHINES = List.of(
			AMBlocks.BASIC_TANK.get(),
			AMBlocks.BASIC_SOLID_GENERATOR.get(),
			AMBlocks.BASIC_FLUID_GENERATOR.get(),
			AMBlocks.BASIC_ELECTRIC_FURNACE.get(),
			AMBlocks.BASIC_ALLOY_SMELTER.get(),
			AMBlocks.BASIC_TRITURATOR.get(),
			AMBlocks.BASIC_PRESSER.get(),
			AMBlocks.BASIC_WIRE_MILL.get(),
			AMBlocks.BASIC_ELECTROLYZER.get(),
			AMBlocks.BASIC_REFINERY.get(),
			AMBlocks.BASIC_FLUID_MIXER.get(),
			AMBlocks.BASIC_SOLIDIFIER.get(),
			AMBlocks.BASIC_MELTER.get(),
			AMBlocks.BASIC_BUFFER.get(),
			AMBlocks.BASIC_CAPACITOR.get()
	);

	public static final List<Block> ADVANCED_MACHINES = List.of(
			AMBlocks.ADVANCED_TANK.get(),
			AMBlocks.ADVANCED_SOLID_GENERATOR.get(),
			AMBlocks.ADVANCED_FLUID_GENERATOR.get(),
			AMBlocks.ADVANCED_ELECTRIC_FURNACE.get(),
			AMBlocks.ADVANCED_ALLOY_SMELTER.get(),
			AMBlocks.ADVANCED_TRITURATOR.get(),
			AMBlocks.ADVANCED_PRESSER.get(),
			AMBlocks.ADVANCED_WIRE_MILL.get(),
			AMBlocks.ADVANCED_ELECTROLYZER.get(),
			AMBlocks.ADVANCED_REFINERY.get(),
			AMBlocks.ADVANCED_FLUID_MIXER.get(),
			AMBlocks.ADVANCED_SOLIDIFIER.get(),
			AMBlocks.ADVANCED_MELTER.get(),
			AMBlocks.ADVANCED_BUFFER.get(),
			AMBlocks.ADVANCED_CAPACITOR.get()
	);

	public static final List<Block> ELITE_MACHINES = List.of(
			AMBlocks.ELITE_TANK.get(),
			AMBlocks.ELITE_SOLID_GENERATOR.get(),
			AMBlocks.ELITE_FLUID_GENERATOR.get(),
			AMBlocks.ELITE_ELECTRIC_FURNACE.get(),
			AMBlocks.ELITE_ALLOY_SMELTER.get(),
			AMBlocks.ELITE_TRITURATOR.get(),
			AMBlocks.ELITE_PRESSER.get(),
			AMBlocks.ELITE_WIRE_MILL.get(),
			AMBlocks.ELITE_ELECTROLYZER.get(),
			AMBlocks.ELITE_REFINERY.get(),
			AMBlocks.ELITE_FLUID_MIXER.get(),
			AMBlocks.ELITE_SOLIDIFIER.get(),
			AMBlocks.ELITE_MELTER.get(),
			AMBlocks.ELITE_BUFFER.get(),
			AMBlocks.ELITE_CAPACITOR.get()
	);

	public static final List<Block> CREATIVE_MACHINES = List.of(
			AMBlocks.CREATIVE_TANK.get(),
			AMBlocks.CREATIVE_BUFFER.get(),
			AMBlocks.CREATIVE_CAPACITOR.get()
	);

	public static final List<Block> MISC_MACHINES = List.of(
			AMBlocks.FLUID_EXTRACTOR.get(),
			AMBlocks.FLUID_INSERTER.get(),

			AMBlocks.BLOCK_BREAKER.get(),
			AMBlocks.BLOCK_PLACER.get()
	);
	
	public static final List<Block> MACHINES = new ImmutableList.Builder<Block>()
			.addAll(PRIMITIVE_MACHINES)
			.addAll(BASIC_MACHINES)
			.addAll(ADVANCED_MACHINES)
			.addAll(ELITE_MACHINES)
			.addAll(CREATIVE_MACHINES)
			.addAll(MISC_MACHINES).build();

	public static final List<Block> ENERGY_CABLES = List.of(
			AMBlocks.PRIMITIVE_ENERGY_CABLE.get(),
			AMBlocks.BASIC_ENERGY_CABLE.get(),
			AMBlocks.ADVANCED_ENERGY_CABLE.get(),
			AMBlocks.ELITE_ENERGY_CABLE.get()
	);

	public static final Map<BlockFamily.Variant, Identifier> VANILLA_ITEM_TAG_VARIANTS = Map.of(
			BlockFamily.Variant.SLAB, new Identifier("slabs"),
			BlockFamily.Variant.STAIRS, new Identifier("stairs"),
			BlockFamily.Variant.WALL, new Identifier("walls"),
			BlockFamily.Variant.BUTTON, new Identifier("buttons"),
			BlockFamily.Variant.DOOR, new Identifier("doors"),
			BlockFamily.Variant.FENCE, new Identifier("fences"),
			BlockFamily.Variant.SIGN, new Identifier("signs"),
			BlockFamily.Variant.TRAPDOOR, new Identifier("trapdoors")
	);

	public static final Map<BlockFamily.Variant, Identifier> VANILLA_BLOCK_TAG_VARIANTS = new ImmutableMap.Builder<BlockFamily.Variant, Identifier>().putAll(Map.of(
			BlockFamily.Variant.FENCE_GATE, new Identifier("fence_gates"),
			BlockFamily.Variant.PRESSURE_PLATE, new Identifier("pressure_plates"),
			BlockFamily.Variant.WALL_SIGN, new Identifier("wall_signs")
	)).putAll(VANILLA_ITEM_TAG_VARIANTS).build();

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator dataGenerator) {
		AMBlockFamilies.init();
		MaterialFamilies.init();
		dataGenerator.addProvider(AMModelProvider::new);
		dataGenerator.addProvider(AMRecipeProvider::new);
		AMBlockTagProvider blockTagProvider = new AMBlockTagProvider(dataGenerator);
		dataGenerator.addProvider(blockTagProvider);
		dataGenerator.addProvider(new AMItemTagProvider(dataGenerator, blockTagProvider));
		dataGenerator.addProvider(AMFluidTagProvider::new);
		dataGenerator.addProvider(AMBlockLootTableProvider::new);
	}

	public static final String COMMON_TAG_NAMESPACE = "c";

	public static Identifier createCommonTagId(String path) {
		return new Identifier(COMMON_TAG_NAMESPACE, path);
	}

	public static Tag.Identified<Block> createBlockTag(Identifier id) {
		return TagFactory.BLOCK.create(id);
	}

	public static Tag.Identified<Item> createItemTag(Identifier id) {
		return TagFactory.ITEM.create(id);
	}

	public static Tag.Identified<Fluid> createFluidTag(Identifier id) {
		return TagFactory.FLUID.create(id);
	}

	public static Tag.Identified<Block> createCommonBlockTag(String path) {
		return createBlockTag(createCommonTagId(path));
	}

	public static Tag.Identified<Item> createCommonItemTag(String path) {
		return createItemTag(createCommonTagId(path));
	}

	public static Tag.Identified<Fluid> createCommonFluidTag(String path) {
		return createFluidTag(createCommonTagId(path));
	}

	public record HarvestData(Tag.Identified<Block> mineableTag, int miningLevel) {
		public static final HarvestData PICKAXE = new HarvestData(MiningLevels.WOOD);
		public static final HarvestData STONE_PICKAXE = new HarvestData(MiningLevels.STONE);
		public static final HarvestData IRON_PICKAXE = new HarvestData(MiningLevels.IRON);
		public static final HarvestData DIAMOND_PICKAXE = new HarvestData(MiningLevels.DIAMOND);
		public static final HarvestData NETHERITE_PICKAXE = new HarvestData(MiningLevels.NETHERITE);
		public static final HarvestData LEVEL_5_PICKAXE = new HarvestData(5);
		public static final HarvestData LEVEL_6_PICKAXE = new HarvestData(6);

		public HarvestData(int miningLevel) {
			this(BlockTags.PICKAXE_MINEABLE, miningLevel);
		}

		public Tag.Identified<Block> miningLevelTag() {
			return getMiningLevelTag(miningLevel());
		}
	}

	@Nullable
	public static Tag.Identified<Block> getMiningLevelTag(int miningLevel) {
		if (miningLevel <= 0) return null;
		return switch (miningLevel) {
			case 1 -> BlockTags.NEEDS_STONE_TOOL;
			case 2 -> BlockTags.NEEDS_IRON_TOOL;
			case 3 -> BlockTags.NEEDS_DIAMOND_TOOL;
			default -> createBlockTag(new Identifier("fabric", "needs_tool_level_" + miningLevel));
		};
	}
}
