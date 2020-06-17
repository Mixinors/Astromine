package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.common.gas.AtmosphericManager;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.event.server.ServerTickCallback;
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
				buffer.writeString(AtmosphericManager.get(player.world, player.getBlockPos().offset(Direction.UP)).toInterfaceString());
				ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, AstromineClientPackets.PRESSURE_UPDATE, buffer);
			}
		});
	}
}
