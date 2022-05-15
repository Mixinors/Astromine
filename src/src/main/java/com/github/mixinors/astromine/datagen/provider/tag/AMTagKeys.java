package com.github.mixinors.astromine.datagen.provider.tag;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.registry.common.AMTags;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.Map;

public class AMTagKeys {
	public static final String COMMON_TAG_NAMESPACE = "c";
	
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
	
	public static final Identifier YELLOW_SANDSTONES_ID = AMTagKeys.createCommonTagId("yellow_sandstones");
	public static final Identifier RED_SANDSTONES_ID = AMTagKeys.createCommonTagId("red_sandstones");
	public static final Identifier SANDSTONES_ID = AMTagKeys.createCommonTagId("sandstones");
	public static final Identifier QUARTZ_BLOCKS_ID = AMTagKeys.createCommonTagId("quartz_blocks");
	public static final Identifier UNWAXED_COPPER_BLOCKS_ID = AMTagKeys.createCommonTagId("unwaxed_copper_blocks");
	public static final Identifier WAXED_COPPER_BLOCKS_ID = AMTagKeys.createCommonTagId("waxed_copper_blocks");
	public static final Identifier COPPER_BLOCKS_ID = AMTagKeys.createCommonTagId("copper_blocks");
	public static final Identifier UNWAXED_CUT_COPPER_ID = AMTagKeys.createCommonTagId("unwaxed_cut_copper");
	public static final Identifier WAXED_CUT_COPPER_ID = AMTagKeys.createCommonTagId("waxed_cut_copper");
	public static final Identifier CUT_COPPER_ID = AMTagKeys.createCommonTagId("cut_copper");
	public static final Identifier PURPUR_BLOCKS_ID = AMTagKeys.createCommonTagId("purpur_blocks");
	public static final Identifier MUSHROOMS_ID = AMTagKeys.createCommonTagId("mushrooms");
	public static final Identifier MUSHROOM_BLOCKS_ID = AMTagKeys.createCommonTagId("mushroom_blocks");
	public static final Identifier NETHER_FUNGI_ID = AMTagKeys.createCommonTagId("nether_fungi");
	public static final Identifier NETHER_ROOTS_ID = AMTagKeys.createCommonTagId("nether_roots");
	public static final Identifier NETHER_VINES_ID = AMTagKeys.createCommonTagId("nether_vines");
	public static final Identifier PUMPKINS_ID = AMTagKeys.createCommonTagId("pumpkins");
	public static final Identifier GOURDS_ID = AMTagKeys.createCommonTagId("gourds");
	public static final Identifier WEEPING_VINES_ID = AMTagKeys.createCommonTagId("weeping_vines");
	public static final Identifier TWISTING_VINES_ID = AMTagKeys.createCommonTagId("twisting_vines");
	public static final Identifier ORES_ID = AMTagKeys.createCommonTagId("ores");
	
	public static class Blocks {
		public static final TagKey<Block> YELLOW_SANDSTONES = AMTagKeys.createBlockTag(YELLOW_SANDSTONES_ID);
		public static final TagKey<Block> RED_SANDSTONES = AMTagKeys.createBlockTag(RED_SANDSTONES_ID);
		public static final TagKey<Block> SANDSTONES = AMTagKeys.createBlockTag(SANDSTONES_ID);
		public static final TagKey<Block> QUARTZ_BLOCKS = AMTagKeys.createBlockTag(QUARTZ_BLOCKS_ID);
		public static final TagKey<Block> UNWAXED_COPPER_BLOCKS = AMTagKeys.createBlockTag(UNWAXED_COPPER_BLOCKS_ID);
		public static final TagKey<Block> WAXED_COPPER_BLOCKS = AMTagKeys.createBlockTag(WAXED_COPPER_BLOCKS_ID);
		public static final TagKey<Block> COPPER_BLOCKS = AMTagKeys.createBlockTag(COPPER_BLOCKS_ID);
		public static final TagKey<Block> UNWAXED_CUT_COPPER = AMTagKeys.createBlockTag(UNWAXED_CUT_COPPER_ID);
		public static final TagKey<Block> WAXED_CUT_COPPER = AMTagKeys.createBlockTag(WAXED_CUT_COPPER_ID);
		public static final TagKey<Block> CUT_COPPER = AMTagKeys.createBlockTag(CUT_COPPER_ID);
		public static final TagKey<Block> PURPUR_BLOCKS = AMTagKeys.createBlockTag(PURPUR_BLOCKS_ID);
		public static final TagKey<Block> MUSHROOMS = AMTagKeys.createBlockTag(MUSHROOMS_ID);
		public static final TagKey<Block> MUSHROOM_BLOCKS = AMTagKeys.createBlockTag(MUSHROOM_BLOCKS_ID);
		public static final TagKey<Block> NETHER_FUNGI = AMTagKeys.createBlockTag(NETHER_FUNGI_ID);
		public static final TagKey<Block> NETHER_ROOTS = AMTagKeys.createBlockTag(NETHER_ROOTS_ID);
		public static final TagKey<Block> NETHER_VINES = AMTagKeys.createBlockTag(NETHER_VINES_ID);
		public static final TagKey<Block> PUMPKINS = AMTagKeys.createBlockTag(PUMPKINS_ID);
		public static final TagKey<Block> GOURDS = AMTagKeys.createBlockTag(GOURDS_ID);
		public static final TagKey<Block> WEEPING_VINES = AMTagKeys.createBlockTag(WEEPING_VINES_ID);
		public static final TagKey<Block> TWISTING_VINES = AMTagKeys.createBlockTag(TWISTING_VINES_ID);
		public static final TagKey<Block> ORES = AMTagKeys.createBlockTag(ORES_ID);
	}
	
	public static class Items {
		public static final TagKey<Item> YELLOW_SANDSTONES = AMTagKeys.createItemTag(YELLOW_SANDSTONES_ID);
		public static final TagKey<Item> RED_SANDSTONES = AMTagKeys.createItemTag(RED_SANDSTONES_ID);
		public static final TagKey<Item> SANDSTONES = AMTagKeys.createItemTag(SANDSTONES_ID);
		public static final TagKey<Item> QUARTZ_BLOCKS = AMTagKeys.createItemTag(QUARTZ_BLOCKS_ID);
		public static final TagKey<Item> UNWAXED_COPPER_BLOCKS = AMTagKeys.createItemTag(UNWAXED_COPPER_BLOCKS_ID);
		public static final TagKey<Item> WAXED_COPPER_BLOCKS = AMTagKeys.createItemTag(WAXED_COPPER_BLOCKS_ID);
		public static final TagKey<Item> COPPER_BLOCKS = AMTagKeys.createItemTag(COPPER_BLOCKS_ID);
		public static final TagKey<Item> UNWAXED_CUT_COPPER = AMTagKeys.createItemTag(UNWAXED_CUT_COPPER_ID);
		public static final TagKey<Item> WAXED_CUT_COPPER = AMTagKeys.createItemTag(WAXED_CUT_COPPER_ID);
		public static final TagKey<Item> CUT_COPPER = AMTagKeys.createItemTag(CUT_COPPER_ID);
		public static final TagKey<Item> PURPUR_BLOCKS = AMTagKeys.createItemTag(PURPUR_BLOCKS_ID);
		public static final TagKey<Item> MUSHROOMS = AMTagKeys.createItemTag(MUSHROOMS_ID);
		public static final TagKey<Item> MUSHROOM_BLOCKS = AMTagKeys.createItemTag(MUSHROOM_BLOCKS_ID);
		public static final TagKey<Item> NETHER_FUNGI = AMTagKeys.createItemTag(NETHER_FUNGI_ID);
		public static final TagKey<Item> NETHER_ROOTS = AMTagKeys.createItemTag(NETHER_ROOTS_ID);
		public static final TagKey<Item> NETHER_VINES = AMTagKeys.createItemTag(NETHER_VINES_ID);
		public static final TagKey<Item> PUMPKINS = AMTagKeys.createItemTag(PUMPKINS_ID);
		public static final TagKey<Item> GOURDS = AMTagKeys.createItemTag(GOURDS_ID);
		public static final TagKey<Item> WEEPING_VINES = AMTagKeys.createItemTag(WEEPING_VINES_ID);
		public static final TagKey<Item> TWISTING_VINES = AMTagKeys.createItemTag(TWISTING_VINES_ID);
		public static final TagKey<Item> ORES = AMTagKeys.createItemTag(ORES_ID);
		
		public static final TagKey<Item> MAKES_ONE_BIOFUEL = createItemTag("makes_one_biofuel");
		public static final TagKey<Item> MAKES_TWO_BIOFUEL = createItemTag("makes_two_biofuel");
		public static final TagKey<Item> MAKES_FOUR_BIOFUEL = createItemTag("makes_four_biofuel");
		public static final TagKey<Item> MAKES_NINE_BIOFUEL = createItemTag("makes_nine_biofuel");
		
		public static final TagKey<Item> BIOFUEL = createCommonItemTag("biofuel");
	}
	
	public static Identifier createCommonTagId(String path) {
		return new Identifier(COMMON_TAG_NAMESPACE, path);
	}
	
	public static TagKey<Block> createBlockTag(Identifier id) {
		return AMTags.ofBlock(id);
	}
	
	public static TagKey<Item> createItemTag(Identifier id) {
		return AMTags.ofItem(id);
	}
	
	public static TagKey<Fluid> createFluidTag(Identifier id) {
		return AMTags.ofFluid(id);
	}
	
	public static TagKey<Block> createCommonBlockTag(String path) {
		return createBlockTag(createCommonTagId(path));
	}
	
	public static TagKey<Item> createCommonItemTag(String path) {
		return createItemTag(createCommonTagId(path));
	}
	
	public static TagKey<Fluid> createCommonFluidTag(String path) {
		return createFluidTag(createCommonTagId(path));
	}
	
	public static TagKey<Block> createBlockTag(String path) {
		return createBlockTag(AMCommon.id(path));
	}
	
	public static TagKey<Item> createItemTag(String path) {
		return createItemTag(AMCommon.id(path));
	}
	
	public static TagKey<Fluid> createFluidTag(String path) {
		return createFluidTag(AMCommon.id(path));
	}
}
