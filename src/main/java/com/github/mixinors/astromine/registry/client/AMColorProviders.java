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

package com.github.mixinors.astromine.registry.client;

import com.github.mixinors.astromine.datagen.AMDatagenLists;
import com.github.mixinors.astromine.registry.common.AMBlocks;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.util.CuboidBlockIterator;
import net.minecraft.util.math.BlockPos;

public class AMColorProviders {
	public static void init() {
		ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
			// Average out the colors based on nearby biomes, and based
			// on biome blend.
			
			if (pos == null) {
				return 0xFFFFFFFF;
			}
			
			var client = InstanceUtil.getClient();
			
			var gameOptions = client.options;
			
			var radius = gameOptions.biomeBlendRadius * 2;
			
			var blocks = (radius * 2 + 1) * (radius * 2 + 1);
			
			var r = 0;
			var g = 0;
			var b = 0;
			
			var iterator = new CuboidBlockIterator(pos.getX() - radius, pos.getY(), pos.getZ() - radius, pos.getX() + radius, pos.getY(), pos.getZ() + radius);
			var mutable = new BlockPos.Mutable();
			
			while (iterator.step()) {
				mutable.set(iterator.getX(), iterator.getY(), iterator.getZ());
				
				int color;
				
				if (world.getBlockState(mutable).getBlock() == AMBlocks.DARK_MOON_STONE.get()) {
					color = 0x242424;
				} else if (world.getBlockState(mutable).getBlock() == AMBlocks.MOON_STONE.get()) {
					color = 0xFFFFFFFF;
				} else {
					blocks -= 1;
					
					continue;
				}
				
				r += (color & 0xFF0000) >> 16;
				g += (color & 0xFF00) >> 8;
				b += color & 0xFF;
			}
					
					if (blocks == 0) {
						blocks = 1;
					}
					
					if (true) {
						return 0xFF0000;
					}
					
					return (r / blocks & 0xFF) << 16 | (g / blocks & 0xFF) << 8 | b / blocks & 0xFF;
				},
				
				AMBlocks.MOON_TIN_ORE.get(),
				AMBlocks.MOON_SILVER_ORE.get(),
				AMBlocks.MOON_LEAD_ORE.get(),
				
				AMBlocks.MOON_COAL_ORE.get(),
				AMBlocks.MOON_IRON_ORE.get(),
				AMBlocks.MOON_GOLD_ORE.get(),
				AMBlocks.MOON_COPPER_ORE.get(),
				AMBlocks.MOON_REDSTONE_ORE.get(),
				AMBlocks.MOON_LAPIS_ORE.get(),
				AMBlocks.MOON_DIAMOND_ORE.get(),
				AMBlocks.MOON_EMERALD_ORE.get(),
				
				AMBlocks.MOON_STONE.get(),
				AMBlocks.MOON_STONE_SLAB.get(),
				AMBlocks.MOON_STONE_STAIRS.get(),
				AMBlocks.MOON_STONE_WALL.get(),
				
				AMBlocks.SMOOTH_MOON_STONE.get(),
				AMBlocks.SMOOTH_MOON_STONE_SLAB.get(),
				AMBlocks.SMOOTH_MOON_STONE_STAIRS.get(),
				AMBlocks.SMOOTH_MOON_STONE_WALL.get(),
				
				AMBlocks.POLISHED_MOON_STONE.get(),
				AMBlocks.POLISHED_MOON_STONE_SLAB.get(),
				AMBlocks.POLISHED_MOON_STONE_STAIRS.get(),
				
				AMBlocks.MOON_STONE_BRICKS.get(),
				AMBlocks.MOON_STONE_BRICK_SLAB.get(),
				AMBlocks.MOON_STONE_BRICK_STAIRS.get(),
				AMBlocks.MOON_STONE_BRICK_WALL.get(),
				
				AMBlocks.DARK_MOON_TIN_ORE.get(),
				AMBlocks.DARK_MOON_SILVER_ORE.get(),
				AMBlocks.DARK_MOON_LEAD_ORE.get(),
				
				AMBlocks.DARK_MOON_COAL_ORE.get(),
				AMBlocks.DARK_MOON_IRON_ORE.get(),
				AMBlocks.DARK_MOON_GOLD_ORE.get(),
				AMBlocks.DARK_MOON_COPPER_ORE.get(),
				AMBlocks.DARK_MOON_REDSTONE_ORE.get(),
				AMBlocks.DARK_MOON_LAPIS_ORE.get(),
				AMBlocks.DARK_MOON_DIAMOND_ORE.get(),
				AMBlocks.DARK_MOON_EMERALD_ORE.get(),
				
				AMBlocks.DARK_MOON_STONE.get(),
				AMBlocks.DARK_MOON_STONE_SLAB.get(),
				AMBlocks.DARK_MOON_STONE_STAIRS.get(),
				AMBlocks.DARK_MOON_STONE_WALL.get(),
				
				AMBlocks.SMOOTH_DARK_MOON_STONE.get(),
				AMBlocks.SMOOTH_DARK_MOON_STONE_SLAB.get(),
				AMBlocks.SMOOTH_DARK_MOON_STONE_STAIRS.get(),
				AMBlocks.SMOOTH_DARK_MOON_STONE_WALL.get(),
				
				AMBlocks.POLISHED_DARK_MOON_STONE.get(),
				AMBlocks.POLISHED_DARK_MOON_STONE_SLAB.get(),
				AMBlocks.POLISHED_DARK_MOON_STONE_STAIRS.get(),
				
				AMBlocks.DARK_MOON_STONE_BRICKS.get(),
				AMBlocks.DARK_MOON_STONE_BRICK_SLAB.get(),
				AMBlocks.DARK_MOON_STONE_BRICK_STAIRS.get(),
				AMBlocks.DARK_MOON_STONE_BRICK_WALL.get());
		
		for (var fluid : AMDatagenLists.FluidLists.FLUIDS) {
			ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> fluid.getTintColor(), fluid.getCauldron());
		}
	}
}
