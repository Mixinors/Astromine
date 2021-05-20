/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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

package com.github.mixinors.astromine.registry.common;

import com.github.mixinors.astromine.AMCommon;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluids;

import com.github.mixinors.astromine.common.component.base.OxygenComponentImpl;
import com.github.mixinors.astromine.common.component.base.FluidComponentImpl;
import com.github.mixinors.astromine.common.component.base.AtmosphereComponentImpl;
import com.github.mixinors.astromine.common.component.world.WorldHoloBridgeComponent;
import com.github.mixinors.astromine.common.component.base.NetworkComponentImpl;
import com.github.mixinors.astromine.common.entity.base.ComponentEnergyEntity;
import com.github.mixinors.astromine.common.entity.base.ComponentEnergyItemEntity;
import com.github.mixinors.astromine.common.entity.base.ComponentFluidEntity;
import com.github.mixinors.astromine.common.entity.base.ComponentFluidItemEntity;
import com.github.mixinors.astromine.common.entity.base.ComponentItemEntity;
import com.github.mixinors.astromine.common.item.base.FluidVolumeItem;
import com.github.mixinors.astromine.common.volume.fluid.FluidVolume;
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
import net.minecraft.util.Identifier;

public class AMComponents implements WorldComponentInitializer, ChunkComponentInitializer, ItemComponentInitializer, EntityComponentInitializer, BlockComponentInitializer {
	public static final Identifier NETWORK = AMCommon.id("network");
	public static final Identifier ATMOSPHERE = AMCommon.id("atmosphere");
	public static final Identifier BRIDGE = AMCommon.id("bridge");

	public static final Identifier ITEM = AMCommon.id("item");
	public static final Identifier FLUID = AMCommon.id("fluid");
	public static final Identifier ENERGY = AMCommon.id("energy");

	public static final Identifier TRANSFER = AMCommon.id("transfer");
	public static final Identifier REDSTONE = AMCommon.id("redstone");

	public static final Identifier OXYGEN = AMCommon.id("oxygen");

	public static void initialize() {}

	@Override
	public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {
		registry.register(NETWORK, NetworkComponentImpl::new);
		registry.register(BRIDGE, WorldHoloBridgeComponent::new);
	}

	@Override
	public void registerChunkComponentFactories(ChunkComponentFactoryRegistry registry) {
		registry.register(ATMOSPHERE, AtmosphereComponentImpl::new);
	}

	@Override
	public void registerItemComponentFactories(ItemComponentFactoryRegistry registry) {
		registry.registerFor(
				item -> item instanceof FluidVolumeItem,
				FLUID,
				stack -> FluidComponentImpl.of(FluidVolume.of(0L, ((FluidVolumeItem) stack.getItem()).getSize(), Fluids.EMPTY))
		);

		registry.registerFor(
			item -> item == AMItems.SPACE_SUIT_CHESTPLATE.get(),
			AMComponents.FLUID,
			stack -> FluidComponentImpl.of(FluidVolume.of(AMConfig.get().spaceSuitFluid, Fluids.EMPTY))
		);
	}

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerFor(LivingEntity.class, OXYGEN, OxygenComponentImpl::of);

		registry.registerFor(ComponentFluidItemEntity.class, FLUID, ComponentFluidItemEntity::createFluidComponent);
		registry.registerFor(ComponentFluidItemEntity.class, ITEM, ComponentFluidItemEntity::createItemComponent);

		registry.registerFor(ComponentEnergyItemEntity.class, ITEM, ComponentEnergyItemEntity::createItemComponent);
		registry.registerFor(ComponentEnergyItemEntity.class, ENERGY, ComponentEnergyItemEntity::createEnergyComponent);

		registry.registerFor(ComponentItemEntity.class, ITEM, ComponentItemEntity::createItemComponent);

		registry.registerFor(ComponentFluidEntity.class, FLUID, ComponentFluidEntity::createFluidComponent);

		registry.registerFor(ComponentEnergyEntity.class, ENERGY, ComponentEnergyEntity::createEnergyComponent);
	}

	@Override
	public void registerBlockComponentFactories(BlockComponentFactoryRegistry registry) {}
}
