package com.github.chainmailstudios.astromine.common.packet;

import net.fabricmc.fabric.api.network.PacketContext;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public interface PacketConsumer {
	void consumePacket(Identifier identifier, PacketByteBuf buffer, PacketContext context);
}
