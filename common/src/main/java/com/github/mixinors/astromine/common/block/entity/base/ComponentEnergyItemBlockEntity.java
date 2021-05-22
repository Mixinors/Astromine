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

import com.github.mixinors.astromine.techreborn.common.component.general.provider.EnergyComponentProvider;
import com.github.mixinors.astromine.techreborn.common.component.general.provider.ItemComponentProvider;
import net.minecraft.block.entity.BlockEntityType;

import com.github.mixinors.astromine.cardinalcomponents.common.component.base.EnergyComponent;
import com.github.mixinors.astromine.cardinalcomponents.common.component.base.ItemComponent;
import com.github.mixinors.astromine.techreborn.common.util.capability.inventory.InventoryItemComponentProvider;

import java.util.function.Supplier;

/**
 * A {@link ComponentBlockEntity} with an attached
 * {@link EnergyComponent} and {@link ItemComponent}.
 */
public abstract class ComponentEnergyItemBlockEntity extends ComponentBlockEntity implements InventoryItemComponentProvider, EnergyComponentProvider, ItemComponentProvider {
	protected final EnergyComponent energy = createEnergyComponent();
	
	protected final ItemComponent items = createItemComponent();

	/** Instantiates a {@link ComponentEnergyItemBlockEntity}. */
	public ComponentEnergyItemBlockEntity(Supplier<? extends BlockEntityType<?>> type) {
		super(type);

		energy.updateListeners();
		items.updateListeners();
	}

	/** Returns the {@link EnergyComponent} to be attached. */
	public abstract EnergyComponent createEnergyComponent();

	/** Returns the attached {@link EnergyComponent}. */
	public EnergyComponent getEnergyComponent() {
		return energy;
	}

	/** Returns the {@link ItemComponent} to be attached. */
	public abstract ItemComponent createItemComponent();

	/** Returns the attached {@link ItemComponent}. */
	@Override
	public ItemComponent getItemComponent() {
		return items;
	}
}
