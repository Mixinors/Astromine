package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;

import net.minecraft.util.Identifier;

import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;

public class AstromineClientPackets {
	public static final Identifier PRESSURE_UPDATE = AstromineCommon.identifier("pressure_update");

	public static void initialize() {
		ClientSidePacketRegistry.INSTANCE.register(PRESSURE_UPDATE, ((context, buffer) -> {
			AstromineScreens.PRESSURE_TEXT.setText(String.valueOf(buffer.readString()));
		}));
	}
}
