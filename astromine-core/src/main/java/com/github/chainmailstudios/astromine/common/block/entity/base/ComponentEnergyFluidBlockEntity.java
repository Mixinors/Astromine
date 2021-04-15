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

package com.github.chainmailstudios.astromine.common.block.entity.base;

import com.github.chainmailstudios.astromine.common.component.general.base.EnergyComponent;
import com.github.chainmailstudios.astromine.common.component.general.base.FluidComponent;
import com.github.chainmailstudios.astromine.common.component.general.base.ItemComponent;
import com.github.chainmailstudios.astromine.common.component.general.provider.EnergyComponentProvider;
import com.github.chainmailstudios.astromine.common.component.general.provider.FluidComponentProvider;
import net.minecraft.block.entity.BlockEntityType;

import com.github.chainmailstudios.astromine.common.utilities.capability.energy.ComponentEnergyProvider;
import com.github.chainmailstudios.astromine.registry.AstromineComponents;

/**
 * A {@link ComponentBlockEntity} with an attached
 * {@link EnergyComponent} and {@link ItemComponent}.
 */
public abstract class ComponentEnergyFluidBlockEntity extends ComponentBlockEntity implements ComponentEnergyProvider, EnergyComponentProvider, FluidComponentProvider {
	private final EnergyComponent energyComponent = createEnergyComponent();

	private final FluidComponent fluidComponent = createFluidComponent();

	/** Instantiates a {@link ComponentEnergyFluidBlockEntity}. */
	public ComponentEnergyFluidBlockEntity(BlockEntityType<?> type) {
		super(type);

		addComponent(AstromineComponents.ENERGY_INVENTORY_COMPONENT, getEnergyComponent());
		getEnergyComponent().updateListeners();

		addComponent(AstromineComponents.FLUID_INVENTORY_COMPONENT, getFluidComponent());
		getFluidComponent().updateListeners();
	}

	/** Returns the {@link EnergyComponent} to be attached. */
	public abstract EnergyComponent createEnergyComponent();

	/** Returns the attached {@link EnergyComponent}. */
	@Override
	public EnergyComponent getEnergyComponent() {
		return energyComponent;
	}

	/** Returns the {@link FluidComponent} to be attached. */
	public abstract FluidComponent createFluidComponent();

	/** Returns the attached {@link FluidComponent}. */
	@Override
	public FluidComponent getFluidComponent() {
		return fluidComponent;
	}
}
