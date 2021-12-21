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
import net.fabricmc.fabric.api.registry.FuelRegistry;

import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BucketItem;
import net.minecraft.nbt.NbtCompound;
import com.github.mixinors.astromine.common.util.tier.MachineTier;
import com.github.mixinors.astromine.registry.common.AMConfig;
import com.github.mixinors.astromine.common.block.entity.machine.EnergySizeProvider;
import com.github.mixinors.astromine.common.block.entity.machine.SpeedProvider;
import com.github.mixinors.astromine.common.block.entity.machine.TierProvider;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.function.Supplier;

public abstract class SolidGeneratorBlockEntity extends ExtendedBlockEntity implements EnergySizeProvider, TierProvider, SpeedProvider {
	private double available = 0;
	private double progress = 0;
	private int limit = 100;
	
	private static final int INPUT_SLOT = 0;
	
	private static final int[] INSERT_SLOTS = new int[] { INPUT_SLOT };
	
	private static final int[] EXTRACT_SLOTS = new int[] { };

	public SolidGeneratorBlockEntity(Supplier<? extends BlockEntityType<?>> type, BlockPos blockPos, BlockState blockState) {
		super(type, blockPos, blockState);
		
		energyStorage = new SimpleEnergyStorage(getEnergySize(), Long.MAX_VALUE, Long.MAX_VALUE);
		
		itemStorage = new SimpleItemStorage(1).insertPredicate((variant, slot) -> {
			if (slot != INPUT_SLOT) {
				return false;
			}
			
			if (!itemStorage.getVariant(INPUT_SLOT).equals(variant)) {
				return false;
			}
			
			return FuelRegistry.INSTANCE.get(variant.getItem()) != null;
		}).extractPredicate((variant, slot) -> {
			return false;
		}).insertSlots(INSERT_SLOTS).extractSlots(EXTRACT_SLOTS);
	}

	@Override
	public void tick() {
		super.tick();

		if (world == null || world.isClient || !shouldRun())
			return;

		if (itemStorage != null && energyStorage != null) {
			try (var transaction = Transaction.openOuter()) {
				if (available > 0) {
					var produced = 5;
					
					for (int i = 0; i < 3 * getMachineSpeed(); ++i) {
						if (progress < limit) {
							// TODO: Make it work without waiting for a whole step.
							if (energyStorage.insert(produced, transaction) == produced) {
								--available;
								
								++produced;
								
								isActive = true;
							} else {
								isActive = false;
							}
							
							if (progress >= limit || available <= 0) {
								progress = 0;
								limit = 0;
								
								isActive = false;
							}
						}
					}
				} else {
					var inputStack = itemStorage.getStack(INPUT_SLOT);
					
					var inputBurnTime = FuelRegistry.INSTANCE.get(inputStack.getItem());
					
					if (inputBurnTime != null) {
						var isFuel = !(inputStack.getItem() instanceof BucketItem) && inputBurnTime > 0;
						
						if (isFuel) {
							available = inputBurnTime;
							limit = inputBurnTime;
							
							progress = 0;
							
							itemStorage.extract(itemStorage.getVariant(INPUT_SLOT), 1, transaction);
						}
						
						if (isFuel || progress != 0) {
							isActive = true;
						} else {
							isActive = false;
						}
					} else {
						isActive = false;
					}
				}
			}
		}
	}

	@Override
	public void writeNbt(NbtCompound nbt) {
		nbt.putDouble("Progress", progress);
		nbt.putInt("Limit", limit);
		nbt.putDouble("Available", available);
		
		super.writeNbt(nbt);
	}

	@Override
	public void readNbt(@NotNull NbtCompound nbt) {
		progress = nbt.getDouble("Progress");
		limit = nbt.getInt("Limit");
		available = nbt.getDouble("Available");
		
		super.readNbt(nbt);
	}

	public static class Primitive extends SolidGeneratorBlockEntity {
		public Primitive(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.PRIMITIVE_SOLID_GENERATOR, blockPos, blockState);
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().primitiveSolidGeneratorSpeed;
		}

		@Override
		public long getEnergySize() {
			return AMConfig.get().primitiveSolidGeneratorEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.PRIMITIVE;
		}
	}

	public static class Basic extends SolidGeneratorBlockEntity {
		public Basic(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.BASIC_SOLID_GENERATOR, blockPos, blockState);
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().basicSolidGeneratorSpeed;
		}

		@Override
		public long getEnergySize() {
			return AMConfig.get().basicSolidGeneratorEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.BASIC;
		}
	}

	public static class Advanced extends SolidGeneratorBlockEntity {
		public Advanced(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.ADVANCED_SOLID_GENERATOR, blockPos, blockState);
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().advancedSolidGeneratorSpeed;
		}

		@Override
		public long getEnergySize() {
			return AMConfig.get().advancedSolidGeneratorEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.ADVANCED;
		}
	}

	public static class Elite extends SolidGeneratorBlockEntity {
		public Elite(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.ELITE_SOLID_GENERATOR, blockPos, blockState);
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().eliteSolidGeneratorSpeed;
		}

		@Override
		public long getEnergySize() {
			return AMConfig.get().eliteSolidGeneratorEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.ELITE;
		}
	}
}
