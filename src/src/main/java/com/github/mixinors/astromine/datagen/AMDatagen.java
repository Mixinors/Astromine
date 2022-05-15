/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
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

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.fluid.SimpleFluid;
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
import com.google.common.collect.ImmutableList;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.mininglevel.v1.MiningLevelManager;
import net.fabricmc.yarn.constants.MiningLevels;
import net.minecraft.block.Block;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.TagKey;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class AMDatagen implements DataGeneratorEntrypoint {
	public static final List<SimpleFluid> FLUIDS = List.of(
			AMFluids.OIL,
			AMFluids.FUEL,
			AMFluids.BIOMASS,
			AMFluids.OXYGEN,
			AMFluids.HYDROGEN,
			AMFluids.MOLTEN_AMETHYST,
			AMFluids.MOLTEN_ASTERITE,
			AMFluids.MOLTEN_BRONZE,
			AMFluids.MOLTEN_COPPER,
			AMFluids.MOLTEN_DIAMOND,
			AMFluids.MOLTEN_ELECTRUM,
			AMFluids.MOLTEN_EMERALD,
			AMFluids.MOLTEN_FOOLS_GOLD,
			AMFluids.MOLTEN_GALAXIUM,
			AMFluids.MOLTEN_GOLD,
			AMFluids.MOLTEN_IRON,
			AMFluids.MOLTEN_LAPIS,
			AMFluids.MOLTEN_LEAD,
			AMFluids.MOLTEN_METEORIC_STEEL,
			AMFluids.MOLTEN_METITE,
			AMFluids.MOLTEN_NETHERITE,
			AMFluids.MOLTEN_QUARTZ,
			AMFluids.MOLTEN_REDSTONE,
			AMFluids.MOLTEN_SILVER,
			AMFluids.MOLTEN_STEEL,
			AMFluids.MOLTEN_STELLUM,
			AMFluids.MOLTEN_TIN,
			AMFluids.MOLTEN_UNIVITE
	);
	
	public static final List<BlockVariant> ORE_VARIANTS = List.of(
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
			AMBlocks.BLOCK_PLACER.get(),
			
			AMBlocks.PUMP.get()
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
	
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator dataGenerator) {
		AMBlockFamilies.init();
		MaterialFamilies.init();
		dataGenerator.addProvider(AMModelProvider::new);
		dataGenerator.addProvider(AMRecipeProvider::new);
		var blockTagProvider = new AMBlockTagProvider(dataGenerator);
		dataGenerator.addProvider(blockTagProvider);
		dataGenerator.addProvider(new AMItemTagProvider(dataGenerator, blockTagProvider));
		dataGenerator.addProvider(AMFluidTagProvider::new);
		dataGenerator.addProvider(AMBlockLootTableProvider::new);
	}
	
	public record HarvestData(TagKey<Block> mineableTag, int miningLevel) {
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
		
		public TagKey<Block> miningLevelTag() {
			return MiningLevelManager.getBlockTag(miningLevel());
		}
	}
	
	public static <T extends Comparable<?>, U> TreeMap<T, U> toTreeMap(Map<T, U> map) {
		return new TreeMap<>(map);
	}
}
