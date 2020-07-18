package com.github.chainmailstudios.astromine.common.registry;

import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.common.registry.base.BiRegistry;
import net.minecraft.util.Identifier;

public class NetworkTypeRegistry extends BiRegistry<Identifier, NetworkType> {
	public static final NetworkTypeRegistry INSTANCE = new NetworkTypeRegistry();

	private NetworkTypeRegistry() {
		// Locked.
	}
}
