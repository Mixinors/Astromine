package com.github.chainmailstudios.astromine.common.registry;

import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.common.registry.base.BiRegistry;

public class NetworkTypeRegistry extends BiRegistry<Identifier, NetworkType> {
	public static final NetworkTypeRegistry INSTANCE = new NetworkTypeRegistry();

	private NetworkTypeRegistry() {
		// Locked.
	}
}
