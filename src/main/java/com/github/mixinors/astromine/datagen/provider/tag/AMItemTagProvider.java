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
import com.github.mixinors.astromine.datagen.DatagenLists;
import com.github.mixinors.astromine.datagen.family.block.AMBlockFamilies;
import com.github.mixinors.astromine.datagen.family.material.MaterialFamilies;
import com.github.mixinors.astromine.datagen.family.material.MaterialFamily;
import com.github.mixinors.astromine.datagen.family.material.variant.ItemVariant;
import com.github.mixinors.astromine.registry.common.AMItems;
import com.github.mixinors.astromine.registry.common.AMTagKeys;
import com.shnupbups.piglib.Piglib;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

public class AMItemTagProvider extends FabricTagProvider.ItemTagProvider {
	
	
	public AMItemTagProvider(FabricDataGenerator dataGenerator, @Nullable BlockTagProvider blockTagProvider) {
		super(dataGenerator, blockTagProvider);
	}
	
	@Override
	protected void generateTags() {
		var beaconPaymentTagBuilder = getOrCreateTagBuilder(net.minecraft.tag.ItemTags.BEACON_PAYMENT_ITEMS);
		var piglinLovedTagBuilder = getOrCreateTagBuilder(net.minecraft.tag.ItemTags.PIGLIN_LOVED);
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
			
			if (family.hasAnyBlockVariants(DatagenLists.BlockVariantLists.ORE_VARIANTS)) {
				var oresBlockTag = family.getBlockTag("ores");
				var oresItemTag = family.getItemTag("ores");
				copy(oresBlockTag, oresItemTag);
				
				if (family.hasAlias()) {
					copy(family.getAliasBlockTag("ores"), family.getAliasItemTag("ores"));
				}
			}
			
			if (family.hasAnyItemVariants(DatagenLists.ItemVariantLists.CLUSTER_VARIANTS)) {
				var clustersTag = family.getItemTag("clusters");
				var clustersTagBuilder = getOrCreateTagBuilder(clustersTag);
				DatagenLists.ItemVariantLists.CLUSTER_VARIANTS.forEach((variant) -> {
					if (family.hasVariant(variant)) {
						clustersTagBuilder.addTag(family.getTag(variant));
					}
				});
				if (family.hasAlias()) {
					getOrCreateTagBuilder(family.getAliasItemTag("clusters")).addTag(clustersTag);
				}
			}
			
			if (family.hasAnyItemVariants(DatagenLists.ItemVariantLists.EQUIPMENT_VARIANTS)) {
				var armorTag = family.getItemTag("armor");
				var toolsTag = family.getItemTag("tools");
				
				var salvageablesTag = family.getItemTag("salvageables");
				var salvageablesTagBuilder = getOrCreateTagBuilder(salvageablesTag);
				
				if (family.hasAnyItemVariants(DatagenLists.ItemVariantLists.ARMOR_VARIANTS)) {
					var armorTagBuilder = getOrCreateTagBuilder(armorTag);
					DatagenLists.ItemVariantLists.ARMOR_VARIANTS.forEach((variant) -> {
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
				
				if (family.hasAnyItemVariants(DatagenLists.ItemVariantLists.TOOL_VARIANTS)) {
					var toolsTagBuilder = getOrCreateTagBuilder(toolsTag);
					DatagenLists.ItemVariantLists.TOOL_VARIANTS.forEach((variant) -> {
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
			if (DatagenLists.ItemTagLists.BLOCK_FAMILY_VARIANTS.containsKey(variant)) {
				getOrCreateTagBuilder(DatagenLists.ItemTagLists.BLOCK_FAMILY_VARIANTS.get(variant)).add(block.asItem());
			}
		}));
		
		DatagenLists.FluidLists.FLUIDS.forEach((fluid) -> {
			var bucketTagBuilder = getOrCreateTagBuilder(AMTagKeys.createCommonItemTag(Registry.FLUID.getId(fluid.getStill()).getPath() + "_buckets"));
			bucketTagBuilder.add(fluid.getBucketItem());
		});
		
		DatagenLists.ItemTagLists.GENERIC_TAGS.forEach((variantSet, tagKey) -> {
			var tag = getOrCreateTagBuilder(tagKey);
			variantSet.forEach((variant) -> {
				if (variant.hasTag()) {
					tag.addTag(variant.getTag());
				}
			});
		});
		
		DatagenLists.ItemVariantLists.TOOL_VARIANTS.forEach((variant) -> getOrCreateTagBuilder(AMTagKeys.createItemTag(new Identifier("fabric", variant.getTagPath()))).addTag(variant.getTag()));
		
		var drillsTagBuilder = getOrCreateTagBuilder(AMTagKeys.createItemTag(AMCommon.id("drills")));
		DatagenLists.ItemLists.DRILLS.forEach(drillsTagBuilder::add);
		
		DatagenLists.ItemTagLists.COPY.forEach(this::copy);
		
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
				.addTag(AMTagKeys.ItemTags.NETHER_VINES)
				.add(Items.VINE);
		
		getOrCreateTagBuilder(AMTagKeys.ItemTags.BIOFUEL)
				.add(AMItems.BIOFUEL.get());
		
		getOrCreateTagBuilder(AMTagKeys.createCommonItemTag("carbon_dusts"))
				.addTag(AMTagKeys.createCommonItemTag("coal_dusts"))
				.addTag(AMTagKeys.createCommonItemTag("charcoal_dusts"));
		
		var oneBiofuelTagBuilder = getOrCreateTagBuilder(AMTagKeys.ItemTags.MAKES_ONE_BIOFUEL);
		DatagenLists.ItemLists.ONE_BIOFUEL_ITEMS.forEach(oneBiofuelTagBuilder::add);
		DatagenLists.ItemTagLists.ONE_BIOFUEL_TAGS.forEach(oneBiofuelTagBuilder::addTag);
		DatagenLists.ItemTagLists.ONE_BIOFUEL_TAGS_FORCED.forEach(oneBiofuelTagBuilder::forceAddTag);
		
		var twoBiofuelTagBuilder = getOrCreateTagBuilder(AMTagKeys.ItemTags.MAKES_TWO_BIOFUEL);
		DatagenLists.ItemLists.TWO_BIOFUEL_ITEMS.forEach(twoBiofuelTagBuilder::add);
		DatagenLists.ItemTagLists.TWO_BIOFUEL_TAGS_FORCED.forEach(twoBiofuelTagBuilder::forceAddTag);
		
		var fourBiofuelTagBuilder = getOrCreateTagBuilder(AMTagKeys.ItemTags.MAKES_FOUR_BIOFUEL);
		DatagenLists.ItemLists.FOUR_BIOFUEL_ITEMS.forEach(fourBiofuelTagBuilder::add);
		DatagenLists.ItemTagLists.FOUR_BIOFUEL_TAGS.forEach(fourBiofuelTagBuilder::addTag);
		DatagenLists.ItemTagLists.FOUR_BIOFUEL_TAGS_FORCED.forEach(fourBiofuelTagBuilder::forceAddTag);
		
		var nineBiofuelTagBuilder = getOrCreateTagBuilder(AMTagKeys.ItemTags.MAKES_NINE_BIOFUEL);
		DatagenLists.ItemLists.NINE_BIOFUEL_ITEMS.forEach(nineBiofuelTagBuilder::add);
		DatagenLists.ItemTagLists.NINE_BIOFUEL_TAGS.forEach(nineBiofuelTagBuilder::addTag);
	}
}
