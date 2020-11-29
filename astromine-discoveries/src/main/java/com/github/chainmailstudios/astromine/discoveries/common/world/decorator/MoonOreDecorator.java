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

package com.github.chainmailstudios.astromine.discoveries.common.world.decorator;

import com.mojang.serialization.Codec;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.behavior.WeightedList;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.configurations.CountConfiguration;
import net.minecraft.world.level.levelgen.placement.DecorationContext;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;

public class MoonOreDecorator extends FeatureDecorator<CountConfiguration> {
	public MoonOreDecorator(Codec<CountConfiguration> configCodec) {
		super(configCodec);
	}

	@Override
	public Stream<BlockPos> getPositions(DecorationContext context, Random random, CountConfiguration config, BlockPos pos) {
		List<BlockPos> positions = new ArrayList<>();

		for (int i = 0; i < config.count().sample(random); i++) {
			// Create position
			int x = pos.getX() + random.nextInt(16);
			int z = pos.getZ() + random.nextInt(16);
			int maxY = context.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);

			// Create mutable for iteration
			BlockPos.MutableBlockPos mutable = pos.mutable();
			mutable.set(x, maxY, z);

			WeightedList<BlockPos> weights = new WeightedList<>();

			// Iterate from y5 (to avoid bedrock) to 1 below the max y (to avoid placing ores on the surface)
			for (int y = 5; y < maxY - 1; y++) {
				mutable.setY(y);

				// Sometimes, just pick the position and go with it
				if (random.nextInt(256) == 0) {
					weights.add(mutable.immutable(), 1);
					continue;
				}

				// Most of the time, try to pick a position near a cave
				if (context.getBlockState(mutable).canOcclude() && context.getBlockState(mutable.above()).isAir()) {
					weights.add(mutable.immutable(), 5);
				}
			}

			if (!weights.isEmpty()) {
				// Store the picked position
				positions.add(weights.getOne(random));
			}
		}

		// Return all the positions
		return positions.stream();
	}
}
