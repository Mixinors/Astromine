package com.github.chainmailstudios.astromine.common.bridge;

import com.github.chainmailstudios.astromine.common.utilities.VoxelShapeUtilities;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.block.Block;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class HolographicBridgeManager {
	public static final Object2ObjectArrayMap<BlockView, Object2ObjectArrayMap<BlockPos, Pair<Direction, Integer[]>>> LEVELS = new Object2ObjectArrayMap<>();

	public static void add(BlockView world, Direction direction, BlockPos position, int lA, int lB) {
		LEVELS.computeIfAbsent(world, (key) -> new Object2ObjectArrayMap<>());

		LEVELS.get(world).put(position, new Pair<>(direction, new Integer[]{lA, lB}));
	}

	public static void remove(BlockView world, BlockPos position) {
		LEVELS.computeIfAbsent(world, (key) -> new Object2ObjectArrayMap<>());

		LEVELS.get(world).remove(position);
	}

	public static Pair<Direction, Integer[]> get(BlockView world, BlockPos position) {
		LEVELS.computeIfAbsent(world, (key) -> new Object2ObjectArrayMap<>());

		return LEVELS.get(world).getOrDefault(position, new Pair<>(Direction.NORTH, new Integer[]{Integer.MIN_VALUE, Integer.MIN_VALUE}));
	}

	public static VoxelShape getShape(BlockView world, BlockPos position) {
		Pair<Direction, Integer[]> pair = get(world, position);
		Integer[] levels = pair.getRight();
		return levels[0] == Integer.MIN_VALUE ? VoxelShapes.fullCube() : getShape(pair.getLeft(), pair.getRight());
	}

	private static VoxelShape getShape(Direction direction, Integer[] levels) {
		float t = levels[0];
		float b = levels[1];

		while (t < 0) {
			t += 16;
			b += 16;
		}

		while (t >= 16) {
			t -= 16;
			b -= 16;
		}

		if (direction == Direction.SOUTH || direction == Direction.EAST) {
			t -= 2;
			b -= 2;
		}

		float y = t;

		float dX = 16f / Math.abs((t - b));

		VoxelShape shape = VoxelShapes.empty();

		for (float x = 16; x > 0; x -= dX) {
			shape = VoxelShapes.union(shape, Block.createCuboidShape(0, y, 0, x, y + 1, 16));
			y += 1;
		}

		switch (direction) {
			case SOUTH: {
				return VoxelShapeUtilities.rotate(Direction.Axis.Y, Math.toRadians(270), shape);
			}
			case NORTH: {
				return VoxelShapeUtilities.rotate(Direction.Axis.Y, Math.toRadians(90), shape);
			}
			case WEST: {
				return VoxelShapeUtilities.rotate(Direction.Axis.Y, Math.toRadians(0), shape);
			}
			default: {
				return VoxelShapeUtilities.rotate(Direction.Axis.Y, Math.toRadians(180), shape);
			}
		}
	}
}
