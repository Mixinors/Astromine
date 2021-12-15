package com.github.mixinors.astromine.datagen.family.block;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.util.WordUtils;
import com.github.mixinors.astromine.registry.common.AMBlocks;

import net.minecraft.data.family.BlockFamilies;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.util.registry.Registry;

public class AMBlockFamilies {
	public static final BlockFamily ASTEROID_STONE = BlockFamilies.register(AMBlocks.ASTEROID_STONE.get()).slab(AMBlocks.ASTEROID_STONE_SLAB.get()).stairs(AMBlocks.ASTEROID_STONE_STAIRS.get()).wall(AMBlocks.ASTEROID_STONE_WALL.get()).polished(AMBlocks.POLISHED_ASTEROID_STONE.get()).build();
	public static final BlockFamily SMOOTH_ASTEROID_STONE = BlockFamilies.register(AMBlocks.SMOOTH_ASTEROID_STONE.get()).slab(AMBlocks.SMOOTH_ASTEROID_STONE_SLAB.get()).stairs(AMBlocks.SMOOTH_ASTEROID_STONE_STAIRS.get()).wall(AMBlocks.SMOOTH_ASTEROID_STONE_WALL.get()).build();
	public static final BlockFamily POLISHED_ASTEROID_STONE = BlockFamilies.register(AMBlocks.POLISHED_ASTEROID_STONE.get()).slab(AMBlocks.POLISHED_ASTEROID_STONE_SLAB.get()).stairs(AMBlocks.POLISHED_ASTEROID_STONE_STAIRS.get()).build();
	public static final BlockFamily ASTEROID_STONE_BRICK = BlockFamilies.register(AMBlocks.ASTEROID_STONE_BRICKS.get()).slab(AMBlocks.ASTEROID_STONE_BRICK_SLAB.get()).stairs(AMBlocks.ASTEROID_STONE_BRICK_STAIRS.get()).wall(AMBlocks.ASTEROID_STONE_BRICK_WALL.get()).build();
	public static final BlockFamily METEOR_STONE = BlockFamilies.register(AMBlocks.METEOR_STONE.get()).slab(AMBlocks.METEOR_STONE_SLAB.get()).stairs(AMBlocks.METEOR_STONE_STAIRS.get()).wall(AMBlocks.METEOR_STONE_WALL.get()).polished(AMBlocks.POLISHED_METEOR_STONE.get()).build();
	public static final BlockFamily SMOOTH_METEOR_STONE = BlockFamilies.register(AMBlocks.SMOOTH_METEOR_STONE.get()).slab(AMBlocks.SMOOTH_METEOR_STONE_SLAB.get()).stairs(AMBlocks.SMOOTH_METEOR_STONE_STAIRS.get()).wall(AMBlocks.SMOOTH_METEOR_STONE_WALL.get()).build();
	public static final BlockFamily POLISHED_METEOR_STONE = BlockFamilies.register(AMBlocks.POLISHED_METEOR_STONE.get()).slab(AMBlocks.POLISHED_METEOR_STONE_SLAB.get()).stairs(AMBlocks.POLISHED_METEOR_STONE_STAIRS.get()).build();
	public static final BlockFamily METEOR_STONE_BRICK = BlockFamilies.register(AMBlocks.METEOR_STONE_BRICKS.get()).slab(AMBlocks.METEOR_STONE_BRICK_SLAB.get()).stairs(AMBlocks.METEOR_STONE_BRICK_STAIRS.get()).wall(AMBlocks.METEOR_STONE_BRICK_WALL.get()).build();

	public static void init() {

	}

	public static boolean isAstromineFamily(BlockFamily family) {
		return Registry.BLOCK.getId(family.getBaseBlock()).getNamespace().equals(AMCommon.MOD_ID);
	}

	public static boolean hasItemTag(BlockFamily.Variant variant) {
		return switch(variant) {
			case BUTTON, DOOR, FENCE, SIGN, SLAB, STAIRS, TRAPDOOR, WALL -> true;
			default -> false;
		};
	}

	public static boolean hasBlockTag(BlockFamily.Variant variant) {
		return switch(variant) {
			case BUTTON, DOOR, FENCE, FENCE_GATE, PRESSURE_PLATE, SIGN, SLAB, STAIRS, TRAPDOOR, WALL, WALL_SIGN -> true;
			default -> false;
		};
	}

	public static String getTagPath(BlockFamily.Variant variant) {
		return switch(variant) {
			case STAIRS -> variant.getName();
			default -> WordUtils.pluralize(variant.getName());
		};
	}
}
