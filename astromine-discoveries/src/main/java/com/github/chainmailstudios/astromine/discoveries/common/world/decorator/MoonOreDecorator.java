package com.github.chainmailstudios.astromine.discoveries.common.world.decorator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import com.mojang.serialization.Codec;

import net.minecraft.util.collection.WeightedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.CountConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.DecoratorContext;

public class MoonOreDecorator extends Decorator<CountConfig> {
	public MoonOreDecorator(Codec<CountConfig> configCodec) {
		super(configCodec);
	}

	@Override
	public Stream<BlockPos> getPositions(DecoratorContext context, Random random, CountConfig config, BlockPos pos) {
		List<BlockPos> positions = new ArrayList<>();

		for (int i = 0; i < config.getCount().getValue(random); i++) {
			// Create position
			int x = pos.getX() + random.nextInt(16);
			int z = pos.getZ() + random.nextInt(16);
			int maxY = context.getTopY(Heightmap.Type.MOTION_BLOCKING, x, z);

			// Create mutable for iteration
			BlockPos.Mutable mutable = pos.mutableCopy();
			mutable.set(x, maxY, z);

			WeightedList<BlockPos> weights = new WeightedList<>();

			// Iterate from y5 (to avoid bedrock) to 1 below the max y (to avoid placing ores on the surface)
			for (int y = 5; y < maxY - 1; y++) {
				mutable.setY(y);

				// Sometimes, just pick the position and go with it
				if (random.nextInt(256) == 0) {
					weights.add(mutable.toImmutable(), 1);
					continue;
				}

				// Most of the time, try to pick a position near a cave
				if (context.getBlockState(mutable).isOpaque() && context.getBlockState(mutable.up()).isAir()) {
					weights.add(mutable.toImmutable(), 5);
				}
			}

			if (!weights.isEmpty()) {
				// Store the picked position
				positions.add(weights.pickRandom(random));
			}
		}

		// Return all the positions
		return positions.stream();
	}
}
