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

import com.github.chainmailstudios.astromine.common.component.block.entity.TransferComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluids;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.component.block.entity.RedstoneComponent;
import com.github.chainmailstudios.astromine.common.component.entity.EntityOxygenComponent;
import com.github.chainmailstudios.astromine.common.component.general.base.EnergyComponent;
import com.github.chainmailstudios.astromine.common.component.general.base.FluidComponent;
import com.github.chainmailstudios.astromine.common.component.general.base.ItemComponent;
import com.github.chainmailstudios.astromine.common.component.general.SimpleFluidComponent;
import com.github.chainmailstudios.astromine.common.component.world.ChunkAtmosphereComponent;
import com.github.chainmailstudios.astromine.common.component.world.WorldBridgeComponent;
import com.github.chainmailstudios.astromine.common.component.world.WorldNetworkComponent;
import com.github.chainmailstudios.astromine.common.entity.base.ComponentEnergyEntity;
import com.github.chainmailstudios.astromine.common.entity.base.ComponentEnergyItemEntity;
import com.github.chainmailstudios.astromine.common.entity.base.ComponentFluidEntity;
import com.github.chainmailstudios.astromine.common.entity.base.ComponentFluidItemEntity;
import com.github.chainmailstudios.astromine.common.entity.base.ComponentItemEntity;
import com.github.chainmailstudios.astromine.common.item.base.FluidVolumeItem;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.common.volume.fraction.Fraction;
import dev.onyxstudios.cca.api.v3.block.BlockComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.block.BlockComponentInitializer;
import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentInitializer;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.item.ItemComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.item.ItemComponentInitializer;
import dev.onyxstudios.cca.api.v3.world.WorldComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.world.WorldComponentInitializer;

public class AstromineComponents implements WorldComponentInitializer, ChunkComponentInitializer, ItemComponentInitializer, EntityComponentInitializer, BlockComponentInitializer {
	public static final ComponentKey<WorldNetworkComponent> WORLD_NETWORK_COMPONENT = ComponentRegistry.getOrCreate(AstromineCommon.identifier("world_network_component"), WorldNetworkComponent.class);
	public static final ComponentKey<ChunkAtmosphereComponent> CHUNK_ATMOSPHERE_COMPONENT = ComponentRegistry.getOrCreate(AstromineCommon.identifier("chunk_atmosphere_component"), ChunkAtmosphereComponent.class);
	public static final ComponentKey<WorldBridgeComponent> WORLD_BRIDGE_COMPONENT = ComponentRegistry.getOrCreate(AstromineCommon.identifier("world_bridge_component"), WorldBridgeComponent.class);

	public static final ComponentKey<ItemComponent> ITEM_INVENTORY_COMPONENT = ComponentRegistry.getOrCreate(AstromineCommon.identifier("item_inventory_component"), ItemComponent.class);
	public static final ComponentKey<FluidComponent> FLUID_INVENTORY_COMPONENT = ComponentRegistry.getOrCreate(AstromineCommon.identifier("fluid_inventory_component"), FluidComponent.class);
	public static final ComponentKey<EnergyComponent> ENERGY_INVENTORY_COMPONENT = ComponentRegistry.getOrCreate(AstromineCommon.identifier("energy_inventory_component"), EnergyComponent.class);

	public static final ComponentKey<TransferComponent> BLOCK_ENTITY_TRANSFER_COMPONENT = ComponentRegistry.getOrCreate(AstromineCommon.identifier("block_entity_transfer_component"), TransferComponent.class);
	public static final ComponentKey<RedstoneComponent> BLOCK_ENTITY_REDSTONE_COMPONENT = ComponentRegistry.getOrCreate(AstromineCommon.identifier("block_entity_redstone_component"), RedstoneComponent.class);

	public static final ComponentKey<EntityOxygenComponent> ENTITY_OXYGEN_COMPONENT = ComponentRegistry.getOrCreate(AstromineCommon.identifier("entity_oxygen_component"), EntityOxygenComponent.class);

	public static void initialize() {}

	@Override
	public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {
		registry.register(WORLD_NETWORK_COMPONENT, WorldNetworkComponent::new);
		registry.register(WORLD_BRIDGE_COMPONENT, WorldBridgeComponent::new);
	}

	@Override
	public void registerChunkComponentFactories(ChunkComponentFactoryRegistry registry) {
		registry.register(CHUNK_ATMOSPHERE_COMPONENT, ChunkAtmosphereComponent::new);
	}

	@Override
	public void registerItemComponentFactories(ItemComponentFactoryRegistry registry) {
		registry.registerFor(
				item -> item instanceof FluidVolumeItem,
				FLUID_INVENTORY_COMPONENT,
				stack -> SimpleFluidComponent.of(FluidVolume.of(Fraction.EMPTY, ((FluidVolumeItem) stack.getItem()).getSize(), Fluids.EMPTY))
		);
	}

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerFor(LivingEntity.class, ENTITY_OXYGEN_COMPONENT, EntityOxygenComponent::of);

		registry.registerFor(ComponentFluidItemEntity.class, FLUID_INVENTORY_COMPONENT, ComponentFluidItemEntity::createFluidComponent);
		registry.registerFor(ComponentFluidItemEntity.class, ITEM_INVENTORY_COMPONENT, ComponentFluidItemEntity::createItemComponent);

		registry.registerFor(ComponentEnergyItemEntity.class, ITEM_INVENTORY_COMPONENT, ComponentEnergyItemEntity::createItemComponent);
		registry.registerFor(ComponentEnergyItemEntity.class, ENERGY_INVENTORY_COMPONENT, ComponentEnergyItemEntity::createEnergyComponent);

		registry.registerFor(ComponentItemEntity.class, ITEM_INVENTORY_COMPONENT, ComponentItemEntity::createItemComponent);

		registry.registerFor(ComponentFluidEntity.class, FLUID_INVENTORY_COMPONENT, ComponentFluidEntity::createFluidComponent);

		registry.registerFor(ComponentEnergyEntity.class, ENERGY_INVENTORY_COMPONENT, ComponentEnergyEntity::createEnergyComponent);
	}

	@Override
	public void registerBlockComponentFactories(BlockComponentFactoryRegistry registry) {}
}
