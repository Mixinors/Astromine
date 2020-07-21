package com.github.chainmailstudios.astromine.common.network;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.registry.NetworkTypeRegistry;

public abstract class NetworkType {
	public static final NetworkType EMPTY = NetworkTypeRegistry.INSTANCE.register(AstromineCommon.identifier("empty_network"), new NetworkType() {
		@Override
		public void tick(NetworkInstance instance) {
			// Unused.
		}
	});

	public abstract void tick(NetworkInstance instance);
}
