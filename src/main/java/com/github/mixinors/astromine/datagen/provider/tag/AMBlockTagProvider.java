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
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class AMBlockTagProvider extends BlockTagsProvider {
	public AMBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, AMCommon.MOD_ID, existingFileHelper);
	}
	
	@Override
	protected void addTags(HolderLookup.Provider provider) {
		var beaconBaseTagBuilder = tag(net.minecraft.tags.BlockTags.BEACON_BASE_BLOCKS);
		
		var guardedByPiglinsTagBuilder = tag(net.minecraft.tags.BlockTags.GUARDED_BY_PIGLINS);
		
		AMMaterialFamilies.getFamilies().filter(MaterialFamily::shouldGenerateTags).forEachOrdered(family -> {
			AMDatagen.toTreeMap(family.getBlockTags()).forEach((variant, tag) -> {
				tag(tag).add(family.getVariant(variant));
				
				if (family.hasAlias()) {
					tag(family.getAliasTag(variant)).addTag(tag);
				}
				
				if (family.isPiglinLoved()) {
					guardedByPiglinsTagBuilder.addTag(tag);
					
					if (family.hasAlias()) {
						guardedByPiglinsTagBuilder.addTag(family.getAliasTag(variant));
					}
				}
				
				if (variant.hasTag()) {
					tag(variant.getTag()).addTag(tag);
					
					if (family.hasAlias()) {
						tag(variant.getTag()).addTag(family.getAliasTag(variant));
					}
				}
				
				if (family.shouldGenerateHarvestTags(variant)) {
					addHarvestData(family.getHarvestData(variant), tag);
				}
			});
			
			if (family.hasAnyBlockVariants(AMDatagenLists.BlockVariantLists.ORE_VARIANTS)) {
				var oresTag = family.getBlockTag("ores");
				var oresTagBuilder = tag(oresTag);
				
				AMDatagenLists.BlockVariantLists.ORE_VARIANTS.forEach((variant) -> {
					if (family.hasVariant(variant)) {
						oresTagBuilder.addTag(family.getTag(variant));
					}
				});
				
				if (family.hasAlias()) {
					tag(family.getAliasBlockTag("ores")).addTag(oresTag);
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
				tag(AMDatagenLists.BlockTagLists.BLOCK_FAMILY_VARIANTS.get(variant)).add(block);
			}
		}));
		
		AMDatagenLists.FluidLists.FLUIDS.forEach((fluid) -> {
			var fluidName = BuiltInRegistries.FLUID.getKey(fluid.getSource()).getPath();
			
			var tagBuilder = tag(AMTagKeys.createCommonBlockTag(fluidName));
			
			tagBuilder.add(fluid.getBlock());
		});
		
		var oresTagBuilder = tag(Tags.Blocks.ORES);
		AMDatagenLists.BlockVariantLists.ORE_VARIANTS.forEach((variant) -> {
			if (variant.hasTag()) {
				oresTagBuilder.addTag(variant.getTag());
			}
		});

		tag(AMTagKeys.BlockTags.DRILL_MINEABLE)
				.addTag(net.minecraft.tags.BlockTags.MINEABLE_WITH_PICKAXE)
				.addTag(net.minecraft.tags.BlockTags.MINEABLE_WITH_SHOVEL);
		
		var yellowSandstonesTag = AMTagKeys.BlockTags.YELLOW_SANDSTONES;
		tag(yellowSandstonesTag)
				.add(Blocks.SANDSTONE)
				.add(Blocks.CHISELED_SANDSTONE)
				.add(Blocks.CUT_SANDSTONE)
				.add(Blocks.SMOOTH_SANDSTONE);
		
		var redSandstonesTag = AMTagKeys.BlockTags.RED_SANDSTONES;
		tag(redSandstonesTag)
				.add(Blocks.RED_SANDSTONE)
				.add(Blocks.CHISELED_RED_SANDSTONE)
				.add(Blocks.CUT_RED_SANDSTONE)
				.add(Blocks.SMOOTH_RED_SANDSTONE);
		
		tag(AMTagKeys.BlockTags.SANDSTONES)
				.addTag(yellowSandstonesTag)
				.addTag(redSandstonesTag);
		
		tag(AMTagKeys.BlockTags.QUARTZ_BLOCKS)
				.add(Blocks.QUARTZ_BLOCK)
				.add(Blocks.QUARTZ_BRICKS)
				.add(Blocks.QUARTZ_PILLAR)
				.add(Blocks.CHISELED_QUARTZ_BLOCK);
		
		var unwaxedCopperBlocksTag = AMTagKeys.BlockTags.UNWAXED_COPPER_BLOCKS;
		tag(unwaxedCopperBlocksTag)
				.add(Blocks.COPPER_BLOCK)
				.add(Blocks.EXPOSED_COPPER)
				.add(Blocks.WEATHERED_COPPER)
				.add(Blocks.OXIDIZED_COPPER);
		
		var waxedCopperBlocksTag = AMTagKeys.BlockTags.WAXED_COPPER_BLOCKS;
		tag(waxedCopperBlocksTag)
				.add(Blocks.WAXED_COPPER_BLOCK)
				.add(Blocks.WAXED_EXPOSED_COPPER)
				.add(Blocks.WAXED_WEATHERED_COPPER)
				.add(Blocks.WAXED_OXIDIZED_COPPER);
		
		tag(AMTagKeys.BlockTags.COPPER_BLOCKS)
				.addTag(unwaxedCopperBlocksTag)
				.addTag(waxedCopperBlocksTag);
		
		var unwaxedCutCopperTag = AMTagKeys.BlockTags.UNWAXED_CUT_COPPER;
		tag(unwaxedCutCopperTag)
				.add(Blocks.CUT_COPPER)
				.add(Blocks.EXPOSED_CUT_COPPER)
				.add(Blocks.WEATHERED_CUT_COPPER)
				.add(Blocks.OXIDIZED_CUT_COPPER);
		
		var waxedCutCopperTag = AMTagKeys.BlockTags.WAXED_CUT_COPPER;
		tag(waxedCutCopperTag)
				.add(Blocks.WAXED_CUT_COPPER)
				.add(Blocks.WAXED_EXPOSED_CUT_COPPER)
				.add(Blocks.WAXED_WEATHERED_CUT_COPPER)
				.add(Blocks.WAXED_OXIDIZED_CUT_COPPER);
		
		tag(AMTagKeys.BlockTags.CUT_COPPER)
				.addTag(unwaxedCutCopperTag)
				.addTag(waxedCutCopperTag);
		
		tag(AMTagKeys.BlockTags.PURPUR_BLOCKS)
				.add(Blocks.PURPUR_BLOCK)
				.add(Blocks.PURPUR_PILLAR);
		
		tag(AMTagKeys.BlockTags.MUSHROOMS)
				.add(Blocks.BROWN_MUSHROOM)
				.add(Blocks.RED_MUSHROOM);
		
		tag(AMTagKeys.BlockTags.MUSHROOM_BLOCKS)
				.add(Blocks.BROWN_MUSHROOM_BLOCK)
				.add(Blocks.RED_MUSHROOM_BLOCK);
		
		tag(AMTagKeys.BlockTags.NETHER_FUNGI)
				.add(Blocks.WARPED_FUNGUS)
				.add(Blocks.CRIMSON_FUNGUS);
		
		tag(AMTagKeys.BlockTags.NETHER_ROOTS)
				.add(Blocks.WARPED_ROOTS)
				.add(Blocks.CRIMSON_ROOTS);
		
		var weepingVinesTag = AMTagKeys.BlockTags.WEEPING_VINES;
		tag(weepingVinesTag)
				.add(Blocks.WEEPING_VINES)
				.add(Blocks.WEEPING_VINES_PLANT);
		
		var twistingVinesTag = AMTagKeys.BlockTags.TWISTING_VINES;
		tag(twistingVinesTag)
				.add(Blocks.TWISTING_VINES)
				.add(Blocks.TWISTING_VINES_PLANT);
		
		var netherVinesTag = AMTagKeys.BlockTags.NETHER_VINES;
		tag(netherVinesTag)
				.addTag(weepingVinesTag)
				.addTag(twistingVinesTag);
		
		var caveVinesTag = AMTagKeys.createCommonBlockTag("cave_vines");
		tag(caveVinesTag)
				.add(Blocks.CAVE_VINES)
				.add(Blocks.CAVE_VINES_PLANT);
		
		tag(AMTagKeys.createCommonBlockTag("vines"))
				.addTag(netherVinesTag)
				.addTag(caveVinesTag)
				.add(Blocks.VINE);
		
		var pumpkinsTag = AMTagKeys.BlockTags.PUMPKINS;
		tag(pumpkinsTag)
				.add(Blocks.PUMPKIN)
				.add(Blocks.CARVED_PUMPKIN)
				.add(Blocks.JACK_O_LANTERN);
		
		tag(AMTagKeys.BlockTags.GOURDS)
				.addTag(pumpkinsTag)
				.add(Blocks.MELON);
		
		var infiniburnTagBuilder = tag(net.minecraft.tags.BlockTags.INFINIBURN_OVERWORLD);
		
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
		var primitiveMachinesTagBuilder = tag(primitiveMachinesTag);
		
		AMDatagenLists.BlockLists.PRIMITIVE_MACHINES.forEach(primitiveMachinesTagBuilder::add);
		
		addHarvestData(HarvestData.PRIMITIVE_MACHINE_HARVEST_DATA, primitiveMachinesTag);
		
		var basicMachinesTag = AMTagKeys.createBlockTag(AMCommon.id("basic_machines"));
		var basicMachinesTagBuilder = tag(basicMachinesTag);
		
		AMDatagenLists.BlockLists.BASIC_MACHINES.forEach(basicMachinesTagBuilder::add);
		
		addHarvestData(HarvestData.BASIC_MACHINE_HARVEST_DATA, basicMachinesTag);
		
		var advancedMachinesTag = AMTagKeys.createBlockTag(AMCommon.id("advanced_machines"));
		var advancedMachinesTagBuilder = tag(advancedMachinesTag);
		
		AMDatagenLists.BlockLists.ADVANCED_MACHINES.forEach(advancedMachinesTagBuilder::add);
		
		addHarvestData(HarvestData.ADVANCED_MACHINE_HARVEST_DATA, advancedMachinesTag);
		
		var eliteMachinesTag = AMTagKeys.createBlockTag(AMCommon.id("elite_machines"));
		var eliteMachinesTagBuilder = tag(eliteMachinesTag);
		
		AMDatagenLists.BlockLists.ELITE_MACHINES.forEach(eliteMachinesTagBuilder::add);
		
		addHarvestData(HarvestData.ELITE_MACHINE_HARVEST_DATA, eliteMachinesTag);
		
		var creativeMachinesTag = AMTagKeys.createBlockTag(AMCommon.id("creative_machines"));
		var creativeMachinesTagBuilder = tag(creativeMachinesTag);
		
		AMDatagenLists.BlockLists.CREATIVE_MACHINES.forEach(creativeMachinesTagBuilder::add);
		
		var miscMachinesTag = AMTagKeys.createBlockTag(AMCommon.id("misc_machines"));
		var miscMachinesTagBuilder = tag(miscMachinesTag);
		
		AMDatagenLists.BlockLists.MISC_MACHINES.forEach(miscMachinesTagBuilder::add);
		
		addHarvestData(HarvestData.MISC_MACHINE_HARVEST_DATA, miscMachinesTag);
		
		tag(AMTagKeys.createBlockTag(AMCommon.id("machines")))
				.addTag(primitiveMachinesTag)
				.addTag(basicMachinesTag)
				.addTag(advancedMachinesTag)
				.addTag(eliteMachinesTag)
				.addTag(creativeMachinesTag)
				.addTag(miscMachinesTag);
		
		var energyCablesTag = AMTagKeys.createBlockTag(AMCommon.id("energy_cables"));
		var energyCablesTagBuilder = tag(energyCablesTag);
		
		AMDatagenLists.BlockLists.ENERGY_CABLES.forEach(energyCablesTagBuilder::add);
		
		addHarvestData(HarvestData.PIPE_AND_CABLE_HARVEST_DATA, energyCablesTag);
		addHarvestData(HarvestData.PIPE_AND_CABLE_HARVEST_DATA, AMBlocks.FLUID_PIPE.get());
		
		addHarvestData(HarvestData.IRON_PICKAXE, AMBlocks.HOLOGRAPHIC_BRIDGE_PROJECTOR.get());
		
		addHarvestData(HarvestData.STONE_PICKAXE, AMBlocks.AIRLOCK.get(), AMBlocks.DRAIN.get());
		
		addHarvestData(HarvestData.LEVEL_5_PICKAXE, AMBlocks.NUCLEAR_WARHEAD.get());
	}
	
	public void addHarvestData(HarvestData harvestData, Block block) {
		tag(harvestData.mineableTag()).add(block);
		
		if (harvestData.miningLevel() > 0) {
			tag(harvestData.miningLevelTag()).add(block);
		}
	}
	
	public void addHarvestData(HarvestData harvestData, Block... blocks) {
		Arrays.stream(blocks).forEach((block) -> addHarvestData(harvestData, block));
	}
	
	public void addHarvestData(HarvestData harvestData, TagKey<Block> tag) {
		tag(harvestData.mineableTag()).addTag(tag);
		
		if (harvestData.miningLevel() > 0) {
			tag(harvestData.miningLevelTag()).addTag(tag);
		}
	}
}
