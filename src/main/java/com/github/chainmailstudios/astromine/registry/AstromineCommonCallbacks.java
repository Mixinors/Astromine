package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.component.WorldAtmosphereComponent;
import com.github.chainmailstudios.astromine.component.WorldBridgeComponent;
import com.github.chainmailstudios.astromine.component.WorldNetworkComponent;
import io.netty.buffer.Unpooled;
import nerdhub.cardinal.components.api.component.ComponentProvider;
import nerdhub.cardinal.components.api.event.WorldComponentCallback;
import net.fabricmc.fabric.api.event.server.ServerTickCallback;
import net.fabricmc.fabric.api.event.world.WorldTickCallback;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Direction;

public class AstromineCommonCallbacks {
	public static void initialize() {
		ServerTickCallback.EVENT.register((server) -> {
			for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
				ComponentProvider componentProvider = ComponentProvider.fromWorld(player.world);

				WorldAtmosphereComponent atmosphereComponent = componentProvider.getComponent(AstromineComponentTypes.WORLD_ATMOSPHERE_COMPONENT);

				PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
				FluidVolume volume = atmosphereComponent.get(player.getBlockPos().offset(Direction.UP));

				buffer.writeString(volume.getFluidString());
				buffer.writeString(volume.getFractionString());

				ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, AstromineClientPackets.PRESSURE_UPDATE, buffer);
			}
		});

		WorldComponentCallback.EVENT.register(((world, container) -> {
			WorldNetworkComponent component = new WorldNetworkComponent(world);
			container.put(AstromineComponentTypes.WORLD_NETWORK_COMPONENT, component);

			WorldTickCallback.EVENT.register((tickWorld -> {
				if (tickWorld == component.getWorld()) {
					component.tick();
				}
			}));
		}));

		WorldComponentCallback.EVENT.register(((world, container) -> {
			WorldAtmosphereComponent component = new WorldAtmosphereComponent(world);
			container.put(AstromineComponentTypes.WORLD_ATMOSPHERE_COMPONENT, component);

			WorldTickCallback.EVENT.register((tickWorld -> {
				if (tickWorld == component.getWorld()) {
					component.tick();
				}
			}));
		}));

		WorldComponentCallback.EVENT.register((world, container) -> {
			WorldBridgeComponent component = new WorldBridgeComponent(world);
			container.put(AstromineComponentTypes.WORLD_BRIDGE_COMPONENT, component);
		});
	}
}
