/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
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

package com.github.chainmailstudios.astromine.foundations.common.world.feature;

import com.github.chainmailstudios.astromine.registry.AstromineConfig;
import net.minecraft.block.Block;
import net.minecraft.block.FluidBlock;
import net.minecraft.util.Lazy;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import com.mojang.serialization.Codec;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.foundations.registry.AstromineFoundationsFluids;
import com.terraformersmc.shapes.api.Position;
import com.terraformersmc.shapes.impl.Shapes;
import com.terraformersmc.shapes.impl.layer.transform.NoiseTranslateLayer;
import com.terraformersmc.shapes.impl.layer.transform.TranslateLayer;

import java.util.Random;

public class CrudeOilFeature extends Feature<DefaultFeatureConfig> {
	private static final Lazy<Block> CRUDE_OIL_BLOCK = new Lazy<>(() -> Registry.BLOCK.get(AstromineCommon.identifier("crude_oil")));

	public CrudeOilFeature(Codec<DefaultFeatureConfig> configCodec) {
		super(configCodec);
	}

	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {
		if (random.nextInt(AstromineConfig.get().crudeOilThreshold) > 1)
			return false;

		int offsetY = MathHelper.clamp(random.nextInt(20), 8, 20);

		Shapes.ellipsoid(8, 8, 8).applyLayer(TranslateLayer.of(Position.of(pos.offset(Direction.UP, offsetY)))).stream().forEach(position -> {
			world.setBlockState(position.toBlockPos(), CRUDE_OIL_BLOCK.get().getDefaultState(), 0);
		});

		Shapes.ellipsoid(12, 12, 4).applyLayer(TranslateLayer.of(Position.of(pos.offset(Direction.UP, 64)))).applyLayer(NoiseTranslateLayer.of(8, random)).stream().forEach(position -> {
			if (world.getBlockState(position.toBlockPos()).getBlock() instanceof FluidBlock) {
				world.setBlockState(position.toBlockPos(), CRUDE_OIL_BLOCK.get().getDefaultState(), 0);
			}
		});

		int airBlocks = 0;

		for (int y = pos.getY() + offsetY; !world.getBlockState(pos.offset(Direction.UP, y)).isAir() || (world.getBlockState(pos.offset(Direction.UP, y)).isAir() && ++airBlocks < offsetY); ++y) {
			world.setBlockState(pos.offset(Direction.UP, y), CRUDE_OIL_BLOCK.get().getDefaultState(), 0);

			for (Direction direction : new Direction[]{ Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST }) {
				world.removeBlock(pos.offset(Direction.UP, y).offset(direction), false);
			}

			world.getFluidTickScheduler().schedule(pos.offset(Direction.UP, y), AstromineFoundationsFluids.CRUDE_OIL, 0);
		}

		return true;
	}
}
