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

package com.github.mixinors.astromine.registry.common;

import com.github.mixinors.astromine.common.callback.ServerChunkManagerCallback;
import com.github.mixinors.astromine.common.component.world.WorldNetworkComponent;
import com.github.mixinors.astromine.common.screenhandler.base.block.entity.ExtendedBlockEntityScreenHandler;
import dev.architectury.event.events.common.TickEvent;

import net.minecraft.server.world.ServerWorld;

public class AMCallbacks {
	public static void init() {
		// TODO: Rewrite Atmosphere stuff, incl. this.
		
		// BlockEvent.PLACE.register(( world, pos, blockState, entity) -> {
			// ChunkAtmosphereComponent atmosphereComponent = ChunkAtmosphereComponent.get(world.getChunk(pos));
//
			// if (atmosphereComponent != null) {
			// 	var centerPos = pos;
			// 	var centerState = world.getBlockState(centerPos);
			// 	var centerVolume = atmosphereComponent.get(centerPos);
//
			// 	var directions = Lists.newArrayList(Direction.values());
//
			// 	Collections.shuffle(directions);
//
			// 	for (var direction : directions) {
			// 		var sidePos = pos.offset(direction);
			// 		var sideState = world.getBlockState(sidePos);
//
			// 		var sideAtmosphereComponent = atmosphereComponent;
//
			// 		if (!atmosphereComponent.isInChunk(sidePos)) {
			// 			sideAtmosphereComponent = ChunkAtmosphereComponent.get(world.getChunk(sidePos));
			// 		}
//
			// 		var sideVolume = sideAtmosphereComponent.get(sidePos);
//
			// 		if (atmosphereComponent.isTraversableForDisplacement(centerState, centerPos, sideState, sidePos, centerVolume, sideVolume, direction)) {
			// 			sideVolume.take(centerVolume);
			// 			sideAtmosphereComponent.add(sidePos, sideVolume);
//
			// 			break;
//
			// 		}
			// 	}
//
			// 	atmosphereComponent.remove(centerPos);
			// }

			// return EventResult.pass();
		// });

		TickEvent.SERVER_PRE.register(( server) -> {
			for (var playerEntity : server.getPlayerManager().getPlayerList()) {
				if (playerEntity.currentScreenHandler instanceof ExtendedBlockEntityScreenHandler screenHandler) {

					if (screenHandler.getBlockEntity() != null) {
						screenHandler.getBlockEntity().syncData();
						break;
					}
				}
			}
		});

		TickEvent.SERVER_LEVEL_PRE.register((world -> {
			var component = WorldNetworkComponent.get(world);

			if (component != null) {
				component.tick();
			}
		}));
		
		ServerChunkManagerCallback.EVENT.register(manager -> {
//			if (manager.threadedAnvilChunkStorage.chunkGenerator instanceof EarthSpaceChunkGenerator) {
//				manager.threadedAnvilChunkStorage.chunkGenerator = ((EarthSpaceChunkGenerator) manager.threadedAnvilChunkStorage.chunkGenerator).withSeedCommon(((ServerWorld) manager.getWorld()).getSeed());
//			}
		});
	}
}
