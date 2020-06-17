package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.common.gas.AtmosphericManager;
import io.netty.buffer.Unpooled;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import net.fabricmc.fabric.api.event.server.ServerTickCallback;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;

public class AstromineCommonCallbacks {
	private static long lastSimulation = 0;

	public static void initialize() {
		ServerTickCallback.EVENT.register((server) -> {
			if (System.currentTimeMillis() - lastSimulation >= 250) {
				for (ServerWorld world : server.getWorlds()) {
					AtmosphericManager.simulate(world);
				}

				lastSimulation = System.currentTimeMillis();
			}
		});

		ServerTickCallback.EVENT.register((server) -> {
			for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
				PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
				buffer.writeString(AtmosphericManager.get(player.world, player.getBlockPos()).toInterfaceString());
				ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, AstromineClientPackets.PRESSURE_UPDATE, buffer);
			}
		});
	}
}
