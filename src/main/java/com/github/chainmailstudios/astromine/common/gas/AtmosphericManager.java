package com.github.chainmailstudios.astromine.common.gas;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.google.common.collect.Lists;

import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FenceBlock;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public class AtmosphericManager {
	private static final ExecutorService pool = Executors.newCachedThreadPool();

	private static final Map<BlockView, Map<BlockPos, FluidVolume>> LEVELS = new ConcurrentHashMap<>();

	public static void simulate(BlockView world) {
		pool.execute(() -> {
			List<Direction> directions = Lists.newArrayList(Direction.values());

			Map<BlockPos, FluidVolume> map = LEVELS.get(world);

			if (map == null) {
				return;
			}

			for (Map.Entry<BlockPos, FluidVolume> pair : map.entrySet()) {
				Collections.shuffle(directions);

				BlockPos position = pair.getKey();

				FluidVolume fluidVolume = get(world, position);

				for (Direction direction : directions) {
					BlockPos offsetPosition = position.offset(direction);

					BlockState offsetBlockState = world.getBlockState(offsetPosition);
					Block offsetBlock = offsetBlockState.getBlock();

					if (offsetBlock instanceof FenceBlock) {
						System.out.println("fuck!");
					}

					if (offsetBlock instanceof AirBlock) {
						FluidVolume offsetFluidVolume = get(world, offsetPosition);

						if (offsetFluidVolume.isEmpty() && fluidVolume.isEmpty()) {
							AtmosphericManager.remove(world, position);
							AtmosphericManager.remove(world, offsetPosition);

							break;
						}

						if (offsetFluidVolume == FluidVolume.EMPTY) {
							add(world, offsetPosition, new FluidVolume(Fluids.WATER, Fraction.EMPTY));
						} else if (!fluidVolume.isFull()) {
							fluidVolume.pull(offsetFluidVolume, Fraction.BOTTLE);
						}
					} else {
						AtmosphericManager.remove(world, offsetPosition);
					}
				}
			}
		});
	}

	public static FluidVolume get(BlockView world, BlockPos position) {
		LEVELS.computeIfAbsent(world, (key) -> new ConcurrentHashMap<>());

		return LEVELS.get(world).getOrDefault(position, FluidVolume.EMPTY);
	}

	public static void remove(BlockView world, BlockPos position) {
		LEVELS.computeIfAbsent(world, (key) -> new ConcurrentHashMap<>());

		LEVELS.get(world).remove(position);
	}

	public static void add(BlockView world, BlockPos position, FluidVolume fluidVolume) {
		LEVELS.computeIfAbsent(world, (key) -> new ConcurrentHashMap<>());

		LEVELS.get(world).put(position, fluidVolume);
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		pool.shutdown();
	}
}
