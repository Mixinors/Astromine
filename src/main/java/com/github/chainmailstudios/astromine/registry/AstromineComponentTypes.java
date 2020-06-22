package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.component.WorldAtmosphereComponent;
import com.github.chainmailstudios.astromine.component.WorldBridgeComponent;
import com.github.chainmailstudios.astromine.component.WorldNetworkComponent;
import nerdhub.cardinal.components.api.ComponentRegistry;
import nerdhub.cardinal.components.api.ComponentType;

public class AstromineComponentTypes {
	public static final ComponentType<WorldNetworkComponent> WORLD_NETWORK_COMPONENT = ComponentRegistry.INSTANCE.registerIfAbsent(AstromineCommon.identifier("world_network_component"), WorldNetworkComponent.class);
	public static final ComponentType<WorldAtmosphereComponent> WORLD_ATMOSPHERE_COMPONENT = ComponentRegistry.INSTANCE.registerIfAbsent(AstromineCommon.identifier("world_atmosphere_component"), WorldAtmosphereComponent.class);
	public static final ComponentType<WorldBridgeComponent> WORLD_BRIDGE_COMPONENT = ComponentRegistry.INSTANCE.registerIfAbsent(AstromineCommon.identifier("world_brige_component"), WorldBridgeComponent.class);

	public static void initialize() {
		// Unused.
	}
}
