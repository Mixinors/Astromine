package com.github.mixinors.astromine.common.component.base;

import com.github.mixinors.astromine.client.atmosphere.ClientAtmosphereManager;
import com.github.mixinors.astromine.common.component.Component;
import com.github.mixinors.astromine.common.volume.fluid.FluidVolume;
import com.github.mixinors.astromine.registry.common.AMBlocks;
import com.github.mixinors.astromine.registry.common.AMComponents;
import com.github.mixinors.astromine.registry.common.AMConfig;
import com.github.mixinors.astromine.registry.common.AMNetworks;
import me.shedaniel.architectury.annotations.ExpectPlatform;
import me.shedaniel.architectury.networking.NetworkManager;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * A {@link Component} which stores information about
 * a {@link Chunk}'s atmosphere.
 * <p>
 * Serialization and deserialization methods are provided for:
 * - {@link CompoundTag} - through {@link #toTag(CompoundTag)} and {@link #fromTag(CompoundTag)}.
 */
public interface AtmosphereComponent extends Component, Component.ServerTicking {
	/** Returns the {@link AtmosphereComponent} of the given {@link V}. */
	@Nullable
	static <V> AtmosphereComponent from(V v) {
		return fromPost(v);
	}
	
	@ExpectPlatform
	static <V> AtmosphereComponent fromPost(V v) {
		throw new AssertionError();
	}
	
	/** Instantiates an {@link AtmosphereComponent}. */
	static AtmosphereComponent of(Chunk chunk) {
		return new AtmosphereComponentImpl(chunk);
	}
	
	/**
	 * Returns this component's world.
	 */
	World getWorld();
	
	/**
	 * Returns this component's chunk.
	 */
	Chunk getChunk();
	
	/**
	 * Returns this component's contents.
	 */
	Map<BlockPos, FluidVolume> getVolumes();
	
	/** Returns this component's age. */
	long getTickCount();
	
	/**
	 * Returns the volume at the given position, defaulting to {@link FluidVolume#ofEmpty()}.
	 */
	default FluidVolume get(BlockPos pos) {
		if (getWorld() == null)
			return FluidVolume.ofEmpty();
		
		return getVolumes().getOrDefault(pos, FluidVolume.ofEmpty());
	}
	
	/**
	 * Removes the volume at the given position.
	 */
	default void remove(BlockPos blockPos) {
		if (getWorld() == null)
			return;
		
		getVolumes().remove(blockPos);
		
		if (!getWorld().isClient) {
			NetworkManager.sendToPlayers((Iterable<ServerPlayerEntity>) getWorld().getPlayers(), AMNetworks.GAS_REMOVED, ClientAtmosphereManager.ofGasRemoved(blockPos));
		}
	}
	
	/**
	 * Adds the given volume at the specified position.
	 */
	default void add(BlockPos blockPos, FluidVolume volume) {
		if (getWorld() == null) return;
		
		getVolumes().put(blockPos, volume);
		
		if (!getWorld().isClient) {
			NetworkManager.sendToPlayers((Iterable<ServerPlayerEntity>) getWorld().getPlayers(), AMNetworks.GAS_ADDED, ClientAtmosphereManager.ofGasAdded(blockPos, volume));
		}
	}
	
	/**
	 * Asserts whether a {@link BlockPos} is within a {@link ChunkPos} or not.
	 */
	static boolean isInChunk(ChunkPos chunkPos, BlockPos pos) {
		return pos.getX() >= chunkPos.getStartX() && pos.getX() <= chunkPos.getEndX() && pos.getZ() >= chunkPos.getStartZ() && pos.getZ() <= chunkPos.getEndZ();
	}
	
	/**
	 * Returns the nearest neighbouring {@link ChunkPos} towards the given {@link BlockPos}.
	 */
	static ChunkPos getNeighborFromPos(ChunkPos chunkPos, BlockPos pos) {
		if (pos.getX() < chunkPos.getStartX()) {
			return new ChunkPos(chunkPos.x - 1, chunkPos.z);
		} else if (pos.getX() > chunkPos.getEndX()) {
			return new ChunkPos(chunkPos.x + 1, chunkPos.z);
		} else if (pos.getZ() < chunkPos.getStartZ()) {
			return new ChunkPos(chunkPos.x, chunkPos.z - 1);
		} else if (pos.getZ() > chunkPos.getEndZ()) {
			return new ChunkPos(chunkPos.x, chunkPos.z + 1);
		}
		return chunkPos;
	}
	
	/**
	 * Returns {@link #getNeighborFromPos(ChunkPos, BlockPos)},
	 * based on this component's chunk's position.
	 */
	default ChunkPos getNeighborFromPos(BlockPos pos) {
		if (getWorld() == null)
			return new ChunkPos(0, 0);
		
		return getNeighborFromPos(getChunk().getPos(), pos);
	}
	
	/**
	 * Asserts whether the given position is within
	 * this component's chunk or not.
	 */
	default boolean isInChunk(BlockPos pos) {
		if (getWorld() == null)
			return false;
		
		return isInChunk(getChunk().getPos(), pos);
	}
	
	/**
	 * Asserts whether gas may traverse from the given
	 * {@link BlockState} centerState, at {@link BlockPos} centerPos,
	 * into {@link BlockState} sideState, at {@link BlockPos} sidePos,
	 * considering {@link FluidVolume} centerVolume and {@link FluidVolume} sideVolume,
	 * from the given {@link Direction} when propagating - that is to say,
	 * when simulating natural gas movement.
	 */
	default boolean isTraversableForPropagation(BlockState centerState, BlockPos centerPos, BlockState sideState, BlockPos sidePos, FluidVolume centerVolume, FluidVolume sideVolume, Direction direction) {
		if (getWorld() == null)
			return false;
		
		return !(sideState.getBlock() == AMBlocks.AIRLOCK && !sideState.get(Properties.POWERED))
				&& (sideState.isAir() || !sideState.isSideSolidFullSquare(getWorld(), sidePos, direction.getOpposite()))
				&& (centerState.isAir() || !centerState.isSideSolidFullSquare(getWorld(), centerPos, direction)) && (sideVolume.isEmpty() || sideVolume.test(centerVolume.getFluid()))
				&& (centerVolume.hasStored(FluidVolume.BOTTLE) && !sideState.isOpaqueFullCube(getWorld(), centerPos))
				&& sideVolume.smallerThan(centerVolume.getAmount());
	}
	
	/**
	 * Asserts whether gas may traverse from the given
	 * {@link BlockState} centerState, at {@link BlockPos} centerPos,
	 * into {@link BlockState} sideState, at {@link BlockPos} sidePos,
	 * considering {@link FluidVolume} centerVolume and {@link FluidVolume} sideVolume,
	 * from the given {@link Direction} when displacing - that is to say,
	 * when a gas is forced out of its position due to, for example, block placement.
	 */
	default boolean isTraversableForDisplacement(BlockState centerState, BlockPos centerPos, BlockState sideState, BlockPos sidePos, FluidVolume centerVolume, FluidVolume sideVolume, Direction direction) {
		if (getWorld() == null)
			return false;
		
		return !(sideState.getBlock() == AMBlocks.AIRLOCK && !sideState.get(Properties.POWERED))
				&& (sideState.isAir() || !sideState.isSideSolidFullSquare(getWorld(), sidePos, direction.getOpposite()))
				&& (centerState.isAir() || !centerState.isSideSolidFullSquare(getWorld(), centerPos, direction)) && (sideVolume.isEmpty() || sideVolume.test(centerVolume.getFluid()))
				&& (!sideState.isOpaqueFullCube(getWorld(), centerPos));
	}
	
	/**
	 * Override behavior to implement atmospheric logic.
	 * <p>
	 * {@link #getTickCount()} is used to count
	 * time between simulations.
	 * <p>
	 * Effectively, this component's volumes are iterated over,
	 * balancing them between their six neighboring volumes.
	 * <p>
	 * The direction of movement is shuffled to avoid propagating
	 * volumes in straight lines.
	 */
	@Override
	default void serverTick() {
		if (getWorld() == null)
			return;
		
		var world = getWorld();
		var chunk = getChunk();
		
		var directions = Arrays.asList(Direction.values());
		
		if (!(getTickCount() == AMConfig.get().gasTickRate && world.isChunkLoaded(chunk.getPos().x, chunk.getPos().z)))
			return;
		
		for (var pair : getVolumes().entrySet()) {
			var centerPos = pair.getKey();
			
			var centerVolume = pair.getValue();
			
			centerVolume.take(AMConfig.get().gasDecayAmount);
			
			if (centerVolume.isEmpty()) {
				remove(centerPos);
			}
			
			Collections.shuffle(directions);
			
			for (var direction : directions) {
				var sidePos = centerPos.offset(direction);
				
				if (isInChunk(sidePos)) {
					var sideVolume = get(sidePos);
					
					var sideState = world.getBlockState(sidePos);
					var centerState = world.getBlockState(centerPos);
					
					if (isTraversableForPropagation(centerState, centerPos, sideState, sidePos, centerVolume, sideVolume, direction)) {
						if (world.isAir(centerPos)) {
							centerVolume.give(sideVolume, FluidVolume.BOTTLE);
						} else if (!centerState.isSideSolidFullSquare(world, centerPos, direction)) {
							centerVolume.give(sideVolume, FluidVolume.BOTTLE);
						} else {
							centerVolume.give(sideVolume, centerVolume.getAmount());
						}
						
						add(sidePos, sideVolume);
					}
				} else {
					var neighborPos = getNeighborFromPos(sidePos);
					
					var chunkAtmosphereComponent = AtmosphereComponent.from(world.getChunk(neighborPos.x, neighborPos.z));
					
					var sideVolume = chunkAtmosphereComponent.get(sidePos);
					
					var sideState = world.getBlockState(sidePos);
					var centerState = world.getBlockState(centerPos);
					
					if (isTraversableForPropagation(centerState, centerPos, sideState, sidePos, centerVolume, sideVolume, direction)) {
						if (world.isAir(centerPos)) {
							centerVolume.give(sideVolume, FluidVolume.BOTTLE);
						} else if (!world.getBlockState(centerPos).isSideSolidFullSquare(world, centerPos, direction)) {
							centerVolume.give(sideVolume, FluidVolume.BOTTLE);
						} else {
							centerVolume.give(sideVolume, centerVolume.getAmount());
						}
						
						chunkAtmosphereComponent.add(sidePos, sideVolume);
					}
				}
			}
		}
	}
	
	/**
	 * Serializes this {@link AtmosphereComponentImpl} to a {@link CompoundTag}.
	 */
	@Override
	default void toTag(CompoundTag tag) {
		if (getWorld() == null)
			return;
		
		var dataTag = new CompoundTag();
		
		int i = 0;
		
		for (var entry : getVolumes().entrySet()) {
			var pointTag = new CompoundTag();
			pointTag.putLong("Position", entry.getKey().asLong());
			pointTag.put("Volume", entry.getValue().toTag());
			
			dataTag.put(String.valueOf(i), pointTag);
			
			++i;
		}
		
		tag.put("Data", dataTag);
	}
	
	/**
	 * Deserializes this {@link AtmosphereComponentImpl} from a {@link CompoundTag}.
	 */
	@Override
	default void fromTag(CompoundTag tag) {
		if (getWorld() == null)
			return;
		
		var dataTag = tag.getCompound("data");
		
		for (var key : dataTag.getKeys()) {
			var pointTag = dataTag.getCompound(key);
			
			getVolumes().put(
					BlockPos.fromLong(pointTag.getLong("Position")),
					FluidVolume.fromTag(pointTag.getCompound("Volume"))
			);
		}
	}
	
	/** Returns this component's {@link Identifier}. */
	@Override
	default Identifier getId() {
		return AMComponents.ATMOSPHERE;
	}
}
