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

import com.github.mixinors.astromine.common.component.world.NetworksComponent;
import com.github.mixinors.astromine.common.gravity.GravityManager;
import com.github.mixinors.astromine.common.manager.BodyManager;
import com.github.mixinors.astromine.common.manager.RocketManager;
import com.github.mixinors.astromine.common.manager.StationManager;
import com.github.mixinors.astromine.common.screen.handler.base.block.entity.ExtendedBlockEntityScreenHandler;
import com.github.mixinors.astromine.common.screen.handler.base.entity.ExtendedEntityScreenHandler;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

public class AMEvents {
	public static void init() {
		NeoForge.EVENT_BUS.addListener(AMEvents::addReloadListeners);
		NeoForge.EVENT_BUS.addListener(AMEvents::onServerTick);
		NeoForge.EVENT_BUS.addListener(AMEvents::onLevelTick);
		NeoForge.EVENT_BUS.addListener(AMEvents::onLevelLoad);
		NeoForge.EVENT_BUS.addListener(AMEvents::onLevelUnload);
		NeoForge.EVENT_BUS.addListener(AMEvents::onServerStarting);
		NeoForge.EVENT_BUS.addListener(AMEvents::onPlayerJoin);
		NeoForge.EVENT_BUS.addListener(AMEvents::onEntityTick);
	}

	private static void addReloadListeners(AddReloadListenerEvent event) {
		event.addListener(new BodyManager.ReloadListener());
	}

	private static void onServerTick(ServerTickEvent.Pre event) {
		BodyManager.flushPendingSync(event.getServer());

		for (var player : event.getServer().getPlayerList().getPlayers()) {
			if (player.containerMenu instanceof ExtendedBlockEntityScreenHandler screenHandler && screenHandler.getBlockEntity() != null) {
				screenHandler.getBlockEntity().syncData();
				break;
			}

			if (player.containerMenu instanceof ExtendedEntityScreenHandler screenHandler && screenHandler.getEntity() != null) {
				screenHandler.getEntity().syncData();
			}
		}
	}

	private static void onLevelTick(LevelTickEvent.Pre event) {
		if (event.getLevel() instanceof ServerLevel level) {
			var component = NetworksComponent.get(level);

			if (component != null) {
				component.tick();
			}
		}
	}

	private static void onEntityTick(EntityTickEvent.Pre event) {
		if (event.getEntity() instanceof LivingEntity entity) {
			GravityManager.apply(entity);
		}
	}

	private static void onLevelLoad(LevelEvent.Load event) {
		if (event.getLevel() instanceof ServerLevel level) {
			BodyManager.onWorldLoad(level.getServer(), level);
		}
	}

	private static void onLevelUnload(LevelEvent.Unload event) {
		if (event.getLevel() instanceof ServerLevel level) {
			BodyManager.onWorldUnload(level.getServer(), level);
		}
	}

	private static void onServerStarting(ServerStartingEvent event) {
		RocketManager.onServerStarting(event.getServer());
		StationManager.onServerStarting(event.getServer());
	}

	private static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			RocketManager.onPlayerJoin(player.getServer());
			StationManager.onPlayerJoin(player.getServer());
			BodyManager.onPlayerJoin(player.getServer());
		}
	}
}
