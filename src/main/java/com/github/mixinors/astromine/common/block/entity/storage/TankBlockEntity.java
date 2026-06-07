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
import com.github.mixinors.astromine.common.util.data.tier.Tier;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
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
	
	private FluidStack filter = FluidStack.EMPTY;
	
	public TankBlockEntity(Supplier<? extends BlockEntityType<?>> type, BlockPos blockPos, BlockState blockState) {
		super(type, blockPos, blockState);
		
		fluidStorage = new SimpleFluidStorage(1, getFluidStorageSize()).insertPredicate((variant, slot) ->
				slot == FLUID_INPUT_SLOT && (filter.isEmpty() || FluidStack.isSameFluidSameComponents(variant, filter))
		).extractPredicate((variant, slot) ->
				slot == FLUID_OUTPUT_SLOT
		).listener(() -> {
			setChanged();
		}).insertSlots(FLUID_INSERT_SLOTS).extractSlots(FLUID_EXTRACT_SLOTS);
		
		itemStorage = new SimpleItemStorage(3).extractPredicate((variant, slot) -> {
			return slot == ITEM_INPUT_SLOT || slot == ITEM_BUFFER_SLOT || slot == ITEM_OUTPUT_SLOT;
		}).insertPredicate((variant, slot) -> {
			if (slot != ITEM_INPUT_SLOT) {
				return false;
			}
			
			return FluidUtil.getFluidHandler(variant).isPresent();
		}).listener(() -> {
			setChanged();
		}).insertSlots(ITEM_INSERT_SLOTS).extractSlots(ITEM_EXTRACT_SLOTS);
	}
	
	public FluidStack getFilter() {
		return filter;
	}
	
	public void setFilter(FluidStack filter) {
		this.filter = filter;
	}
	
	@Override
	public void tick() {
		super.tick();
		
		if (level == null || level.isClientSide || !shouldRun()) {
			return;
		}
		
		unloadInputFluidItem();
		loadOutputFluidItem();
		moveEmptyInputItemToBuffer();
	}
	
	@Override
	public void saveAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
		if (!filter.isEmpty()) {
			nbt.put(FILTER_KEY, filter.save(registries));
		}
		
		super.saveAdditional(nbt, registries);
	}
	
	@Override
	protected void loadAdditional(@NotNull CompoundTag nbt, HolderLookup.Provider registries) {
		filter = nbt.contains(FILTER_KEY) ? FluidStack.parseOptional(registries, nbt.getCompound(FILTER_KEY)) : FluidStack.EMPTY;
		
		super.loadAdditional(nbt, registries);
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
		public Tier getMachineTier() {
			return Tier.PRIMITIVE;
		}
	}
	
	public static class Basic extends TankBlockEntity {
		public Basic(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.BASIC_TANK, blockPos, blockState);
		}
		
		@Override
		public Tier getMachineTier() {
			return Tier.BASIC;
		}
	}
	
	public static class Advanced extends TankBlockEntity {
		public Advanced(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.ADVANCED_TANK, blockPos, blockState);
		}
		
		@Override
		public Tier getMachineTier() {
			return Tier.ADVANCED;
		}
	}
	
	public static class Elite extends TankBlockEntity {
		public Elite(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.ELITE_TANK, blockPos, blockState);
		}
		
		@Override
		public Tier getMachineTier() {
			return Tier.ELITE;
		}
	}
	
	public static class Creative extends TankBlockEntity {
		public Creative(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.CREATIVE_TANK, blockPos, blockState);
		}
		
		@Override
		public Tier getMachineTier() {
			return Tier.CREATIVE;
		}
		
		@Override
		public void tick() {
			super.tick();
			
			if (fluidStorage.getStorage(FLUID_OUTPUT_SLOT).getResource().getFluid() != Fluids.EMPTY) {
				fluidStorage.getStorage(FLUID_OUTPUT_SLOT).insert(fluidStorage.getStorage(FLUID_OUTPUT_SLOT).getResource(), getFluidStorageSize(), true, false);
			}
		}
	}
	
	private void unloadInputFluidItem() {
		var inputStack = itemStorage.getItem(ITEM_INPUT_SLOT);
		
		if (inputStack.isEmpty()) {
			return;
		}
		
		var handler = FluidUtil.getFluidHandler(inputStack).orElse(null);
		
		if (handler == null) {
			return;
		}
		
		var drained = handler.drain(FluidType.BUCKET_VOLUME, IFluidHandler.FluidAction.SIMULATE);
		
		if (drained.isEmpty() || (!filter.isEmpty() && !FluidStack.isSameFluidSameComponents(drained, filter))) {
			return;
		}
		
		var accepted = fluidStorage.fill(drained, IFluidHandler.FluidAction.SIMULATE);
		
		if (accepted <= 0) {
			return;
		}
		
		var moved = handler.drain(drained.copyWithAmount(accepted), IFluidHandler.FluidAction.EXECUTE);
		
		fluidStorage.fill(moved, IFluidHandler.FluidAction.EXECUTE);
		itemStorage.setItem(ITEM_INPUT_SLOT, getContainer(handler));
	}
	
	private void loadOutputFluidItem() {
		var outputStack = itemStorage.getItem(ITEM_OUTPUT_SLOT);
		
		if (outputStack.isEmpty()) {
			return;
		}
		
		var handler = FluidUtil.getFluidHandler(outputStack).orElse(null);
		
		if (handler == null) {
			return;
		}
		
		var stored = fluidStorage.getStorage(FLUID_OUTPUT_SLOT).getResource();
		
		if (stored.isEmpty()) {
			return;
		}
		
		var accepted = handler.fill(stored.copyWithAmount(Math.min(stored.getAmount(), FluidType.BUCKET_VOLUME)), IFluidHandler.FluidAction.SIMULATE);
		
		if (accepted <= 0) {
			return;
		}
		
		var drained = fluidStorage.drain(stored.copyWithAmount(accepted), IFluidHandler.FluidAction.EXECUTE);
		
		handler.fill(drained, IFluidHandler.FluidAction.EXECUTE);
		itemStorage.setItem(ITEM_OUTPUT_SLOT, getContainer(handler));
	}
	
	private void moveEmptyInputItemToBuffer() {
		var inputStack = itemStorage.getItem(ITEM_INPUT_SLOT);
		
		if (inputStack.isEmpty() || !isEmptyFluidContainer(inputStack)) {
			return;
		}
		
		var bufferStack = itemStorage.getItem(ITEM_BUFFER_SLOT);
		
		if (!bufferStack.isEmpty() && !ItemStack.isSameItemSameComponents(inputStack, bufferStack)) {
			return;
		}
		
		if (bufferStack.getCount() >= bufferStack.getMaxStackSize()) {
			return;
		}
		
		var moved = inputStack.copyWithCount(1);
		
		inputStack.shrink(1);
		
		if (bufferStack.isEmpty()) {
			itemStorage.setItem(ITEM_BUFFER_SLOT, moved);
		} else {
			bufferStack.grow(1);
			itemStorage.setItem(ITEM_BUFFER_SLOT, bufferStack);
		}
		
		itemStorage.setItem(ITEM_INPUT_SLOT, inputStack);
	}
	
	private static boolean isEmptyFluidContainer(ItemStack stack) {
		var handler = FluidUtil.getFluidHandler(stack).orElse(null);
		
		if (handler == null) {
			return false;
		}
		
		for (var tank = 0; tank < handler.getTanks(); ++tank) {
			if (!handler.getFluidInTank(tank).isEmpty()) {
				return false;
			}
		}
		
		return true;
	}
	
	private static ItemStack getContainer(IFluidHandler handler) {
		return handler instanceof IFluidHandlerItem itemHandler ? itemHandler.getContainer() : ItemStack.EMPTY;
	}
}
