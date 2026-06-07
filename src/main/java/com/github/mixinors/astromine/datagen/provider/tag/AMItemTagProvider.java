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
import com.github.mixinors.astromine.datagen.family.block.AMBlockFamilies;
import com.github.mixinors.astromine.datagen.family.material.AMMaterialFamilies;
import com.github.mixinors.astromine.datagen.family.material.family.MaterialFamily;
import com.github.mixinors.astromine.datagen.family.material.variant.ItemVariant;
import com.github.mixinors.astromine.registry.common.AMItems;
import com.github.mixinors.astromine.registry.common.AMTagKeys;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class AMItemTagProvider extends net.minecraft.data.tags.ItemTagsProvider {
	
	
	public AMItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagsProvider.TagLookup<Block>> blockTags, ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, blockTags, AMCommon.MOD_ID, existingFileHelper);
	}
	
	@Override
	protected void addTags(HolderLookup.Provider provider) {
		var beaconPaymentTagBuilder = tag(net.minecraft.tags.ItemTags.BEACON_PAYMENT_ITEMS);
		
		var piglinLovedTagBuilder = tag(net.minecraft.tags.ItemTags.PIGLIN_LOVED);
		var piglinLovedNuggetsTagBuilder = tag(AMTagKeys.createCommonItemTag("piglin_loved_nuggets"));
		var piglinBarteringItemsTagBuilder = tag(AMTagKeys.createCommonItemTag("piglin_bartering_items"));
		var piglinSafeArmorTagBuilder = tag(AMTagKeys.createCommonItemTag("piglin_safe_armor"));
		
		AMMaterialFamilies.getFamilies().filter(MaterialFamily::shouldGenerateTags).forEachOrdered(family -> {
			AMDatagen.toTreeMap(family.getItemTags()).forEach((variant, tag) -> {
				tag(tag).add(family.getVariant(variant));
				
				if (family.hasAlias()) {
					tag(family.getAliasTag(variant)).addTag(tag);
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
					tag(variant.getTag()).addTag(tag);
					
					if (family.hasAlias()) {
						tag(variant.getTag()).addTag(family.getAliasTag(variant));
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
					tag(variant.getItemTag()).addTag(tag);
					
					if (family.hasAlias()) {
						tag(variant.getItemTag()).addTag(family.getAliasItemTag(variant));
					}
				}
			});
			
			if (family.hasAnyBlockVariants(AMDatagenLists.BlockVariantLists.ORE_VARIANTS)) {
				var oresBlockTag = family.getBlockTag("ores");
				var oresItemTag = family.getItemTag("ores");
				
				copy(oresBlockTag, oresItemTag);
				
				if (family.hasAlias()) {
					copy(family.getAliasBlockTag("ores"), family.getAliasItemTag("ores"));
				}
			}
			
			if (family.hasAnyItemVariants(AMDatagenLists.ItemVariantLists.CLUSTER_VARIANTS)) {
				var clustersTag = family.getItemTag("clusters");
				var clustersTagBuilder = tag(clustersTag);
				
				AMDatagenLists.ItemVariantLists.CLUSTER_VARIANTS.forEach((variant) -> {
					if (family.hasVariant(variant)) {
						clustersTagBuilder.addTag(family.getTag(variant));
					}
				});
				
				if (family.hasAlias()) {
					tag(family.getAliasItemTag("clusters")).addTag(clustersTag);
				}
			}
			
			if (family.hasAnyItemVariants(AMDatagenLists.ItemVariantLists.EQUIPMENT_VARIANTS)) {
				var armorTag = family.getItemTag("armor");
				var toolsTag = family.getItemTag("tools");
				
				var salvageablesTag = family.getItemTag("salvageables");
				var salvageablesTagBuilder = tag(salvageablesTag);
				
				if (family.hasAnyItemVariants(AMDatagenLists.ItemVariantLists.ARMOR_VARIANTS)) {
					var armorTagBuilder = tag(armorTag);
					
					AMDatagenLists.ItemVariantLists.ARMOR_VARIANTS.forEach((variant) -> {
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
						tag(family.getAliasItemTag("armor")).addTag(armorTag);
					}
				}
				
				if (family.hasAnyItemVariants(AMDatagenLists.ItemVariantLists.TOOL_VARIANTS)) {
					var toolsTagBuilder = tag(toolsTag);
					
					AMDatagenLists.ItemVariantLists.TOOL_VARIANTS.forEach((variant) -> {
						if (family.hasVariant(variant)) {
							toolsTagBuilder.addTag(family.getTag(variant));
						}
					});
					
					salvageablesTagBuilder.addTag(toolsTag);
					
					if (family.hasAlias()) {
						tag(family.getAliasItemTag("tools")).addTag(toolsTag);
					}
				}
				
				if (family.hasVariant(ItemVariant.HORSE_ARMOR)) {
					salvageablesTagBuilder.add(family.getVariant(ItemVariant.HORSE_ARMOR));
				}
				
				if (family.hasAlias()) {
					tag(family.getAliasItemTag("salvageables")).addTag(salvageablesTag);
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
			if (AMDatagenLists.ItemTagLists.BLOCK_FAMILY_VARIANTS.containsKey(variant)) {
				tag(AMDatagenLists.ItemTagLists.BLOCK_FAMILY_VARIANTS.get(variant)).add(block.asItem());
			}
		}));
		
		AMDatagenLists.FluidLists.FLUIDS.forEach((fluid) -> {
			var bucketTagBuilder = tag(AMTagKeys.createCommonItemTag(BuiltInRegistries.FLUID.getKey(fluid.getSource()).getPath() + "_buckets"));
			bucketTagBuilder.add(fluid.getBucket());
		});
		
		AMDatagenLists.ItemTagLists.GENERIC_TAGS.forEach((variantSet, tagKey) -> {
			var tag = tag(tagKey);
			
			variantSet.forEach((variant) -> {
				if (variant.hasTag()) {
					tag.addTag(variant.getTag());
				}
			});
		});
		
		AMDatagenLists.ItemVariantLists.TOOL_VARIANTS.forEach((variant) -> tag(toolTag(variant)).addTag(variant.getTag()));
		
		var drillsTagBuilder = tag(AMTagKeys.createItemTag(AMCommon.id("drills")));
		
		AMDatagenLists.ItemLists.DRILLS.forEach(drillsTagBuilder::add);
		
		AMDatagenLists.ItemTagLists.COPY.forEach(this::copy);
		
		tag(AMTagKeys.createCommonItemTag("gold_apples"))
				.add(Items.ENCHANTED_GOLDEN_APPLE);
		
		tag(AMTagKeys.createCommonItemTag("seeds"))
				.add(Items.WHEAT_SEEDS)
				.add(Items.BEETROOT_SEEDS)
				.add(Items.MELON_SEEDS)
				.add(Items.PUMPKIN_SEEDS);
		
		tag(AMTagKeys.createCommonItemTag("berries"))
				.add(Items.SWEET_BERRIES)
				.add(Items.GLOW_BERRIES);
		
		var weepingVinesTag = AMTagKeys.ItemTags.WEEPING_VINES;
		tag(weepingVinesTag)
				.add(Items.WEEPING_VINES);
		
		var twistingVinesTag = AMTagKeys.ItemTags.TWISTING_VINES;
		tag(twistingVinesTag)
				.add(Items.TWISTING_VINES);
		
		tag(AMTagKeys.ItemTags.NETHER_VINES)
				.addTag(weepingVinesTag)
				.addTag(twistingVinesTag);
		
		tag(AMTagKeys.createCommonItemTag("vines"))
				.addTag(AMTagKeys.ItemTags.NETHER_VINES)
				.add(Items.VINE);
		
		tag(AMTagKeys.ItemTags.BIOFUEL)
				.add(AMItems.BIOFUEL.get());
		
		tag(AMTagKeys.createCommonItemTag("carbon_dusts"))
				.addTag(AMTagKeys.createCommonItemTag("coal_dusts"))
				.addTag(AMTagKeys.createCommonItemTag("charcoal_dusts"));
		
		var oneBiofuelTagBuilder = tag(AMTagKeys.ItemTags.MAKES_ONE_BIOFUEL);
		
		AMDatagenLists.ItemLists.ONE_BIOFUEL_ITEMS.forEach(oneBiofuelTagBuilder::add);
		AMDatagenLists.ItemTagLists.ONE_BIOFUEL_TAGS.forEach(oneBiofuelTagBuilder::addTag);
		AMDatagenLists.ItemTagLists.ONE_BIOFUEL_TAGS_FORCED.forEach(oneBiofuelTagBuilder::addOptionalTag);
		
		var twoBiofuelTagBuilder = tag(AMTagKeys.ItemTags.MAKES_TWO_BIOFUEL);
		
		AMDatagenLists.ItemLists.TWO_BIOFUEL_ITEMS.forEach(twoBiofuelTagBuilder::add);
		AMDatagenLists.ItemTagLists.TWO_BIOFUEL_TAGS_FORCED.forEach(twoBiofuelTagBuilder::addOptionalTag);
		
		var fourBiofuelTagBuilder = tag(AMTagKeys.ItemTags.MAKES_FOUR_BIOFUEL);
		
		AMDatagenLists.ItemLists.FOUR_BIOFUEL_ITEMS.forEach(fourBiofuelTagBuilder::add);
		AMDatagenLists.ItemTagLists.FOUR_BIOFUEL_TAGS.forEach(fourBiofuelTagBuilder::addTag);
		AMDatagenLists.ItemTagLists.FOUR_BIOFUEL_TAGS_FORCED.forEach(fourBiofuelTagBuilder::addOptionalTag);
		
		var nineBiofuelTagBuilder = tag(AMTagKeys.ItemTags.MAKES_NINE_BIOFUEL);
		
		AMDatagenLists.ItemLists.NINE_BIOFUEL_ITEMS.forEach(nineBiofuelTagBuilder::add);
		AMDatagenLists.ItemTagLists.NINE_BIOFUEL_TAGS.forEach(nineBiofuelTagBuilder::addTag);
	}
	
	private static TagKey<Item> toolTag(ItemVariant variant) {
		return switch (variant) {
			case PICKAXE -> ItemTags.PICKAXES;
			case AXE -> ItemTags.AXES;
			case SHOVEL -> ItemTags.SHOVELS;
			case SWORD -> ItemTags.SWORDS;
			case HOE -> ItemTags.HOES;
			default -> throw new IllegalArgumentException("Not a vanilla tool variant: " + variant);
		};
	}
}
