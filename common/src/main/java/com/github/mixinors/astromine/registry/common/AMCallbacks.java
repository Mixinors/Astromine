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

package com.github.mixinors.astromine.registry.common;

import com.github.mixinors.astromine.common.event.ServerChunkManagerEvents;


import com.github.mixinors.astromine.common.world.generation.space.EarthSpaceChunkGenerator;
import me.shedaniel.architectury.event.events.BlockEvent;
import me.shedaniel.architectury.event.events.TickEvent;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import com.github.mixinors.astromine.common.block.transfer.TransferType;
import com.github.mixinors.astromine.common.component.world.ChunkAtmosphereComponent;
import com.github.mixinors.astromine.common.component.world.WorldNetworkComponent;
import com.github.mixinors.astromine.common.screenhandler.base.block.ComponentBlockEntityScreenHandler;
import com.github.mixinors.astromine.common.volume.fluid.FluidVolume;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;

public class AMCallbacks {
	public static void init() {
		BlockEvent.PLACE.register((world, pos, blockState, entity) -> {
			ChunkAtmosphereComponent atmosphereComponent = ChunkAtmosphereComponent.get(world.getChunk(pos));

			if (atmosphereComponent != null) {
				BlockPos centerPos = pos;
				BlockState centerState = world.getBlockState(centerPos);
				FluidVolume centerVolume = atmosphereComponent.get(centerPos);

				List<Direction> directions = Lists.newArrayList(Direction.values());

				Collections.shuffle(directions);

				for (var direction : directions) {
					BlockPos sidePos = pos.offset(direction);
					BlockState sideState = world.getBlockState(sidePos);

					ChunkAtmosphereComponent sideAtmosphereComponent = atmosphereComponent;

					if (!atmosphereComponent.isInChunk(sidePos)) {
						sideAtmosphereComponent = ChunkAtmosphereComponent.get(world.getChunk(sidePos));
					}

					FluidVolume sideVolume = sideAtmosphereComponent.get(sidePos);

					if (atmosphereComponent.isTraversableForDisplacement(centerState, centerPos, sideState, sidePos, centerVolume, sideVolume, direction)) {
						sideVolume.take(centerVolume);
						sideAtmosphereComponent.add(sidePos, sideVolume);

						break;

					}
				}

				atmosphereComponent.remove(centerPos);
			}

			return ActionResult.PASS;
		});

		TickEvent.SERVER_PRE.register((server) -> {
			for (PlayerEntity playerEntity : server.getPlayerManager().getPlayerList()) {
				if (playerEntity.currentScreenHandler instanceof ComponentBlockEntityScreenHandler) {
					ComponentBlockEntityScreenHandler screenHandler = (ComponentBlockEntityScreenHandler) playerEntity.currentScreenHandler;

					if (screenHandler.getBlockEntity() != null) {
						screenHandler.getBlockEntity().syncData();
						break;
					}
				}
			}
		});

		TickEvent.SERVER_WORLD_PRE.register((world -> {
			WorldNetworkComponent component = WorldNetworkComponent.get(world);

			if (component != null) {
				component.tick();
			}
		}));

		TransferEntryEvents.EVENT.register((entry) -> {
			if (entry.getComponentKey() == AMComponents.ENERGY) {
				for (var direction : Direction.values()) {
					entry.set(direction, TransferType.INPUT_OUTPUT);
				}
			}
		});
		
		ServerChunkManagerEvents.INITIALIZATION.register(manager -> {
			if (manager.chunkGenerator instanceof EarthSpaceChunkGenerator) {
				manager.chunkGenerator = ((EarthSpaceChunkGenerator) manager.chunkGenerator).withSeedCommon(((ServerWorld) manager.getWorld()).getSeed());
			}
		});
		
		// ServerChunkManagerCallback.EVENT.register(manager -> {
		// 	}
		// });
		//
		// ServerChunkManagerCallback.EVENT.register(manager -> {
		// 	}
		// });
		//
		// ServerChunkManagerCallback.EVENT.register(manager -> {
		// 	}
		// });
		//
		// ServerChunkManagerCallback.EVENT.register(manager -> {
		// 	}
		// });
	}
}
