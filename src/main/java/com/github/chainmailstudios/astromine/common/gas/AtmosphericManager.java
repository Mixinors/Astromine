package com.github.chainmailstudios.astromine.common.gas;

import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.google.common.collect.Lists;
import net.minecraft.block.AirBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AtmosphericManager {
	private static final Fraction TRESHHOLD = new Fraction(2, 1);

	private static final ExecutorService POOL = Executors.newCachedThreadPool();

	private static final Map<BlockView, Map<BlockPos, FluidVolume>> LEVELS = new ConcurrentHashMap<>();

	public static void add(BlockView world, BlockPos position, FluidVolume fluidVolume) {
		LEVELS.computeIfAbsent(world, (key) -> new ConcurrentHashMap<>());

		LEVELS.get(world).put(position, fluidVolume);
	}

	public static void remove(BlockView world, BlockPos position) {
		LEVELS.computeIfAbsent(world, (key) -> new ConcurrentHashMap<>());

		LEVELS.get(world).remove(position);
	}

	public static FluidVolume get(BlockView world, BlockPos position) {
		LEVELS.computeIfAbsent(world, (key) -> new ConcurrentHashMap<>());

		if (world instanceof World) {
			RegistryKey<DimensionType> key = ((World) world).getDimensionRegistryKey();
			boolean isVanilla = (key == DimensionType.OVERWORLD_REGISTRY_KEY || key == DimensionType.OVERWORLD_CAVES_REGISTRY_KEY || key == DimensionType.THE_NETHER_REGISTRY_KEY || key == DimensionType.THE_END_REGISTRY_KEY);

			if (isVanilla && !LEVELS.get(world).containsKey(position)) {
				return FluidVolume.oxygen();
			} else {
				return LEVELS.get(world).getOrDefault(position, FluidVolume.empty());
			}
		} else {
			return LEVELS.get(world).getOrDefault(position, FluidVolume.empty());
		}
	}

	public static void simulate(BlockView world) {
		POOL.execute(() -> {
			Map<BlockPos, FluidVolume> map = LEVELS.get(world);

			if (map == null) return;

			List<Direction> directions = Lists.newArrayList(Direction.values());

			for (Map.Entry<BlockPos, FluidVolume> pair : map.entrySet()) {
				final FluidVolume fluidVolume = get(world, pair.getKey());

				Collections.shuffle(directions);

				for (Direction direction : directions) {
					final BlockPos offsetPosition = pair.getKey().offset(direction);

					if (world.getBlockState(offsetPosition).getBlock() instanceof AirBlock) {
						FluidVolume offsetFluidVolume = get(world, offsetPosition);

						if (!fluidVolume.isEmpty() && fluidVolume.getFraction().isBiggerThan(Fraction.max(TRESHHOLD, offsetFluidVolume.getFraction())) && offsetFluidVolume.canInsert(fluidVolume.getFluid(), Fraction.BUCKET)) {
							fluidVolume.pushVolume(offsetFluidVolume, Fraction.BUCKET);
							AtmosphericManager.add(world, offsetPosition, offsetFluidVolume);
						} else if (!fluidVolume.isEmpty() && fluidVolume.getFraction().isBiggerThan(Fraction.max(TRESHHOLD, offsetFluidVolume.getFraction())) && offsetFluidVolume.equals(FluidVolume.oxygen())) {
							FluidVolume newVolume = new FluidVolume();
							fluidVolume.pushVolume(newVolume, Fraction.BUCKET);
							AtmosphericManager.add(world, offsetPosition, newVolume);
						}
					} else {
						AtmosphericManager.remove(world, offsetPosition);
					}
				}
			}
		});
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		POOL.shutdown();
	}
}
