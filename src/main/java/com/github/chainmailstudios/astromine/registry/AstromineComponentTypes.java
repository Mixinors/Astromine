package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.component.block.entity.BlockEntityTransferComponent;
import com.github.chainmailstudios.astromine.common.component.entity.EntityOxygenComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.EnergyInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.FluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.ItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.world.WorldAtmosphereComponent;
import com.github.chainmailstudios.astromine.common.component.world.WorldBridgeComponent;
import com.github.chainmailstudios.astromine.common.component.world.WorldMultiblockComponent;
import com.github.chainmailstudios.astromine.common.component.world.WorldNetworkComponent;
import nerdhub.cardinal.components.api.ComponentRegistry;
import nerdhub.cardinal.components.api.ComponentType;

public class AstromineComponentTypes {
	public static final ComponentType<WorldNetworkComponent> WORLD_NETWORK_COMPONENT = ComponentRegistry.INSTANCE.registerIfAbsent(AstromineCommon.identifier("world_network_component"), WorldNetworkComponent.class);
	public static final ComponentType<WorldAtmosphereComponent> WORLD_ATMOSPHERE_COMPONENT = ComponentRegistry.INSTANCE.registerIfAbsent(AstromineCommon.identifier("world_atmosphere_component"), WorldAtmosphereComponent.class);
	public static final ComponentType<WorldBridgeComponent> WORLD_BRIDGE_COMPONENT = ComponentRegistry.INSTANCE.registerIfAbsent(AstromineCommon.identifier("world_bridge_component"), WorldBridgeComponent.class);
	public static final ComponentType<WorldMultiblockComponent> WORLD_MULTIBLOCK_COMPONENT = ComponentRegistry.INSTANCE.registerIfAbsent(AstromineCommon.identifier("world_multiblock_component"), WorldMultiblockComponent.class);

	public static final ComponentType<ItemInventoryComponent> ITEM_INVENTORY_COMPONENT = ComponentRegistry.INSTANCE.registerIfAbsent(AstromineCommon.identifier("item_inventory_component"), ItemInventoryComponent.class);
	public static final ComponentType<FluidInventoryComponent> FLUID_INVENTORY_COMPONENT = ComponentRegistry.INSTANCE.registerIfAbsent(AstromineCommon.identifier("fluid_inventory_component"), FluidInventoryComponent.class);
	public static final ComponentType<EnergyInventoryComponent> ENERGY_INVENTORY_COMPONENT = ComponentRegistry.INSTANCE.registerIfAbsent(AstromineCommon.identifier("energy_inventory_component"), EnergyInventoryComponent.class);

	public static final ComponentType<BlockEntityTransferComponent> BLOCK_ENTITY_TRANSFER_COMPONENT = ComponentRegistry.INSTANCE.registerIfAbsent(AstromineCommon.identifier("block_entity_transfer_component"), BlockEntityTransferComponent.class);

	public static final ComponentType<EntityOxygenComponent> ENTITY_OXYGEN_COMPONENT = ComponentRegistry.INSTANCE.registerIfAbsent(AstromineCommon.identifier("entity_oxygen_component"), EntityOxygenComponent.class);

	public static void initialize() {
		// Unused.
	}
}
