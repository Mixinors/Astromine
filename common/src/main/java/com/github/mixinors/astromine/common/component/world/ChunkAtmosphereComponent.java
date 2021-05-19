/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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

import com.github.mixinors.astromine.common.component.Component;
import com.github.mixinors.astromine.common.component.ServerTickingComponent;
import com.github.mixinors.astromine.registry.common.AMNetworks;

import me.shedaniel.architectury.networking.NetworkManager;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.WorldChunk;

import com.github.mixinors.astromine.client.atmosphere.ClientAtmosphereManager;
import com.github.mixinors.astromine.common.volume.fluid.FluidVolume;
import com.github.mixinors.astromine.registry.common.AMConfig;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A {@link Component} which stores information about
 * a {@link Chunk}'s atmosphere.
 * <p>
 * Serialization and deserialization methods are provided for:
 * - {@link CompoundTag} - through {@link #toTag(CompoundTag)} and {@link #fromTag(CompoundTag)}.
 */
public final class ChunkAtmosphereComponent implements Component, ServerTickingComponent {
	private final List<Direction> directions = Lists.newArrayList(Direction.values());

	private final Map<BlockPos, FluidVolume> volumes = new ConcurrentHashMap<>();

	private final World world;

	private final Chunk chunk;

	private int atmosphereTickCounter = 0;

	/**
	 * Instantiates a {@link ChunkAtmosphereComponent}.
	 */
	public ChunkAtmosphereComponent(Chunk chunk) {
		if (chunk instanceof WorldChunk) {
			this.world = ((WorldChunk) chunk).getWorld();
			this.chunk = chunk;
		} else {
			this.world = null;
			this.chunk = null;
		}
	}

	/**
	 * Returns this component's world.
	 */
	public World getWorld() {
		return world;
	}

	/**
	 * Returns this component's chunk.
	 */
	public Chunk getChunk() {
		return chunk;
	}

	/**
	 * Returns this component's contents.
	 */
	public Map<BlockPos, FluidVolume> getVolumes() {
		return volumes;
	}

	/**
	 * Returns the volume at the given position, defaulting to {@link FluidVolume#ofEmpty()}.
	 */
	public FluidVolume get(BlockPos position) {
		if (world == null)
			return FluidVolume.ofEmpty();

		return volumes.getOrDefault(position, FluidVolume.ofEmpty());
	}

	/**
	 * Removes the volume at the given position.
	 */
	public void remove(BlockPos blockPos) {
		if (world == null)
			return;

		volumes.remove(blockPos);

		if (!world.isClient) {
			NetworkManager.sendToPlayers((Iterable<ServerPlayerEntity>) world.getPlayers(), AMNetworks.GAS_REMOVED, ClientAtmosphereManager.ofGasRemoved(blockPos));
		}
	}

	/**
	 * Adds the given volume at the specified position.
	 */
	public void add(BlockPos blockPos, FluidVolume volume) {
		if (world == null) return;

		volumes.put(blockPos, volume);

		if (!world.isClient) {
			NetworkManager.sendToPlayers((Iterable<ServerPlayerEntity>) world.getPlayers(), AMNetworks.GAS_ADDED, ClientAtmosphereManager.ofGasAdded(blockPos, volume));
		}
	}

	/**
	 * Asserts whether a {@link BlockPos} is within a {@link ChunkPos} or not.
	 */
	public static boolean isInChunk(ChunkPos chunkPos, BlockPos pos) {
		return pos.getX() >= chunkPos.getStartX() && pos.getX() <= chunkPos.getEndX() && pos.getZ() >= chunkPos.getStartZ() && pos.getZ() <= chunkPos.getEndZ();
	}

	/**
	 * Returns the nearest neighbouring {@link ChunkPos} towards the given {@link BlockPos}.
	 */
	public static ChunkPos getNeighborFromPos(ChunkPos chunkPos, BlockPos pos) {
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
	public ChunkPos getNeighborFromPos(BlockPos pos) {
		if (world == null) return new ChunkPos(0, 0);

		return getNeighborFromPos(chunk.getPos(), pos);
	}

	/**
	 * Asserts whether the given position is within
	 * this component's chunk or not.
	 */
	public boolean isInChunk(BlockPos pos) {
		if (world == null) return false;

		return isInChunk(chunk.getPos(), pos);
	}

	/**
	 * Asserts whether gas may traverse from the given
	 * {@link BlockState} centerState, at {@link BlockPos} centerPos,
	 * into {@link BlockState} sideState, at {@link BlockPos} sidePos,
	 * considering {@link FluidVolume} centerVolume and {@link FluidVolume} sideVolume,
	 * from the given {@link Direction} when propagating - that is to say,
	 * when simulating natural gas movement.
	 */
	public boolean isTraversableForPropagation(BlockState centerState, BlockPos centerPos, BlockState sideState, BlockPos sidePos, FluidVolume centerVolume, FluidVolume sideVolume, Direction direction) {
		if (world == null) return false;

		return !(Registry.BLOCK.getId(sideState.getBlock()).toString().equals("astromine:airlock") && !sideState.get(Properties.POWERED))
		       && (sideState.isAir() || !sideState.isSideSolidFullSquare(world, sidePos, direction.getOpposite()))
		       && (centerState.isAir() || !centerState.isSideSolidFullSquare(world, centerPos, direction)) && (sideVolume.isEmpty() || sideVolume.test(centerVolume.getFluid()))
		       && (centerVolume.hasStored(FluidVolume.BOTTLE) && !sideState.isOpaqueFullCube(world, centerPos))
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
	public boolean isTraversableForDisplacement(BlockState centerState, BlockPos centerPos, BlockState sideState, BlockPos sidePos, FluidVolume centerVolume, FluidVolume sideVolume, Direction direction) {
		if (world == null) return false;

		return !(Registry.BLOCK.getId(sideState.getBlock()).toString().equals("astromine:airlock") && !sideState.get(Properties.POWERED))
		       && (sideState.isAir() || !sideState.isSideSolidFullSquare(world, sidePos, direction.getOpposite()))
		       && (centerState.isAir() || !centerState.isSideSolidFullSquare(world, centerPos, direction)) && (sideVolume.isEmpty() || sideVolume.test(centerVolume.getFluid()))
		       && (!sideState.isOpaqueFullCube(world, centerPos));
	}

	/**
	 * Override behavior to implement atmospheric logic.
	 * <p>
	 * {@link #atmosphereTickCounter} is used to count
	 * time between simulations.
	 * <p>
	 * Effectively, this component's volumes are iterated over,
	 * balancing them between their six neighboring volumes.
	 * <p>
	 * The direction of movement is shuffled to avoid propagating
	 * volumes in straight lines.
	 */
	@Override
	public void serverTick() {
		if (world == null)
			return;

		if (atmosphereTickCounter < AMConfig.get().gasTickRate) {
			atmosphereTickCounter++;
		} else {
			atmosphereTickCounter = 0;
		}

		if (!(atmosphereTickCounter == AMConfig.get().gasTickRate && world.isChunkLoaded(chunk.getPos().x, chunk.getPos().z)))
			return;

		for (Map.Entry<BlockPos, FluidVolume> pair : volumes.entrySet()) {
			BlockPos centerPos = pair.getKey();

			FluidVolume centerVolume = pair.getValue();

			centerVolume.take(AMConfig.get().gasDecayAmount);

			if (centerVolume.isEmpty()) {
				remove(centerPos);
			}

			Collections.shuffle(directions);

			for (var direction : directions) {
				BlockPos sidePos = centerPos.offset(direction);

				if (isInChunk(sidePos)) {
					FluidVolume sideVolume = get(sidePos);

					BlockState sideState = world.getBlockState(sidePos);
					BlockState centerState = world.getBlockState(centerPos);

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
					ChunkPos neighborPos = getNeighborFromPos(sidePos);

					ChunkAtmosphereComponent chunkAtmosphereComponent = ChunkAtmosphereComponent.get(world.getChunk(neighborPos.x, neighborPos.z));

					FluidVolume sideVolume = chunkAtmosphereComponent.get(sidePos);

					BlockState sideState = world.getBlockState(sidePos);
					BlockState centerState = world.getBlockState(centerPos);

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
	 * Serializes this {@link ChunkAtmosphereComponent} to a {@link CompoundTag}.
	 */
	@Override
	public void toTag(CompoundTag tag) {
		if (world == null)
			return;

		CompoundTag dataTag = new CompoundTag();

		int i = 0;

		for (Map.Entry<BlockPos, FluidVolume> entry : volumes.entrySet()) {
			CompoundTag pointTag = new CompoundTag();
			pointTag.putLong("pos", entry.getKey().asLong());
			pointTag.put("volume", entry.getValue().toTag());

			dataTag.put(String.valueOf(i), pointTag);
			++i;
		}

		tag.put("data", dataTag);
	}

	/**
	 * Deserializes this {@link ChunkAtmosphereComponent} from a {@link CompoundTag}.
	 */
	@Override
	public void fromTag(CompoundTag tag) {
		if (world == null)
			return;

		CompoundTag dataTag = tag.getCompound("data");

		for (String key : dataTag.getKeys()) {
			CompoundTag pointTag = dataTag.getCompound(key);

			volumes.put(BlockPos.fromLong(pointTag.getLong("pos")), FluidVolume.fromTag(pointTag.getCompound("volume")));
		}
	}

	/**
	 * Returns the {@link ChunkAtmosphereComponent} of the given {@link V}.
	 */
	@Nullable
	public static <V> ChunkAtmosphereComponent get(V v) {
		throw new UnsupportedOperationException("This method belongs to the common module, and must be overwritten!");
	}
}
