package com.github.mixinors.astromine.datagen.provider;

import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.fluid.ExtendedFluid;
import com.github.mixinors.astromine.datagen.family.AMBlockFamilies;
import com.github.mixinors.astromine.datagen.family.MaterialFamilies;
import com.github.mixinors.astromine.datagen.family.MaterialFamily;
import com.github.mixinors.astromine.datagen.family.MaterialFamily.BlockVariant;
import com.github.mixinors.astromine.datagen.family.MaterialFamily.ItemVariant;
import com.github.mixinors.astromine.datagen.family.MaterialFamily.MaterialType;
import com.github.mixinors.astromine.registry.common.AMFluids;
import org.apache.commons.lang3.function.TriFunction;

import net.minecraft.block.Block;
import net.minecraft.data.family.BlockFamilies;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.FabricTagBuilder;
import net.fabricmc.fabric.api.tag.TagFactory;

public class AMTagProviders {

	public static class AMBlockTagProvider extends FabricTagProvider.BlockTagProvider {
		public static Function<BlockVariant, MaterialFamilyTagAdder<Block>> BLOCK_TAG_ADDER = (variant) -> (tagBuilder, family) -> tagBuilder.addTag(family.getTag(variant));
		public static MaterialFamilyTagAdder<Block> FAMILY_BLOCK_TAG_ADDER = (tagBuilder, family) -> {
			family.getBlockTags().values().forEach(tagBuilder::addTag);
			return tagBuilder;
		};
		public static Function<BlockVariant, BasicTagAdder<Block>> BLOCK_VARIANT_TAG_ADDER = variant -> tagBuilder -> tagBuilder.addTag(variant.getTag());
		public static Function<BlockFamily.Variant, BlockFamilyTagAdder<Block>> BLOCK_FAMILY_TAG_ADDER = variant -> (tagBuilder, family) -> tagBuilder.add(family.getVariant(variant));

		public static Set<BlockVariant> ORES = Set.of(
				BlockVariant.ORE,
				BlockVariant.DEEPSLATE_ORE,
				BlockVariant.NETHER_ORE,
				BlockVariant.METEOR_ORE,
				BlockVariant.ASTEROID_ORE
		);
		public static Set<BlockVariant> BLOCKS = Set.of(
				BlockVariant.BLOCK,
				BlockVariant.BLOCK_2x2
		);
		public static Set<BlockFamily.Variant> VANILLA_BLOCK_TAGS = Set.of(
				BlockFamily.Variant.SLAB,
				BlockFamily.Variant.STAIRS,
				BlockFamily.Variant.WALL,
				BlockFamily.Variant.BUTTON,
				BlockFamily.Variant.DOOR,
				BlockFamily.Variant.FENCE,
				BlockFamily.Variant.SIGN,
				BlockFamily.Variant.TRAPDOOR,
				BlockFamily.Variant.FENCE_GATE,
				BlockFamily.Variant.PRESSURE_PLATE,
				BlockFamily.Variant.WALL_SIGN
		);

		public final FabricTagBuilder<Block> beaconBaseBlocks = getOrCreateTagBuilder(new Identifier("beacon_base_blocks"));

		public final Function<String, MaterialFamilyTagBuilder<Block>> getTagId = variant -> family -> getOrCreateTagBuilder(family.getTagId(variant));
		public final MaterialFamilyTagBuilder<Block> ores = getTagId.apply("ores");

		public FabricTagBuilder<Block> getOrCreateTagBuilder(Identifier id) {
			return getOrCreateTagBuilder(TagFactory.BLOCK.create(id));
		}

		public AMBlockTagProvider(FabricDataGenerator dataGenerator) {
			super(dataGenerator);
		}

		@Override
		protected void generateTags() {
			MaterialFamilies.getFamilies().filter(MaterialFamily::shouldGenerateTags).forEach((family) -> {
				family.getBlockVariants().forEach(((variant, block) -> {
					getOrCreateTagBuilder(family.getTag(variant)).add(block);
					FabricTagBuilder<Block> variantTag = getOrCreateTagBuilder(variant.getTag());
					BLOCK_TAG_ADDER.apply(variant).apply(variantTag, family);
				}));
				ORES.forEach((variant) -> {
					if(family.hasVariant(variant))	{
						BLOCK_TAG_ADDER.apply(variant).apply(ores.apply(family), family);
					}
				});
				BLOCKS.forEach((variant) -> {
					if(family.isValidForBeacon() && family.hasVariant(variant)) {
						beaconBaseBlocks.addTag(family.getTag(variant));
					}
				});
			});
			BlockFamilies.getFamilies().filter(AMBlockFamilies::isAstromineFamily).forEach(family -> {
				VANILLA_BLOCK_TAGS.forEach((variant) -> {
					if(family.getVariants().containsKey(variant)) {
						FabricTagBuilder<Block> vanillaTag = getOrCreateTagBuilder(TagFactory.BLOCK.create(new Identifier(AMBlockFamilies.getTagPath(variant))));
						BLOCK_FAMILY_TAG_ADDER.apply(variant).apply(vanillaTag, family);
					}
				});
			});
		}
	}

	public static class AMItemTagProvider extends FabricTagProvider.ItemTagProvider {
		public static Function<ItemVariant, MaterialFamilyTagAdder<Item>> ITEM_TAG_ADDER = variant -> (tagBuilder, family) -> tagBuilder.addTag(family.getTag(variant));
		public static Function<BlockVariant, MaterialFamilyTagAdder<Item>> BLOCK_ITEM_TAG_ADDER = variant -> (tagBuilder, family) -> tagBuilder.addTag(family.getItemTag(variant));
		public static MaterialFamilyTagAdder<Item> FAMILY_ITEM_TAG_ADDER = (tagBuilder, family) -> {
			family.getAllItemTags().forEach((variant, tag) -> tagBuilder.addTag(tag));
			return tagBuilder;
		};
		public static Function<ItemVariant, BasicTagAdder<Item>> ITEM_VARIANT_TAG_ADDER = variant -> tagBuilder -> {
			if(variant.hasVariantTag()) {
				tagBuilder.addTag(variant.getTag());
			}
			return tagBuilder;
		};
		public static Function<BlockVariant, BasicTagAdder<Item>> BLOCK_ITEM_VARIANT_TAG_ADDER = variant -> tagBuilder -> tagBuilder.addTag(variant.getItemTag());
		public static Function<BlockFamily.Variant, BlockFamilyTagAdder<Item>> BLOCK_FAMILY_TAG_ADDER = variant -> (tagBuilder, family) -> tagBuilder.add(family.getVariant(variant).asItem());

		public static Set<BlockVariant> ORES = Set.of(
				BlockVariant.ORE,
				BlockVariant.DEEPSLATE_ORE,
				BlockVariant.NETHER_ORE,
				BlockVariant.METEOR_ORE,
				BlockVariant.ASTEROID_ORE
		);
		public static Set<ItemVariant> CLUSTERS = Set.of(
				ItemVariant.METEOR_CLUSTER,
				ItemVariant.ASTEROID_CLUSTER
		);
		public static Set<ItemVariant> ARMOR = Set.of(
				ItemVariant.HELMET,
				ItemVariant.CHESTPLATE,
				ItemVariant.LEGGINGS,
				ItemVariant.BOOTS
		);
		public static Set<ItemVariant> TOOLS = Set.of(
				ItemVariant.PICKAXE,
				ItemVariant.AXE,
				ItemVariant.SHOVEL,
				ItemVariant.SWORD,
				ItemVariant.HOE
		);
		public static Set<ItemVariant> SALVAGEABLES = Set.of(
				ItemVariant.HELMET,
				ItemVariant.CHESTPLATE,
				ItemVariant.LEGGINGS,
				ItemVariant.BOOTS,
				ItemVariant.PICKAXE,
				ItemVariant.AXE,
				ItemVariant.SHOVEL,
				ItemVariant.SWORD,
				ItemVariant.HOE,
				ItemVariant.HORSE_ARMOR
		);
		public static Set<BlockFamily.Variant> VANILLA_ITEM_TAGS = Set.of(
				BlockFamily.Variant.SLAB,
				BlockFamily.Variant.STAIRS,
				BlockFamily.Variant.WALL,
				BlockFamily.Variant.BUTTON,
				BlockFamily.Variant.DOOR,
				BlockFamily.Variant.FENCE,
				BlockFamily.Variant.SIGN,
				BlockFamily.Variant.TRAPDOOR
		);

		public AMItemTagProvider(FabricDataGenerator dataGenerator) {
			super(dataGenerator);
		}

		@Override
		protected void generateTags() {
			MaterialFamilies.getFamilies().filter(MaterialFamily::shouldGenerateTags).forEach((family) -> {
				FabricTagBuilder<Item> piglinLoved = getOrCreateTagBuilder(TagFactory.ITEM.create(new Identifier("piglin_loved")));
				family.getItemVariants().forEach(((variant, item) -> {
					Tag.Identified<Item> tag = family.getTag(variant);
					getOrCreateTagBuilder(tag).add(item);
					if(family.isPiglinLoved()) {
						if(variant.equals(ItemVariant.NUGGET)) getOrCreateTagBuilder(TagFactory.ITEM.create(new Identifier("piglib", "piglin_loved_nuggets")));
						else piglinLoved.addTag(tag);
					}
					if(variant.hasVariantTag()) {
						FabricTagBuilder<Item> variantTag = getOrCreateTagBuilder(variant.getTag());
						ITEM_TAG_ADDER.apply(variant).apply(variantTag, family);
					}
				}));
				family.getBlockVariants().forEach(((variant, block) -> {
					Tag.Identified<Item> tag = family.getItemTag(variant);
					getOrCreateTagBuilder(tag).add(block.asItem());
					if(family.isPiglinLoved()) {
						piglinLoved.addTag(tag);
					}
					FabricTagBuilder<Item> variantTag = getOrCreateTagBuilder(variant.getItemTag());
					BLOCK_ITEM_TAG_ADDER.apply(variant).apply(variantTag, family);
				}));
				FabricTagBuilder<Item> ores = getOrCreateTagBuilder(TagFactory.ITEM.create(family.getTagId("ores")));
				ORES.forEach((variant) -> {
					if(family.hasVariant(variant))	{
						BLOCK_ITEM_TAG_ADDER.apply(variant).apply(ores, family);
					}
				});
				FabricTagBuilder<Item> fragments = getOrCreateTagBuilder(TagFactory.ITEM.create(family.getTagId("fragments")));
				if(family.hasVariant(ItemVariant.NUGGET) && family.getType().equals(MaterialType.GEM))	{
					ITEM_TAG_ADDER.apply(ItemVariant.NUGGET).apply(fragments, family);
				}
				FabricTagBuilder<Item> clusters = getOrCreateTagBuilder(TagFactory.ITEM.create(family.getTagId("clusters")));
				CLUSTERS.forEach((variant) -> {
					if(family.hasVariant(variant))	{
						ITEM_TAG_ADDER.apply(variant).apply(clusters, family);
					}
				});
				FabricTagBuilder<Item> salvageables = getOrCreateTagBuilder(TagFactory.ITEM.create(family.getTagId("salvageables")));
				SALVAGEABLES.forEach((variant) -> {
					if(family.hasVariant(variant))	{
						ITEM_TAG_ADDER.apply(variant).apply(salvageables, family);
					}
				});
				if(family.isPiglinLoved()) {
					FabricTagBuilder<Item> piglinSafeArmor = getOrCreateTagBuilder(TagFactory.ITEM.create(new Identifier("piglib", "piglin_safe_armor")));
					ARMOR.forEach((variant) -> {
						if(family.hasVariant(variant))	{
							ITEM_TAG_ADDER.apply(variant).apply(piglinSafeArmor, family);
						}
					});
					FabricTagBuilder<Item> piglinBarteringItems = getOrCreateTagBuilder(TagFactory.ITEM.create(new Identifier("piglib", "piglin_bartering_items")));
					ITEM_TAG_ADDER.apply(family.getBaseVariant()).apply(piglinBarteringItems, family);
				}
				if(family.isValidForBeacon()) {
					getOrCreateTagBuilder(TagFactory.ITEM.create(new Identifier("beacon_payment_items"))).addTag(family.getBaseTag());
				}
			});
			FabricTagBuilder<Item> tricksPiglins = getOrCreateTagBuilder(TagFactory.ITEM.create(AMCommon.id("tricks_piglins")));
			FAMILY_ITEM_TAG_ADDER.apply(tricksPiglins, MaterialFamilies.FOOLS_GOLD);
			TOOLS.forEach((variant) -> {
				FabricTagBuilder<Item> fabricToolTag = getOrCreateTagBuilder(TagFactory.ITEM.create(new Identifier("fabric", variant.getTagPath())));
				ITEM_VARIANT_TAG_ADDER.apply(variant).apply(fabricToolTag);
			});
			BlockFamilies.getFamilies().filter(AMBlockFamilies::isAstromineFamily).forEach(family -> {
				VANILLA_ITEM_TAGS.forEach((variant) -> {
					if(family.getVariants().containsKey(variant)) {
						FabricTagBuilder<Item> vanillaTag = getOrCreateTagBuilder(TagFactory.ITEM.create(new Identifier(AMBlockFamilies.getTagPath(variant))));
						BLOCK_FAMILY_TAG_ADDER.apply(variant).apply(vanillaTag, family);
					}
				});
			});
		}
	}

	public static class AMFluidTagProvider extends FabricTagProvider.FluidTagProvider {
		public static final Set<ExtendedFluid> FLUIDS = Set.of(
				AMFluids.OIL,
				AMFluids.FUEL,
				AMFluids.BIOMASS,
				AMFluids.OXYGEN,
				AMFluids.HYDROGEN
		);

		public AMFluidTagProvider(FabricDataGenerator dataGenerator) {
			super(dataGenerator);
		}

		@Override
		protected void generateTags() {
			FLUIDS.forEach((fluid) -> {
				FabricTagBuilder<Fluid> tag = getOrCreateTagBuilder(TagFactory.FLUID.create(new Identifier("c", Registry.FLUID.getId(fluid.getStill()).getPath())));
				tag.add(fluid.getStill(), fluid.getFlowing());
			});
		}
	}

	@FunctionalInterface
	public interface MaterialFamilyTagAdder<T> extends BiFunction<FabricTagBuilder<T>, MaterialFamily, FabricTagBuilder<T>> { }

	@FunctionalInterface
	public interface BlockFamilyTagAdder<T> extends BiFunction<FabricTagBuilder<T>, BlockFamily, FabricTagBuilder<T>> { }

	@FunctionalInterface
	public interface BasicTagAdder<T> extends Function<FabricTagBuilder<T>, FabricTagBuilder<T>> { }

	@FunctionalInterface
	public interface MaterialFamilyVariantTagBuilder<T extends ItemConvertible> extends BiFunction<MaterialFamily, MaterialFamily.Variant<T>, FabricTagBuilder<T>> {}

	@FunctionalInterface
	public interface MaterialFamilyTagBuilder<T extends ItemConvertible> extends Function<MaterialFamily, FabricTagBuilder<T>> {}
}
