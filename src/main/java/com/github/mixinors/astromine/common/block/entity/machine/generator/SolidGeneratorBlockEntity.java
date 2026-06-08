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
import com.github.mixinors.astromine.common.config.entry.tiered.SolidGeneratorConfig;
import com.github.mixinors.astromine.common.provider.config.tiered.MachineConfigProvider;
import com.github.mixinors.astromine.common.transfer.storage.SimpleItemStorage;
import com.github.mixinors.astromine.common.util.data.tier.Tier;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import com.github.mixinors.astromine.common.transfer.storage.LongEnergyStorage;

import java.util.function.Supplier;

public abstract class SolidGeneratorBlockEntity extends ExtendedBlockEntity implements MachineConfigProvider<SolidGeneratorConfig> {
	public static final String AVAILABLE_KEY = "Available";
	
	private double available = 0;
	
	public static final int INPUT_SLOT = 0;
	
	public static final int[] INSERT_SLOTS = new int[] { INPUT_SLOT };
	
	public static final int[] EXTRACT_SLOTS = new int[] { };
	
	public SolidGeneratorBlockEntity(Supplier<? extends BlockEntityType<?>> type, BlockPos blockPos, BlockState blockState) {
		super(type, blockPos, blockState);
		
		energyStorage = new LongEnergyStorage(getEnergyStorageSize(), 0L, getMaxTransferRate());
		
		itemStorage = new SimpleItemStorage(1).insertPredicate((variant, slot) -> {
			if (slot != INPUT_SLOT) {
				return false;
			}
			
			if (!itemStorage.isEmpty() && !itemStorage.getVariant(INPUT_SLOT).equals(variant)) {
				return false;
			}
			
			return !(variant.getItem() instanceof BucketItem) && variant.getBurnTime(null) > 0;
		}).extractPredicate((variant, slot) ->
				false
		).listener(() -> {
			setChanged();
		}).insertSlots(INSERT_SLOTS).extractSlots(EXTRACT_SLOTS);
	}
	
	@Override
	public void tick() {
		super.tick();
		
		if (level == null || level.isClientSide || !shouldRun()) {
			return;
		}
		
		if (itemStorage != null && energyStorage != null) {
			if (available > 0) {
				progress = limit - available;
				
				var output = Math.max(1L, Math.round(getConfig().getBaseEnergyOutputPerTick() * getSpeed()));
				var headroom = energyStorage.capacity - energyStorage.amount;
				
				if (headroom > 0) {
					var generated = Math.min(Math.min(output, headroom), (long) Math.ceil(available));
					
					energyStorage.amount += generated;
					available -= generated;
					progress = limit - available;
					
					active = true;
				} else {
					active = false;
				}
				
				if (available <= 0) {
					available = 0;
					progress = 0.0D;
					limit = 0;
					
					active = false;
				}
			} else {
				progress = 0.0D;
				
				var inputStack = itemStorage.getItem(INPUT_SLOT);
				var inputBurnTime = inputStack.getBurnTime(null);
				var isFuel = !(inputStack.getItem() instanceof BucketItem) && inputBurnTime > 0;
				
				if (isFuel && energyStorage.amount < energyStorage.capacity) {
					available = inputBurnTime * getConfig().getEnergyPerBurnTick();
					limit = available;
					
					progress = 0.0D;
					
					itemStorage.removeItem(INPUT_SLOT, 1);
				}
				
				active = available > 0 || progress != 0;
			}
		}
	}
	
	@Override
	public void saveAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
		nbt.putDouble(AVAILABLE_KEY, available);
		
		super.saveAdditional(nbt, registries);
	}
	
	@Override
	protected void loadAdditional(@NotNull CompoundTag nbt, HolderLookup.Provider registries) {
		available = nbt.getDouble(AVAILABLE_KEY);
		
		super.loadAdditional(nbt, registries);
	}
	
	@Override
	public SolidGeneratorConfig getConfig() {
		return AMConfig.get().blocks.machines.solidGenerator;
	}
	
	public static class Primitive extends SolidGeneratorBlockEntity {
		public Primitive(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.PRIMITIVE_SOLID_GENERATOR, blockPos, blockState);
		}
		
		@Override
		public Tier getMachineTier() {
			return Tier.PRIMITIVE;
		}
	}
	
	public static class Basic extends SolidGeneratorBlockEntity {
		public Basic(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.BASIC_SOLID_GENERATOR, blockPos, blockState);
		}
		
		@Override
		public Tier getMachineTier() {
			return Tier.BASIC;
		}
	}
	
	public static class Advanced extends SolidGeneratorBlockEntity {
		public Advanced(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.ADVANCED_SOLID_GENERATOR, blockPos, blockState);
		}
		
		@Override
		public Tier getMachineTier() {
			return Tier.ADVANCED;
		}
	}
	
	public static class Elite extends SolidGeneratorBlockEntity {
		public Elite(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.ELITE_SOLID_GENERATOR, blockPos, blockState);
		}
		
		@Override
		public Tier getMachineTier() {
			return Tier.ELITE;
		}
	}
}
