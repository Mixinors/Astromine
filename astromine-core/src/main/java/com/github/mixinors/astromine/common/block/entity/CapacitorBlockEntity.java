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

package com.github.mixinors.astromine.common.block.entity;

import com.github.mixinors.astromine.common.component.general.*;
import com.github.mixinors.astromine.common.component.general.base.EnergyComponent;
import com.github.mixinors.astromine.common.component.general.base.ItemComponent;
import com.github.mixinors.astromine.registry.AMBlockEntityTypes;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;

import com.github.mixinors.astromine.common.block.entity.base.ComponentEnergyItemBlockEntity;
import com.github.mixinors.astromine.common.util.tier.MachineTier;
import com.github.mixinors.astromine.common.volume.energy.InfiniteEnergyVolume;
import com.github.mixinors.astromine.registry.AMConfig;
import com.github.mixinors.astromine.common.block.entity.machine.EnergySizeProvider;
import com.github.mixinors.astromine.common.block.entity.machine.SpeedProvider;
import com.github.mixinors.astromine.common.block.entity.machine.TierProvider;
import team.reborn.energy.Energy;
import team.reborn.energy.EnergyHandler;

public abstract class CapacitorBlockEntity extends ComponentEnergyItemBlockEntity implements EnergySizeProvider, TierProvider, SpeedProvider {
	public CapacitorBlockEntity(BlockEntityType<?> type) {
		super(type);
	}

	@Override
	public ItemComponent createItemComponent() {
		return SimpleDirectionalItemComponent.of(this, 2).withInsertPredicate((direction, stack, slot) -> {
			return slot == 0;
		}).withExtractPredicate((direction, stack, slot) -> {
			return slot == 1;
		});
	}

	@Override
	public EnergyComponent createEnergyComponent() {
		return SimpleEnergyComponent.of(getEnergySize());
	}

	@Override
	public void tick() {
		super.tick();

		if (world == null || world.isClient || !tickRedstone())
			return;

		ItemComponent itemComponent = getItemComponent();

		ItemStack inputStack = itemComponent.getFirst();
		if (Energy.valid(inputStack)) {
			EnergyHandler energyHandler = Energy.of(inputStack);
			energyHandler.into(Energy.of(this)).move(1024 * getMachineSpeed());
		}

		ItemStack outputStack = itemComponent.getSecond();
		if (Energy.valid(outputStack)) {
			EnergyHandler energyHandler = Energy.of(outputStack);
			Energy.of(this).into(energyHandler).move(1024 * getMachineSpeed());
		}
	}

	public static class Primitive extends CapacitorBlockEntity {
		public Primitive() {
			super(AMBlockEntityTypes.PRIMITIVE_CAPACITOR);
		}

		@Override
		public double getEnergySize() {
			return AMConfig.get().primitiveCapacitorEnergy;
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().primitiveCapacitorSpeed;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.PRIMITIVE;
		}
	}

	public static class Basic extends CapacitorBlockEntity {
		public Basic() {
			super(AMBlockEntityTypes.BASIC_CAPACITOR);
		}

		@Override
		public double getEnergySize() {
			return AMConfig.get().basicCapacitorEnergy;
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().basicCapacitorSpeed;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.BASIC;
		}
	}

	public static class Advanced extends CapacitorBlockEntity {
		public Advanced() {
			super(AMBlockEntityTypes.ADVANCED_CAPACITOR);
		}

		@Override
		public double getEnergySize() {
			return AMConfig.get().advancedCapacitorEnergy;
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().advancedCapacitorSpeed;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.ADVANCED;
		}
	}

	public static class Elite extends CapacitorBlockEntity {
		public Elite() {
			super(AMBlockEntityTypes.ELITE_CAPACITOR);
		}

		@Override
		public double getEnergySize() {
			return AMConfig.get().eliteCapacitorEnergy;
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().eliteCapacitorSpeed;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.ELITE;
		}
	}

	public static class Creative extends CapacitorBlockEntity {
		public Creative() {
			super(AMBlockEntityTypes.CREATIVE_CAPACITOR);
		}

		@Override
		public EnergyComponent createEnergyComponent() {
			return SimpleEnergyComponent.of(InfiniteEnergyVolume.of());
		}

		@Override
		public double getEnergySize() {
			return Double.MAX_VALUE;
		}

		@Override
		public double getMachineSpeed() {
			return Double.MAX_VALUE;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.CREATIVE;
		}
	}
}
