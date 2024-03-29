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
import com.terraformersmc.terraform.shapes.api.Position;
import com.terraformersmc.terraform.shapes.impl.Shapes;
import com.terraformersmc.terraform.shapes.impl.layer.transform.TranslateLayer;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class OilWellFeature extends Feature<DefaultFeatureConfig> {
	private static final int BOTTOM_WELL_SIZE = 8;
	private static final int BOTTOM_WELL_MAX_OFFSET = 20;
	private static final int TOP_WELL_WIDTH = 12;
	private static final int TOP_WELL_HEIGHT = 4;
	private static final int TOP_WELL_NOISE = 2;
	private static final int GEYSER_MIN_HEIGHT = 3;
	private static final int GEYSER_MAX_HEIGHT = 10;
	
	public OilWellFeature(Codec<DefaultFeatureConfig> configCodec) {
		super(configCodec);
	}
	
	@Override
	public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
		var random = context.getRandom();
		var world = context.getWorld();
		var pos = context.getOrigin();
		
		var oceanFloorPos = world.getTopPosition(Heightmap.Type.WORLD_SURFACE, pos);
		
		var offsetY = (int) (random.nextFloat() * (BOTTOM_WELL_MAX_OFFSET - BOTTOM_WELL_SIZE) + BOTTOM_WELL_SIZE); // TODO: Check if this works!
		
		if (pos.getY() - offsetY > oceanFloorPos.getY() - BOTTOM_WELL_MAX_OFFSET - BOTTOM_WELL_SIZE) {
			pos = new BlockPos(pos.getX(), oceanFloorPos.getY() - offsetY - BOTTOM_WELL_MAX_OFFSET - BOTTOM_WELL_SIZE, pos.getZ());
		}
		
		var oilState = AMFluids.OIL.getBlock().getDefaultState();
		
		Shapes.ellipsoid(BOTTOM_WELL_SIZE, BOTTOM_WELL_SIZE, BOTTOM_WELL_SIZE).applyLayer(TranslateLayer.of(Position.of(pos.offset(Direction.UP, offsetY)))).stream().forEach(wellPos ->
				world.setBlockState(wellPos.toBlockPos(), oilState, 0)
		);
		
		var topPos = world.getTopPosition(Heightmap.Type.WORLD_SURFACE, pos);
		
		for (var x = pos.getX() - (TOP_WELL_WIDTH); x < pos.getX() + (TOP_WELL_WIDTH); ++x) {
			for (var z = pos.getZ() - (TOP_WELL_WIDTH); z < pos.getZ() + (TOP_WELL_WIDTH); ++z) {
				var dX = (x - pos.getX());
				var dZ = (z - pos.getZ());
				
				var distance = (int) (1.0D + Math.ceil(Math.sqrt(dX * dX + dZ * dZ)));
				
				if (random.nextInt(TOP_WELL_WIDTH) > distance || random.nextInt(TOP_WELL_WIDTH) > distance) {
					var offsetTopPos = world.getTopPosition(Heightmap.Type.WORLD_SURFACE, new BlockPos(x, topPos.getY(), z)).down();
					
					var offsetTopState = world.getBlockState(offsetTopPos);
					
					if (!offsetTopState.getFluidState().isEmpty()) {
						world.setBlockState(offsetTopPos, oilState, 0);
						
						world.createAndScheduleFluidTick(offsetTopPos, AMFluids.OIL, 0);
					}
				}
			}
		}
		
		var geyserHeight = (int) (random.nextFloat() - (GEYSER_MAX_HEIGHT - GEYSER_MIN_HEIGHT) + GEYSER_MIN_HEIGHT);
		
		for (var mutablePos = new BlockPos.Mutable(pos.getX(), pos.getY() + offsetY + BOTTOM_WELL_SIZE, pos.getZ()); mutablePos.getY() < topPos.getY() + geyserHeight; mutablePos.move(Direction.UP)) {
			world.setBlockState(mutablePos, oilState, 0);
			
			for (var direction : new Direction[] { Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST }) {
				world.setBlockState(mutablePos.offset(direction), Blocks.AIR.getDefaultState(), 0);
			}
			
			world.createAndScheduleFluidTick(mutablePos, AMFluids.OIL, 0);
		}
		
		return true;
	}
}
