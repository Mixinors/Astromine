package com.github.mixinors.astromine.cardinalcomponents.common.component.base;

import com.github.mixinors.astromine.cardinalcomponents.common.component.Component;
import com.github.mixinors.astromine.techreborn.common.util.VoxelShapeUtils;
import com.github.mixinors.astromine.registry.common.AMComponents;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap;
import me.shedaniel.architectury.utils.NbtType;
import net.minecraft.block.Block;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

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
 * - {@link CompoundTag} - through {@link #toTag(CompoundTag)} and {@link #fromTag(CompoundTag)}.
 */
public interface BridgeComponent extends Component {
	/** Returns the {@link BridgeComponent} of the given {@link V}. */
	@Nullable
	static <V> BridgeComponent from(V v) {
		return null;
	}
	
	/** Instantiates an {@link BridgeComponent}. */
	static BridgeComponent of(World world) {
		return new BridgeComponentImpl(world);
	}
	
	/** Returns this component's world. */
	World getWorld();
	
	/** Returns this component's entries. */
	Long2ObjectArrayMap<Set<Vec3i>> getEntries();
	
	/** Returns this component's cache. */
	Long2ObjectArrayMap<VoxelShape> getCache();
	
	/** Adds the given step at the specified {@link BlockPos}'s long representation. */
	default void add(BlockPos pos, Vec3i vec) {
		add(pos.asLong(), vec);
	}
	
	/** Adds the given step at the specified position. */
	default void add(long pos, Vec3i top) {
		getEntries().computeIfAbsent(pos, (k) -> Sets.newHashSet());
		getEntries().get(pos).add(top);
		
		getCache().remove(pos);
	}
	
	/** Removes the steps at the specified {@link BlockPos}'s long representation. */
	default void remove(BlockPos pos) {
		remove(pos.asLong());
	}
	
	/** Removes the steps at the specified position. */
	default void remove(long pos) {
		getEntries().remove(pos);
		
		getCache().remove(pos);
	}
	
	/** Returns the steps at the given {@link BlockPos}'s long representation. */
	default Set<Vec3i> get(BlockPos pos) {
		return get(pos.asLong());
	}
	
	/** Returns the sTeps at the given position. */
	default Set<Vec3i> get(long pos) {
		return getEntries().getOrDefault(pos, Sets.newHashSet());
	}
	
	/** Returns the {@link VoxelShape} at the given {@link BlockPos}'s long representation. */
	default VoxelShape getShape(BlockPos pos) {
		return getShape(pos.asLong());
	}
	
	/** Returns the {@link VoxelShape} at the given position. */
	default VoxelShape getShape(long pos) {
		if (getCache().containsKey(pos)) return getCache().get(pos);
		
		var vectors = get(pos);
		
		if (vectors == null)
			return VoxelShapes.fullCube();
		
		var shape = getShape(vectors);
		
		getCache().put(pos, shape);
		
		return shape;
	}
	
	/** Returns the {@link VoxelShape} formed by the given {@link Set} of steps.
	 * I made this work months ago; and I don't know how. Accept it, or suffer. */
	private VoxelShape getShape(Set<Vec3i> vectors) {
		var shape = VoxelShapes.empty();
		
		var a = vectors.stream().allMatch(vec -> vec.getZ() == 0);
		var b = vectors.stream().allMatch(vec -> vec.getX() == 0);
		var c = false;
		var d = false;
		
		for (var vector : vectors) {
			if (!c && vector.getX() < 0)
				c = true;
			if (!d && vector.getZ() < 0)
				d = true;
			
			shape = VoxelShapes.union(shape, Block.createCuboidShape(Math.abs(vector.getX()), Math.abs(vector.getY()) - 1, Math.abs(vector.getZ()), b ? 16 : Math.abs(vector.getX() + 1), Math.abs(vector.getY()) + 1, a ? 16 : Math.abs(vector.getZ() + 1)));
		}
		
		if (c || d) {
			return VoxelShapeUtils.rotate(Direction.Axis.Y, Math.toRadians(180), shape);
		}
		
		return shape;
	}
	
	/** Serializes this {@link BridgeComponent} to a {@link CompoundTag}. */
	@Override
	default void toTag(CompoundTag tag) {
		var dataTag = new ListTag();
		
		for (var entry : getEntries().long2ObjectEntrySet()) {
			var pointTag = new CompoundTag();
			var vectors = new long[entry.getValue().size()];
			
			int i = 0;
			
			for (var vector : entry.getValue()) {
				vectors[i++] = BlockPos.asLong(vector.getX(), vector.getY(), vector.getZ());
			}
			
			pointTag.putLong("Positions", entry.getLongKey());
			pointTag.put("Vectors", new LongArrayTag(vectors));
			
			dataTag.add(pointTag);
		}
		
		tag.put("Data", dataTag);
	}
	
	/** Deserializes this {@link BridgeComponent} from a {@link CompoundTag}. */
	@Override
	default void fromTag(CompoundTag tag) {
		var dataTag = tag.getList("Data", NbtType.COMPOUND);
		
		for (var pointTag : dataTag) {
			var vectors = ((CompoundTag) pointTag).getLongArray("Vectors");
			
			var position = ((CompoundTag) pointTag).getLong("Position");
			
			for (var vector : vectors) {
				add(position, BlockPos.fromLong(vector));
			}
		}
	}
	
	/** Returns this component's {@link Identifier}. */
	@Override
	default Identifier getId() {
		return AMComponents.BRIDGE;
	}
}
