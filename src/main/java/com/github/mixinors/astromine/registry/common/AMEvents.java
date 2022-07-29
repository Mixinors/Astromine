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

import com.github.mixinors.astromine.common.component.world.NetworkComponent;
import com.github.mixinors.astromine.common.event.ServerChunkManagerEvents;
import com.github.mixinors.astromine.common.manager.BodyManager;
import com.github.mixinors.astromine.common.screen.handler.base.block.entity.ExtendedBlockEntityScreenHandler;
import com.github.mixinors.astromine.common.screen.handler.base.entity.ExtendedEntityScreenHandler;
import com.github.mixinors.astromine.common.screen.handler.body.BodySelectorScreenHandler;
import com.github.mixinors.astromine.common.world.generation.space.EarthOrbitChunkGenerator;
import com.github.mixinors.astromine.common.world.generation.space.MoonChunkGenerator;
import com.github.mixinors.astromine.common.world.generation.space.MoonOrbitChunkGenerator;
import dev.architectury.event.events.common.TickEvent;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.gen.chunk.ChunkGenerator;

public class AMEvents {
	public static void init() {
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new BodyManager.ReloadListener());
		
		ServerWorldEvents.LOAD.register(BodyManager::onWorldLoad);
		ServerWorldEvents.UNLOAD.register(BodyManager::onWorldUnload);
		
		ServerPlayConnectionEvents.JOIN.register(BodyManager::onPlayerJoin);
		
		TickEvent.SERVER_PRE.register((server) -> {
			for (var playerEntity : server.getPlayerManager().getPlayerList()) {
				if (playerEntity.currentScreenHandler instanceof ExtendedBlockEntityScreenHandler screenHandler) {
					if (screenHandler.getBlockEntity() != null) {
						screenHandler.getBlockEntity().syncData();
						break;
					}
				}
				
				if (playerEntity.currentScreenHandler instanceof ExtendedEntityScreenHandler screenHandler) {
					if (screenHandler.getEntity() != null) {
						screenHandler.getEntity().syncData();
					}
				}
			}
		});
		
		TickEvent.SERVER_LEVEL_PRE.register((world -> {
			var component = NetworkComponent.get(world);
			
			if (component != null) {
				component.tick();
			}
		}));
		
		ServerChunkManagerEvents.INIT.register(manager -> {
			// TODO: See if Mojang fixed this!
			// if (manager.threadedAnvilChunkStorage.chunkGenerator instanceof ChunkGenerator) {
			// 	manager.threadedAnvilChunkStorage.chunkGenerator = manager.threadedAnvilChunkStorage.chunkGenerator.withSeed(((ServerWorld) manager.getWorld()).getSeed());
			// }
			//
			// if (manager.threadedAnvilChunkStorage.chunkGenerator instanceof MoonOrbitChunkGenerator) {
			// 	manager.threadedAnvilChunkStorage.chunkGenerator = ((MoonOrbitChunkGenerator) manager.threadedAnvilChunkStorage.chunkGenerator).withSeedCommon(((ServerWorld) manager.getWorld()).getSeed());
			// }
			//
			// if (manager.threadedAnvilChunkStorage.chunkGenerator instanceof MoonChunkGenerator) {
			// 	manager.threadedAnvilChunkStorage.chunkGenerator = ((MoonChunkGenerator) manager.threadedAnvilChunkStorage.chunkGenerator).withSeedCommon(((ServerWorld) manager.getWorld()).getSeed());
			// }
		});
	}
}
