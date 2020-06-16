package com.github.chainmailstudios.astromine.common.gas;

import com.github.chainmailstudios.astromine.common.fluid.logic.Volume;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.google.common.collect.Lists;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FenceBlock;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GasManager {
	private static final ExecutorService pool = Executors.newCachedThreadPool();

	private static final Map<BlockView, Map<BlockPos, Volume>> LEVELS = new ConcurrentHashMap<>();

	public static void add(BlockView world, BlockPos position, Volume volume) {
		LEVELS.computeIfAbsent(world, (key) -> new ConcurrentHashMap<>());

		LEVELS.get(world).put(position, volume);
	}

	public static void remove(BlockView world, BlockPos position) {
		LEVELS.computeIfAbsent(world, (key) -> new ConcurrentHashMap<>());

		LEVELS.get(world).remove(position);
	}

	public static Volume get(BlockView world, BlockPos position) {
		LEVELS.computeIfAbsent(world, (key) -> new ConcurrentHashMap<>());

		return LEVELS.get(world).getOrDefault(position, Volume.EMPTY);
	}

	public static void simulate(BlockView world) {
		pool.execute(() -> {
			List<Direction> directions = Lists.newArrayList(Direction.values());

			Map<BlockPos, Volume> map = LEVELS.get(world);

			if (map == null) return;

			final int maxAdditions = 16;
			int additions = 0;

			for (Map.Entry<BlockPos, Volume> pair : map.entrySet()) {
				Collections.shuffle(directions);

				BlockPos position = pair.getKey();

				Volume volume = get(world, position);

				for (Direction direction : directions) {
					BlockPos offsetPosition = position.offset(direction);

					BlockState offsetBlockState = world.getBlockState(offsetPosition);
					Block offsetBlock = offsetBlockState.getBlock();

					if (offsetBlock instanceof FenceBlock) {
						System.out.println("fuck!");
					}

					if (offsetBlock instanceof AirBlock) {
						Volume offsetVolume = get(world, offsetPosition);

						if (offsetVolume.isEmpty() && volume.isEmpty()) {
							GasManager.remove(world, position);
							GasManager.remove(world, offsetPosition);

							break;
						}

						if (offsetVolume == Volume.EMPTY) {
							add(world, offsetPosition, new Volume(Fluids.WATER, Fraction.EMPTY));
						} else if (!volume.isFull()) {
							volume.pull(offsetVolume, Fraction.BOTTLE).commit();
						}
					} else {
						GasManager.remove(world, offsetPosition);
					}
				}
			}
		});
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		pool.shutdown();
	}
}
