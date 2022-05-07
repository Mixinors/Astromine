/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
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

package com.github.mixinors.astromine.common.block.entity.storage;

import java.util.function.Supplier;

import com.github.mixinors.astromine.common.block.entity.base.ExtendedBlockEntity;
import com.github.mixinors.astromine.common.config.AMConfig;
import com.github.mixinors.astromine.common.config.entry.tiered.SimpleMachineConfig;
import com.github.mixinors.astromine.common.provider.config.tiered.MachineConfigProvider;
import com.github.mixinors.astromine.common.transfer.storage.SimpleItemStorage;
import com.github.mixinors.astromine.common.util.data.tier.MachineTier;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.EnergyStorageUtil;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;

public abstract class CapacitorBlockEntity extends ExtendedBlockEntity implements MachineConfigProvider<SimpleMachineConfig> {
	private static final int INPUT_SLOT = 0;
	
	private static final int OUTPUT_SLOT = 1;
	
	private static final int[] INSERT_SLOTS = new int[] { INPUT_SLOT };
	
	private static final int[] EXTRACT_SLOTS = new int[] { OUTPUT_SLOT };
	
	public CapacitorBlockEntity(Supplier<? extends BlockEntityType<?>> type, BlockPos blockPos, BlockState blockState) {
		super(type, blockPos, blockState);
		
		energyStorage = new SimpleEnergyStorage(getEnergyStorageSize(), getMachineTier() == MachineTier.CREATIVE ? 0L : getMaxTransferRate(), getMaxTransferRate());
		
		itemStorage = new SimpleItemStorage(2).insertPredicate((variant, slot) ->
			slot == INPUT_SLOT
		).extractPredicate((stack, slot) ->
			slot == OUTPUT_SLOT
		).listener(() -> {
			markDirty();
		}).insertSlots(INSERT_SLOTS).extractSlots(EXTRACT_SLOTS);
	}
	
	@Override
	public void tick() {
		super.tick();

		if (world == null || world.isClient || !shouldRun())
			return;
		
		var wildItemStorage = itemStorage.getWildProxy();
		
		var inputStack = wildItemStorage.getStack(INPUT_SLOT);
		var inputEnergyStorage = EnergyStorage.ITEM.find(inputStack, ContainerItemContext.ofSingleSlot(wildItemStorage.getStorage(INPUT_SLOT)));
		
		var outputStack = wildItemStorage.getStack(OUTPUT_SLOT);
		var outputEnergyStorage = EnergyStorage.ITEM.find(outputStack, ContainerItemContext.ofSingleSlot(wildItemStorage.getStorage(OUTPUT_SLOT)));
		
		try (var transaction = Transaction.openOuter()) {
			EnergyStorageUtil.move(inputEnergyStorage, energyStorage, getMaxTransferRate(), transaction);
			EnergyStorageUtil.move(energyStorage, outputEnergyStorage, getMaxTransferRate(), transaction);
			
			transaction.commit();
		}
	}

	@Override
	public SimpleMachineConfig getConfig() {
		return AMConfig.get().blocks.utilities.capacitors;
	}

	public static class Primitive extends CapacitorBlockEntity {
		public Primitive(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.PRIMITIVE_CAPACITOR, blockPos, blockState);
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
		public MachineTier getMachineTier() {
			return MachineTier.BASIC;
		}
	}

	public static class Advanced extends CapacitorBlockEntity {
		public Advanced(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.ADVANCED_CAPACITOR, blockPos, blockState);
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
		public MachineTier getMachineTier() {
			return MachineTier.ELITE;
		}
	}

	public static class Creative extends CapacitorBlockEntity {
		public Creative(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.CREATIVE_CAPACITOR, blockPos, blockState);
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.CREATIVE;
		}

		@Override
		public void tick() {
			super.tick();
			if (energyStorage.getAmount() < getEnergyStorageSize()) {
				try (Transaction transaction = Transaction.openOuter()) {
					energyStorage.insert(getEnergyStorageSize(), transaction);
					transaction.commit();
				}
			}
		}
	}
}
