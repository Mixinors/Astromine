package com.github.mixinors.astromine.common.component.world;

import com.github.mixinors.astromine.common.util.VoxelShapeUtils;
import com.github.mixinors.astromine.registry.common.AMComponents;
import com.google.common.collect.Sets;
import dev.architectury.utils.NbtType;
import dev.onyxstudios.cca.api.v3.component.Component;
import it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap;
import net.minecraft.block.Block;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtLongArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Set;


public final class WorldHoloBridgeComponent implements Component {
	private final Long2ObjectArrayMap<Set<Vec3i>> entries = new Long2ObjectArrayMap<>();
	
	private final Long2ObjectArrayMap<VoxelShape> cache = new Long2ObjectArrayMap<>();
	
	private final World world;
	
	public WorldHoloBridgeComponent(World world) {
		this.world = world;
	}
	
	@Nullable
	public static <V> WorldHoloBridgeComponent get(V v) {
		try {
			return AMComponents.WORLD_BRIDGE_COMPONENT.get(v);
		} catch (Exception justShutUpAlready) {
			return null;
		}
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
	public void writeToNbt(NbtCompound tag) {
		var dataTag = new NbtList();
		
		for (var entry : entries.long2ObjectEntrySet()) {
			var pointTag = new NbtCompound();
			var vecs = new long[entry.getValue().size()];
			
			var i = 0;
			
			for (var vec : entry.getValue()) {
				vecs[i++] = BlockPos.asLong(vec.getX(), vec.getY(), vec.getZ());
			}
			
			pointTag.putLong("Positions", entry.getLongKey());
			pointTag.put("Vectors", new NbtLongArray(vecs));
			
			dataTag.add(pointTag);
		}
		
		tag.put("Data", dataTag);
	}
	
	@Override
	public void readFromNbt(NbtCompound tag) {
		var dataTag = tag.getList("Data", NbtType.COMPOUND);
		
		for (var pointTag : dataTag) {
			var vecs = ((NbtCompound) pointTag).getLongArray("Vectors");
			
			var pos = ((NbtCompound) pointTag).getLong("Position");
			
			for (var vec : vecs) {
				add(pos, BlockPos.fromLong(vec));
			}
		}
	}
}
