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

package com.github.mixinors.astromine.common.world.generation.space;

import com.github.mixinors.astromine.common.config.AMConfig;
import com.github.mixinors.astromine.common.noise.OctaveNoiseSampler;
import com.github.mixinors.astromine.common.util.NoiseUtils;
import com.github.mixinors.astromine.registry.common.AMBlocks;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import java.util.concurrent.CompletableFuture;

final class AsteroidFieldGenerator {
	private static final int MIN_Y = 0;
	private static final int MAX_Y = 256;
	private static final double[] NOISE_FALLOFF = createNoiseFalloff();

	private AsteroidFieldGenerator() {
	}

	static CompletableFuture<ChunkAccess> fillFromNoise(ChunkAccess chunk, long seed) {
		return CompletableFuture.supplyAsync(Util.wrapThreadWithTaskName("astromine_asteroid_field", () -> {
			fill(chunk, seed);

			return chunk;
		}), Util.backgroundExecutor());
	}

	private static void fill(ChunkAccess chunk, long seed) {
		var sampler = NoiseUtils.getSampler(seed, 3, 200, 1.225F, 1.0F);
		var threshold = AMConfig.get().world.asteroidGenerationThreshold;
		var asteroidState = AMBlocks.ASTEROID_STONE.get().defaultBlockState();
		var mutable = new BlockPos.MutableBlockPos();
		var x1 = chunk.getPos().getMinBlockX();
		var z1 = chunk.getPos().getMinBlockZ();
		var x2 = chunk.getPos().getMaxBlockX();
		var z2 = chunk.getPos().getMaxBlockZ();
		var random = new WorldgenRandom(RandomSource.create(0L));

		random.setDecorationSeed(0L, x1, z1);

		for (var x = x1; x <= x2; ++x) {
			for (var z = z1; z <= z2; ++z) {
				fillColumn(chunk, sampler, mutable, random, asteroidState, threshold, x, z);
			}
		}
	}

	private static void fillColumn(ChunkAccess chunk, OctaveNoiseSampler<?> sampler, BlockPos.MutableBlockPos mutable, WorldgenRandom random, BlockState asteroidState, float threshold, int x, int z) {
		for (var y = MIN_Y; y <= MAX_Y; ++y) {
			if (sampler.sample(x, y, z) <= threshold + NOISE_FALLOFF[y]) {
				continue;
			}

			if (random.nextInt(64) != 0) {
				chunk.setBlockState(mutable.set(x, y, z), asteroidState, false);
			}
		}
	}

	private static double[] createNoiseFalloff() {
		var falloff = new double[MAX_Y + 1];

		for (var y = MIN_Y; y <= MAX_Y; ++y) {
			falloff[y] = (10.0 / (y + 1.0)) - (10.0 / (y - 257.0)) - 0.155;
		}

		return falloff;
	}
}
