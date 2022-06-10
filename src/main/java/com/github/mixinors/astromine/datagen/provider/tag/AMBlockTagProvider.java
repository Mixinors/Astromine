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

package com.github.mixinors.astromine.datagen.provider.tag;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.datagen.AMDatagen;
import com.github.mixinors.astromine.datagen.AMDatagenLists;
import com.github.mixinors.astromine.datagen.HarvestData;
import com.github.mixinors.astromine.datagen.family.block.AMBlockFamilies;
import com.github.mixinors.astromine.datagen.family.material.AMMaterialFamilies;
import com.github.mixinors.astromine.datagen.family.material.family.MaterialFamily;
import com.github.mixinors.astromine.datagen.family.material.variant.BlockVariant;
import com.github.mixinors.astromine.registry.common.AMBlocks;
import com.github.mixinors.astromine.registry.common.AMTagKeys;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBlockTags;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;

import java.util.Arrays;

public class AMBlockTagProvider extends FabricTagProvider.BlockTagProvider {
	public AMBlockTagProvider(FabricDataGenerator dataGenerator) {
		super(dataGenerator);
	}
	
	@Override
	protected void generateTags() {
		var beaconBaseTagBuilder = getOrCreateTagBuilder(net.minecraft.tag.BlockTags.BEACON_BASE_BLOCKS);
		
		var guardedByPiglinsTagBuilder = getOrCreateTagBuilder(net.minecraft.tag.BlockTags.GUARDED_BY_PIGLINS);
		
		AMMaterialFamilies.getFamilies().filter(MaterialFamily::shouldGenerateTags).forEachOrdered(family -> {
			AMDatagen.toTreeMap(family.getBlockTags()).forEach((variant, tag) -> {
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
				
				if (family.shouldGenerateHarvestTags(variant)) {
					addHarvestData(family.getHarvestData(variant), tag);
				}
			});
			
			if (family.hasAnyBlockVariants(AMDatagenLists.BlockVariantLists.ORE_VARIANTS)) {
				var oresTag = family.getBlockTag("ores");
				var oresTagBuilder = getOrCreateTagBuilder(oresTag);
				
				AMDatagenLists.BlockVariantLists.ORE_VARIANTS.forEach((variant) -> {
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
			if (AMDatagenLists.BlockTagLists.BLOCK_FAMILY_VARIANTS.containsKey(variant)) {
				getOrCreateTagBuilder(AMDatagenLists.BlockTagLists.BLOCK_FAMILY_VARIANTS.get(variant)).add(block);
			}
		}));
		
		var cauldronsTagBuilder = getOrCreateTagBuilder(net.minecraft.tag.BlockTags.CAULDRONS);
		AMDatagenLists.FluidLists.FLUIDS.forEach((fluid) -> {
			var fluidName = Registry.FLUID.getId(fluid.getStill()).getPath();
			
			var tagBuilder = getOrCreateTagBuilder(AMTagKeys.createCommonBlockTag(fluidName));
			
			var cauldronTag = AMTagKeys.createCommonBlockTag(fluidName + "_cauldrons");
			var cauldronTagBuilder = getOrCreateTagBuilder(cauldronTag);
			
			tagBuilder.add(fluid.getBlock());
			
			cauldronTagBuilder.add(fluid.getCauldron());
			cauldronsTagBuilder.addTag(cauldronTag);
		});
		
		var oresTagBuilder = getOrCreateTagBuilder(ConventionalBlockTags.ORES);
		AMDatagenLists.BlockVariantLists.ORE_VARIANTS.forEach((variant) -> {
			if (variant.hasTag()) {
				oresTagBuilder.addTag(variant.getTag());
			}
		});
		
		var yellowSandstonesTag = AMTagKeys.BlockTags.YELLOW_SANDSTONES;
		getOrCreateTagBuilder(yellowSandstonesTag)
				.add(Blocks.SANDSTONE)
				.add(Blocks.CHISELED_SANDSTONE)
				.add(Blocks.CUT_SANDSTONE)
				.add(Blocks.SMOOTH_SANDSTONE);
		
		var redSandstonesTag = AMTagKeys.BlockTags.RED_SANDSTONES;
		getOrCreateTagBuilder(redSandstonesTag)
				.add(Blocks.RED_SANDSTONE)
				.add(Blocks.CHISELED_RED_SANDSTONE)
				.add(Blocks.CUT_RED_SANDSTONE)
				.add(Blocks.SMOOTH_RED_SANDSTONE);
		
		getOrCreateTagBuilder(AMTagKeys.BlockTags.SANDSTONES)
				.addTag(yellowSandstonesTag)
				.addTag(redSandstonesTag);
		
		getOrCreateTagBuilder(AMTagKeys.BlockTags.QUARTZ_BLOCKS)
				.add(Blocks.QUARTZ_BLOCK)
				.add(Blocks.QUARTZ_BRICKS)
				.add(Blocks.QUARTZ_PILLAR)
				.add(Blocks.CHISELED_QUARTZ_BLOCK);
		
		var unwaxedCopperBlocksTag = AMTagKeys.BlockTags.UNWAXED_COPPER_BLOCKS;
		getOrCreateTagBuilder(unwaxedCopperBlocksTag)
				.add(Blocks.COPPER_BLOCK)
				.add(Blocks.EXPOSED_COPPER)
				.add(Blocks.WEATHERED_COPPER)
				.add(Blocks.OXIDIZED_COPPER);
		
		var waxedCopperBlocksTag = AMTagKeys.BlockTags.WAXED_COPPER_BLOCKS;
		getOrCreateTagBuilder(waxedCopperBlocksTag)
				.add(Blocks.WAXED_COPPER_BLOCK)
				.add(Blocks.WAXED_EXPOSED_COPPER)
				.add(Blocks.WAXED_WEATHERED_COPPER)
				.add(Blocks.WAXED_OXIDIZED_COPPER);
		
		getOrCreateTagBuilder(AMTagKeys.BlockTags.COPPER_BLOCKS)
				.addTag(unwaxedCopperBlocksTag)
				.addTag(waxedCopperBlocksTag);
		
		var unwaxedCutCopperTag = AMTagKeys.BlockTags.UNWAXED_CUT_COPPER;
		getOrCreateTagBuilder(unwaxedCutCopperTag)
				.add(Blocks.CUT_COPPER)
				.add(Blocks.EXPOSED_CUT_COPPER)
				.add(Blocks.WEATHERED_CUT_COPPER)
				.add(Blocks.OXIDIZED_CUT_COPPER);
		
		var waxedCutCopperTag = AMTagKeys.BlockTags.WAXED_CUT_COPPER;
		getOrCreateTagBuilder(waxedCutCopperTag)
				.add(Blocks.WAXED_CUT_COPPER)
				.add(Blocks.WAXED_EXPOSED_CUT_COPPER)
				.add(Blocks.WAXED_WEATHERED_CUT_COPPER)
				.add(Blocks.WAXED_OXIDIZED_CUT_COPPER);
		
		getOrCreateTagBuilder(AMTagKeys.BlockTags.CUT_COPPER)
				.addTag(unwaxedCutCopperTag)
				.addTag(waxedCutCopperTag);
		
		getOrCreateTagBuilder(AMTagKeys.BlockTags.PURPUR_BLOCKS)
				.add(Blocks.PURPUR_BLOCK)
				.add(Blocks.PURPUR_PILLAR);
		
		getOrCreateTagBuilder(AMTagKeys.BlockTags.MUSHROOMS)
				.add(Blocks.BROWN_MUSHROOM)
				.add(Blocks.RED_MUSHROOM);
		
		getOrCreateTagBuilder(AMTagKeys.BlockTags.MUSHROOM_BLOCKS)
				.add(Blocks.BROWN_MUSHROOM_BLOCK)
				.add(Blocks.RED_MUSHROOM_BLOCK);
		
		getOrCreateTagBuilder(AMTagKeys.BlockTags.NETHER_FUNGI)
				.add(Blocks.WARPED_FUNGUS)
				.add(Blocks.CRIMSON_FUNGUS);
		
		getOrCreateTagBuilder(AMTagKeys.BlockTags.NETHER_ROOTS)
				.add(Blocks.WARPED_ROOTS)
				.add(Blocks.CRIMSON_ROOTS);
		
		var weepingVinesTag = AMTagKeys.BlockTags.WEEPING_VINES;
		getOrCreateTagBuilder(weepingVinesTag)
				.add(Blocks.WEEPING_VINES)
				.add(Blocks.WEEPING_VINES_PLANT);
		
		var twistingVinesTag = AMTagKeys.BlockTags.TWISTING_VINES;
		getOrCreateTagBuilder(twistingVinesTag)
				.add(Blocks.TWISTING_VINES)
				.add(Blocks.TWISTING_VINES_PLANT);
		
		var netherVinesTag = AMTagKeys.BlockTags.NETHER_VINES;
		getOrCreateTagBuilder(netherVinesTag)
				.addTag(weepingVinesTag)
				.addTag(twistingVinesTag);
		
		var caveVinesTag = AMTagKeys.createCommonBlockTag("cave_vines");
		getOrCreateTagBuilder(caveVinesTag)
				.add(Blocks.CAVE_VINES)
				.add(Blocks.CAVE_VINES_PLANT);
		
		getOrCreateTagBuilder(AMTagKeys.createCommonBlockTag("vines"))
				.addTag(netherVinesTag)
				.addTag(caveVinesTag)
				.add(Blocks.VINE);
		
		var pumpkinsTag = AMTagKeys.BlockTags.PUMPKINS;
		getOrCreateTagBuilder(pumpkinsTag)
				.add(Blocks.PUMPKIN)
				.add(Blocks.CARVED_PUMPKIN)
				.add(Blocks.JACK_O_LANTERN);
		
		getOrCreateTagBuilder(AMTagKeys.BlockTags.GOURDS)
				.addTag(pumpkinsTag)
				.add(Blocks.MELON);
		
		var infiniburnTagBuilder = getOrCreateTagBuilder(net.minecraft.tag.BlockTags.INFINIBURN_OVERWORLD);
		
		AMDatagenLists.BlockLists.INFINIBURN_BLOCKS.forEach(infiniburnTagBuilder::add);
		AMDatagenLists.BlockTagLists.INFINIBURN_TAGS.forEach(infiniburnTagBuilder::addTag);
		
		AMDatagenLists.BlockFamilyLists.SPACE_STONE_FAMILIES.forEach((family) -> {
			AMDatagen.toTreeMap(family.getVariants()).forEach((variant, block) -> {
				infiniburnTagBuilder.add(block);
				
				addHarvestData(HarvestData.SPACE_STONE_HARVEST_DATA, block);
			});
			
			addHarvestData(HarvestData.SPACE_STONE_HARVEST_DATA, family.getBaseBlock());
		});

		AMDatagenLists.BlockFamilyLists.MOON_STONE_FAMILIES.forEach((family) -> {
			AMDatagen.toTreeMap(family.getVariants()).forEach((variant, block) -> {
				addHarvestData(HarvestData.MOON_STONE_HARVEST_DATA, block);
			});

			addHarvestData(HarvestData.MOON_STONE_HARVEST_DATA, family.getBaseBlock());
		});
		
		var primitiveMachinesTag = AMTagKeys.createBlockTag(AMCommon.id("primitive_machines"));
		var primitiveMachinesTagBuilder = getOrCreateTagBuilder(primitiveMachinesTag);
		
		AMDatagenLists.BlockLists.PRIMITIVE_MACHINES.forEach(primitiveMachinesTagBuilder::add);
		
		addHarvestData(HarvestData.PRIMITIVE_MACHINE_HARVEST_DATA, primitiveMachinesTag);
		
		var basicMachinesTag = AMTagKeys.createBlockTag(AMCommon.id("basic_machines"));
		var basicMachinesTagBuilder = getOrCreateTagBuilder(basicMachinesTag);
		
		AMDatagenLists.BlockLists.BASIC_MACHINES.forEach(basicMachinesTagBuilder::add);
		
		addHarvestData(HarvestData.BASIC_MACHINE_HARVEST_DATA, basicMachinesTag);
		
		var advancedMachinesTag = AMTagKeys.createBlockTag(AMCommon.id("advanced_machines"));
		var advancedMachinesTagBuilder = getOrCreateTagBuilder(advancedMachinesTag);
		
		AMDatagenLists.BlockLists.ADVANCED_MACHINES.forEach(advancedMachinesTagBuilder::add);
		
		addHarvestData(HarvestData.ADVANCED_MACHINE_HARVEST_DATA, advancedMachinesTag);
		
		var eliteMachinesTag = AMTagKeys.createBlockTag(AMCommon.id("elite_machines"));
		var eliteMachinesTagBuilder = getOrCreateTagBuilder(eliteMachinesTag);
		
		AMDatagenLists.BlockLists.ELITE_MACHINES.forEach(eliteMachinesTagBuilder::add);
		
		addHarvestData(HarvestData.ELITE_MACHINE_HARVEST_DATA, eliteMachinesTag);
		
		var creativeMachinesTag = AMTagKeys.createBlockTag(AMCommon.id("creative_machines"));
		var creativeMachinesTagBuilder = getOrCreateTagBuilder(creativeMachinesTag);
		
		AMDatagenLists.BlockLists.CREATIVE_MACHINES.forEach(creativeMachinesTagBuilder::add);
		
		var miscMachinesTag = AMTagKeys.createBlockTag(AMCommon.id("misc_machines"));
		var miscMachinesTagBuilder = getOrCreateTagBuilder(miscMachinesTag);
		
		AMDatagenLists.BlockLists.MISC_MACHINES.forEach(miscMachinesTagBuilder::add);
		
		addHarvestData(HarvestData.MISC_MACHINE_HARVEST_DATA, miscMachinesTag);
		
		getOrCreateTagBuilder(AMTagKeys.createBlockTag(AMCommon.id("machines")))
				.addTag(primitiveMachinesTag)
				.addTag(basicMachinesTag)
				.addTag(advancedMachinesTag)
				.addTag(eliteMachinesTag)
				.addTag(creativeMachinesTag)
				.addTag(miscMachinesTag);
		
		var energyCablesTag = AMTagKeys.createBlockTag(AMCommon.id("energy_cables"));
		var energyCablesTagBuilder = getOrCreateTagBuilder(energyCablesTag);
		
		AMDatagenLists.BlockLists.ENERGY_CABLES.forEach(energyCablesTagBuilder::add);
		
		addHarvestData(HarvestData.PIPE_AND_CABLE_HARVEST_DATA, energyCablesTag);
		addHarvestData(HarvestData.PIPE_AND_CABLE_HARVEST_DATA, AMBlocks.FLUID_PIPE.get());
		
		addHarvestData(HarvestData.IRON_PICKAXE, AMBlocks.HOLOGRAPHIC_BRIDGE_PROJECTOR.get());
		
		addHarvestData(HarvestData.STONE_PICKAXE, AMBlocks.AIRLOCK.get(), AMBlocks.DRAIN.get());
		
		addHarvestData(HarvestData.LEVEL_5_PICKAXE, AMBlocks.NUCLEAR_WARHEAD.get());
	}
	
	public void addHarvestData(HarvestData harvestData, Block block) {
		getOrCreateTagBuilder(harvestData.mineableTag()).add(block);
		
		if (harvestData.miningLevel() > 0) {
			getOrCreateTagBuilder(harvestData.miningLevelTag()).add(block);
		}
	}
	
	public void addHarvestData(HarvestData harvestData, Block... blocks) {
		Arrays.stream(blocks).forEach((block) -> addHarvestData(harvestData, block));
	}
	
	public void addHarvestData(HarvestData harvestData, TagKey<Block> tag) {
		getOrCreateTagBuilder(harvestData.mineableTag()).addTag(tag);
		
		if (harvestData.miningLevel() > 0) {
			getOrCreateTagBuilder(harvestData.miningLevelTag()).addTag(tag);
		}
	}
}
