package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.common.component.entity.EntityOxygenComponent;
import nerdhub.cardinal.components.api.event.EntityComponentCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Direction;

import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedBlockEntity;
import com.github.chainmailstudios.astromine.common.component.world.WorldAtmosphereComponent;
import com.github.chainmailstudios.astromine.common.component.world.WorldBridgeComponent;
import com.github.chainmailstudios.astromine.common.component.world.WorldNetworkComponent;
import com.github.chainmailstudios.astromine.common.container.base.DefaultedBlockEntityContainer;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import io.netty.buffer.Unpooled;
import nerdhub.cardinal.components.api.component.ComponentProvider;
import nerdhub.cardinal.components.api.event.WorldComponentCallback;

public class AstromineCommonCallbacks {
	public static void initialize() {
		ServerTickEvents.START_SERVER_TICK.register((server) -> {
			for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
				ComponentProvider componentProvider = ComponentProvider.fromWorld(player.world);

				WorldAtmosphereComponent atmosphereComponent = componentProvider.getComponent(AstromineComponentTypes.WORLD_ATMOSPHERE_COMPONENT);

				PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
				FluidVolume volume = atmosphereComponent.get(player.getBlockPos().offset(Direction.UP));

				buffer.writeIdentifier(volume.getFluidIdentifier());

				ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, AstromineCommonPackets.PRESSURE_UPDATE, buffer);
			}
		});

		ServerTickEvents.START_SERVER_TICK.register((server) -> {
			for (PlayerEntity playerEntity : server.getPlayerManager().getPlayerList()) {
				if (playerEntity.currentScreenHandler instanceof DefaultedBlockEntityContainer) {
					DefaultedBlockEntityContainer container = (DefaultedBlockEntityContainer) playerEntity.currentScreenHandler;

					if (container.syncBlockEntity instanceof DefaultedBlockEntity) {
						((DefaultedBlockEntity) container.syncBlockEntity).sync();
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
	}
}
