/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.registry.common.AMConfig;
import com.github.mixinors.astromine.registry.common.AMFluids;
import com.mojang.serialization.Codec;
import com.terraformersmc.terraform.shapes.api.Position;
import com.terraformersmc.terraform.shapes.impl.Shapes;
import com.terraformersmc.terraform.shapes.impl.layer.transform.NoiseTranslateLayer;
import com.terraformersmc.terraform.shapes.impl.layer.transform.TranslateLayer;
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

import java.util.Random;

public class CrudeOilFeature extends Feature<DefaultFeatureConfig> {
	private static final Lazy<Block> CRUDE_OIL_BLOCK = new Lazy<>(() -> Registry.BLOCK.get(AMCommon.id("crude_oil")));

	public CrudeOilFeature(Codec<DefaultFeatureConfig> configCodec) {
		super(configCodec);
	}

	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos spawnPos, DefaultFeatureConfig config) {
		if (random.nextInt(AMConfig.get().crudeOilThreshold) > 1) {
			return false;
		}

		var offsetY = MathHelper.clamp(random.nextInt(20), 8, 20);

		Shapes.ellipsoid(8, 8, 8)
				.applyLayer(
						TranslateLayer.of(Position.of(spawnPos.offset(Direction.UP, offsetY)))
				).stream()
				.forEach(pos -> world.setBlockState(pos.toBlockPos(), CRUDE_OIL_BLOCK.get().getDefaultState(), 0));

		Shapes.ellipsoid(12, 12, 4)
				.applyLayer(
						TranslateLayer.of(Position.of(spawnPos.offset(Direction.UP, 64)))
				).applyLayer(
						NoiseTranslateLayer.of(8, random)
				).stream()
				.filter(pos -> !world.getBlockState(pos.toBlockPos()).getFluidState().isEmpty())
				.forEach(pos -> world.setBlockState(pos.toBlockPos(), CRUDE_OIL_BLOCK.get().getDefaultState(), 0));

		var airBlocks = 0;

		for (var y = spawnPos.getY() + offsetY; !world.getBlockState(spawnPos.offset(Direction.UP, y)).isAir() || (world.getBlockState(spawnPos.offset(Direction.UP, y)).isAir() && ++airBlocks < offsetY); ++y) {
			world.setBlockState(spawnPos.offset(Direction.UP, y), CRUDE_OIL_BLOCK.get().getDefaultState(), 0);

			for (var direction : new Direction[]{ Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST }) {
				world.removeBlock(spawnPos.offset(Direction.UP, y).offset(direction), false);
			}

			world.getFluidTickScheduler().schedule(spawnPos.offset(Direction.UP, y), AMFluids.OIL, 0);
		}

		return true;
	}
}
