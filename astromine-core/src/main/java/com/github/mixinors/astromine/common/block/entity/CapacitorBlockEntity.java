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

import com.github.mixinors.astromine.common.block.entity.base.ExtendedBlockEntity;
import com.github.mixinors.astromine.common.transfer.storage.SimpleItemStorage;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;

import com.github.mixinors.astromine.common.util.tier.MachineTier;
import com.github.mixinors.astromine.registry.common.AMConfig;
import com.github.mixinors.astromine.common.block.entity.machine.EnergySizeProvider;
import com.github.mixinors.astromine.common.block.entity.machine.SpeedProvider;
import com.github.mixinors.astromine.common.block.entity.machine.TierProvider;
import net.minecraft.util.math.BlockPos;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.function.Supplier;

public abstract class CapacitorBlockEntity extends ExtendedBlockEntity implements EnergySizeProvider, TierProvider, SpeedProvider {
	public CapacitorBlockEntity(Supplier<? extends BlockEntityType<?>> type, BlockPos blockPos, BlockState blockState) {
		super(type, blockPos, blockState);
		
		energyStorage = new SimpleEnergyStorage(getEnergySize(), Long.MAX_VALUE, Long.MAX_VALUE);
		
		itemStorage = new SimpleItemStorage(2).insertPredicate((variant, slot) -> {
			return slot == 0;
		}).extractPredicate((stack, slot) -> {
			return slot == 1;
		});
	}
	
	@Override
	public void tick() {
		super.tick();

		if (world == null || world.isClient || !shouldRun())
			return;

		var extractStack = itemStorage.getStack(0);
		var extractEnergyStorage = EnergyStorage.ITEM.find(extractStack, ContainerItemContext.ofSingleSlot((SingleSlotStorage<ItemVariant>) itemStorage.getStorage(0)));
		
		try (var extractTransaction = Transaction.openOuter()) {
			extractEnergyStorage.extract((long) Math.min(1024 * getMachineSpeed(), energyStorage.getCapacity() - energyStorage.getAmount()), extractTransaction);
			
			try (var insertTranscation = extractTransaction.openNested()) {
				var insertStack = itemStorage.getStack(1);
				var insertEnergyStorage = EnergyStorage.ITEM.find(insertStack, ContainerItemContext.ofSingleSlot((SingleSlotStorage<ItemVariant>) itemStorage.getStorage(1)));
				
				insertEnergyStorage.insert((long) (1024 * getMachineSpeed()), insertTranscation);
				
				insertTranscation.commit();
				extractTransaction.commit();
			}
			
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
			
			energyStorage = new SimpleEnergyStorage(Long.MAX_VALUE, Long.MAX_VALUE, Long.MAX_VALUE);
		}
		

		@Override
		public long getEnergySize() {
			return Long.MAX_VALUE;
		}

		@Override
		public double getMachineSpeed() {
			return Long.MAX_VALUE;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.CREATIVE;
		}
	}
}
