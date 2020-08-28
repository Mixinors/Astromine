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

package com.github.chainmailstudios.astromine.discoveries.common.world.feature;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import com.mojang.serialization.Codec;

import com.github.chainmailstudios.astromine.common.noise.OpenSimplexNoise;

import java.util.Random;

public class MoonCraterFeature extends Feature<DefaultFeatureConfig> {
	private static final double SCALE = 1 / 19.42;
	private long seed = 0;
	private OpenSimplexNoise noise = new OpenSimplexNoise(0);

	public MoonCraterFeature(Codec<DefaultFeatureConfig> configCodec) {
		super(configCodec);
	}

	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator generator, Random random, BlockPos pos, DefaultFeatureConfig config) {
		if (this.seed != world.getSeed()) {
			this.noise = new OpenSimplexNoise(world.getSeed());
			this.seed = world.getSeed();
		}

		int radius = random.nextInt(7) + 4;

		int radiusSquared = radius * radius;

		for (int x = -radius; x <= radius; x++) {
			for (int z = -radius; z <= radius; z++) {

				for (int y = -radius; y <= radius; y++) {
					int squareDistance = (x * x) + (y * y) + (z * z);

					if (squareDistance <= radiusSquared) {
						BlockPos local = pos.add(x, y, z);

						double noiseX = local.getX() * SCALE;
						double noiseY = local.getY() * SCALE;
						double noiseZ = local.getZ() * SCALE;

						double noise = this.noise.sample(noiseX, noiseY, noiseZ);
						noise += computeNoiseFalloff(y, radius);

						if (noise > 0 && !world.getBlockState(local).isAir()) {
							world.setBlockState(local, Blocks.AIR.getDefaultState(), 3);
						}
					}
				}
			}
		}

		return true;
	}

	private double computeNoiseFalloff(int y, int radius) {
		return -(2.0 / (y + radius + 0.5));
	}
}
