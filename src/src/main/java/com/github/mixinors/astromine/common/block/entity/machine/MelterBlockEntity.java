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

package com.github.mixinors.astromine.common.block.entity.machine;

import com.github.mixinors.astromine.common.block.entity.base.ExtendedBlockEntity;
import com.github.mixinors.astromine.common.config.AMConfig;
import com.github.mixinors.astromine.common.config.entry.tiered.FluidStorageMachineConfig;
import com.github.mixinors.astromine.common.provider.config.tiered.FluidStorageMachineConfigProvider;
import com.github.mixinors.astromine.common.recipe.MeltingRecipe;
import com.github.mixinors.astromine.common.transfer.storage.SimpleFluidStorage;
import com.github.mixinors.astromine.common.transfer.storage.SimpleItemStorage;
import com.github.mixinors.astromine.common.util.data.tier.MachineTier;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.Optional;
import java.util.function.Supplier;

public abstract class MelterBlockEntity extends ExtendedBlockEntity implements FluidStorageMachineConfigProvider {
	public static final int FLUID_OUTPUT_SLOT = 0;
	
	public static final int[] FLUID_INSERT_SLOTS = new int[] { };
	
	public static final int[] FLUID_EXTRACT_SLOTS = new int[] { FLUID_OUTPUT_SLOT };
	
	public static final int ITEM_INPUT_SLOT = 0;
	
	public static final int[] ITEM_INSERT_SLOTS = new int[] { ITEM_INPUT_SLOT };
	
	public static final int[] ITEM_EXTRACT_SLOTS = new int[] { };
	
	private Optional<MeltingRecipe> optionalRecipe = Optional.empty();
	
	public MelterBlockEntity(Supplier<? extends BlockEntityType<?>> type, BlockPos blockPos, BlockState blockState) {
		super(type, blockPos, blockState);
		
		energyStorage = new SimpleEnergyStorage(getEnergyStorageSize(), getMaxTransferRate(), 0L);
		
		fluidStorage = new SimpleFluidStorage(1, getFluidStorageSize()).extractPredicate((variant, slot) ->
				slot == FLUID_OUTPUT_SLOT
		).insertPredicate((variant, slot) ->
				false
		).listener(() -> {
			if (optionalRecipe.isPresent() && !optionalRecipe.get().matches(itemStorage.slice(ITEM_INPUT_SLOT), fluidStorage.slice(FLUID_OUTPUT_SLOT))) {
				optionalRecipe = Optional.empty();
			}
			
			markDirty();
		}).insertSlots(FLUID_INSERT_SLOTS).extractSlots(FLUID_EXTRACT_SLOTS);
		
		fluidStorage.getStorage(FLUID_OUTPUT_SLOT).setCapacity(getFluidStorageSize());
		
		itemStorage = new SimpleItemStorage(1).extractPredicate((variant, slot) ->
				false
		).insertPredicate((variant, slot) -> {
			if (slot != ITEM_INPUT_SLOT) {
				return false;
			}
			
			return MeltingRecipe.allows(world, variant);
		}).listener(() -> {
			if (optionalRecipe.isPresent() && !optionalRecipe.get().matches(itemStorage.slice(ITEM_INPUT_SLOT), fluidStorage.slice(FLUID_OUTPUT_SLOT))) {
				optionalRecipe = Optional.empty();
			}
			
			markDirty();
		}).insertSlots(ITEM_INSERT_SLOTS).extractSlots(ITEM_EXTRACT_SLOTS);
	}
	
	@Override
	public void tick() {
		super.tick();
		
		if (world == null || world.isClient || !shouldRun()) {
			return;
		}
		
		if (itemStorage != null && fluidStorage != null && energyStorage != null) {
			if (optionalRecipe.isEmpty()) {
				optionalRecipe = MeltingRecipe.matching(world, itemStorage.slice(ITEM_INPUT_SLOT), fluidStorage.slice(FLUID_OUTPUT_SLOT));
			}
			
			if (optionalRecipe.isPresent()) {
				var recipe = optionalRecipe.get();
				
				limit = recipe.time();
				
				var speed = Math.min(getSpeed(), limit - progress);
				var consumed = (long) (recipe.energyInput() * speed / limit);
				
				try (var transaction = Transaction.openOuter()) {
					if (energyStorage.amount >= consumed) {
						energyStorage.amount -= consumed;
						
						if (progress + speed >= limit) {
							optionalRecipe = Optional.empty();
							
							var inputStorage = itemStorage.getStorage(ITEM_INPUT_SLOT);
							
							inputStorage.extract(inputStorage.getResource(), recipe.input().getAmount(), transaction, true);
							
							var outputStorage = fluidStorage.getStorage(FLUID_OUTPUT_SLOT);
							
							outputStorage.insert(recipe.output().variant(), recipe.output().amount(), transaction, true);
							
							transaction.commit();
							
							progress = 0.0D;
						} else {
							progress += speed;
						}
						
						active = true;
					} else {
						active = false;
					}
				}
			} else {
				progress = 0.0D;
				limit = 100.0D;
				
				active = false;
			}
		}
	}
	
	@Override
	public FluidStorageMachineConfig getConfig() {
		return AMConfig.get().blocks.machines.melter;
	}
	
	public static class Primitive extends MelterBlockEntity {
		public Primitive(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.PRIMITIVE_MELTER, blockPos, blockState);
		}
		
		@Override
		public MachineTier getMachineTier() {
			return MachineTier.PRIMITIVE;
		}
	}
	
	public static class Basic extends MelterBlockEntity {
		public Basic(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.BASIC_MELTER, blockPos, blockState);
		}
		
		@Override
		public MachineTier getMachineTier() {
			return MachineTier.BASIC;
		}
	}
	
	public static class Advanced extends MelterBlockEntity {
		public Advanced(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.ADVANCED_MELTER, blockPos, blockState);
		}
		
		@Override
		public MachineTier getMachineTier() {
			return MachineTier.ADVANCED;
		}
	}
	
	public static class Elite extends MelterBlockEntity {
		public Elite(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.ELITE_MELTER, blockPos, blockState);
		}
		
		@Override
		public MachineTier getMachineTier() {
			return MachineTier.ELITE;
		}
	}
}
