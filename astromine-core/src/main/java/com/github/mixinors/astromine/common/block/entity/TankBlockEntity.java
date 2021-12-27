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
import com.github.mixinors.astromine.common.transfer.storage.SimpleFluidStorage;
import com.github.mixinors.astromine.common.transfer.storage.SimpleItemStorage;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import com.google.common.base.Predicates;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

import com.github.mixinors.astromine.common.util.tier.MachineTier;
import com.github.mixinors.astromine.registry.common.AMConfig;
import com.github.mixinors.astromine.common.block.entity.machine.FluidSizeProvider;
import com.github.mixinors.astromine.common.block.entity.machine.SpeedProvider;
import com.github.mixinors.astromine.common.block.entity.machine.TierProvider;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public abstract class TankBlockEntity extends ExtendedBlockEntity implements TierProvider, FluidSizeProvider, SpeedProvider {
	private static final int FLUID_INPUT_SLOT = 0;
	
	private static final int FLUID_OUTPUT_SLOT = 0;
	
	private static final int[] FLUID_INSERT_SLOTS = new int[] { FLUID_INPUT_SLOT };
	
	private static final int[] FLUID_EXTRACT_SLOTS = new int[] { FLUID_OUTPUT_SLOT };
	
	private static final int ITEM_INPUT_SLOT = 0;
	
	private static final int ITEM_OUTPUT_SLOT = 1;
	
	private static final int[] ITEM_INSERT_SLOTS = new int[] { ITEM_INPUT_SLOT };
	
	private static final int[] ITEM_EXTRACT_SLOTS = new int[] { ITEM_OUTPUT_SLOT };
	
	public TankBlockEntity(Supplier<? extends BlockEntityType<?>> type, BlockPos blockPos, BlockState blockState) {
		super(type, blockPos, blockState);
		
		fluidStorage = new SimpleFluidStorage(1).extractPredicate((variant, slot) -> {
			return slot == FLUID_INPUT_SLOT;
		}).extractPredicate((variant, slot) -> {
			return slot == FLUID_OUTPUT_SLOT;
		}).insertSlots(FLUID_INSERT_SLOTS).extractSlots(FLUID_EXTRACT_SLOTS);
		
		itemStorage = new SimpleItemStorage(2).extractPredicate((variant, slot) -> {
			return slot == ITEM_INPUT_SLOT;
		}).insertPredicate((variant, slot) -> {
			if (slot != ITEM_OUTPUT_SLOT) {
				return false;
			}
			
			return FluidStorage.ITEM.getProvider(variant.getItem()) != null;
		}).insertSlots(ITEM_INSERT_SLOTS).extractSlots(ITEM_EXTRACT_SLOTS);
	}

	private Fluid filter = Fluids.EMPTY;

	public Fluid getFilter() {
		return filter;
	}

	public void setFilter(Fluid filter) {
		this.filter = filter;
	}

	@Override
	public void tick() {
		super.tick();

		if (world == null || world.isClient || !shouldRun())
			return;

		var inputStack = itemStorage.getStack(ITEM_INPUT_SLOT);
		var inputFluidStorage = FluidStorage.ITEM.find(inputStack, ContainerItemContext.ofSingleSlot(itemStorage.getStorage(ITEM_INPUT_SLOT)));

		var outputStack = itemStorage.getStack(ITEM_OUTPUT_SLOT);
		var outputFluidStorage = FluidStorage.ITEM.find(outputStack, ContainerItemContext.ofSingleSlot(itemStorage.getStorage(ITEM_OUTPUT_SLOT)));
		
		try (Transaction transaction = Transaction.openOuter()) {
			StorageUtil.move(inputFluidStorage, fluidStorage.getStorage(FLUID_INPUT_SLOT), Predicates.alwaysTrue(), FluidConstants.BUCKET, transaction);
			StorageUtil.move(fluidStorage.getStorage(FLUID_OUTPUT_SLOT), outputFluidStorage, Predicates.alwaysTrue(), FluidConstants.BUCKET, transaction);
			
			transaction.commit();
		}
	}

	@Override
	public void writeNbt(NbtCompound nbt) {
		nbt.putString("Fluid", Registry.FLUID.getId(filter).toString());
		
		super.writeNbt(nbt);
	}

	@Override
	public void readNbt(@NotNull NbtCompound nbt) {
		Registry.FLUID.getOrEmpty(new Identifier(nbt.getString("Fluid"))).ifPresent(filter -> this.filter = filter);

		super.readNbt(nbt);
	}

	public static class Primitive extends TankBlockEntity {
		public Primitive(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.PRIMITIVE_TANK, blockPos, blockState);
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().primitiveTankSpeed;
		}

		@Override
		public long getFluidSize() {
			return AMConfig.get().primitiveTankFluid;
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
		public double getMachineSpeed() {
			return AMConfig.get().basicTankSpeed;
		}

		@Override
		public long getFluidSize() {
			return AMConfig.get().basicTankFluid;
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
		public double getMachineSpeed() {
			return AMConfig.get().advancedTankSpeed;
		}

		@Override
		public long getFluidSize() {
			return AMConfig.get().advancedTankFluid;
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
		public double getMachineSpeed() {
			return AMConfig.get().eliteTankSpeed;
		}

		@Override
		public long getFluidSize() {
			return AMConfig.get().eliteTankFluid;
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
		public double getMachineSpeed() {
			return Double.MAX_VALUE;
		}

		@Override
		public long getFluidSize() {
			return Long.MAX_VALUE;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.CREATIVE;
		}

		@Override
		public void tick() {
			super.tick();
			
			try (Transaction transaction = Transaction.openOuter()) {
				fluidStorage.getStorage(FLUID_OUTPUT_SLOT).insert(fluidStorage.getStorage(FLUID_OUTPUT_SLOT).getResource(), Long.MAX_VALUE, transaction);
				
				transaction.commit();
			}
		}
	}
}
