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

import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.WorldChunk;

import com.github.chainmailstudios.astromine.client.cca.ClientAtmosphereManager;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.common.volume.fraction.Fraction;
import com.github.chainmailstudios.astromine.registry.AstromineComponents;
import com.github.chainmailstudios.astromine.registry.AstromineConfig;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChunkAtmosphereComponent implements Component, ServerTickingComponent {
	private final List<Direction> directions = Lists.newArrayList(Direction.values());

	private final Map<BlockPos, FluidVolume> volumes = new ConcurrentHashMap<>();

	private final World world;
	private final Chunk chunk;

	public int atmosphereTickCounter = 0;

	public ChunkAtmosphereComponent(Chunk chunk) {
		if (chunk instanceof WorldChunk) {
			this.world = ((WorldChunk) chunk).getWorld();
			this.chunk = chunk;
		} else {
			this.world = null;
			this.chunk = null;
		}
	}

	public static boolean isInChunk(ChunkPos chunkPos, BlockPos pos) {
		return pos.getX() >= chunkPos.getStartX() && pos.getX() <= chunkPos.getEndX() && pos.getZ() >= chunkPos.getStartZ() && pos.getZ() <= chunkPos.getEndZ();
	}

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

	@Nullable
	public static <V> ChunkAtmosphereComponent get(V v) {
		try {
			return AstromineComponents.CHUNK_ATMOSPHERE_COMPONENT.get(v);
		} catch (Exception justShutUpAlready) {
			return null;
		}
	}

	public World getWorld() {
		return world;
	}

	public Chunk getChunk() {
		return chunk;
	}

	public Map<BlockPos, FluidVolume> getVolumes() {
		return volumes;
	}

	public FluidVolume get(BlockPos position) {
		if (world == null)
			return FluidVolume.empty();

		return volumes.getOrDefault(position, FluidVolume.empty());
	}

	public void add(BlockPos blockPos, FluidVolume volume) {
		if (world == null)
			return;

		volumes.put(blockPos, volume);

		if (!world.isClient) {
			world.getPlayers().forEach((player) -> {
				ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, ClientAtmosphereManager.GAS_ADDED, ClientAtmosphereManager.ofGasAdded(blockPos, volume));
			});
		}
	}

	public void remove(BlockPos blockPos) {
		if (world == null)
			return;

		volumes.remove(blockPos);

		if (!world.isClient) {
			world.getPlayers().forEach((player) -> {
				ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, ClientAtmosphereManager.GAS_REMOVED, ClientAtmosphereManager.ofGasRemoved(blockPos));
			});
		}
	}

	@Override
	public void serverTick() {
		if (world == null)
			return;

		if (atmosphereTickCounter < AstromineConfig.get().gasTickRate) {
			atmosphereTickCounter++;
		} else {
			atmosphereTickCounter = 0;
		}

		if (!(atmosphereTickCounter == AstromineConfig.get().gasTickRate && world.isChunkLoaded(chunk.getPos().x, chunk.getPos().z)))
			return;

		for (Map.Entry<BlockPos, FluidVolume> pair : volumes.entrySet()) {
			BlockPos centerPos = pair.getKey();

			FluidVolume centerVolume = pair.getValue();

			centerVolume.minus(Fraction.of(AstromineConfig.get().gasDecayNumerator, AstromineConfig.get().gasDecayDenominator));

			if (centerVolume.isEmpty()) {
				remove(centerPos);
			}

			Collections.shuffle(directions);

			for (Direction direction : directions) {
				BlockPos sidePos = centerPos.offset(direction);

				if (isInChunk(sidePos)) {
					FluidVolume sideVolume = get(sidePos);

					BlockState sideState = world.getBlockState(sidePos);
					BlockState centerState = world.getBlockState(centerPos);

					if (isTraversableForPropagation(centerState, centerPos, sideState, sidePos, centerVolume, sideVolume, direction)) {
						if (world.isAir(centerPos)) {
							centerVolume.add(sideVolume, Fraction.BOTTLE);
						} else if (!centerState.isSideSolidFullSquare(world, centerPos, direction)) {
							centerVolume.add(sideVolume, Fraction.BOTTLE);
						} else {
							centerVolume.add(sideVolume, centerVolume.getAmount());
						}

						add(sidePos, sideVolume);
					}
				} else {
					ChunkPos neighborPos = getNeighborFromPos(sidePos);

					ChunkAtmosphereComponent chunkAtmosphereComponent = AstromineComponents.CHUNK_ATMOSPHERE_COMPONENT.get(world.getChunk(neighborPos.x, neighborPos.z));

					FluidVolume sideVolume = chunkAtmosphereComponent.get(sidePos);

					BlockState sideState = world.getBlockState(sidePos);
					BlockState centerState = world.getBlockState(centerPos);

					if (isTraversableForPropagation(centerState, centerPos, sideState, sidePos, centerVolume, sideVolume, direction)) {
						if (world.isAir(centerPos)) {
							centerVolume.add(sideVolume, Fraction.BOTTLE);
						} else if (!world.getBlockState(centerPos).isSideSolidFullSquare(world, centerPos, direction)) {
							centerVolume.add(sideVolume, Fraction.BOTTLE);
						} else {
							centerVolume.add(sideVolume, centerVolume.getAmount());
						}

						chunkAtmosphereComponent.add(sidePos, sideVolume);
					}
				}
			}
		}
	}

	public boolean isInChunk(BlockPos pos) {
		if (world == null)
			return false;

		return isInChunk(chunk.getPos(), pos);
	}

	public ChunkPos getNeighborFromPos(BlockPos pos) {
		if (world == null)
			return new ChunkPos(0, 0);

		return getNeighborFromPos(chunk.getPos(), pos);
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
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

	@Override
	public void readFromNbt(CompoundTag tag) {
		if (world == null)
			return;

		CompoundTag dataTag = tag.getCompound("data");

		for (String key : dataTag.getKeys()) {
			CompoundTag pointTag = dataTag.getCompound(key);

			volumes.put(BlockPos.fromLong(pointTag.getLong("pos")), FluidVolume.fromTag(pointTag.getCompound("volume")));
		}
	}

	public boolean isTraversableForPropagation(BlockState centerState, BlockPos centerPos, BlockState sideState, BlockPos sidePos, FluidVolume centerVolume, FluidVolume sideVolume, Direction direction) {
		if (world == null)
			return false;

		return !(Registry.BLOCK.getId(sideState.getBlock()).toString().equals("astromine:airlock") && !sideState.get(Properties.POWERED)) && (sideState.isAir() || !sideState.isSideSolidFullSquare(world, sidePos, direction.getOpposite())) && (centerState.isAir() || !centerState
			.isSideSolidFullSquare(world, centerPos, direction)) && (sideVolume.isEmpty() || sideVolume.test(centerVolume.getFluid())) && (centerVolume.hasStored(Fraction.BOTTLE) && !sideState.isOpaqueFullCube(world, centerPos)) && sideVolume.smallerThan(centerVolume
				.getAmount());
	}

	public boolean isTraversableForDisplacement(BlockState centerState, BlockPos centerPos, BlockState sideState, BlockPos sidePos, FluidVolume centerVolume, FluidVolume sideVolume, Direction direction) {
		if (world == null)
			return false;

		return !(Registry.BLOCK.getId(sideState.getBlock()).toString().equals("astromine:airlock") && !sideState.get(Properties.POWERED)) && (sideState.isAir() || !sideState.isSideSolidFullSquare(world, sidePos, direction.getOpposite())) && (centerState.isAir() || !centerState
			.isSideSolidFullSquare(world, centerPos, direction)) && (sideVolume.isEmpty() || sideVolume.test(centerVolume.getFluid())) && (!sideState.isOpaqueFullCube(world, centerPos));
	}
}
