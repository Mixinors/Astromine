package com.github.chainmailstudios.astromine.common.gas;

import com.github.chainmailstudios.astromine.common.fluid.logic.Transaction;
import com.github.chainmailstudios.astromine.common.fluid.logic.Volume;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.network.*;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluids;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.apache.commons.lang3.mutable.MutableBoolean;

import java.util.*;

public class GasManager {
	private static final Map<BlockView, Map<BlockPos, Volume>> LEVELS = new HashMap<>();

	public static void add(BlockView world, BlockPos position, Volume volume) {
		LEVELS.computeIfAbsent(world, (key) -> new Object2ObjectArrayMap<>());

		LEVELS.get(world).put(position, volume);
	}

	public static void remove(BlockView world, BlockPos position) {
		LEVELS.computeIfAbsent(world, (key) -> new Object2ObjectArrayMap<>());

		LEVELS.get(world).remove(position);
	}

	public static Volume get(BlockView world, BlockPos position) {
		LEVELS.computeIfAbsent(world, (key) -> new Object2ObjectArrayMap<>());

		return LEVELS.get(world).getOrDefault(position, Volume.EMPTY);
	}

	public static void propagate(BlockView world, BlockPos initialPosition) {
		Block block = world.getBlockState(initialPosition).getBlock();

		if (!(block instanceof AirBlock)) {
			return;
		}

		Set<BlockPos> cache = new HashSet<>();
		cache.add(initialPosition);

		ArrayDeque<BlockPos> positions = new ArrayDeque<>();
		positions.add(initialPosition);

		Direction[] directions = Direction.values();

		while (!positions.isEmpty()) {
			BlockPos position = positions.getLast();

			positions.removeLast();

			Volume previousVolume = get(world, initialPosition);

			BlockPos[] airPositions = new BlockPos[6];

			for (int i = 0; i < 6; ++i) {
				Direction direction = directions[i];


			}

			for (Direction direction : directions) {
				BlockPos offsetPosition = position.offset(direction);

				if (cache.contains(offsetPosition)) continue;

				Block offsetBlock = world.getBlockState(offsetPosition).getBlock();

				if (offsetBlock instanceof AirBlock) {
					Volume volume = get(world, offsetPosition);

					if (volume == Volume.EMPTY) {
						volume = new Volume(Fluids.WATER, Fraction.EMPTY);

						add(world, offsetPosition, volume);
					} else {
						positions.add(offsetPosition);
					}
					airPositions.add(offsetPosition);
				} else {
					GasManager.remove(world, offsetPosition);
				}

				cache.add(offsetPosition);
			}

			Fraction share = new Fraction(1, airPositions.size());

			for (BlockPos airPos : airPositions) {
				Volume volume = get(world, airPos);

				if (volume.getFraction().isSmallerThan(Fraction.BUCKET) && volume.getFraction().isSmallerThan(previousVolume.getFraction())) {
					previousVolume.push(volume, share).commit();
				}
			}

			if (cache.size() > 512) {
				break;
			}
		}
	}
}
