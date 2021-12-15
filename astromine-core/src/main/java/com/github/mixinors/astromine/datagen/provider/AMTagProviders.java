package com.github.mixinors.astromine.datagen.provider;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;

import com.github.mixinors.astromine.datagen.AMDatagen;
import com.github.mixinors.astromine.datagen.family.block.AMBlockFamilies;
import com.github.mixinors.astromine.datagen.family.material.MaterialFamilies;
import com.github.mixinors.astromine.datagen.family.material.MaterialFamily;
import com.github.mixinors.astromine.datagen.family.material.variant.BlockVariant;
import com.github.mixinors.astromine.datagen.family.material.variant.ItemVariant;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.data.family.BlockFamilies;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.TagFactory;

import com.shnupbups.piglib.Piglib;

public class AMTagProviders {
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

	public static class AMBlockTagProvider extends FabricTagProvider.BlockTagProvider {

		public AMBlockTagProvider(FabricDataGenerator dataGenerator) {
			super(dataGenerator);
		}

		@Override
		protected void generateTags() {
			FabricTagBuilder<Block> beaconBaseTagBuilder = getOrCreateTagBuilder(BlockTags.BEACON_BASE_BLOCKS);
			FabricTagBuilder<Block> guardedByPiglinsTagBuilder = getOrCreateTagBuilder(BlockTags.GUARDED_BY_PIGLINS);

			MaterialFamilies.getFamilies().filter(MaterialFamily::shouldGenerateTags).forEach(family -> {
				family.getBlockTags().forEach((variant, tag) -> {
					getOrCreateTagBuilder(tag).add(family.getVariant(variant));

					if(family.isPiglinLoved()) {
						guardedByPiglinsTagBuilder.addTag(tag);
					}

					if(variant.hasTag()) {
						getOrCreateTagBuilder(variant.getTag()).addTag(tag);
					}
				});

				if(family.hasAnyBlockVariants(AMDatagen.ORE_VARIANTS)) {
					FabricTagBuilder<Block> oresTagBuilder = getOrCreateTagBuilder(family.getBlockTag("ores"));
					AMDatagen.ORE_VARIANTS.forEach((variant) -> {
						if(family.hasVariant(variant)) {
							oresTagBuilder.addTag(family.getTag(variant));
						}
					});
				}

				if(family.isValidForBeacon() && family.hasVariant(BlockVariant.BLOCK)) {
					beaconBaseTagBuilder.addTag(family.getTag(BlockVariant.BLOCK));
				}
			});

			BlockFamilies.getFamilies().filter(AMBlockFamilies::isAstromineFamily).forEach(family -> family.getVariants().forEach((variant, block) -> {
				if(VANILLA_BLOCK_TAG_VARIANTS.containsKey(variant)) {
					getOrCreateTagBuilder(createBlockTag(VANILLA_BLOCK_TAG_VARIANTS.get(variant))).add(block);
				}
			}));

			AMDatagen.FLUIDS.forEach((fluid) -> {
				String fluidName = Registry.FLUID.getId(fluid.getStill()).getPath();
				FabricTagBuilder<Block> tag = getOrCreateTagBuilder(createCommonBlockTag(fluidName));
				FabricTagBuilder<Block> cauldronTag = getOrCreateTagBuilder(createCommonBlockTag(fluidName+"_cauldrons"));
				tag.add(fluid.getBlock());
				cauldronTag.add(fluid.getCauldron());
			});

			FabricTagBuilder<Block> oresTagBuilder = getOrCreateTagBuilder(createCommonBlockTag("ores"));
			AMDatagen.ORE_VARIANTS.forEach((variant) -> {
				if(variant.hasTag()) oresTagBuilder.addTag(variant.getTag());
			});
		}
	}

	public static class AMItemTagProvider extends FabricTagProvider.ItemTagProvider {
		public static final Map<Set<ItemVariant>, Identifier> GENERIC_TAGS = Map.of(
				AMDatagen.CLUSTER_VARIANTS, createCommonTagId("clusters"),
				AMDatagen.ARMOR_VARIANTS, createCommonTagId("armor"),
				AMDatagen.TOOL_VARIANTS, createCommonTagId("tools")
		);

		public AMItemTagProvider(FabricDataGenerator dataGenerator, @Nullable BlockTagProvider blockTagProvider) {
			super(dataGenerator, blockTagProvider);
		}

		@Override
		protected void generateTags() {
			FabricTagBuilder<Item> beaconPaymentTagBuilder = getOrCreateTagBuilder(ItemTags.BEACON_PAYMENT_ITEMS);
			FabricTagBuilder<Item> piglinLovedTagBuilder = getOrCreateTagBuilder(ItemTags.PIGLIN_LOVED);
			FabricTagBuilder<Item> piglinLovedNuggetsTagBuilder = getOrCreateTagBuilder(Piglib.PIGLIN_LOVED_NUGGETS);
			FabricTagBuilder<Item> piglinBarteringItemsTagBuilder = getOrCreateTagBuilder(Piglib.PIGLIN_BARTERING_ITEMS);
			FabricTagBuilder<Item> piglinSafeArmorTagBuilder = getOrCreateTagBuilder(Piglib.PIGLIN_SAFE_ARMOR);

			MaterialFamilies.getFamilies().filter(MaterialFamily::shouldGenerateTags).forEach(family -> {
				family.getItemTags().forEach((variant, tag) -> {
					getOrCreateTagBuilder(tag).add(family.getVariant(variant));

					if(family.isPiglinLoved()) {
						if(variant.equals(ItemVariant.NUGGET)) piglinLovedNuggetsTagBuilder.addTag(tag);
						else piglinLovedTagBuilder.addTag(tag);
					}

					if(variant.hasTag()) {
						getOrCreateTagBuilder(variant.getTag()).addTag(tag);
					}
				});
				family.getBlockItemTags().forEach((variant, tag) -> {
					Tag.Identified<Block> blockTag = family.getTag(variant);

					copy(blockTag, tag);

					if(family.isPiglinLoved()) {
						piglinLovedTagBuilder.addTag(tag);
					}

					if(variant.hasTag()) {
						getOrCreateTagBuilder(variant.getItemTag()).addTag(tag);
					}
				});

				if(family.hasAnyBlockVariants(AMDatagen.ORE_VARIANTS)) {
					Tag.Identified<Block> oresBlockTag = family.getBlockTag("ores");
					Tag.Identified<Item> oresItemTag = family.getItemTag("ores");
					copy(oresBlockTag, oresItemTag);
				}

				if(family.hasAnyItemVariants(AMDatagen.CLUSTER_VARIANTS)) {
					FabricTagBuilder<Item> clustersTagBuilder = getOrCreateTagBuilder(family.getItemTag("clusters"));
					AMDatagen.CLUSTER_VARIANTS.forEach((variant) -> {
						if(family.hasVariant(variant)) {
							clustersTagBuilder.addTag(family.getTag(variant));
						}
					});
				}

				if(family.hasAnyItemVariants(AMDatagen.EQUIPMENT_VARIANTS)) {
					Tag.Identified<Item> armorTag = family.getItemTag("armor");
					Tag.Identified<Item> toolsTag = family.getItemTag("tools");

					FabricTagBuilder<Item> salvageablesTagBuilder = getOrCreateTagBuilder(family.getItemTag("salvageables"));

					if(family.hasAnyItemVariants(AMDatagen.ARMOR_VARIANTS)) {
						FabricTagBuilder<Item> armorTagBuilder = getOrCreateTagBuilder(armorTag);
						AMDatagen.ARMOR_VARIANTS.forEach((variant) -> {
							if(family.hasVariant(variant)) {
								armorTagBuilder.addTag(family.getTag(variant));
							}
						});

						if(family.isPiglinLoved()) {
							piglinSafeArmorTagBuilder.addTag(armorTag);
						}

						salvageablesTagBuilder.addTag(armorTag);
					}

					if(family.hasAnyItemVariants(AMDatagen.TOOL_VARIANTS)) {
						FabricTagBuilder<Item> toolsTagBuilder = getOrCreateTagBuilder(toolsTag);
						AMDatagen.TOOL_VARIANTS.forEach((variant) -> {
							if(family.hasVariant(variant)) {
								toolsTagBuilder.addTag(family.getTag(variant));
							}
						});

						salvageablesTagBuilder.addTag(toolsTag);
					}

					if(family.hasVariant(ItemVariant.HORSE_ARMOR)) {
						salvageablesTagBuilder.add(family.getVariant(ItemVariant.HORSE_ARMOR));
					}
				}

				if(family.isValidForBeacon()) {
					beaconPaymentTagBuilder.addTag(family.getBaseTag());
				}

				if(family.isPiglinLoved()) {
					piglinBarteringItemsTagBuilder.addTag(family.getBaseTag());
				}
			});

			BlockFamilies.getFamilies().filter(AMBlockFamilies::isAstromineFamily).forEach(family -> family.getVariants().forEach((variant, block) -> {
				if(VANILLA_ITEM_TAG_VARIANTS.containsKey(variant)) {
					getOrCreateTagBuilder(createItemTag(VANILLA_ITEM_TAG_VARIANTS.get(variant))).add(block.asItem());
				}
			}));

			AMDatagen.FLUIDS.forEach((fluid) -> {
				FabricTagBuilder<Item> bucketTag = getOrCreateTagBuilder(createCommonItemTag(Registry.FLUID.getId(fluid.getStill()).getPath()+"_buckets"));
				bucketTag.add(fluid.getBucketItem());
			});

			copy(createCommonBlockTag("ores"), createCommonItemTag("ores"));

			GENERIC_TAGS.forEach((variantSet, id) -> {
				FabricTagBuilder<Item> tag = getOrCreateTagBuilder(createItemTag(id));
				variantSet.forEach((variant) -> {
					if(variant.hasTag()) tag.addTag(variant.getTag());
				});
			});

			AMDatagen.TOOL_VARIANTS.forEach((variant) -> {
				getOrCreateTagBuilder(createItemTag(new Identifier("fabric", variant.getTagPath()))).addTag(variant.getTag());
			});
		}
	}

	public static class AMFluidTagProvider extends FabricTagProvider.FluidTagProvider {

		public AMFluidTagProvider(FabricDataGenerator dataGenerator) {
			super(dataGenerator);
		}

		@Override
		protected void generateTags() {
			AMDatagen.FLUIDS.forEach((fluid) -> {
				FabricTagBuilder<Fluid> tag = getOrCreateTagBuilder(createCommonFluidTag(Registry.FLUID.getId(fluid.getStill()).getPath()));
				tag.add(fluid.getStill(), fluid.getFlowing());
			});
		}
	}
}
