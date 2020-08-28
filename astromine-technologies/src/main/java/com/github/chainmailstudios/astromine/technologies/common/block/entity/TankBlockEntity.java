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

package com.github.chainmailstudios.astromine.technologies.common.block.entity;

import com.github.chainmailstudios.astromine.common.block.entity.base.ComponentFluidBlockEntity;
import com.github.chainmailstudios.astromine.common.block.entity.base.ComponentFluidInventoryBlockEntity;
import com.github.chainmailstudios.astromine.common.component.inventory.FluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.ItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleFluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlockEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineConfig;
import nerdhub.cardinal.components.api.component.ComponentProvider;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;

import java.util.Map;

public class TankBlockEntity extends ComponentFluidInventoryBlockEntity {
	public TankBlockEntity() {
		super(AstromineTechnologiesBlockEntityTypes.TANK);

		fluidComponent.getVolume(0).setSize(new Fraction(AstromineConfig.get().tankFluid, 1));
	}

	public TankBlockEntity(BlockEntityType<?> type) {
		super(type);

		fluidComponent.getVolume(0).setSize(new Fraction(AstromineConfig.get().tankFluid, 1));
	}

	@Override
	protected FluidInventoryComponent createFluidComponent() {
		return new SimpleFluidInventoryComponent(1);
	}

	@Override
	protected ItemInventoryComponent createItemComponent() {
		return new SimpleItemInventoryComponent(2);
	}

	@Override
	public void tick() {
		super.tick();

		ItemStack leftStack = itemComponent.getStack(0);
		ItemStack rightStack = itemComponent.getStack(1);

		ComponentProvider leftProvider = ComponentProvider.fromItemStack(leftStack);
		ComponentProvider rightProvider = ComponentProvider.fromItemStack(rightStack);

		FluidInventoryComponent leftComponent = leftProvider.getComponent(AstromineComponentTypes.FLUID_INVENTORY_COMPONENT);
		FluidInventoryComponent rightComponent = rightProvider.getComponent(AstromineComponentTypes.FLUID_INVENTORY_COMPONENT);

		if (leftComponent != null) {
			for (FluidVolume volume : leftComponent.getContents().values()) {
				if (volume.equalsFluid(fluidComponent.getVolume(0)) || fluidComponent.getVolume(0).isEmpty()) {
					volume.pushVolume(fluidComponent.getVolume(0), Fraction.min(Fraction.bottle(), fluidComponent.getVolume(0).getAvailable()));
					break;
				}
			}
		}

		if (!fluidComponent.getVolume(0).isEmpty() && rightComponent != null) {
			for (FluidVolume volume : rightComponent.getContents().values()) {
				if (volume.equalsFluid(fluidComponent.getVolume(0)) || volume.isEmpty()) {
					fluidComponent.getVolume(0).pushVolume(volume, Fraction.min(Fraction.bottle(), volume.getAvailable()));
				}
			}
		}
	}
}
