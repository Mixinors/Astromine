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
import com.github.mixinors.astromine.datagen.family.block.AMBlockFamilies;
import com.github.mixinors.astromine.datagen.family.material.MaterialFamilies;
import com.github.mixinors.astromine.datagen.family.material.MaterialFamily;
import com.github.mixinors.astromine.datagen.family.material.variant.ItemVariant;
import com.github.mixinors.astromine.registry.common.AMItems;
import com.shnupbups.piglib.Piglib;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

public class AMItemTagProvider extends FabricTagProvider.ItemTagProvider {
	public static final Map<List<ItemVariant>, Identifier> GENERIC_TAGS = Map.of(
			AMDatagen.CLUSTER_VARIANTS, AMTagKeys.createCommonTagId("clusters"),
			AMDatagen.ARMOR_VARIANTS, AMTagKeys.createCommonTagId("armor"),
			AMDatagen.TOOL_VARIANTS, AMTagKeys.createCommonTagId("tools")
	);

	public static final Map<TagKey<Block>, TagKey<Item>> COPY = ImmutableMap.<TagKey<Block>, TagKey<Item>>builder()
			.putAll(Map.of(
					AMTagKeys.Blocks.YELLOW_SANDSTONES, AMTagKeys.Items.YELLOW_SANDSTONES,
					AMTagKeys.Blocks.RED_SANDSTONES, AMTagKeys.Items.RED_SANDSTONES,
					AMTagKeys.Blocks.SANDSTONES, AMTagKeys.Items.SANDSTONES,
					AMTagKeys.Blocks.QUARTZ_BLOCKS, AMTagKeys.Items.QUARTZ_BLOCKS,
					AMTagKeys.Blocks.UNWAXED_COPPER_BLOCKS, AMTagKeys.Items.UNWAXED_COPPER_BLOCKS,
					AMTagKeys.Blocks.WAXED_COPPER_BLOCKS, AMTagKeys.Items.WAXED_COPPER_BLOCKS,
					AMTagKeys.Blocks.COPPER_BLOCKS, AMTagKeys.Items.COPPER_BLOCKS,
					AMTagKeys.Blocks.UNWAXED_CUT_COPPER, AMTagKeys.Items.UNWAXED_CUT_COPPER,
					AMTagKeys.Blocks.WAXED_CUT_COPPER, AMTagKeys.Items.WAXED_CUT_COPPER,
					AMTagKeys.Blocks.CUT_COPPER, AMTagKeys.Items.CUT_COPPER
			)).putAll(Map.of(
					AMTagKeys.Blocks.PURPUR_BLOCKS, AMTagKeys.Items.PURPUR_BLOCKS,
					AMTagKeys.Blocks.MUSHROOMS, AMTagKeys.Items.MUSHROOMS,
					AMTagKeys.Blocks.MUSHROOM_BLOCKS, AMTagKeys.Items.MUSHROOM_BLOCKS,
					AMTagKeys.Blocks.NETHER_FUNGI, AMTagKeys.Items.NETHER_FUNGI,
					AMTagKeys.Blocks.NETHER_ROOTS, AMTagKeys.Items.NETHER_ROOTS,
					AMTagKeys.Blocks.NETHER_VINES, AMTagKeys.Items.NETHER_VINES,
					AMTagKeys.Blocks.PUMPKINS, AMTagKeys.Items.PUMPKINS,
					AMTagKeys.Blocks.GOURDS, AMTagKeys.Items.GOURDS,
					AMTagKeys.Blocks.WEEPING_VINES, AMTagKeys.Items.WEEPING_VINES,
					AMTagKeys.Blocks.TWISTING_VINES, AMTagKeys.Items.TWISTING_VINES
			)).putAll(Map.of(
					AMTagKeys.Blocks.ORES, AMTagKeys.Items.ORES
			)).build();
	
	public static final List<Item> DRILLS = List.of(
			AMItems.PRIMITIVE_DRILL.get(),
			AMItems.BASIC_DRILL.get(),
			AMItems.ADVANCED_DRILL.get(),
			AMItems.ELITE_DRILL.get()
	);
	
	public static final List<TagKey<Item>> ONE_BIOFUEL_TAGS = List.of(
			AMTagKeys.createCommonItemTag("mushrooms"),
			AMTagKeys.createCommonItemTag("nether_fungi"),
			AMTagKeys.createCommonItemTag("nether_roots"),
			AMTagKeys.createCommonItemTag("vines"),
			AMTagKeys.createCommonItemTag("berries"),
			AMTagKeys.createCommonItemTag("seeds")
	);

	public static final List<TagKey<Item>> ONE_BIOFUEL_TAGS_FORCED = List.of(
			ItemTags.SMALL_FLOWERS,
			ItemTags.LEAVES
	);
	
	public static final List<Item> ONE_BIOFUEL_ITEMS = List.of(
			Items.GRASS,
			Items.FERN,
			Items.DEAD_BUSH,
			Items.SEAGRASS,
			Items.SEA_PICKLE,
			Items.NETHER_SPROUTS,
			Items.LILY_PAD,
			Items.MELON_SLICE,
			Items.WHEAT,
			Items.APPLE,
			Items.COOKIE,
			Items.SUGAR_CANE,
			Items.KELP,
			Items.DRIED_KELP,
			Items.BAMBOO,
			Items.CARROT,
			Items.POTATO,
			Items.BAKED_POTATO,
			Items.POISONOUS_POTATO,
			Items.BEETROOT,
			Items.GLOW_LICHEN,
			Items.SUGAR,
			Items.HONEYCOMB
	);
	
	public static final List<TagKey<Item>> TWO_BIOFUEL_TAGS_FORCED = List.of(
			ItemTags.TALL_FLOWERS,
			ItemTags.SAPLINGS
	);
	
	public static final List<Item> TWO_BIOFUEL_ITEMS = List.of(
			Items.PORKCHOP,
			Items.COOKED_PORKCHOP,
			Items.BEEF,
			Items.COOKED_BEEF,
			Items.CHICKEN,
			Items.COOKED_CHICKEN,
			Items.ROTTEN_FLESH,
			Items.SPIDER_EYE,
			Items.FERMENTED_SPIDER_EYE,
			Items.RABBIT,
			Items.COOKED_RABBIT,
			Items.MUTTON,
			Items.COOKED_MUTTON,
			Items.CACTUS,
			Items.TALL_GRASS,
			Items.LARGE_FERN,
			Items.MOSS_CARPET,
			Items.LEATHER,
			Items.EGG,
			Items.CHORUS_FRUIT,
			Items.POPPED_CHORUS_FRUIT,
			Items.CHORUS_FLOWER,
			Items.CHORUS_PLANT
	);
	
	public static final List<TagKey<Item>> FOUR_BIOFUEL_TAGS = List.of(
			AMTagKeys.createCommonItemTag("metal_apples")
	);

	public static final List<TagKey<Item>> FOUR_BIOFUEL_TAGS_FORCED = List.of(
			ItemTags.FISHES
	);
	
	public static final List<Item> FOUR_BIOFUEL_ITEMS = List.of(
			Items.CAKE,
			Items.SPORE_BLOSSOM
	);
	
	public static final List<TagKey<Item>> NINE_BIOFUEL_TAGS = List.of(
			AMTagKeys.createCommonItemTag("gourds")
	);
	
	public static final List<Item> NINE_BIOFUEL_ITEMS = List.of(
			Items.HONEY_BLOCK,
			Items.HONEYCOMB_BLOCK,
			Items.PUMPKIN_PIE,
			Items.MOSS_BLOCK
	);
	
	public AMItemTagProvider(FabricDataGenerator dataGenerator, @Nullable BlockTagProvider blockTagProvider) {
		super(dataGenerator, blockTagProvider);
	}
	
	@Override
	protected void generateTags() {
		var beaconPaymentTagBuilder = getOrCreateTagBuilder(ItemTags.BEACON_PAYMENT_ITEMS);
		var piglinLovedTagBuilder = getOrCreateTagBuilder(ItemTags.PIGLIN_LOVED);
		var piglinLovedNuggetsTagBuilder = getOrCreateTagBuilder(Piglib.PIGLIN_LOVED_NUGGETS);
		var piglinBarteringItemsTagBuilder = getOrCreateTagBuilder(Piglib.PIGLIN_BARTERING_ITEMS);
		var piglinSafeArmorTagBuilder = getOrCreateTagBuilder(Piglib.PIGLIN_SAFE_ARMOR);
		
		MaterialFamilies.getFamilies().filter(MaterialFamily::shouldGenerateTags).forEachOrdered(family -> {
			AMDatagen.toTreeMap(family.getItemTags()).forEach((variant, tag) -> {
				getOrCreateTagBuilder(tag).add(family.getVariant(variant));
				
				if (family.hasAlias()) {
					getOrCreateTagBuilder(family.getAliasTag(variant)).addTag(tag);
				}
				
				if (family.isPiglinLoved()) {
					if (variant.equals(ItemVariant.NUGGET)) {
						piglinLovedNuggetsTagBuilder.addTag(tag);
						
						if (family.hasAlias()) {
							piglinLovedNuggetsTagBuilder.addTag(family.getAliasTag(variant));
						}
					} else {
						piglinLovedTagBuilder.addTag(tag);
						
						if (family.hasAlias()) {
							piglinLovedTagBuilder.addTag(family.getAliasTag(variant));
						}
					}
				}
				
				if (variant.hasTag()) {
					getOrCreateTagBuilder(variant.getTag()).addTag(tag);
					
					if (family.hasAlias()) {
						getOrCreateTagBuilder(variant.getTag()).addTag(family.getAliasTag(variant));
					}
				}
			});
			AMDatagen.toTreeMap(family.getBlockItemTags()).forEach((variant, tag) -> {
				var blockTag = family.getTag(variant);
				
				copy(blockTag, tag);
				
				if (family.hasAlias()) {
					copy(family.getAliasTag(variant), family.getAliasItemTag(variant));
				}
				
				if (family.isPiglinLoved()) {
					piglinLovedTagBuilder.addTag(tag);
				}
				
				if (variant.hasTag()) {
					getOrCreateTagBuilder(variant.getItemTag()).addTag(tag);
					
					if (family.hasAlias()) {
						getOrCreateTagBuilder(variant.getItemTag()).addTag(family.getAliasItemTag(variant));
					}
				}
			});
			
			if (family.hasAnyBlockVariants(AMDatagen.ORE_VARIANTS)) {
				var oresBlockTag = family.getBlockTag("ores");
				var oresItemTag = family.getItemTag("ores");
				copy(oresBlockTag, oresItemTag);
				
				if (family.hasAlias()) {
					copy(family.getAliasBlockTag("ores"), family.getAliasItemTag("ores"));
				}
			}
			
			if (family.hasAnyItemVariants(AMDatagen.CLUSTER_VARIANTS)) {
				var clustersTag = family.getItemTag("clusters");
				var clustersTagBuilder = getOrCreateTagBuilder(clustersTag);
				AMDatagen.CLUSTER_VARIANTS.forEach((variant) -> {
					if (family.hasVariant(variant)) {
						clustersTagBuilder.addTag(family.getTag(variant));
					}
				});
				if (family.hasAlias()) {
					getOrCreateTagBuilder(family.getAliasItemTag("clusters")).addTag(clustersTag);
				}
			}
			
			if (family.hasAnyItemVariants(AMDatagen.EQUIPMENT_VARIANTS)) {
				var armorTag = family.getItemTag("armor");
				var toolsTag = family.getItemTag("tools");
				
				var salvageablesTag = family.getItemTag("salvageables");
				var salvageablesTagBuilder = getOrCreateTagBuilder(salvageablesTag);
				
				if (family.hasAnyItemVariants(AMDatagen.ARMOR_VARIANTS)) {
					var armorTagBuilder = getOrCreateTagBuilder(armorTag);
					AMDatagen.ARMOR_VARIANTS.forEach((variant) -> {
						if (family.hasVariant(variant)) {
							armorTagBuilder.addTag(family.getTag(variant));
						}
					});
					
					if (family.isPiglinLoved()) {
						piglinSafeArmorTagBuilder.addTag(armorTag);
						
						if (family.hasAlias()) {
							piglinSafeArmorTagBuilder.addTag(family.getAliasItemTag("armor"));
						}
					}
					
					salvageablesTagBuilder.addTag(armorTag);
					
					if (family.hasAlias()) {
						getOrCreateTagBuilder(family.getAliasItemTag("armor")).addTag(armorTag);
					}
				}
				
				if (family.hasAnyItemVariants(AMDatagen.TOOL_VARIANTS)) {
					var toolsTagBuilder = getOrCreateTagBuilder(toolsTag);
					AMDatagen.TOOL_VARIANTS.forEach((variant) -> {
						if (family.hasVariant(variant)) {
							toolsTagBuilder.addTag(family.getTag(variant));
						}
					});
					
					salvageablesTagBuilder.addTag(toolsTag);
					
					if (family.hasAlias()) {
						getOrCreateTagBuilder(family.getAliasItemTag("tools")).addTag(toolsTag);
					}
				}
				
				if (family.hasVariant(ItemVariant.HORSE_ARMOR)) {
					salvageablesTagBuilder.add(family.getVariant(ItemVariant.HORSE_ARMOR));
				}
				
				if (family.hasAlias()) {
					getOrCreateTagBuilder(family.getAliasItemTag("salvageables")).addTag(salvageablesTag);
				}
			}
			
			if (family.isValidForBeacon()) {
				beaconPaymentTagBuilder.addTag(family.getBaseTag());
				
				if (family.hasAlias()) {
					beaconPaymentTagBuilder.addTag(family.getAliasBaseTag());
				}
			}
			
			if (family.isPiglinLoved()) {
				piglinBarteringItemsTagBuilder.addTag(family.getBaseTag());
				
				if (family.hasAlias()) {
					piglinBarteringItemsTagBuilder.addTag(family.getAliasBaseTag());
				}
			}
		});
		
		AMBlockFamilies.getFamilies().forEachOrdered(family -> family.getVariants().forEach((variant, block) -> {
			if (AMTagKeys.VANILLA_ITEM_TAG_VARIANTS.containsKey(variant)) {
				getOrCreateTagBuilder(AMTagKeys.createItemTag(AMTagKeys.VANILLA_ITEM_TAG_VARIANTS.get(variant))).add(block.asItem());
			}
		}));
		
		AMDatagen.FLUIDS.forEach((fluid) -> {
			var bucketTagBuilder = getOrCreateTagBuilder(AMTagKeys.createCommonItemTag(Registry.FLUID.getId(fluid.getStill()).getPath() + "_buckets"));
			bucketTagBuilder.add(fluid.getBucketItem());
		});
		
		GENERIC_TAGS.forEach((variantSet, id) -> {
			var tag = getOrCreateTagBuilder(AMTagKeys.createItemTag(id));
			variantSet.forEach((variant) -> {
				if (variant.hasTag()) {
					tag.addTag(variant.getTag());
				}
			});
		});
		
		AMDatagen.TOOL_VARIANTS.forEach((variant) -> getOrCreateTagBuilder(AMTagKeys.createItemTag(new Identifier("fabric", variant.getTagPath()))).addTag(variant.getTag()));
		
		var drillsTagBuilder = getOrCreateTagBuilder(AMTagKeys.createItemTag(AMCommon.id("drills")));
		DRILLS.forEach(drillsTagBuilder::add);
		
		COPY.forEach(this::copy);
		
		getOrCreateTagBuilder(AMTagKeys.createCommonItemTag("gold_apples"))
				.add(Items.ENCHANTED_GOLDEN_APPLE);
		
		getOrCreateTagBuilder(AMTagKeys.createCommonItemTag("seeds"))
				.add(Items.WHEAT_SEEDS)
				.add(Items.BEETROOT_SEEDS)
				.add(Items.MELON_SEEDS)
				.add(Items.PUMPKIN_SEEDS);
		
		getOrCreateTagBuilder(AMTagKeys.createCommonItemTag("berries"))
				.add(Items.SWEET_BERRIES)
				.add(Items.GLOW_BERRIES);
		
		getOrCreateTagBuilder(AMTagKeys.createCommonItemTag("vines"))
				.addTag(AMTagKeys.Items.NETHER_VINES)
				.add(Items.VINE);
		
		getOrCreateTagBuilder(AMTagKeys.Items.BIOFUEL)
				.add(AMItems.BIOFUEL.get());
		
		getOrCreateTagBuilder(AMTagKeys.createCommonItemTag("carbon_dusts"))
				.addTag(AMTagKeys.createCommonItemTag("coal_dusts"))
				.addTag(AMTagKeys.createCommonItemTag("charcoal_dusts"));
		
		var oneBiofuelTagBuilder = getOrCreateTagBuilder(AMTagKeys.Items.MAKES_ONE_BIOFUEL);
		ONE_BIOFUEL_ITEMS.forEach(oneBiofuelTagBuilder::add);
		ONE_BIOFUEL_TAGS.forEach(oneBiofuelTagBuilder::addTag);
		ONE_BIOFUEL_TAGS_FORCED.forEach(oneBiofuelTagBuilder::forceAddTag);
		
		var twoBiofuelTagBuilder = getOrCreateTagBuilder(AMTagKeys.Items.MAKES_TWO_BIOFUEL);
		TWO_BIOFUEL_ITEMS.forEach(twoBiofuelTagBuilder::add);
		TWO_BIOFUEL_TAGS_FORCED.forEach(twoBiofuelTagBuilder::forceAddTag);
		
		var fourBiofuelTagBuilder = getOrCreateTagBuilder(AMTagKeys.Items.MAKES_FOUR_BIOFUEL);
		FOUR_BIOFUEL_ITEMS.forEach(fourBiofuelTagBuilder::add);
		FOUR_BIOFUEL_TAGS.forEach(fourBiofuelTagBuilder::addTag);
		FOUR_BIOFUEL_TAGS_FORCED.forEach(fourBiofuelTagBuilder::forceAddTag);
		
		var nineBiofuelTagBuilder = getOrCreateTagBuilder(AMTagKeys.Items.MAKES_NINE_BIOFUEL);
		NINE_BIOFUEL_ITEMS.forEach(nineBiofuelTagBuilder::add);
		NINE_BIOFUEL_TAGS.forEach(nineBiofuelTagBuilder::addTag);
	}
}
