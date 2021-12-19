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

import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;

import com.github.mixinors.astromine.common.util.tier.MachineTier;
import com.github.mixinors.astromine.common.volume.energy.InfiniteEnergyVolume;
import com.github.mixinors.astromine.registry.common.AMConfig;
import com.github.mixinors.astromine.common.block.entity.machine.EnergySizeProvider;
import com.github.mixinors.astromine.common.block.entity.machine.SpeedProvider;
import com.github.mixinors.astromine.common.block.entity.machine.TierProvider;
import net.minecraft.util.math.BlockPos;
import team.reborn.energy.Energy;
import team.reborn.energy.EnergyHandler;

import java.util.function.Supplier;

public abstract class CapacitorBlockEntity extends ComponentEnergyItemBlockEntity implements EnergySizeProvider, TierProvider, SpeedProvider {
	public CapacitorBlockEntity(Supplier<? extends BlockEntityType<?>> type, BlockPos blockPos, BlockState blockState) {
		super(type, blockPos, blockState);
	}

	@Override
	public SimpleItemStorage createItemComponent() {
		return SimpleDirectionalItemComponent.of(this, 2).withInsertPredicate((direction, stack, slot) -> {
			return slot == 0;
		}).withExtractPredicate((direction, stack, slot) -> {
			return slot == 1;
		});
	}

	@Override
	public EnergyStore createEnergyComponent() {
		return SimpleEnergyComponent.of(getEnergySize());
	}

	@Override
	public void tick() {
		super.tick();

		if (world == null || world.isClient || !shouldRun())
			return;

		SimpleItemStorage itemStorage = getItemComponent();

		ItemStack inputStack = itemStorage.getStack(0);
		if (Energy.valid(inputStack)) {
			EnergyHandler energyHandler = Energy.of(inputStack);
			energyHandler.into(Energy.of(this)).move(1024 * getMachineSpeed());
		}

		ItemStack outputStack = itemStorage.getStack(1);
		if (Energy.valid(outputStack)) {
			EnergyHandler energyHandler = Energy.of(outputStack);
			Energy.of(this).into(energyHandler).move(1024 * getMachineSpeed());
		}
	}

	public static class Primitive extends CapacitorBlockEntity {
		public Primitive(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.PRIMITIVE_CAPACITOR, blockPos, blockState);
		}

		@Override
		public long getEnergySize() {
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
		public Basic(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.BASIC_CAPACITOR, blockPos, blockState);
		}

		@Override
		public long getEnergySize() {
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
		public Advanced(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.ADVANCED_CAPACITOR, blockPos, blockState);
		}

		@Override
		public long getEnergySize() {
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
		public Elite(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.ELITE_CAPACITOR, blockPos, blockState);
		}

		@Override
		public long getEnergySize() {
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
		public Creative(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.CREATIVE_CAPACITOR, blockPos, blockState);
		}

		@Override
		public EnergyStore createEnergyComponent() {
			return SimpleEnergyComponent.of(InfiniteEnergyVolume.of());
		}

		@Override
		public long getEnergySize() {
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
