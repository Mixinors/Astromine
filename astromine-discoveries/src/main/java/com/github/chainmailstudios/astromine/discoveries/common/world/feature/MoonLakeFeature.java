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

import com.mojang.serialization.Codec;

import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesBlocks;

import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class MoonLakeFeature extends Feature<NoneFeatureConfiguration> {
	public MoonLakeFeature(Codec<NoneFeatureConfiguration> configCodec) {
		super(configCodec);
	}

	@Override
	public boolean place(WorldGenLevel world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, NoneFeatureConfiguration featureConfig) {
		BlockPos.MutableBlockPos mutable = pos.mutable();

		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				for (int y = -4; y <= 4; y++) {
					if (!world.getBlockState(mutable.setWithOffset(pos, x, y, z)).is(AstromineDiscoveriesBlocks.MOON_STONE)) {
						return false;
					}
				}
			}
		}

		double radius = 4 * 4;

		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				for (int y = -4; y <= 4; y++) {
					double dist = (x * x) + (y * y) + (z * z);
					if (dist <= radius) {
						if (y < -1) {
							world.setBlock(mutable.setWithOffset(pos, x, y, z), Blocks.ICE.defaultBlockState(), 3);
						} else {
							world.setBlock(mutable.setWithOffset(pos, x, y, z), Blocks.AIR.defaultBlockState(), 3);
						}
					}
				}
			}
		}

		return true;
	}
}
