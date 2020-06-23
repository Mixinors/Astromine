package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.component.inventory.EnergyInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.FluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.ItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.packet.PacketHandlerComponent;
import com.github.chainmailstudios.astromine.common.component.world.WorldAtmosphereComponent;
import com.github.chainmailstudios.astromine.common.component.world.WorldBridgeComponent;
import com.github.chainmailstudios.astromine.common.component.world.WorldNetworkComponent;
import nerdhub.cardinal.components.api.ComponentRegistry;
import nerdhub.cardinal.components.api.ComponentType;

public class AstromineComponentTypes {
	public static final ComponentType<WorldNetworkComponent> WORLD_NETWORK_COMPONENT = ComponentRegistry.INSTANCE.registerIfAbsent(AstromineCommon.identifier("world_network_component"), WorldNetworkComponent.class);
	public static final ComponentType<WorldAtmosphereComponent> WORLD_ATMOSPHERE_COMPONENT = ComponentRegistry.INSTANCE.registerIfAbsent(AstromineCommon.identifier("world_atmosphere_component"), WorldAtmosphereComponent.class);
	public static final ComponentType<WorldBridgeComponent> WORLD_BRIDGE_COMPONENT = ComponentRegistry.INSTANCE.registerIfAbsent(AstromineCommon.identifier("world_bridge_component"), WorldBridgeComponent.class);

	public static final ComponentType<ItemInventoryComponent> ITEM_INVENTORY_COMPONENT = ComponentRegistry.INSTANCE.registerIfAbsent(AstromineCommon.identifier("item_inventory_component"), ItemInventoryComponent.class);
	public static final ComponentType<FluidInventoryComponent> FLUID_INVENTORY_COMPONENT = ComponentRegistry.INSTANCE.registerIfAbsent(AstromineCommon.identifier("fluid_inventory_component"), FluidInventoryComponent.class);
	public static final ComponentType<EnergyInventoryComponent> ENERGY_INVENTORY_COMPONENT = ComponentRegistry.INSTANCE.registerIfAbsent(AstromineCommon.identifier("energy_inventory_component"), EnergyInventoryComponent.class);

	public static final ComponentType<PacketHandlerComponent> PACKET_HANDLER_COMPONENT = ComponentRegistry.INSTANCE.registerIfAbsent(AstromineCommon.identifier("packet_handler_component"), PacketHandlerComponent.class);

	public static void initialize() {
		// Unused.
	}
}
