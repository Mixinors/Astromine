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

package com.github.chainmailstudios.astromine.registry;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import com.github.chainmailstudios.astromine.common.block.transfer.TransferType;
import com.github.chainmailstudios.astromine.common.callback.TransferEntryCallback;
import com.github.chainmailstudios.astromine.common.component.world.ChunkAtmosphereComponent;
import com.github.chainmailstudios.astromine.common.component.world.WorldNetworkComponent;
import com.github.chainmailstudios.astromine.common.screenhandler.base.block.ComponentBlockEntityScreenHandler;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import me.shedaniel.cloth.api.common.events.v1.BlockPlaceCallback;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;

public class AstromineCommonCallbacks {
	public static void initialize() {
		BlockPlaceCallback.EVENT.register(((world, pos, state, entity, stack) -> {
			ChunkAtmosphereComponent atmosphereComponent = ChunkAtmosphereComponent.get(world.getChunk(pos));

			if (atmosphereComponent != null) {
				BlockPos centerPos = pos;
				BlockState centerState = world.getBlockState(centerPos);
				FluidVolume centerVolume = atmosphereComponent.get(centerPos);

				List<Direction> directions = Lists.newArrayList(Direction.values());

				Collections.shuffle(directions);

				for (Direction direction : directions) {
					BlockPos sidePos = pos.offset(direction);
					BlockState sideState = world.getBlockState(sidePos);

					ChunkAtmosphereComponent sideAtmosphereComponent = atmosphereComponent;

					if (!atmosphereComponent.isInChunk(sidePos)) {
						sideAtmosphereComponent = ChunkAtmosphereComponent.get(world.getChunk(sidePos));
					}

					FluidVolume sideVolume = sideAtmosphereComponent.get(sidePos);

					if (atmosphereComponent.isTraversableForDisplacement(centerState, centerPos, sideState, sidePos, centerVolume, sideVolume, direction)) {
						sideVolume.moveFrom(centerVolume);
						sideAtmosphereComponent.add(sidePos, sideVolume);

						break;

					}
				}

				atmosphereComponent.remove(centerPos);
			}

			return ActionResult.PASS;
		}));

		ServerTickEvents.START_SERVER_TICK.register((server) -> {
			for (PlayerEntity playerEntity : server.getPlayerManager().getPlayerList()) {
				if (playerEntity.currentScreenHandler instanceof ComponentBlockEntityScreenHandler) {
					ComponentBlockEntityScreenHandler screenHandler = (ComponentBlockEntityScreenHandler) playerEntity.currentScreenHandler;

					if (screenHandler.syncBlockEntity != null) {
						screenHandler.syncBlockEntity.sync();
						break;
					}
				}
			}
		});

		ServerTickEvents.START_WORLD_TICK.register((world -> {
			WorldNetworkComponent component = WorldNetworkComponent.get(world);

			if (component != null) {
				component.tick();
			}
		}));

		TransferEntryCallback.EVENT.register((entry) -> {
			if (entry.getComponentKey() == AstromineComponents.ENERGY_INVENTORY_COMPONENT) {
				for (Direction direction : Direction.values()) {
					entry.set(direction, TransferType.INPUT_OUTPUT);
				}
			}
		});
	}
}
