package com.github.chainmailstudios.astromine.common.network;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.registry.NetworkTypeRegistry;
import net.minecraft.util.registry.Registry;

public abstract class NetworkType {
	public static final NetworkType EMPTY = NetworkTypeRegistry.INSTANCE.register(AstromineCommon.identifier("empty_network"), new NetworkType() {
		@Override
		public void simulate(NetworkInstance controller) {
			// Unused.
		}
	});

	public abstract void simulate(NetworkInstance controller);
}
