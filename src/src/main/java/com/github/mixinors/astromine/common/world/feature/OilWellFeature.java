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

import com.github.mixinors.astromine.common.config.AMConfig;
import com.github.mixinors.astromine.registry.common.AMFluids;
import com.mojang.serialization.Codec;
import com.terraformersmc.terraform.shapes.api.Position;
import com.terraformersmc.terraform.shapes.impl.Shapes;
import com.terraformersmc.terraform.shapes.impl.layer.transform.NoiseTranslateLayer;
import com.terraformersmc.terraform.shapes.impl.layer.transform.TranslateLayer;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
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
		
		var offsetY = random.nextInt(BOTTOM_WELL_SIZE, BOTTOM_WELL_MAX_OFFSET);
		
		var oilState = AMFluids.OIL.getBlock().getDefaultState();
		
		Shapes.ellipsoid(BOTTOM_WELL_SIZE, BOTTOM_WELL_SIZE, BOTTOM_WELL_SIZE).applyLayer(TranslateLayer.of(Position.of(pos.offset(Direction.UP, offsetY)))).stream().forEach(position ->
				world.setBlockState(position.toBlockPos(), oilState, 0)
		);
		
		BlockPos oceanTop = world.getTopPosition(Heightmap.Type.WORLD_SURFACE_WG, pos);
		
		Shapes.ellipsoid(TOP_WELL_WIDTH, TOP_WELL_WIDTH, TOP_WELL_HEIGHT).applyLayer(NoiseTranslateLayer.of(TOP_WELL_NOISE, random)).applyLayer(TranslateLayer.of(Position.of(oceanTop))).stream().forEach(position -> {
			if (world.getBlockState(position.toBlockPos()).getBlock() instanceof FluidBlock) {
				world.setBlockState(position.toBlockPos(), oilState, 0);
			}
		});
		
		int geyserHeight = random.nextInt(GEYSER_MIN_HEIGHT, GEYSER_MAX_HEIGHT);
		
		for (var mutablePos = new BlockPos.Mutable(pos.getX(), pos.getY() + offsetY + BOTTOM_WELL_SIZE, pos.getZ()); mutablePos.getY() < oceanTop.getY() + geyserHeight; mutablePos.move(Direction.UP)) {
			world.setBlockState(mutablePos, oilState, 0);
			
			for (var direction : new Direction[] { Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST }) {
				world.setBlockState(mutablePos.offset(direction), Blocks.AIR.getDefaultState(), 0);
			}
			
			world.createAndScheduleFluidTick(mutablePos, AMFluids.OIL, 0);
		}
		
		return true;
	}
}
