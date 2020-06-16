package com.github.chainmailstudios.astromine.common.bridge;

import com.github.chainmailstudios.astromine.common.utilities.VoxelShapeUtilities;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Pair;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class HolographicBridgeManager {
	public static final Object2ObjectArrayMap<BlockView, Object2ObjectArrayMap<BlockPos, Set<Vec3i>>> LEVELS = new Object2ObjectArrayMap<>();

	public static void add(BlockView world, BlockPos position, Vec3i top) {
		LEVELS.computeIfAbsent(world, (key) -> new Object2ObjectArrayMap<>());

		LEVELS.get(world).computeIfAbsent(position, (key) -> new HashSet<>());

		LEVELS.get(world).get(position).add(top);
	}

	public static void remove(BlockView world, BlockPos position) {
		LEVELS.computeIfAbsent(world, (key) -> new Object2ObjectArrayMap<>());

		LEVELS.get(world).remove(position);
	}

	public static Set<Vec3i> get(BlockView world, BlockPos position) {
		LEVELS.computeIfAbsent(world, (key) -> new Object2ObjectArrayMap<>());

		return LEVELS.get(world).getOrDefault(position, new HashSet<>());
	}

	public static VoxelShape getShape(BlockView world, BlockPos position) {
		Set<Vec3i> vecs = get(world, position);
		if (vecs == null) return VoxelShapes.fullCube();
		else return getShape(vecs);
	}

	private static VoxelShape getShape(Set<Vec3i> vecs) {
		VoxelShape shape = VoxelShapes.empty();

		boolean a = vecs.stream().allMatch(vec -> vec.getZ() == 0);
		boolean b = vecs.stream().allMatch(vec -> vec.getX() == 0);

		for (Vec3i vec : vecs) {
			shape = VoxelShapes.union(shape, Block.createCuboidShape(
					Math.abs(vec.getX()) - 1,
					Math.abs(vec.getY()) - 1,
					Math.abs(vec.getZ()) - 1,
					b ? 16 : Math.abs(vec.getX()),
					Math.abs(vec.getY()) + 1,
					a ? 16 : Math.abs(vec.getZ())));
		}

		return shape;
	}
}
