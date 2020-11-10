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

package com.github.chainmailstudios.astromine.common.entity.base;

import com.github.chainmailstudios.astromine.common.component.inventory.EnergyComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.FluidComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.ItemComponent;
import com.github.chainmailstudios.astromine.common.utilities.capability.inventory.InventoryFromItemComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public abstract class ComponentEnergyFluidItemEntity extends ComponentEntity implements InventoryFromItemComponent {
	public ComponentEnergyFluidItemEntity(EntityType<?> type, World world) {
		super(type, world);
	}

	public abstract ItemComponent createItemComponent();

	public abstract FluidComponent createFluidComponent();

	public abstract EnergyComponent createEnergyComponent();

	public ItemComponent getItemComponent() {
		return ItemComponent.get(this);
	}

	public FluidComponent getFluidComponent() {
		return FluidComponent.get(this);
	}

	public EnergyComponent getEnergyComponent() {
		return EnergyComponent.get(this);
	}
}
