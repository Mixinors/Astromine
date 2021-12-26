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

package com.github.mixinors.astromine.datagen.provider.tag;

import java.util.Arrays;
import java.util.List;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.datagen.AMDatagen;
import com.github.mixinors.astromine.datagen.AMDatagen.HarvestData;
import com.github.mixinors.astromine.datagen.family.block.AMBlockFamilies;
import com.github.mixinors.astromine.datagen.family.material.MaterialFamilies;
import com.github.mixinors.astromine.datagen.family.material.MaterialFamily;
import com.github.mixinors.astromine.datagen.family.material.variant.BlockVariant;
import com.github.mixinors.astromine.registry.common.AMBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.registry.Registry;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;

public class AMBlockTagProvider extends FabricTagProvider.BlockTagProvider {
	public static final List<Block> INFINIBURN_BLOCKS = List.of(
			AMBlocks.BLAZING_ASTEROID_STONE.get()
	);

	public static final List<Tag.Identified<Block>> INFINIBURN_TAGS = List.of(
			AMDatagen.createCommonBlockTag("meteor_ores"),
			AMDatagen.createCommonBlockTag("asteroid_ores")
	);

	public static final HarvestData SPACE_STONE_HARVEST_DATA = HarvestData.DIAMOND_PICKAXE;

	public static final HarvestData PRIMITIVE_MACHINE_HARVEST_DATA = HarvestData.STONE_PICKAXE;
	public static final HarvestData BASIC_MACHINE_HARVEST_DATA = HarvestData.IRON_PICKAXE;
	public static final HarvestData ADVANCED_MACHINE_HARVEST_DATA = HarvestData.IRON_PICKAXE;
	public static final HarvestData ELITE_MACHINE_HARVEST_DATA = HarvestData.NETHERITE_PICKAXE;

	public static final HarvestData MISC_MACHINE_HARVEST_DATA = HarvestData.IRON_PICKAXE;

	public static final HarvestData PIPE_AND_CABLE_HARVEST_DATA = HarvestData.STONE_PICKAXE;

	public static final List<BlockFamily> SPACE_STONE_FAMILIES = List.of(
			AMBlockFamilies.METEOR_STONE,
			AMBlockFamilies.SMOOTH_METEOR_STONE,
			AMBlockFamilies.POLISHED_METEOR_STONE,
			AMBlockFamilies.METEOR_STONE_BRICK,
			AMBlockFamilies.ASTEROID_STONE,
			AMBlockFamilies.SMOOTH_ASTEROID_STONE,
			AMBlockFamilies.POLISHED_ASTEROID_STONE,
			AMBlockFamilies.ASTEROID_STONE_BRICK
	);

	public AMBlockTagProvider(FabricDataGenerator dataGenerator) {
		super(dataGenerator);
	}

	@Override
	protected void generateTags() {
		FabricTagBuilder<Block> beaconBaseTagBuilder = getOrCreateTagBuilder(BlockTags.BEACON_BASE_BLOCKS);
		FabricTagBuilder<Block> guardedByPiglinsTagBuilder = getOrCreateTagBuilder(BlockTags.GUARDED_BY_PIGLINS);

		MaterialFamilies.getFamilies().filter(MaterialFamily::shouldGenerateTags).forEachOrdered(family -> {
			family.getBlockTags().forEach((variant, tag) -> {
				getOrCreateTagBuilder(tag).add(family.getVariant(variant));

				if (family.hasAlias()) {
					getOrCreateTagBuilder(family.getAliasTag(variant)).addTag(tag);
				}

				if (family.isPiglinLoved()) {
					guardedByPiglinsTagBuilder.addTag(tag);

					if (family.hasAlias()) {
						guardedByPiglinsTagBuilder.addTag(family.getAliasTag(variant));
					}
				}

				if (variant.hasTag()) {
					getOrCreateTagBuilder(variant.getTag()).addTag(tag);

					if (family.hasAlias()) {
						getOrCreateTagBuilder(variant.getTag()).addTag(family.getAliasTag(variant));
					}
				}

				if (family.shouldGenerateHarvestTags(variant)) addHarvestData(family.getHarvestData(variant), tag);
			});

			if (family.hasAnyBlockVariants(AMDatagen.ORE_VARIANTS)) {
				Tag.Identified<Block> oresTag = family.getBlockTag("ores");
				FabricTagBuilder<Block> oresTagBuilder = getOrCreateTagBuilder(oresTag);
				AMDatagen.ORE_VARIANTS.forEach((variant) -> {
					if (family.hasVariant(variant)) {
						oresTagBuilder.addTag(family.getTag(variant));
					}
				});
				if (family.hasAlias()) {
					getOrCreateTagBuilder(family.getAliasBlockTag("ores")).addTag(oresTag);
				}
			}

			if (family.isValidForBeacon() && family.hasVariant(BlockVariant.BLOCK)) {
				beaconBaseTagBuilder.addTag(family.getTag(BlockVariant.BLOCK));

				if (family.hasAlias()) {
					beaconBaseTagBuilder.addTag(family.getAliasTag(BlockVariant.BLOCK));
				}
			}
		});

		AMBlockFamilies.getFamilies().forEachOrdered(family -> family.getVariants().forEach((variant, block) -> {
			if (AMDatagen.VANILLA_BLOCK_TAG_VARIANTS.containsKey(variant)) {
				getOrCreateTagBuilder(AMDatagen.createBlockTag(AMDatagen.VANILLA_BLOCK_TAG_VARIANTS.get(variant))).add(block);
			}
		}));

		FabricTagBuilder<Block> cauldronsTagBuilder = getOrCreateTagBuilder(BlockTags.CAULDRONS);
		AMDatagen.FLUIDS.forEach((fluid) -> {
			String fluidName = Registry.FLUID.getId(fluid.getStill()).getPath();
			FabricTagBuilder<Block> tagBuilder = getOrCreateTagBuilder(AMDatagen.createCommonBlockTag(fluidName));
			Tag.Identified<Block> cauldronTag = AMDatagen.createCommonBlockTag(fluidName + "_cauldrons");
			FabricTagBuilder<Block> cauldronTagBuilder = getOrCreateTagBuilder(cauldronTag);
			tagBuilder.add(fluid.getBlock());
			cauldronTagBuilder.add(fluid.getCauldron());
			cauldronsTagBuilder.addTag(cauldronTag);
		});

		FabricTagBuilder<Block> oresTagBuilder = getOrCreateTagBuilder(AMDatagen.createCommonBlockTag("ores"));
		AMDatagen.ORE_VARIANTS.forEach((variant) -> {
			if (variant.hasTag()) {
				oresTagBuilder.addTag(variant.getTag());
			}
		});

		Tag.Identified<Block> yellowSandstonesTag = AMDatagen.createCommonBlockTag("yellow_sandstones");
		getOrCreateTagBuilder(yellowSandstonesTag)
				.add(Blocks.SANDSTONE)
				.add(Blocks.CHISELED_SANDSTONE)
				.add(Blocks.CUT_SANDSTONE)
				.add(Blocks.SMOOTH_SANDSTONE);

		Tag.Identified<Block> redSandstonesTag = AMDatagen.createCommonBlockTag("red_sandstones");
		getOrCreateTagBuilder(redSandstonesTag)
				.add(Blocks.RED_SANDSTONE)
				.add(Blocks.CHISELED_RED_SANDSTONE)
				.add(Blocks.CUT_RED_SANDSTONE)
				.add(Blocks.SMOOTH_RED_SANDSTONE);

		getOrCreateTagBuilder(AMDatagen.createCommonBlockTag("sandstones"))
				.addTag(yellowSandstonesTag)
				.addTag(redSandstonesTag);

		getOrCreateTagBuilder(AMDatagen.createCommonBlockTag("quartz_blocks"))
				.add(Blocks.QUARTZ_BLOCK)
				.add(Blocks.QUARTZ_BRICKS)
				.add(Blocks.QUARTZ_PILLAR)
				.add(Blocks.CHISELED_QUARTZ_BLOCK);

		Tag.Identified<Block> unwaxedCopperBlocksTag = AMDatagen.createCommonBlockTag("unwaxed_copper_blocks");
		getOrCreateTagBuilder(unwaxedCopperBlocksTag)
				.add(Blocks.COPPER_BLOCK)
				.add(Blocks.EXPOSED_COPPER)
				.add(Blocks.WEATHERED_COPPER)
				.add(Blocks.OXIDIZED_COPPER);

		Tag.Identified<Block> waxedCopperBlocksTag = AMDatagen.createCommonBlockTag("waxed_copper_blocks");
		getOrCreateTagBuilder(waxedCopperBlocksTag)
				.add(Blocks.WAXED_COPPER_BLOCK)
				.add(Blocks.WAXED_EXPOSED_COPPER)
				.add(Blocks.WAXED_WEATHERED_COPPER)
				.add(Blocks.WAXED_OXIDIZED_COPPER);

		getOrCreateTagBuilder(AMDatagen.createCommonBlockTag("copper_blocks"))
				.addTag(unwaxedCopperBlocksTag)
				.addTag(waxedCopperBlocksTag);

		Tag.Identified<Block> unwaxedCutCopperTag = AMDatagen.createCommonBlockTag("unwaxed_cut_copper");
		getOrCreateTagBuilder(unwaxedCutCopperTag)
				.add(Blocks.CUT_COPPER)
				.add(Blocks.EXPOSED_CUT_COPPER)
				.add(Blocks.WEATHERED_CUT_COPPER)
				.add(Blocks.OXIDIZED_CUT_COPPER);

		Tag.Identified<Block> waxedCutCopperTag = AMDatagen.createCommonBlockTag("waxed_cut_copper");
		getOrCreateTagBuilder(waxedCutCopperTag)
				.add(Blocks.WAXED_CUT_COPPER)
				.add(Blocks.WAXED_EXPOSED_CUT_COPPER)
				.add(Blocks.WAXED_WEATHERED_CUT_COPPER)
				.add(Blocks.WAXED_OXIDIZED_CUT_COPPER);

		getOrCreateTagBuilder(AMDatagen.createCommonBlockTag("cut_copper"))
				.addTag(unwaxedCutCopperTag)
				.addTag(waxedCutCopperTag);

		getOrCreateTagBuilder(AMDatagen.createCommonBlockTag("purpur_blocks"))
				.add(Blocks.PURPUR_BLOCK)
				.add(Blocks.PURPUR_PILLAR);

		getOrCreateTagBuilder(AMDatagen.createCommonBlockTag("mushrooms"))
				.add(Blocks.BROWN_MUSHROOM)
				.add(Blocks.RED_MUSHROOM);

		getOrCreateTagBuilder(AMDatagen.createCommonBlockTag("mushroom_blocks"))
				.add(Blocks.BROWN_MUSHROOM_BLOCK)
				.add(Blocks.RED_MUSHROOM_BLOCK);

		getOrCreateTagBuilder(AMDatagen.createCommonBlockTag("nether_fungi"))
				.add(Blocks.WARPED_FUNGUS)
				.add(Blocks.CRIMSON_FUNGUS);

		getOrCreateTagBuilder(AMDatagen.createCommonBlockTag("nether_roots"))
				.add(Blocks.WARPED_ROOTS)
				.add(Blocks.CRIMSON_ROOTS);

		Tag.Identified<Block> weepingVinesTag = AMDatagen.createCommonBlockTag("weeping_vines");
		getOrCreateTagBuilder(weepingVinesTag)
				.add(Blocks.WEEPING_VINES)
				.add(Blocks.WEEPING_VINES_PLANT);

		Tag.Identified<Block> twistingVinesTag = AMDatagen.createCommonBlockTag("twisting_vines");
		getOrCreateTagBuilder(twistingVinesTag)
				.add(Blocks.TWISTING_VINES)
				.add(Blocks.TWISTING_VINES_PLANT);

		Tag.Identified<Block> netherVinesTag = AMDatagen.createCommonBlockTag("nether_vines");
		getOrCreateTagBuilder(netherVinesTag)
				.addTag(weepingVinesTag)
				.addTag(twistingVinesTag);

		Tag.Identified<Block> caveVinesTag = AMDatagen.createCommonBlockTag("cave_vines");
		getOrCreateTagBuilder(caveVinesTag)
				.add(Blocks.CAVE_VINES)
				.add(Blocks.CAVE_VINES_PLANT);

		getOrCreateTagBuilder(AMDatagen.createCommonBlockTag("vines"))
				.addTag(netherVinesTag)
				.addTag(caveVinesTag)
				.add(Blocks.VINE);

		Tag.Identified<Block> pumpkinsTag = AMDatagen.createCommonBlockTag("pumpkins");
		getOrCreateTagBuilder(pumpkinsTag)
				.add(Blocks.PUMPKIN)
				.add(Blocks.CARVED_PUMPKIN)
				.add(Blocks.JACK_O_LANTERN);

		getOrCreateTagBuilder(AMDatagen.createCommonBlockTag("gourds"))
				.addTag(pumpkinsTag)
				.add(Blocks.MELON);

		FabricTagBuilder<Block> infiniburnTagBuilder = getOrCreateTagBuilder(BlockTags.INFINIBURN_OVERWORLD);
		INFINIBURN_BLOCKS.forEach(infiniburnTagBuilder::add);
		INFINIBURN_TAGS.forEach(infiniburnTagBuilder::addTag);

		SPACE_STONE_FAMILIES.forEach((family) -> family.getVariants().forEach((variant, block) -> {
			infiniburnTagBuilder.add(block);
			addHarvestData(SPACE_STONE_HARVEST_DATA, block);
		}));
		
		Tag.Identified<Block> primitiveMachinesTag = AMDatagen.createBlockTag(AMCommon.id("primitive_machines"));
		FabricTagBuilder<Block> primitiveMachinesTagBuilder = getOrCreateTagBuilder(primitiveMachinesTag);
		AMDatagen.PRIMITIVE_MACHINES.forEach(primitiveMachinesTagBuilder::add);
		addHarvestData(PRIMITIVE_MACHINE_HARVEST_DATA, primitiveMachinesTag);

		Tag.Identified<Block> basicMachinesTag = AMDatagen.createBlockTag(AMCommon.id("basic_machines"));
		FabricTagBuilder<Block> basicMachinesTagBuilder = getOrCreateTagBuilder(basicMachinesTag);
		AMDatagen.BASIC_MACHINES.forEach(basicMachinesTagBuilder::add);
		addHarvestData(BASIC_MACHINE_HARVEST_DATA, basicMachinesTag);

		Tag.Identified<Block> advancedMachinesTag = AMDatagen.createBlockTag(AMCommon.id("advanced_machines"));
		FabricTagBuilder<Block> advancedMachinesTagBuilder = getOrCreateTagBuilder(advancedMachinesTag);
		AMDatagen.ADVANCED_MACHINES.forEach(advancedMachinesTagBuilder::add);
		addHarvestData(ADVANCED_MACHINE_HARVEST_DATA, advancedMachinesTag);

		Tag.Identified<Block> eliteMachinesTag = AMDatagen.createBlockTag(AMCommon.id("elite_machines"));
		FabricTagBuilder<Block> eliteMachinesTagBuilder = getOrCreateTagBuilder(eliteMachinesTag);
		AMDatagen.ELITE_MACHINES.forEach(eliteMachinesTagBuilder::add);
		addHarvestData(ELITE_MACHINE_HARVEST_DATA, eliteMachinesTag);

		Tag.Identified<Block> creativeMachinesTag = AMDatagen.createBlockTag(AMCommon.id("creative_machines"));
		FabricTagBuilder<Block> creativeMachinesTagBuilder = getOrCreateTagBuilder(creativeMachinesTag);
		AMDatagen.CREATIVE_MACHINES.forEach(creativeMachinesTagBuilder::add);

		Tag.Identified<Block> miscMachinesTag = AMDatagen.createBlockTag(AMCommon.id("misc_machines"));
		FabricTagBuilder<Block> miscMachinesTagBuilder = getOrCreateTagBuilder(miscMachinesTag);
		AMDatagen.MISC_MACHINES.forEach(miscMachinesTagBuilder::add);
		addHarvestData(MISC_MACHINE_HARVEST_DATA, miscMachinesTag);
		
		getOrCreateTagBuilder(AMDatagen.createBlockTag(AMCommon.id("machines")))
				.addTag(primitiveMachinesTag)
				.addTag(basicMachinesTag)
				.addTag(advancedMachinesTag)
				.addTag(eliteMachinesTag)
				.addTag(creativeMachinesTag)
				.addTag(miscMachinesTag);

		Tag.Identified<Block> energyCablesTag = AMDatagen.createBlockTag(AMCommon.id("energy_cables"));
		FabricTagBuilder<Block> energyCablesTagBuilder = getOrCreateTagBuilder(energyCablesTag);
		AMDatagen.ENERGY_CABLES.forEach(energyCablesTagBuilder::add);
		addHarvestData(PIPE_AND_CABLE_HARVEST_DATA, energyCablesTag);
		addHarvestData(PIPE_AND_CABLE_HARVEST_DATA, AMBlocks.FLUID_PIPE.get());

		addHarvestData(HarvestData.IRON_PICKAXE, AMBlocks.HOLOGRAPHIC_BRIDGE_PROJECTOR.get());

		addHarvestData(HarvestData.STONE_PICKAXE, AMBlocks.AIRLOCK.get(), AMBlocks.DRAIN.get());

		addHarvestData(HarvestData.LEVEL_5_PICKAXE, AMBlocks.NUCLEAR_WARHEAD.get());
	}

	public void addHarvestData(HarvestData harvestData, Block block) {
		getOrCreateTagBuilder(harvestData.mineableTag()).add(block);
		if (harvestData.miningLevel() > 0) getOrCreateTagBuilder(harvestData.miningLevelTag()).add(block);
	}

	public void addHarvestData(HarvestData harvestData, Block... blocks) {
		Arrays.stream(blocks).forEach((block) -> addHarvestData(harvestData, block));
	}

	public void addHarvestData(HarvestData harvestData, Tag.Identified<Block> tag) {
		getOrCreateTagBuilder(harvestData.mineableTag()).addTag(tag);
		if (harvestData.miningLevel() > 0) getOrCreateTagBuilder(harvestData.miningLevelTag()).addTag(tag);
	}
}
