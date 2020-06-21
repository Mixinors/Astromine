package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.component.WorldNetworkComponent;
import nerdhub.cardinal.components.api.ComponentRegistry;
import nerdhub.cardinal.components.api.ComponentType;

public class AstromineComponentTypes {
	public static final ComponentType<WorldNetworkComponent> WORLD_NETWORK_COMPONENT = ComponentRegistry.INSTANCE.registerIfAbsent(AstromineCommon.identifier("world_network_component"), WorldNetworkComponent.class);


	public static void initialize() {
		// Unused.
	}
}
