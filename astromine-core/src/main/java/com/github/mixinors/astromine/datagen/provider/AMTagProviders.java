package com.github.mixinors.astromine.datagen.provider;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.datagen.AMDatagen;
import com.github.mixinors.astromine.datagen.family.block.AMBlockFamilies;
import com.github.mixinors.astromine.datagen.family.material.MaterialFamilies;
import com.github.mixinors.astromine.datagen.family.material.MaterialFamily;
import com.github.mixinors.astromine.datagen.family.material.variant.BlockVariant;
import com.github.mixinors.astromine.datagen.family.material.variant.ItemVariant;
import com.github.mixinors.astromine.registry.common.AMItems;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
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

			MaterialFamilies.getFamilies().filter(MaterialFamily::shouldGenerateTags).forEachOrdered(family -> {
				family.getBlockTags().forEach((variant, tag) -> {
					getOrCreateTagBuilder(tag).add(family.getVariant(variant));

					if(family.hasAlias()) {
						getOrCreateTagBuilder(family.getAliasTag(variant)).addTag(tag);
					}

					if(family.isPiglinLoved()) {
						guardedByPiglinsTagBuilder.addTag(tag);

						if(family.hasAlias()) {
							guardedByPiglinsTagBuilder.addTag(family.getAliasTag(variant));
						}
					}

					if(variant.hasTag()) {
						getOrCreateTagBuilder(variant.getTag()).addTag(tag);

						if(family.hasAlias()) {
							getOrCreateTagBuilder(variant.getTag()).addTag(family.getAliasTag(variant));
						}
					}
				});

				if(family.hasAnyBlockVariants(AMDatagen.ORE_VARIANTS)) {
					Tag.Identified<Block> oresTag = family.getBlockTag("ores");
					FabricTagBuilder<Block> oresTagBuilder = getOrCreateTagBuilder(oresTag);
					AMDatagen.ORE_VARIANTS.forEach((variant) -> {
						if(family.hasVariant(variant)) {
							oresTagBuilder.addTag(family.getTag(variant));
						}
					});
					if(family.hasAlias()) {
						getOrCreateTagBuilder(family.getAliasBlockTag("ores")).addTag(oresTag);
					}
				}

				if(family.isValidForBeacon() && family.hasVariant(BlockVariant.BLOCK)) {
					beaconBaseTagBuilder.addTag(family.getTag(BlockVariant.BLOCK));

					if(family.hasAlias()) {
						beaconBaseTagBuilder.addTag(family.getAliasTag(BlockVariant.BLOCK));
					}
				}
			});

			AMBlockFamilies.getFamilies().forEachOrdered(family -> family.getVariants().forEach((variant, block) -> {
				if(VANILLA_BLOCK_TAG_VARIANTS.containsKey(variant)) {
					getOrCreateTagBuilder(createBlockTag(VANILLA_BLOCK_TAG_VARIANTS.get(variant))).add(block);
				}
			}));

			FabricTagBuilder<Block> cauldronsTagBuilder = getOrCreateTagBuilder(BlockTags.CAULDRONS);
			AMDatagen.FLUIDS.forEach((fluid) -> {
				String fluidName = Registry.FLUID.getId(fluid.getStill()).getPath();
				FabricTagBuilder<Block> tagBuilder = getOrCreateTagBuilder(createCommonBlockTag(fluidName));
				Tag.Identified<Block> cauldronTag = createCommonBlockTag(fluidName+"_cauldrons");
				FabricTagBuilder<Block> cauldronTagBuilder = getOrCreateTagBuilder(cauldronTag);
				tagBuilder.add(fluid.getBlock());
				cauldronTagBuilder.add(fluid.getCauldron());
				cauldronsTagBuilder.addTag(cauldronTag);
			});

			FabricTagBuilder<Block> oresTagBuilder = getOrCreateTagBuilder(createCommonBlockTag("ores"));
			AMDatagen.ORE_VARIANTS.forEach((variant) -> {
				if(variant.hasTag()) {
					oresTagBuilder.addTag(variant.getTag());
				}
			});

			Tag.Identified<Block> yellowSandstonesTag = createCommonBlockTag("yellow_sandstones");
			getOrCreateTagBuilder(yellowSandstonesTag)
					.add(Blocks.SANDSTONE)
					.add(Blocks.CHISELED_SANDSTONE)
					.add(Blocks.CUT_SANDSTONE)
					.add(Blocks.SMOOTH_SANDSTONE);

			Tag.Identified<Block> redSandstonesTag = createCommonBlockTag("red_sandstones");
			getOrCreateTagBuilder(redSandstonesTag)
					.add(Blocks.RED_SANDSTONE)
					.add(Blocks.CHISELED_RED_SANDSTONE)
					.add(Blocks.CUT_RED_SANDSTONE)
					.add(Blocks.SMOOTH_RED_SANDSTONE);

			getOrCreateTagBuilder(createCommonBlockTag("sandstones"))
					.addTag(yellowSandstonesTag)
					.addTag(redSandstonesTag);

			getOrCreateTagBuilder(createCommonBlockTag("quartz_blocks"))
					.add(Blocks.QUARTZ_BLOCK)
					.add(Blocks.QUARTZ_BRICKS)
					.add(Blocks.QUARTZ_PILLAR)
					.add(Blocks.CHISELED_QUARTZ_BLOCK);

			Tag.Identified<Block> unwaxedCopperBlocksTag = createCommonBlockTag("unwaxed_copper_blocks");
			getOrCreateTagBuilder(unwaxedCopperBlocksTag)
					.add(Blocks.COPPER_BLOCK)
					.add(Blocks.EXPOSED_COPPER)
					.add(Blocks.WEATHERED_COPPER)
					.add(Blocks.OXIDIZED_COPPER);

			Tag.Identified<Block> waxedCopperBlocksTag = createCommonBlockTag("waxed_copper_blocks");
			getOrCreateTagBuilder(waxedCopperBlocksTag)
					.add(Blocks.WAXED_COPPER_BLOCK)
					.add(Blocks.WAXED_EXPOSED_COPPER)
					.add(Blocks.WAXED_WEATHERED_COPPER)
					.add(Blocks.WAXED_OXIDIZED_COPPER);

			getOrCreateTagBuilder(createCommonBlockTag("copper_blocks"))
					.addTag(unwaxedCopperBlocksTag)
					.addTag(waxedCopperBlocksTag);

			Tag.Identified<Block> unwaxedCutCopperTag = createCommonBlockTag("unwaxed_cut_copper");
			getOrCreateTagBuilder(unwaxedCutCopperTag)
					.add(Blocks.CUT_COPPER)
					.add(Blocks.EXPOSED_CUT_COPPER)
					.add(Blocks.WEATHERED_CUT_COPPER)
					.add(Blocks.OXIDIZED_CUT_COPPER);

			Tag.Identified<Block> waxedCutCopperTag = createCommonBlockTag("waxed_cut_copper");
			getOrCreateTagBuilder(waxedCutCopperTag)
					.add(Blocks.WAXED_CUT_COPPER)
					.add(Blocks.WAXED_EXPOSED_CUT_COPPER)
					.add(Blocks.WAXED_WEATHERED_CUT_COPPER)
					.add(Blocks.WAXED_OXIDIZED_CUT_COPPER);

			getOrCreateTagBuilder(createCommonBlockTag("cut_copper"))
					.addTag(unwaxedCutCopperTag)
					.addTag(waxedCutCopperTag);

			getOrCreateTagBuilder(createCommonBlockTag("purpur_blocks"))
					.add(Blocks.PURPUR_BLOCK)
					.add(Blocks.PURPUR_PILLAR);
		}
	}

	public static class AMItemTagProvider extends FabricTagProvider.ItemTagProvider {
		public static final Map<List<ItemVariant>, Identifier> GENERIC_TAGS = Map.of(
				AMDatagen.CLUSTER_VARIANTS, createCommonTagId("clusters"),
				AMDatagen.ARMOR_VARIANTS, createCommonTagId("armor"),
				AMDatagen.TOOL_VARIANTS, createCommonTagId("tools")
		);

		public static final List<Identifier> COPY = List.of(
				createCommonTagId("yellow_sandstones"),
				createCommonTagId("red_sandstones"),
				createCommonTagId("sandstones"),
				createCommonTagId("quartz_blocks"),
				createCommonTagId("unwaxed_copper_blocks"),
				createCommonTagId("waxed_copper_blocks"),
				createCommonTagId("copper_blocks"),
				createCommonTagId("unwaxed_cut_copper"),
				createCommonTagId("waxed_cut_copper"),
				createCommonTagId("cut_copper"),
				createCommonTagId("purpur_blocks")
		);

		public static final List<Item> DRILLS = List.of(
				AMItems.PRIMITIVE_DRILL.get(),
				AMItems.BASIC_DRILL.get(),
				AMItems.ADVANCED_DRILL.get(),
				AMItems.ELITE_DRILL.get()
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

			MaterialFamilies.getFamilies().filter(MaterialFamily::shouldGenerateTags).forEachOrdered(family -> {
				family.getItemTags().forEach((variant, tag) -> {
					getOrCreateTagBuilder(tag).add(family.getVariant(variant));

					if(family.hasAlias()) {
						getOrCreateTagBuilder(family.getAliasTag(variant)).addTag(tag);
					}

					if(family.isPiglinLoved()) {
						if(variant.equals(ItemVariant.NUGGET)) {
							piglinLovedNuggetsTagBuilder.addTag(tag);

							if(family.hasAlias()) {
								piglinLovedNuggetsTagBuilder.addTag(family.getAliasTag(variant));
							}
						}
						else {
							piglinLovedTagBuilder.addTag(tag);

							if(family.hasAlias()) {
								piglinLovedTagBuilder.addTag(family.getAliasTag(variant));
							}
						}
					}

					if(variant.hasTag()) {
						getOrCreateTagBuilder(variant.getTag()).addTag(tag);

						if(family.hasAlias()) {
							getOrCreateTagBuilder(variant.getTag()).addTag(family.getAliasTag(variant));
						}
					}
				});
				family.getBlockItemTags().forEach((variant, tag) -> {
					Tag.Identified<Block> blockTag = family.getTag(variant);

					copy(blockTag, tag);

					if(family.hasAlias()) {
						copy(family.getAliasTag(variant), family.getAliasItemTag(variant));
					}

					if(family.isPiglinLoved()) {
						piglinLovedTagBuilder.addTag(tag);
					}

					if(variant.hasTag()) {
						getOrCreateTagBuilder(variant.getItemTag()).addTag(tag);

						if(family.hasAlias()) {
							getOrCreateTagBuilder(variant.getItemTag()).addTag(family.getAliasItemTag(variant));
						}
					}
				});

				if(family.hasAnyBlockVariants(AMDatagen.ORE_VARIANTS)) {
					Tag.Identified<Block> oresBlockTag = family.getBlockTag("ores");
					Tag.Identified<Item> oresItemTag = family.getItemTag("ores");
					copy(oresBlockTag, oresItemTag);

					if(family.hasAlias()) {
						copy(family.getAliasBlockTag("ores"), family.getAliasItemTag("ores"));
					}
				}

				if(family.hasAnyItemVariants(AMDatagen.CLUSTER_VARIANTS)) {
					Tag.Identified<Item> clustersTag = family.getItemTag("clusters");
					FabricTagBuilder<Item> clustersTagBuilder = getOrCreateTagBuilder(clustersTag);
					AMDatagen.CLUSTER_VARIANTS.forEach((variant) -> {
						if(family.hasVariant(variant)) {
							clustersTagBuilder.addTag(family.getTag(variant));
						}
					});
					if(family.hasAlias()) {
						getOrCreateTagBuilder(family.getAliasItemTag("clusters")).addTag(clustersTag);
					}
				}

				if(family.hasAnyItemVariants(AMDatagen.EQUIPMENT_VARIANTS)) {
					Tag.Identified<Item> armorTag = family.getItemTag("armor");
					Tag.Identified<Item> toolsTag = family.getItemTag("tools");

					Tag.Identified<Item> salvageablesTag = family.getItemTag("salvageables");
					FabricTagBuilder<Item> salvageablesTagBuilder = getOrCreateTagBuilder(salvageablesTag);

					if(family.hasAnyItemVariants(AMDatagen.ARMOR_VARIANTS)) {
						FabricTagBuilder<Item> armorTagBuilder = getOrCreateTagBuilder(armorTag);
						AMDatagen.ARMOR_VARIANTS.forEach((variant) -> {
							if(family.hasVariant(variant)) {
								armorTagBuilder.addTag(family.getTag(variant));
							}
						});

						if(family.isPiglinLoved()) {
							piglinSafeArmorTagBuilder.addTag(armorTag);

							if(family.hasAlias()) {
								piglinSafeArmorTagBuilder.addTag(family.getAliasItemTag("armor"));
							}
						}

						salvageablesTagBuilder.addTag(armorTag);

						if(family.hasAlias()) {
							getOrCreateTagBuilder(family.getAliasItemTag("armor")).addTag(armorTag);
						}
					}

					if(family.hasAnyItemVariants(AMDatagen.TOOL_VARIANTS)) {
						FabricTagBuilder<Item> toolsTagBuilder = getOrCreateTagBuilder(toolsTag);
						AMDatagen.TOOL_VARIANTS.forEach((variant) -> {
							if(family.hasVariant(variant)) {
								toolsTagBuilder.addTag(family.getTag(variant));
							}
						});

						salvageablesTagBuilder.addTag(toolsTag);

						if(family.hasAlias()) {
							getOrCreateTagBuilder(family.getAliasItemTag("tools")).addTag(toolsTag);
						}
					}

					if(family.hasVariant(ItemVariant.HORSE_ARMOR)) {
						salvageablesTagBuilder.add(family.getVariant(ItemVariant.HORSE_ARMOR));
					}

					if(family.hasAlias()) {
						getOrCreateTagBuilder(family.getAliasItemTag("salvageables")).addTag(salvageablesTag);
					}
				}

				if(family.isValidForBeacon()) {
					beaconPaymentTagBuilder.addTag(family.getBaseTag());

					if(family.hasAlias()) {
						beaconPaymentTagBuilder.addTag(family.getAliasBaseTag());
					}
				}

				if(family.isPiglinLoved()) {
					piglinBarteringItemsTagBuilder.addTag(family.getBaseTag());

					if(family.hasAlias()) {
						piglinBarteringItemsTagBuilder.addTag(family.getAliasBaseTag());
					}
				}
			});

			AMBlockFamilies.getFamilies().forEachOrdered(family -> family.getVariants().forEach((variant, block) -> {
				if(VANILLA_ITEM_TAG_VARIANTS.containsKey(variant)) {
					getOrCreateTagBuilder(createItemTag(VANILLA_ITEM_TAG_VARIANTS.get(variant))).add(block.asItem());
				}
			}));

			AMDatagen.FLUIDS.forEach((fluid) -> {
				FabricTagBuilder<Item> bucketTagBuilder = getOrCreateTagBuilder(createCommonItemTag(Registry.FLUID.getId(fluid.getStill()).getPath()+"_buckets"));
				bucketTagBuilder.add(fluid.getBucketItem());
			});

			copy(createCommonBlockTag("ores"), createCommonItemTag("ores"));

			GENERIC_TAGS.forEach((variantSet, id) -> {
				FabricTagBuilder<Item> tag = getOrCreateTagBuilder(createItemTag(id));
				variantSet.forEach((variant) -> {
					if(variant.hasTag()) tag.addTag(variant.getTag());
				});
			});

			AMDatagen.TOOL_VARIANTS.forEach((variant) -> getOrCreateTagBuilder(createItemTag(new Identifier("fabric", variant.getTagPath()))).addTag(variant.getTag()));

			FabricTagBuilder<Item> drillsTagBuilder = getOrCreateTagBuilder(createItemTag(AMCommon.id("drills")));
			DRILLS.forEach(drillsTagBuilder::add);

			COPY.forEach((id -> copy(createBlockTag(id), createItemTag(id))));
		}
	}

	public static class AMFluidTagProvider extends FabricTagProvider.FluidTagProvider {

		public AMFluidTagProvider(FabricDataGenerator dataGenerator) {
			super(dataGenerator);
		}

		@Override
		protected void generateTags() {
			AMDatagen.FLUIDS.forEach((fluid) -> {
				FabricTagBuilder<Fluid> tagBuilder = getOrCreateTagBuilder(createCommonFluidTag(Registry.FLUID.getId(fluid.getStill()).getPath()));
				tagBuilder.add(fluid.getStill(), fluid.getFlowing());
			});
		}
	}
}
