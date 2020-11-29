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

import com.mojang.serialization.Codec;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.foundations.registry.AstromineFoundationsFluids;
import com.github.chainmailstudios.astromine.registry.AstromineConfig;
import com.terraformersmc.shapes.api.Position;
import com.terraformersmc.shapes.impl.Shapes;
import com.terraformersmc.shapes.impl.layer.transform.NoiseTranslateLayer;
import com.terraformersmc.shapes.impl.layer.transform.TranslateLayer;

import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.util.Mth;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class CrudeOilFeature extends Feature<NoneFeatureConfiguration> {
	private static final LazyLoadedValue<Block> CRUDE_OIL_BLOCK = new LazyLoadedValue<>(() -> Registry.BLOCK.get(AstromineCommon.identifier("crude_oil")));

	public CrudeOilFeature(Codec<NoneFeatureConfiguration> configCodec) {
		super(configCodec);
	}

	@Override
	public boolean place(WorldGenLevel world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, NoneFeatureConfiguration config) {
		if (random.nextInt(AstromineConfig.get().crudeOilThreshold) > 1)
			return false;

		int offsetY = Mth.clamp(random.nextInt(20), 8, 20);

		Shapes.ellipsoid(8, 8, 8).applyLayer(TranslateLayer.of(Position.of(pos.relative(Direction.UP, offsetY)))).stream().forEach(position -> {
			world.setBlock(position.toBlockPos(), CRUDE_OIL_BLOCK.get().defaultBlockState(), 0);
		});

		Shapes.ellipsoid(12, 12, 4).applyLayer(TranslateLayer.of(Position.of(pos.relative(Direction.UP, 64)))).applyLayer(NoiseTranslateLayer.of(8, random)).stream().forEach(position -> {
			if (world.getBlockState(position.toBlockPos()).getBlock() instanceof LiquidBlock) {
				world.setBlock(position.toBlockPos(), CRUDE_OIL_BLOCK.get().defaultBlockState(), 0);
			}
		});

		int airBlocks = 0;

		for (int y = pos.getY() + offsetY; !world.getBlockState(pos.relative(Direction.UP, y)).isAir() || (world.getBlockState(pos.relative(Direction.UP, y)).isAir() && ++airBlocks < offsetY); ++y) {
			world.setBlock(pos.relative(Direction.UP, y), CRUDE_OIL_BLOCK.get().defaultBlockState(), 0);

			for (Direction direction : new Direction[]{ Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST }) {
				world.removeBlock(pos.relative(Direction.UP, y).relative(direction), false);
			}

			world.getLiquidTicks().scheduleTick(pos.relative(Direction.UP, y), AstromineFoundationsFluids.CRUDE_OIL, 0);
		}

		return true;
	}
}
