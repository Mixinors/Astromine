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

package com.github.mixinors.astromine.common.block.entity.machine.generator;

import com.github.mixinors.astromine.common.block.entity.base.ExtendedBlockEntity;
import com.github.mixinors.astromine.common.config.AMConfig;
import com.github.mixinors.astromine.common.config.entry.tiered.SimpleMachineConfig;
import com.github.mixinors.astromine.common.provider.config.tiered.MachineConfigProvider;
import com.github.mixinors.astromine.common.transfer.storage.SimpleItemStorage;
import com.github.mixinors.astromine.common.util.data.tier.MachineTier;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BucketItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.function.Supplier;

public abstract class SolidGeneratorBlockEntity extends ExtendedBlockEntity implements MachineConfigProvider<SimpleMachineConfig> {
	public static final String AVAILABLE_KEY = "Available";
	
	private double available = 0;
	
	public static final int INPUT_SLOT = 0;
	
	public static final int[] INSERT_SLOTS = new int[] { INPUT_SLOT };
	
	public static final int[] EXTRACT_SLOTS = new int[] { };
	
	public SolidGeneratorBlockEntity(Supplier<? extends BlockEntityType<?>> type, BlockPos blockPos, BlockState blockState) {
		super(type, blockPos, blockState);
		
		energyStorage = new SimpleEnergyStorage(getEnergyStorageSize(), 0L, getMaxTransferRate());
		
		itemStorage = new SimpleItemStorage(1).insertPredicate((variant, slot) -> {
			if (slot != INPUT_SLOT) {
				return false;
			}
			
			if (!itemStorage.isEmpty() && !itemStorage.getVariant(INPUT_SLOT).equals(variant)) {
				return false;
			}
			
			return FuelRegistry.INSTANCE.get(variant.getItem()) != null;
		}).extractPredicate((variant, slot) ->
				false
		).listener(() -> {
			markDirty();
		}).insertSlots(INSERT_SLOTS).extractSlots(EXTRACT_SLOTS);
	}
	
	@Override
	public void tick() {
		super.tick();
		
		if (world == null || world.isClient || !shouldRun()) {
			return;
		}
		
		if (itemStorage != null && energyStorage != null) {
			try (var transaction = Transaction.openOuter()) {
				if (available > 0) {
					progress = limit - available;
					
					var produced = 5;
					
					for (var i = 0; i < 3 * getSpeed(); ++i) {
						if (progress < limit) {
							var nestedTransaction = transaction.openNested();
							
							if (energyStorage.amount + produced <= energyStorage.capacity) {
								energyStorage.amount += produced;
								
								--available;
								
								++produced;
								
								active = true;
								
								nestedTransaction.commit();
							} else {
								nestedTransaction.abort();
								
								active = false;
							}
							
							if (progress >= limit || available <= 0) {
								progress = 0.0D;
								limit = 0;
								
								active = false;
							}
						}
					}
				} else {
					progress = 0.0D;
					
					var inputStack = itemStorage.getStack(INPUT_SLOT);
					
					var inputBurnTime = FuelRegistry.INSTANCE.get(inputStack.getItem());
					
					if (inputBurnTime != null) {
						var isFuel = !(inputStack.getItem() instanceof BucketItem) && inputBurnTime > 0;
						
						if (isFuel) {
							available = inputBurnTime;
							limit = inputBurnTime;
							
							progress = 0.0D;
							
							var nestedTransaction = transaction.openNested();
							
							itemStorage.removeStack(INPUT_SLOT, 1);
							
							nestedTransaction.commit();
						}
						
						active = isFuel || progress != 0;
					} else {
						active = false;
					}
				}
				
				transaction.commit();
			}
		}
	}
	
	@Override
	public void writeNbt(NbtCompound nbt) {
		nbt.putDouble(AVAILABLE_KEY, available);
		
		super.writeNbt(nbt);
	}
	
	@Override
	public void readNbt(@NotNull NbtCompound nbt) {
		available = nbt.getDouble(AVAILABLE_KEY);
		
		super.readNbt(nbt);
	}
	
	@Override
	public SimpleMachineConfig getConfig() {
		return AMConfig.get().blocks.machines.solidGenerator;
	}
	
	public static class Primitive extends SolidGeneratorBlockEntity {
		public Primitive(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.PRIMITIVE_SOLID_GENERATOR, blockPos, blockState);
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
		public MachineTier getMachineTier() {
			return MachineTier.BASIC;
		}
	}
	
	public static class Advanced extends SolidGeneratorBlockEntity {
		public Advanced(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.ADVANCED_SOLID_GENERATOR, blockPos, blockState);
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
		public MachineTier getMachineTier() {
			return MachineTier.ELITE;
		}
	}
}
