package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.common.gas.AtmosphericManager;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.component.WorldNetworkComponent;
import io.netty.buffer.Unpooled;
import nerdhub.cardinal.components.api.event.WorldComponentCallback;
import net.fabricmc.fabric.api.event.server.ServerTickCallback;
import net.fabricmc.fabric.api.event.world.WorldTickCallback;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Direction;

public class AstromineCommonCallbacks {
	public static void initialize() {
		ServerTickCallback.EVENT.register((server) -> {
			for (ServerWorld world : server.getWorlds()) {
				AtmosphericManager.simulate(world);
			}
		});

		ServerTickCallback.EVENT.register((server) -> {
			for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
				PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
				FluidVolume volume = AtmosphericManager.get(player.world, player.getBlockPos().offset(Direction.UP));
				buffer.writeString(volume.getFluidString());
				buffer.writeString(volume.getFractionString());
				ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, AstromineClientPackets.PRESSURE_UPDATE, buffer);
			}
		});

		WorldComponentCallback.EVENT.register(((world, container) -> {
			WorldNetworkComponent component = new WorldNetworkComponent(world);
			container.put(AstromineComponentTypes.WORLD_NETWORK_COMPONENT, component);

			WorldTickCallback.EVENT.register((tickWorld ->  {
				if (tickWorld == component.getWorld()) {
					component.tick();
				}
			}));
		}));
	}
}
