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

package com.github.mixinors.astromine.common.block.entity.base;

import com.github.mixinors.astromine.registry.common.AMComponents;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;

import com.github.mixinors.astromine.common.util.capability.energy.ComponentEnergyProvider;
import com.github.mixinors.astromine.common.util.capability.inventory.ComponentInventoryProvider;
import net.minecraft.util.math.BlockPos;

import java.util.function.Supplier;

/**
 * A {@link ComponentBlockEntity} with an attached
 * {@link EnergyStore}, {@link FluidStore}
 * and {@link ItemStore}.
 */
public abstract class ComponentEnergyFluidItemBlockEntity extends ComponentBlockEntity implements ComponentEnergyProvider, ComponentInventoryProvider, EnergyComponentProvider, FluidComponentProvider, ItemComponentProvider {
	private final EnergyStore energyComponent = createEnergyComponent();

	private final FluidStore fluidComponent = createFluidComponent();

	private final ItemStore itemComponent = createItemComponent();

	/** Instantiates a {@link ComponentEnergyFluidItemBlockEntity}. */
	public ComponentEnergyFluidItemBlockEntity(Supplier<? extends BlockEntityType<?>> type, BlockPos blockPos, BlockState blockState) {
		super(type, blockPos, blockState);

		addComponent(AMComponents.ENERGY_INVENTORY_COMPONENT, getEnergyComponent());
		getEnergyComponent().updateListeners();

		addComponent(AMComponents.FLUID_INVENTORY_COMPONENT, getFluidComponent());
		getFluidComponent().updateListeners();
		
		addComponent(AMComponents.ITEM_INVENTORY_COMPONENT, getItemComponent());
		getItemComponent().triggerListeners();
	}

	/** Returns the {@link EnergyStore} to be attached. */
	public abstract EnergyStore createEnergyComponent();

	/** Returns the attached {@link EnergyStore}. */
	@Override
	public EnergyStore getEnergyComponent() {
		return energyComponent;
	}

	/** Returns the {@link FluidStore} to be attached. */
	public abstract FluidStore createFluidComponent();

	/** Returns the attached {@link FluidStore}. */
	@Override
	public FluidStore getFluidComponent() {
		return fluidComponent;
	}

	/** Returns the {@link ItemStore} to be attached. */
	public abstract ItemStore createItemComponent();

	/** Returns the attached {@link ItemStore}. */
	@Override
	public ItemStore getItemComponent() {
		return itemComponent;
	}
}
