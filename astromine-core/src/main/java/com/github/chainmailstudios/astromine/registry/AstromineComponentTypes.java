/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.component.block.entity.BlockEntityTransferComponent;
import com.github.chainmailstudios.astromine.common.component.entity.EntityOxygenComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.EnergyInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.FluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.ItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.world.WorldAtmosphereComponent;
import com.github.chainmailstudios.astromine.common.component.world.WorldBridgeComponent;
import com.github.chainmailstudios.astromine.common.component.world.WorldNetworkComponent;
import nerdhub.cardinal.components.api.ComponentRegistry;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.component.Component;
import net.minecraft.util.Identifier;

public class AstromineComponentTypes {
	public static final ComponentType<WorldNetworkComponent> WORLD_NETWORK_COMPONENT = register(AstromineCommon.identifier("world_network_component"), WorldNetworkComponent.class);
	public static final ComponentType<WorldAtmosphereComponent> WORLD_ATMOSPHERE_COMPONENT = register(AstromineCommon.identifier("world_atmosphere_component"), WorldAtmosphereComponent.class);
	public static final ComponentType<WorldBridgeComponent> WORLD_BRIDGE_COMPONENT = register(AstromineCommon.identifier("world_bridge_component"), WorldBridgeComponent.class);

	public static final ComponentType<ItemInventoryComponent> ITEM_INVENTORY_COMPONENT = register(AstromineCommon.identifier("item_inventory_component"), ItemInventoryComponent.class);
	public static final ComponentType<FluidInventoryComponent> FLUID_INVENTORY_COMPONENT = register(AstromineCommon.identifier("fluid_inventory_component"), FluidInventoryComponent.class);
	public static final ComponentType<EnergyInventoryComponent> ENERGY_INVENTORY_COMPONENT = register(AstromineCommon.identifier("energy_inventory_component"), EnergyInventoryComponent.class);

	public static final ComponentType<BlockEntityTransferComponent> BLOCK_ENTITY_TRANSFER_COMPONENT = register(AstromineCommon.identifier("block_entity_transfer_component"), BlockEntityTransferComponent.class);

	public static final ComponentType<EntityOxygenComponent> ENTITY_OXYGEN_COMPONENT = register(AstromineCommon.identifier("entity_oxygen_component"), EntityOxygenComponent.class);

	public static void initialize() {

	}

	public static <T extends Component> ComponentType<T> register(Identifier id, Class<T> clazz) {
		return ComponentRegistry.INSTANCE.registerIfAbsent(id, clazz);
	}
}
