package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.common.gas.GasManager;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;

public class AstromineClientCallbacks {
	public static void initialize() {
		ClientTickCallback.EVENT.register((client) -> {
			if (AstromineScreens.PRESSURE_TEXT != null && client != null && client.player != null && client.player.world != null) {
				AstromineScreens.PRESSURE_TEXT.setText(GasManager.get(client.player.world, client.player.getBlockPos()).getFraction().toString());
			}
		});
	}
}
