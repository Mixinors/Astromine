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

import com.github.chainmailstudios.astromine.common.component.world.WorldAtmosphereComponent;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.registry.Registry;

import com.github.chainmailstudios.astromine.common.component.entity.EntityOxygenComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.FluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleFluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.world.WorldBridgeComponent;
import com.github.chainmailstudios.astromine.common.component.world.WorldNetworkComponent;
import com.github.chainmailstudios.astromine.common.item.base.FluidVolumeItem;
import com.github.chainmailstudios.astromine.common.screenhandler.base.DefaultedBlockEntityScreenHandler;
import nerdhub.cardinal.components.api.event.EntityComponentCallback;
import nerdhub.cardinal.components.api.event.ItemComponentCallbackV2;
import nerdhub.cardinal.components.api.event.WorldComponentCallback;

public class AstromineCommonCallbacks {
	@SuppressWarnings("UnstableApiUsage")
	public static void initialize() {
		ServerTickEvents.START_SERVER_TICK.register((server) -> {
			// for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
			// ComponentProvider componentProvider = ComponentProvider.fromWorld(player.world);
			//
			// WorldAtmosphereComponent atmosphereComponent = componentProvider.getComponent(AstromineComponentTypes.WORLD_ATMOSPHERE_COMPONENT);
			//
			// PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
			// FluidVolume volume = atmosphereComponent.get(player.getBlockPos().offset(Direction.UP));
			//
			// buffer.writeIdentifier(volume.getFluidIdentifier());
			//
			// ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, AstromineCommonPackets.PRESSURE_UPDATE, buffer);
			// }
		});

		ServerTickEvents.START_SERVER_TICK.register((server) -> {
			for (PlayerEntity playerEntity : server.getPlayerManager().getPlayerList()) {
				if (playerEntity.currentScreenHandler instanceof DefaultedBlockEntityScreenHandler) {
					DefaultedBlockEntityScreenHandler screenHandler = (DefaultedBlockEntityScreenHandler) playerEntity.currentScreenHandler;

					if (screenHandler.syncBlockEntity != null) {
						screenHandler.syncBlockEntity.sync();
						break;
					}
				}
			}
		});

		WorldComponentCallback.EVENT.register(((world, container) -> {
			WorldNetworkComponent component = new WorldNetworkComponent(world);
			container.put(AstromineComponentTypes.WORLD_NETWORK_COMPONENT, component);

			ServerTickEvents.START_WORLD_TICK.register((tickWorld -> {
				if (tickWorld == component.getWorld()) {
					component.tick();
				}
			}));
		}));

		WorldComponentCallback.EVENT.register(((world, container) -> {
			WorldAtmosphereComponent component = new WorldAtmosphereComponent(world);
			container.put(AstromineComponentTypes.WORLD_ATMOSPHERE_COMPONENT, component);

			ServerTickEvents.START_WORLD_TICK.register((tickWorld -> {
				if (tickWorld == component.getWorld()) {
					component.tick();
				}
			}));
		}));

		WorldComponentCallback.EVENT.register((world, container) -> {
			WorldBridgeComponent component = new WorldBridgeComponent(world);
			container.put(AstromineComponentTypes.WORLD_BRIDGE_COMPONENT, component);
		});

		EntityComponentCallback.register(AstromineComponentTypes.ENTITY_OXYGEN_COMPONENT, LivingEntity.class, ((entity) -> {
			return new EntityOxygenComponent(0, entity);
		}));

		Registry.ITEM.forEach(item -> {
			if (item instanceof FluidVolumeItem) {
				ItemComponentCallbackV2.register(AstromineComponentTypes.FLUID_INVENTORY_COMPONENT, item, (useless, stack) -> {
					FluidInventoryComponent component = new SimpleFluidInventoryComponent(1);
					component.getVolume(0).setSize(((FluidVolumeItem) item).getSize());
					return component;
				});
			}
		});
	}
}
