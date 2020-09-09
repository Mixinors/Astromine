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

import com.github.chainmailstudios.astromine.common.callback.ServerChunkTickCallback;
import com.github.chainmailstudios.astromine.common.component.entity.EntityOxygenComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.FluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleFluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.world.ChunkAtmosphereComponent;
import com.github.chainmailstudios.astromine.common.component.world.WorldBridgeComponent;
import com.github.chainmailstudios.astromine.common.component.world.WorldNetworkComponent;
import com.github.chainmailstudios.astromine.common.entity.base.*;
import com.github.chainmailstudios.astromine.common.item.base.FluidVolumeItem;
import com.github.chainmailstudios.astromine.common.screenhandler.base.block.ComponentBlockEntityScreenHandler;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.common.volume.fraction.Fraction;
import nerdhub.cardinal.components.api.component.ComponentProvider;
import nerdhub.cardinal.components.api.event.ChunkComponentCallback;
import nerdhub.cardinal.components.api.event.EntityComponentCallback;
import nerdhub.cardinal.components.api.event.ItemComponentCallbackV2;
import nerdhub.cardinal.components.api.event.WorldComponentCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.chunk.WorldChunk;

import java.util.function.Consumer;

public class AstromineCommonCallbacks {
	public static int atmosphereTickCounter = 0;

	@SuppressWarnings("UnstableApiUsage")
	public static void initialize() {
		ServerTickEvents.START_SERVER_TICK.register(server -> {
			if (atmosphereTickCounter < AstromineConfig.get().gasTickRate) {
				atmosphereTickCounter++;
			} else {
				atmosphereTickCounter = 0;
			}
		});

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

		WorldComponentCallback.register(AstromineComponentTypes.WORLD_NETWORK_COMPONENT, WorldNetworkComponent::new);

		ServerTickEvents.START_WORLD_TICK.register((world -> {
			WorldNetworkComponent component = ComponentProvider.fromWorld(world).getComponent(AstromineComponentTypes.WORLD_NETWORK_COMPONENT);
			if (component != null) {
				component.tick();
			}
		}));

		ChunkComponentCallback.EVENT.register((chunk, components) -> {
			if (chunk instanceof WorldChunk) {
				components.put(AstromineComponentTypes.CHUNK_ATMOSPHERE_COMPONENT, new ChunkAtmosphereComponent(((WorldChunk) chunk).getWorld(), chunk));
			}
		});

		ServerChunkTickCallback.EVENT.register((world, chunk) -> {
			ChunkAtmosphereComponent component = ComponentProvider.fromChunk(chunk).getComponent(AstromineComponentTypes.CHUNK_ATMOSPHERE_COMPONENT);
			if (component != null) {
				if (atmosphereTickCounter == AstromineConfig.get().gasTickRate && component.getWorld().isChunkLoaded(chunk.getPos().x, chunk.getPos().z) && world == component.getWorld()) {
					component.tick();
				}
			}
		});

		WorldComponentCallback.EVENT.register((world, container) -> {
			WorldBridgeComponent component = new WorldBridgeComponent(world);
			container.put(AstromineComponentTypes.WORLD_BRIDGE_COMPONENT, component);
		});

		EntityComponentCallback.register(AstromineComponentTypes.ENTITY_OXYGEN_COMPONENT, LivingEntity.class, EntityOxygenComponent::defaulted);

		Consumer<Item> itemConsumer = (item) -> {
			if (item instanceof FluidVolumeItem) {
				FluidVolumeItem volumeItem = (FluidVolumeItem) item;

				ItemComponentCallbackV2.register(AstromineComponentTypes.FLUID_INVENTORY_COMPONENT, item, (useless, stack) -> {
					FluidInventoryComponent component = new SimpleFluidInventoryComponent(1);
					component.setVolume(0, FluidVolume.of(Fraction.empty(), volumeItem.getSize(), Fluids.EMPTY));
					return component;
				});
			}
		};
		Registry.ITEM.forEach(itemConsumer);
		RegistryEntryAddedCallback.event(Registry.ITEM).register((i, identifier, item) -> itemConsumer.accept(item));

		EntityComponentCallback.register(AstromineComponentTypes.FLUID_INVENTORY_COMPONENT, ComponentFluidInventoryEntity.class, ComponentFluidInventoryEntity::createFluidComponent);
		EntityComponentCallback.register(AstromineComponentTypes.ITEM_INVENTORY_COMPONENT, ComponentFluidInventoryEntity.class, ComponentFluidInventoryEntity::createItemComponent);

		EntityComponentCallback.register(AstromineComponentTypes.ITEM_INVENTORY_COMPONENT, ComponentEnergyItemEntity.class, ComponentEnergyItemEntity::createItemComponent);
		EntityComponentCallback.register(AstromineComponentTypes.ENERGY_INVENTORY_COMPONENT, ComponentEnergyItemEntity.class, ComponentEnergyItemEntity::createEnergyComponent);

		EntityComponentCallback.register(AstromineComponentTypes.ITEM_INVENTORY_COMPONENT, ComponentItemEntity.class, ComponentItemEntity::createItemComponent);

		EntityComponentCallback.register(AstromineComponentTypes.FLUID_INVENTORY_COMPONENT, ComponentFluidEntity.class, ComponentFluidEntity::createFluidComponent);

		EntityComponentCallback.register(AstromineComponentTypes.ENERGY_INVENTORY_COMPONENT, ComponentEnergyEntity.class, ComponentEnergyEntity::createEnergyComponent);
	}
}
