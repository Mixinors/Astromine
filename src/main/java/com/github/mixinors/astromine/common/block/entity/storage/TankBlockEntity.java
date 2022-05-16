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

import com.github.mixinors.astromine.common.block.entity.base.ExtendedBlockEntity;
import com.github.mixinors.astromine.common.config.AMConfig;
import com.github.mixinors.astromine.common.config.entry.tiered.TankConfig;
import com.github.mixinors.astromine.common.provider.config.tiered.TankConfigProvider;
import com.github.mixinors.astromine.common.transfer.storage.SimpleFluidStorage;
import com.github.mixinors.astromine.common.transfer.storage.SimpleItemStorage;
import com.github.mixinors.astromine.common.util.data.tier.MachineTier;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public abstract class TankBlockEntity extends ExtendedBlockEntity implements TankConfigProvider {
	public static final String FILTER_KEY = "Filter";
	
	public static final int FLUID_INPUT_SLOT = 0;
	
	public static final int FLUID_OUTPUT_SLOT = 0;
	
	public static final int[] FLUID_INSERT_SLOTS = new int[] { FLUID_INPUT_SLOT };
	
	public static final int[] FLUID_EXTRACT_SLOTS = new int[] { FLUID_OUTPUT_SLOT };
	
	public static final int ITEM_INPUT_SLOT = 0;
	
	public static final int ITEM_BUFFER_SLOT = 1;
	
	public static final int ITEM_OUTPUT_SLOT = 2;
	
	public static final int[] ITEM_INSERT_SLOTS = new int[] { ITEM_INPUT_SLOT };
	
	public static final int[] ITEM_EXTRACT_SLOTS = new int[] { ITEM_BUFFER_SLOT, ITEM_OUTPUT_SLOT };
	
	private FluidVariant filter = FluidVariant.blank();
	
	public TankBlockEntity(Supplier<? extends BlockEntityType<?>> type, BlockPos blockPos, BlockState blockState) {
		super(type, blockPos, blockState);
		
		fluidStorage = new SimpleFluidStorage(1, getFluidStorageSize()).insertPredicate((variant, slot) ->
				slot == FLUID_INPUT_SLOT && (filter.isBlank() || variant.equals(filter))
		).extractPredicate((variant, slot) ->
				slot == FLUID_OUTPUT_SLOT
		).listener(() -> {
			markDirty();
		}).insertSlots(FLUID_INSERT_SLOTS).extractSlots(FLUID_EXTRACT_SLOTS);
		
		itemStorage = new SimpleItemStorage(3).extractPredicate((variant, slot) -> {
			return slot == ITEM_INPUT_SLOT || slot == ITEM_BUFFER_SLOT || slot == ITEM_OUTPUT_SLOT;
		}).insertPredicate((variant, slot) -> {
			if (slot != ITEM_INPUT_SLOT) {
				return false;
			}
			
			return FluidStorage.ITEM.getProvider(variant.getItem()) != null;
		}).listener(() -> {
			markDirty();
		}).insertSlots(ITEM_INSERT_SLOTS).extractSlots(ITEM_EXTRACT_SLOTS);
		
		fluidStorage.getStorage(FLUID_INPUT_SLOT).setCapacity(getFluidStorageSize());
	}
	
	public FluidVariant getFilter() {
		return filter;
	}
	
	public void setFilter(FluidVariant filter) {
		this.filter = filter;
	}
	
	@Override
	public void tick() {
		super.tick();
		
		if (world == null || world.isClient || !shouldRun()) {
			return;
		}
		
		var wildItemStorage = itemStorage.getWildProxy();
		var wildFluidStorage = fluidStorage.getWildProxy();
		
		var itemInputStorage = wildItemStorage.getStorage(ITEM_INPUT_SLOT);
		
		var itemBufferStorage = wildItemStorage.getStorage(ITEM_BUFFER_SLOT);
		
		var itemOutputStorage2 = wildItemStorage.getStorage(ITEM_OUTPUT_SLOT);
		
		var fluidInputStorage = wildFluidStorage.getStorage(FLUID_INPUT_SLOT);
		
		var fluidOutputStorage = wildFluidStorage.getStorage(FLUID_OUTPUT_SLOT);
		
		var unloadFluidStorages = FluidStorage.ITEM.find(itemInputStorage.getStack(), ContainerItemContext.ofSingleSlot(itemInputStorage));
		
		var loadFluidStorages = FluidStorage.ITEM.find(itemOutputStorage2.getStack(), ContainerItemContext.ofSingleSlot(itemOutputStorage2));
		
		try (var transaction = Transaction.openOuter()) {
			StorageUtil.move(unloadFluidStorages, fluidInputStorage, fluidVariant -> !fluidVariant.isBlank() && (filter.isBlank() || fluidVariant.equals(filter)), FluidConstants.BUCKET, transaction);
			StorageUtil.move(fluidOutputStorage, loadFluidStorages, fluidVariant -> !fluidVariant.isBlank(), FluidConstants.BUCKET, transaction);
			
			StorageUtil.move(itemInputStorage, itemBufferStorage, (variant) -> {
				var stored = StorageUtil.findStoredResource(unloadFluidStorages, transaction);
				return stored == null || stored.isBlank();
			}, 1, transaction);
			
			transaction.commit();
		}
	}
	
	@Override
	public void writeNbt(NbtCompound nbt) {
		nbt.put(FILTER_KEY, filter.toNbt());
		
		super.writeNbt(nbt);
	}
	
	@Override
	public void readNbt(@NotNull NbtCompound nbt) {
		filter = FluidVariant.fromNbt(nbt.getCompound(FILTER_KEY));
		
		super.readNbt(nbt);
	}
	
	@Override
	public TankConfig getConfig() {
		return AMConfig.get().blocks.utilities.tanks;
	}
	
	public static class Primitive extends TankBlockEntity {
		public Primitive(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.PRIMITIVE_TANK, blockPos, blockState);
		}
		
		@Override
		public MachineTier getMachineTier() {
			return MachineTier.PRIMITIVE;
		}
	}
	
	public static class Basic extends TankBlockEntity {
		public Basic(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.BASIC_TANK, blockPos, blockState);
		}
		
		@Override
		public MachineTier getMachineTier() {
			return MachineTier.BASIC;
		}
	}
	
	public static class Advanced extends TankBlockEntity {
		public Advanced(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.ADVANCED_TANK, blockPos, blockState);
		}
		
		@Override
		public MachineTier getMachineTier() {
			return MachineTier.ADVANCED;
		}
	}
	
	public static class Elite extends TankBlockEntity {
		public Elite(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.ELITE_TANK, blockPos, blockState);
		}
		
		@Override
		public MachineTier getMachineTier() {
			return MachineTier.ELITE;
		}
	}
	
	public static class Creative extends TankBlockEntity {
		public Creative(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.CREATIVE_TANK, blockPos, blockState);
		}
		
		@Override
		public MachineTier getMachineTier() {
			return MachineTier.CREATIVE;
		}
		
		@Override
		public void tick() {
			super.tick();
			
			if (fluidStorage.getStorage(FLUID_OUTPUT_SLOT).getResource().getFluid() != Fluids.EMPTY) {
				try (var transaction = Transaction.openOuter()) {
					fluidStorage.getStorage(FLUID_OUTPUT_SLOT).insert(fluidStorage.getStorage(FLUID_OUTPUT_SLOT).getResource(), getFluidStorageSize(), transaction);
					
					transaction.commit();
				}
			}
		}
	}
}
