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

import com.github.mixinors.astromine.common.component.base.EnergyComponent;
import com.github.mixinors.astromine.common.component.base.ItemComponent;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import net.minecraft.block.entity.BlockEntityType;

import com.github.mixinors.astromine.common.block.entity.base.ComponentEnergyItemBlockEntity;
import com.github.mixinors.astromine.common.volume.energy.InfiniteEnergyVolume;
import com.github.mixinors.astromine.registry.common.AMConfig;
import com.github.mixinors.astromine.common.block.entity.machine.EnergySizeProvider;
import com.github.mixinors.astromine.common.block.entity.machine.SpeedProvider;

import java.util.function.Supplier;

public abstract class CapacitorBlockEntity extends ComponentEnergyItemBlockEntity implements EnergySizeProvider, SpeedProvider {
	public CapacitorBlockEntity(Supplier<? extends BlockEntityType<?>> type) {
		super(type);
	}

	@Override
	public ItemComponent createItemComponent() {
		return ItemComponent.of(this, 2).withInsertPredicate((direction, stack, slot) -> {
			if (!transfer.getItem(direction).canInsert()) {
				return false;
			}
			
			return slot == 0;
		}).withExtractPredicate((direction, stack, slot) -> {
			if (!transfer.getItem(direction).canExtract()) {
				return false;
			}
			
			return slot == 1;
		});
	}

	@Override
	public EnergyComponent createEnergyComponent() {
		return EnergyComponent.of(getEnergySize());
	}

	@Override
	public void tick() {
		super.tick();

		if (world == null || world.isClient || !tickRedstone())
			return;
		
		var input = items.getFirst();
		var output = items.getSecond();
		
		var inputEnergy = EnergyComponent.from(input);
		var outputEnergy = EnergyComponent.from(output);
		
		if (inputEnergy != null) {
			inputEnergy.into(energy, 1024.0D * getMachineSpeed());
		}
		
		if (outputEnergy != null) {
			energy.into(outputEnergy, 1024.0D * getMachineSpeed());
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
	}

	public static class Creative extends CapacitorBlockEntity {
		public Creative() {
			super(AMBlockEntityTypes.CREATIVE_CAPACITOR);
		}

		@Override
		public EnergyComponent createEnergyComponent() {
			return EnergyComponent.of(InfiniteEnergyVolume.of());
		}

		@Override
		public double getEnergySize() {
			return Double.MAX_VALUE;
		}

		@Override
		public double getMachineSpeed() {
			return Double.MAX_VALUE;
		}
	}
}
