package com.github.chainmailstudios.astromine.common.bridge;

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

import java.util.HashMap;
import java.util.Map;

public class HolographicBridgeManager {
	private static final VoxelShape[] SHAPES = new VoxelShape[] {
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

	public static final Object2IntArrayMap<BlockPos> LEVELS = new Object2IntArrayMap<>();

	public static void add(BlockPos position, int level) {
		LEVELS.put(position, level);
	}

	public static int get(BlockPos position) {
		return LEVELS.getOrDefault(position, -1);
	}

	public static VoxelShape getShape(BlockPos position) {
		int level = get(position);
		return level == -1 ? SHAPES[15] : getShape(15 - level);
	}

	private static VoxelShape getShape(int level) {
		return SHAPES[level - 1];
	}
}
