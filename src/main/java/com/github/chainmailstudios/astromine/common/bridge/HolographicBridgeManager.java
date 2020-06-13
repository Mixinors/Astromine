package com.github.chainmailstudios.astromine.common.bridge;

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class HolographicBridgeManager {
	private static final VoxelShape[] SHAPES = new VoxelShape[]{
			Block.createCuboidShape(0, 0, 0, 16, 1, 16),
			Block.createCuboidShape(0, 1, 0, 16, 2, 16),
			Block.createCuboidShape(0, 2, 0, 16, 3, 16),
			Block.createCuboidShape(0, 3, 0, 16, 4, 16),
			Block.createCuboidShape(0, 4, 0, 16, 5, 16),
			Block.createCuboidShape(0, 5, 0, 16, 6, 16),
			Block.createCuboidShape(0, 6, 0, 16, 7, 16),
			Block.createCuboidShape(0, 7, 0, 16, 8, 16),
			Block.createCuboidShape(0, 8, 0, 16, 9, 16),
			Block.createCuboidShape(0, 9, 0, 16, 10, 16),
			Block.createCuboidShape(0, 10, 0, 16, 11, 16),
			Block.createCuboidShape(0, 11, 0, 16, 12, 16),
			Block.createCuboidShape(0, 12, 0, 16, 13, 16),
			Block.createCuboidShape(0, 13, 0, 16, 14, 16),
			Block.createCuboidShape(0, 14, 0, 16, 15, 16),
			Block.createCuboidShape(0, 15, 0, 16, 16, 16)
	};

	public static final Object2ObjectArrayMap<BlockView, Object2ObjectArrayMap<BlockPos, Integer[]>> LEVELS = new Object2ObjectArrayMap<>();

	public static void add(BlockView world, BlockPos position, int lA, int lB) {
		LEVELS.computeIfAbsent(world, (key) -> new Object2ObjectArrayMap<>());

		LEVELS.get(world).put(position, new Integer[]{lA, lB});
	}

	public static void remove(BlockView world, BlockPos position) {
		LEVELS.computeIfAbsent(world, (key) -> new Object2ObjectArrayMap<>());

		LEVELS.get(world).remove(position);
	}

	public static Integer[] get(BlockView world, BlockPos position) {
		LEVELS.computeIfAbsent(world, (key) -> new Object2ObjectArrayMap<>());

		return LEVELS.get(world).getOrDefault(position, new Integer[]{Integer.MIN_VALUE, Integer.MIN_VALUE});
	}

	public static VoxelShape getShape(BlockView world, BlockPos position) {
		Integer[] levels = get(world, position);
		return levels[0] == Integer.MIN_VALUE ? VoxelShapes.fullCube(): getShape(levels);
	}

	private static VoxelShape getShape(Integer[] levels) {
		float t = levels[0];
		float b = levels[1];

		float y = t;

		float dX = 16f / (t - b);

		VoxelShape shape = VoxelShapes.empty();

		for (float x = 16; x > 0; x -= dX) {
			shape = VoxelShapes.union(shape, Block.createCuboidShape(0, y, 0, x, y + 1, 16));
			y += 1;
		}

		return shape;
	}
}
