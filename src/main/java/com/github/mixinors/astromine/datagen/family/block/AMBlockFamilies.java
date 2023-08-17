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

package com.github.mixinors.astromine.datagen.family.block;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.registry.common.AMBlocks;
import net.minecraft.data.family.BlockFamilies;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.registry.Registries;

import java.util.Comparator;
import java.util.stream.Stream;

public class AMBlockFamilies {
	public static final BlockFamily ASTEROID_STONE = BlockFamilies.register(AMBlocks.ASTEROID_STONE.get())
																  .slab(AMBlocks.ASTEROID_STONE_SLAB.get())
																  .stairs(AMBlocks.ASTEROID_STONE_STAIRS.get())
																  .wall(AMBlocks.ASTEROID_STONE_WALL.get())
																  .polished(AMBlocks.POLISHED_ASTEROID_STONE.get())
																  .build();
	
	public static final BlockFamily SMOOTH_ASTEROID_STONE = BlockFamilies.register(AMBlocks.SMOOTH_ASTEROID_STONE.get())
																		 .slab(AMBlocks.SMOOTH_ASTEROID_STONE_SLAB.get())
																		 .stairs(AMBlocks.SMOOTH_ASTEROID_STONE_STAIRS.get())
																		 .wall(AMBlocks.SMOOTH_ASTEROID_STONE_WALL.get())
																		 .build();
	
	public static final BlockFamily POLISHED_ASTEROID_STONE = BlockFamilies.register(AMBlocks.POLISHED_ASTEROID_STONE.get())
																		   .slab(AMBlocks.POLISHED_ASTEROID_STONE_SLAB.get())
																		   .stairs(AMBlocks.POLISHED_ASTEROID_STONE_STAIRS.get())
																		   .build();
	
	public static final BlockFamily ASTEROID_STONE_BRICK = BlockFamilies.register(AMBlocks.ASTEROID_STONE_BRICKS.get())
																		.slab(AMBlocks.ASTEROID_STONE_BRICK_SLAB.get())
																		.stairs(AMBlocks.ASTEROID_STONE_BRICK_STAIRS.get())
																		.wall(AMBlocks.ASTEROID_STONE_BRICK_WALL.get())
																		.build();
	
	public static final BlockFamily METEOR_STONE = BlockFamilies.register(AMBlocks.METEOR_STONE.get())
																.slab(AMBlocks.METEOR_STONE_SLAB.get())
																.stairs(AMBlocks.METEOR_STONE_STAIRS.get())
																.wall(AMBlocks.METEOR_STONE_WALL.get())
																.polished(AMBlocks.POLISHED_METEOR_STONE.get())
																.build();
	
	public static final BlockFamily SMOOTH_METEOR_STONE = BlockFamilies.register(AMBlocks.SMOOTH_METEOR_STONE.get())
																	   .slab(AMBlocks.SMOOTH_METEOR_STONE_SLAB.get())
																	   .stairs(AMBlocks.SMOOTH_METEOR_STONE_STAIRS.get())
																	   .wall(AMBlocks.SMOOTH_METEOR_STONE_WALL.get())
																	   .build();
	
	public static final BlockFamily POLISHED_METEOR_STONE = BlockFamilies.register(AMBlocks.POLISHED_METEOR_STONE.get())
																		 .slab(AMBlocks.POLISHED_METEOR_STONE_SLAB.get())
																		 .stairs(AMBlocks.POLISHED_METEOR_STONE_STAIRS.get())
																		 .build();
	
	public static final BlockFamily METEOR_STONE_BRICK = BlockFamilies.register(AMBlocks.METEOR_STONE_BRICKS.get())
																	  .slab(AMBlocks.METEOR_STONE_BRICK_SLAB.get())
																	  .stairs(AMBlocks.METEOR_STONE_BRICK_STAIRS.get())
																	  .wall(AMBlocks.METEOR_STONE_BRICK_WALL.get())
																	  .build();

	public static final BlockFamily MOON_STONE = BlockFamilies.register(AMBlocks.MOON_STONE.get())
			.slab(AMBlocks.MOON_STONE_SLAB.get())
			.stairs(AMBlocks.MOON_STONE_STAIRS.get())
			.wall(AMBlocks.MOON_STONE_WALL.get())
			.polished(AMBlocks.POLISHED_MOON_STONE.get())
			.build();

	public static final BlockFamily SMOOTH_MOON_STONE = BlockFamilies.register(AMBlocks.SMOOTH_MOON_STONE.get())
			.slab(AMBlocks.SMOOTH_MOON_STONE_SLAB.get())
			.stairs(AMBlocks.SMOOTH_MOON_STONE_STAIRS.get())
			.wall(AMBlocks.SMOOTH_MOON_STONE_WALL.get())
			.build();

	public static final BlockFamily POLISHED_MOON_STONE = BlockFamilies.register(AMBlocks.POLISHED_MOON_STONE.get())
			.slab(AMBlocks.POLISHED_MOON_STONE_SLAB.get())
			.stairs(AMBlocks.POLISHED_MOON_STONE_STAIRS.get())
			.build();

	public static final BlockFamily MOON_STONE_BRICK = BlockFamilies.register(AMBlocks.MOON_STONE_BRICKS.get())
			.slab(AMBlocks.MOON_STONE_BRICK_SLAB.get())
			.stairs(AMBlocks.MOON_STONE_BRICK_STAIRS.get())
			.wall(AMBlocks.MOON_STONE_BRICK_WALL.get())
			.build();

	public static final BlockFamily DARK_MOON_STONE = BlockFamilies.register(AMBlocks.DARK_MOON_STONE.get())
			.slab(AMBlocks.DARK_MOON_STONE_SLAB.get())
			.stairs(AMBlocks.DARK_MOON_STONE_STAIRS.get())
			.wall(AMBlocks.DARK_MOON_STONE_WALL.get())
			.polished(AMBlocks.POLISHED_DARK_MOON_STONE.get())
			.build();

	public static final BlockFamily SMOOTH_DARK_MOON_STONE = BlockFamilies.register(AMBlocks.SMOOTH_DARK_MOON_STONE.get())
			.slab(AMBlocks.SMOOTH_DARK_MOON_STONE_SLAB.get())
			.stairs(AMBlocks.SMOOTH_DARK_MOON_STONE_STAIRS.get())
			.wall(AMBlocks.SMOOTH_DARK_MOON_STONE_WALL.get())
			.build();

	public static final BlockFamily POLISHED_DARK_MOON_STONE = BlockFamilies.register(AMBlocks.POLISHED_DARK_MOON_STONE.get())
			.slab(AMBlocks.POLISHED_DARK_MOON_STONE_SLAB.get())
			.stairs(AMBlocks.POLISHED_DARK_MOON_STONE_STAIRS.get())
			.build();

	public static final BlockFamily DARK_MOON_STONE_BRICK = BlockFamilies.register(AMBlocks.DARK_MOON_STONE_BRICKS.get())
			.slab(AMBlocks.DARK_MOON_STONE_BRICK_SLAB.get())
			.stairs(AMBlocks.DARK_MOON_STONE_BRICK_STAIRS.get())
			.wall(AMBlocks.DARK_MOON_STONE_BRICK_WALL.get())
			.build();
	
	public static void init() {
	
	}
	
	public static boolean isAstromineFamily(BlockFamily family) {
		return Registries.BLOCK.getId(family.getBaseBlock()).getNamespace().equals(AMCommon.MOD_ID);
	}
	
	public static Stream<BlockFamily> getFamilies() {
		return BlockFamilies.getFamilies()
							.filter(AMBlockFamilies::isAstromineFamily)
							.sorted(Comparator.comparing(family -> Registries.BLOCK.getId(family.getBaseBlock()).toString()));
	}
}
