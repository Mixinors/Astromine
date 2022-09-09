/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
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

package com.github.mixinors.astromine.common.component.world;

import com.github.mixinors.astromine.common.util.VoxelShapeUtils;
import com.github.mixinors.astromine.registry.common.AMComponents;
import com.google.common.collect.Sets;
import dev.onyxstudios.cca.api.v3.component.Component;
import it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap;
import net.minecraft.block.Block;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtLongArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public final class HoloBridgesComponent implements Component {
	private static final String POSITION_KEY = "Positions";
	private static final String VECTORS_KEY = "Vectors";
	private static final String DATA_KEY = "Data";
	
	private final Long2ObjectArrayMap<Set<Vec3i>> entries = new Long2ObjectArrayMap<>();
	
	private final Long2ObjectArrayMap<VoxelShape> cache = new Long2ObjectArrayMap<>();
	
	private final World world;

	@Nullable
	public static <V> HoloBridgesComponent get(V v) {
		try {
			return AMComponents.HOLO_BRIDGES.get(v);
		} catch (Exception justShutUpAlready) {
			return null;
		}
	}
	
	public HoloBridgesComponent(World world) {
		this.world = world;
	}
	
	public World getWorld() {
		return world;
	}
	
	public void add(BlockPos pos, Vec3i vec) {
		add(pos.asLong(), vec);
	}
	
	public void add(long pos, Vec3i top) {
		entries.computeIfAbsent(pos, (k) -> Sets.newHashSet());
		
		entries.get(pos).add(top);
		
		cache.remove(pos);
	}
	
	public void remove(BlockPos pos) {
		remove(pos.asLong());
	}
	
	public void remove(long pos) {
		entries.remove(pos);
		
		cache.remove(pos);
	}
	
	public Set<Vec3i> get(BlockPos pos) {
		return get(pos.asLong());
	}
	
	public Set<Vec3i> get(long pos) {
		return entries.getOrDefault(pos, Sets.newHashSet());
	}
	
	public VoxelShape getShape(BlockPos pos) {
		return getShape(pos.asLong());
	}
	
	public VoxelShape getShape(long pos) {
		var vectors = get(pos);
		
		if (vectors == null) {
			return VoxelShapes.fullCube();
		}
		
		if (cache.containsKey(pos)) {
			return cache.get(pos);
		}
		
		var shape = getShape(vectors);
		
		cache.put(pos, shape);
		
		return shape;
	}
	
	private VoxelShape getShape(Set<Vec3i> vecs) {
		var shape = VoxelShapes.empty();
		
		var isX = vecs.stream().allMatch(vec -> vec.getX() == 0);
		var isZ = vecs.stream().allMatch(vec -> vec.getZ() == 0);
		
		var isNegativeX = false;
		var isNegativeZ = false;
		
		for (var vec : vecs) {
			if (isX && (vec.getZ() == 0)) {
				continue;
			}
			if (isZ && (vec.getX() == 0)) {
				continue;
			}
			
			if (vec.getX() < 0) {
				isNegativeX = true;
			}
			if (vec.getZ() < 0) {
				isNegativeZ = true;
			}
			
			var startX = isX ? 0 : Math.abs(vec.getX());
			var startY = vec.getY() < 0 ? 16 - Math.abs(vec.getY()) : Math.abs(vec.getY());
			var startZ = isZ ? 0 : Math.abs(vec.getZ());
			
			var endX = isX ? 16 : startX + 1;
			var endY = startY + 1;
			var endZ = isZ ? 16 : startZ + 1;
			
			if (startX > endX) {
				var copy = startX;
				startX = endX;
				endX = copy;
			}
			
			if (startZ > endZ) {
				var copy = startZ;
				startZ = endZ;
				endZ = copy;
			}
			
			shape = VoxelShapes.union(shape, Block.createCuboidShape(startX, startY, startZ, endX, endY, endZ));
		}
		
		if (isNegativeX || isNegativeZ) {
			return VoxelShapeUtils.rotate(Direction.Axis.Y, Math.toRadians(180), shape);
		}
		
		return shape;
	}
	
	@Override
	public void writeToNbt(@NotNull NbtCompound nbt) {
		var dataList = new NbtList();
		
		for (var entry : entries.long2ObjectEntrySet()) {
			var pointData = new NbtCompound();
			
			var vecs = new long[entry.getValue().size()];
			
			var i = 0;
			
			for (var vec : entry.getValue()) {
				vecs[i++] = BlockPos.asLong(vec.getX(), vec.getY(), vec.getZ());
			}
			
			pointData.putLong(POSITION_KEY, entry.getLongKey());
			pointData.put(VECTORS_KEY, new NbtLongArray(vecs));
			
			dataList.add(pointData);
		}
		
		nbt.put(DATA_KEY, dataList);
	}
	
	@Override
	public void readFromNbt(NbtCompound nbt) {
		var dataTag = nbt.getList(DATA_KEY, NbtElement.COMPOUND_TYPE);
		
		for (var pointTag : dataTag) {
			var vecs = ((NbtCompound) pointTag).getLongArray(VECTORS_KEY);
			
			var pos = ((NbtCompound) pointTag).getLong(POSITION_KEY);
			
			for (var vec : vecs) {
				add(pos, BlockPos.fromLong(vec));
			}
		}
	}
}
