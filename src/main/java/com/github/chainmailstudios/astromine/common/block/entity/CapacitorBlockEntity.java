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

package com.github.chainmailstudios.astromine.common.block.entity;

import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedEnergyItemBlockEntity;
import com.github.chainmailstudios.astromine.common.component.inventory.ItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleItemInventoryComponent;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineConfig;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tickable;
import team.reborn.energy.Energy;
import team.reborn.energy.EnergyHandler;

public abstract class CapacitorBlockEntity extends DefaultedEnergyItemBlockEntity implements Tickable {
	public CapacitorBlockEntity(BlockEntityType<?> type) {
		super(type);
	}

	@Override
	protected ItemInventoryComponent createItemComponent() {
		return new SimpleItemInventoryComponent(2);
	}

	@Override
	public void tick() {
		super.tick();

		if (world.isClient())
			return;
		// input
		ItemStack inputStack = itemComponent.getStack(0);
		if (Energy.valid(inputStack)) {
			EnergyHandler energyHandler = Energy.of(inputStack);
			energyHandler.into(asEnergy()).move();
		}

		ItemStack outputStack = itemComponent.getStack(1);
		if (Energy.valid(outputStack)) {
			EnergyHandler energyHandler = Energy.of(outputStack);
			asEnergy().into(energyHandler).move();
		}
	}

	public static class Primitive extends CapacitorBlockEntity {
		public Primitive() {
			super(AstromineBlockEntityTypes.PRIMITIVE_CAPACITOR);
		}

		@Override
		protected double getEnergySize() {
			return AstromineConfig.get().primitiveCapacitorEnergy;
		}
	}

	public static class Basic extends CapacitorBlockEntity {
		public Basic() {
			super(AstromineBlockEntityTypes.BASIC_CAPACITOR);
		}

		@Override
		protected double getEnergySize() {
			return AstromineConfig.get().basicCapacitorEnergy;
		}
	}

	public static class Advanced extends CapacitorBlockEntity {
		public Advanced() {
			super(AstromineBlockEntityTypes.ADVANCED_CAPACITOR);
		}

		@Override
		protected double getEnergySize() {
			return AstromineConfig.get().advancedCapacitorEnergy;
		}
	}

	public static class Elite extends CapacitorBlockEntity {
		public Elite() {
			super(AstromineBlockEntityTypes.ELITE_CAPACITOR);
		}

		@Override
		protected double getEnergySize() {
			return AstromineConfig.get().eliteCapacitorEnergy;
		}
	}
}
