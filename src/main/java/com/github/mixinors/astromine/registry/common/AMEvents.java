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
import com.github.mixinors.astromine.common.entity.slime.SuperSpaceSlimeEntity;
import com.github.mixinors.astromine.common.manager.BodyManager;
import com.github.mixinors.astromine.common.manager.RocketManager;
import com.github.mixinors.astromine.common.manager.StationManager;
import com.github.mixinors.astromine.common.screen.handler.base.block.entity.ExtendedBlockEntityScreenHandler;
import com.github.mixinors.astromine.common.screen.handler.base.entity.ExtendedEntityScreenHandler;
import dev.architectury.event.events.common.TickEvent;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.ActionResult;

public class AMEvents {
	public static void init() {
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new BodyManager.ReloadListener());
		
		AttackEntityCallback.EVENT.register(SuperSpaceSlimeEntity::onAttackEntity);

		TickEvent.SERVER_PRE.register(ExtendedBlockEntityScreenHandler::onServerPre);
		TickEvent.SERVER_PRE.register(ExtendedEntityScreenHandler::onServerPre);

		TickEvent.SERVER_PRE.register(RocketManager::onServerPre);

		TickEvent.SERVER_LEVEL_PRE.register(NetworksComponent::onServerLevelPre);
		
		ServerWorldEvents.LOAD.register(BodyManager::onWorldLoad);
		ServerWorldEvents.UNLOAD.register(BodyManager::onWorldUnload);
		
		ServerLifecycleEvents.SERVER_STARTING.register(RocketManager::onServerStarting);
		ServerLifecycleEvents.SERVER_STARTING.register(StationManager::onServerStarting);
		
		ServerLifecycleEvents.SERVER_STOPPING.register(RocketManager::onServerStopping);
		ServerLifecycleEvents.SERVER_STOPPING.register(StationManager::onServerStopping);
		
		ServerPlayConnectionEvents.JOIN.register(RocketManager::onPlayerJoin);
		ServerPlayConnectionEvents.JOIN.register(StationManager::onPlayerJoin);
		
		ServerPlayConnectionEvents.JOIN.register(BodyManager::onPlayerJoin);
	}
}
