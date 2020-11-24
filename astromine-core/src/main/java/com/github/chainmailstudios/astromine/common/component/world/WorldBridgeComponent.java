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

package com.github.chainmailstudios.astromine.common.component.world;

import net.minecraft.block.Block;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;

import com.github.chainmailstudios.astromine.common.utilities.VoxelShapeUtilities;
import com.github.chainmailstudios.astromine.registry.AstromineComponents;
import dev.onyxstudios.cca.api.v3.component.Component;
import it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Sets;
import java.util.Map;
import java.util.Set;

/**
 * A {@link Component} which stores information about
 * a {@link World}'s holographic bridges.
 *
 * It is important to understand how information is stored here.
 * A {@link Map} of {@link Long}-represented {@link BlockPos} positions
 * to a {@link Set} of {@link Vec3i} contains points in the block's
 * 16x16 vertical grid intersected by a bridge's line.
 *
 * That is to say, if the bridge crosses a block horizontally entirely,
 * there will be 16 {@link Vec3i}s, at least, representing 1-wide steps
 * which will be used to build the block's {@link VoxelShape}.
 *
 * Serialization and deserialization methods are provided for:
 * - {@link CompoundTag} - through {@link #writeToNbt(CompoundTag)} and {@link #readFromNbt(CompoundTag)}.
 */
public final class WorldBridgeComponent implements Component {
	private final Long2ObjectArrayMap<Set<Vec3i>> entries = new Long2ObjectArrayMap<>();

	private final Long2ObjectArrayMap<VoxelShape> cache = new Long2ObjectArrayMap<>();

	private final World world;

	/** Instantiates a {@link WorldBridgeComponent}. */
	public WorldBridgeComponent(World world) {
		this.world = world;
	}

	/** Returns this component's world. */
	public World getWorld() {
		return world;
	}

	/** Adds the given step at the specified {@link BlockPos}'s long representation. */
	public void add(BlockPos pos, Vec3i vec) {
		add(pos.asLong(), vec);
	}

	/** Adds the given step at the specified position. */
	public void add(long pos, Vec3i top) {
		entries.computeIfAbsent(pos, (k) -> Sets.newHashSet());
		entries.get(pos).add(top);

		cache.remove(pos);
	}

	/** Removes the steps at the specified {@link BlockPos}'s long representation. */
	public void remove(BlockPos pos) {
		remove(pos.asLong());
	}

	/** Removes the steps at the specified position. */
	public void remove(long pos) {
		entries.remove(pos);

		cache.remove(pos);
	}

	/** Returns the steps at the given {@link BlockPos}'s long representation. */
	public Set<Vec3i> get(BlockPos pos) {
		return get(pos.asLong());
	}

	/** Returns the sTeps at the given position. */
	public Set<Vec3i> get(long pos) {
		return entries.getOrDefault(pos, Sets.newHashSet());
	}

	/** Returns the {@link VoxelShape} at the given {@link BlockPos}'s long representation. */
	public VoxelShape getShape(BlockPos pos) {
		return getShape(pos.asLong());
	}

	/** Returns the {@link VoxelShape} at the given position. */
	public VoxelShape getShape(long pos) {
		if (cache.containsKey(pos)) return cache.get(pos);

		Set<Vec3i> vecs = get(pos);

		if (vecs == null)
			return VoxelShapes.fullCube();

		VoxelShape shape = getShape(vecs);

		cache.put(pos, shape);

		return shape;
	}

	/** Returns the {@link VoxelShape} formed by the given {@link Set} of steps.
	 * I made this work months ago; and I don't know how. Accept it, or suffer. */
	private VoxelShape getShape(Set<Vec3i> vecs) {
		VoxelShape shape = VoxelShapes.empty();

		boolean a = vecs.stream().allMatch(vec -> vec.getZ() == 0);
		boolean b = vecs.stream().allMatch(vec -> vec.getX() == 0);
		boolean c = false;
		boolean d = false;

		for (Vec3i vec : vecs) {
			if (!c && vec.getX() < 0)
				c = true;
			if (!d && vec.getZ() < 0)
				d = true;

			shape = VoxelShapes.union(shape, Block.createCuboidShape(Math.abs(vec.getX()), Math.abs(vec.getY()) - 1, Math.abs(vec.getZ()), b ? 16 : Math.abs(vec.getX() + 1), Math.abs(vec.getY()) + 1, a ? 16 : Math.abs(vec.getZ() + 1)));
		}

		if (c || d) {
			return VoxelShapeUtilities.rotate(Direction.Axis.Y, Math.toRadians(180), shape);
		}

		return shape;
	}

	/** Serializes this {@link WorldBridgeComponent} to a {@link CompoundTag}. */
	@Override
	public void writeToNbt(CompoundTag tag) {
		CompoundTag dataTag = new CompoundTag();

		int k = 0;

		for (Map.Entry<Long, Set<Vec3i>> entry : entries.entrySet()) {
			CompoundTag pointTag = new CompoundTag();
			CompoundTag vecTag = new CompoundTag();

			pointTag.putLong("pos", entry.getKey());

			int i = 0;

			for (Vec3i vec : entry.getValue()) {
				vecTag.putLong(String.valueOf(i), BlockPos.asLong(vec.getX(), vec.getY(), vec.getZ()));

				++i;
			}

			pointTag.put("vecs", vecTag);

			dataTag.put(String.valueOf(k), pointTag);

			++k;
		}

		tag.put("data", dataTag);
	}

	/** Deserializes this {@link WorldBridgeComponent} from a {@link CompoundTag}. */
	@Override
	public void readFromNbt(CompoundTag tag) {
		CompoundTag dataTag = tag.getCompound("data");

		for (String key : dataTag.getKeys()) {
			CompoundTag pointTag = dataTag.getCompound(key);
			CompoundTag vecTag = pointTag.getCompound("vecs");

			long pos = pointTag.getLong("pos");

			for (String vecKey : vecTag.getKeys()) {
				add(pos, BlockPos.fromLong(vecTag.getLong(vecKey)));
			}
		}
	}

	/** Returns the {@link WorldBridgeComponent} of the given {@link V}. */
	@Nullable
	public static <V> WorldBridgeComponent get(V v) {
		try {
			return AstromineComponents.WORLD_BRIDGE_COMPONENT.get(v);
		} catch (Exception justShutUpAlready) {
			return null;
		}
	}
}
