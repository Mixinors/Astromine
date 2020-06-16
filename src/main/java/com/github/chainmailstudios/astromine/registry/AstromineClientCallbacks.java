package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.common.gas.GasManager;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;

public class AstromineClientCallbacks {
	public static void initialize() {
		ClientTickCallback.EVENT.register((client) -> {

		});
	}
}
