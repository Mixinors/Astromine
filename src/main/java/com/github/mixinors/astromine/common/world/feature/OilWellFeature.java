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

package com.github.mixinors.astromine.common.world.feature;

import com.github.mixinors.astromine.registry.common.AMFluids;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class OilWellFeature extends Feature<NoneFeatureConfiguration> {
	private static final int BOTTOM_WELL_SIZE = 8;
	private static final int BOTTOM_WELL_MAX_OFFSET = 20;
	private static final int TOP_WELL_WIDTH = 12;
	private static final int TOP_WELL_HEIGHT = 4;
	private static final int TOP_WELL_NOISE = 2;
	private static final int GEYSER_MIN_HEIGHT = 3;
	private static final int GEYSER_MAX_HEIGHT = 10;
	
	public OilWellFeature(Codec<NoneFeatureConfiguration> configCodec) {
		super(configCodec);
	}
	
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
		var random = context.random();
		var world = context.level();
		var pos = context.origin();
		
		var oceanFloorPos = world.getHeightmapPos(Heightmap.Types.WORLD_SURFACE, pos);
		
		var offsetY = nextIntBetween(random, BOTTOM_WELL_SIZE, BOTTOM_WELL_MAX_OFFSET);
		
		if (pos.getY() - offsetY > oceanFloorPos.getY() - BOTTOM_WELL_MAX_OFFSET - BOTTOM_WELL_SIZE) {
			pos = new BlockPos(pos.getX(), oceanFloorPos.getY() - offsetY - BOTTOM_WELL_MAX_OFFSET - BOTTOM_WELL_SIZE, pos.getZ());
		}
		
		var oilState = AMFluids.OIL.getBlock().defaultBlockState();
		
		var bottomCenter = pos.relative(Direction.UP, offsetY);
		
		for (var x = -BOTTOM_WELL_SIZE; x <= BOTTOM_WELL_SIZE; ++x) {
			for (var y = -BOTTOM_WELL_SIZE; y <= BOTTOM_WELL_SIZE; ++y) {
				for (var z = -BOTTOM_WELL_SIZE; z <= BOTTOM_WELL_SIZE; ++z) {
					if (x * x + y * y + z * z <= BOTTOM_WELL_SIZE * BOTTOM_WELL_SIZE) {
						world.setBlock(bottomCenter.offset(x, y, z), oilState, 0);
					}
				}
			}
		}
		
		var topPos = world.getHeightmapPos(Heightmap.Types.WORLD_SURFACE, pos);
		
		for (var x = pos.getX() - (TOP_WELL_WIDTH); x < pos.getX() + (TOP_WELL_WIDTH); ++x) {
			for (var z = pos.getZ() - (TOP_WELL_WIDTH); z < pos.getZ() + (TOP_WELL_WIDTH); ++z) {
				var dX = (x - pos.getX());
				var dZ = (z - pos.getZ());
				
				var distance = (int) (1.0D + Math.ceil(Math.sqrt(dX * dX + dZ * dZ)));
				
				if (random.nextInt(TOP_WELL_WIDTH) > distance || random.nextInt(TOP_WELL_WIDTH) > distance) {
					var offsetTopPos = world.getHeightmapPos(Heightmap.Types.WORLD_SURFACE, new BlockPos(x, topPos.getY(), z)).below();
					
					var offsetTopState = world.getBlockState(offsetTopPos);
					
					if (!offsetTopState.getFluidState().isEmpty()) {
						world.setBlock(offsetTopPos, oilState, 0);
						
						world.scheduleTick(offsetTopPos, AMFluids.OIL.getSource(), 0);
					}
				}
			}
		}
		
		var geyserHeight = nextIntBetween(random, GEYSER_MIN_HEIGHT, GEYSER_MAX_HEIGHT);
		
		for (var mutablePos = new BlockPos.MutableBlockPos(pos.getX(), pos.getY() + offsetY + BOTTOM_WELL_SIZE, pos.getZ()); mutablePos.getY() < topPos.getY() + geyserHeight; mutablePos.move(Direction.UP)) {
			world.setBlock(mutablePos, oilState, 0);
			
			for (var direction : new Direction[] { Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST }) {
				world.setBlock(mutablePos.relative(direction), Blocks.AIR.defaultBlockState(), 0);
			}
			
			world.scheduleTick(mutablePos, AMFluids.OIL.getSource(), 0);
		}
		
		return true;
	}
	
	private static int nextIntBetween(RandomSource random, int min, int max) {
		return random.nextInt(max - min + 1) + min;
	}
}
