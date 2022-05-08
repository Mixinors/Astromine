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
import com.github.mixinors.astromine.common.recipe.FluidMixingRecipe;
import com.github.mixinors.astromine.common.transfer.storage.SimpleFluidStorage;
import com.github.mixinors.astromine.common.util.data.tier.MachineTier;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.Optional;
import java.util.function.Supplier;

public abstract class FluidMixerBlockEntity extends ExtendedBlockEntity implements FluidStorageMachineConfigProvider {
	public static final int INPUT_SLOT_1 = 0;
	public static final int INPUT_SLOT_2 = 1;
	
	public static final int OUTPUT_SLOT = 2;
	
	public static final int[] INSERT_SLOTS = new int[] { INPUT_SLOT_1, INPUT_SLOT_2 };
	
	public static final int[] EXTRACT_SLOTS = new int[] { OUTPUT_SLOT };
	
	private Optional<FluidMixingRecipe> optionalRecipe = Optional.empty();
	
	public FluidMixerBlockEntity(Supplier<? extends BlockEntityType<?>> type, BlockPos blockPos, BlockState blockState) {
		super(type, blockPos, blockState);
		
		energyStorage = new SimpleEnergyStorage(getEnergyStorageSize(), getMaxTransferRate(), 0L);
		
		fluidStorage = new SimpleFluidStorage(3, getFluidStorageSize()).extractPredicate((variant, slot) ->
				slot == OUTPUT_SLOT
		).insertPredicate((variant, slot) -> {
			if (slot != INPUT_SLOT_1 && slot != INPUT_SLOT_2) {
				return false;
			}
			
			return FluidMixingRecipe.allows(world, variant, fluidStorage.getVariant(1)) ||
					FluidMixingRecipe.allows(world, fluidStorage.getVariant(0), variant);
		}).listener(() -> {
			if (optionalRecipe.isPresent() && !optionalRecipe.get().matches(fluidStorage.slice(INPUT_SLOT_1, INPUT_SLOT_2, OUTPUT_SLOT))) {
				optionalRecipe = Optional.empty();
			}
			
			markDirty();
		}).insertSlots(INSERT_SLOTS).extractSlots(EXTRACT_SLOTS);
		
		fluidStorage.getStorage(INPUT_SLOT_1).setCapacity(getFluidStorageSize());
		fluidStorage.getStorage(INPUT_SLOT_2).setCapacity(getFluidStorageSize());
		fluidStorage.getStorage(OUTPUT_SLOT).setCapacity(getFluidStorageSize());
	}
	
	@Override
	public void tick() {
		super.tick();
		
		if (world == null || world.isClient || !shouldRun()) {
			return;
		}
		
		if (fluidStorage != null && energyStorage != null) {
			if (optionalRecipe.isEmpty()) {
				optionalRecipe = FluidMixingRecipe.matching(world, fluidStorage.slice(INPUT_SLOT_1, INPUT_SLOT_2));
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
							
							var firstInputStorage = fluidStorage.getStorage(INPUT_SLOT_1);
							var secondInputStorage = fluidStorage.getStorage(INPUT_SLOT_2);
							
							if (recipe.firstInput().test(firstInputStorage) && recipe.secondInput().test(secondInputStorage)) {
								firstInputStorage.extract(firstInputStorage.getResource(), recipe.firstInput().getAmount(), transaction, true);
								secondInputStorage.extract(secondInputStorage.getResource(), recipe.secondInput().getAmount(), transaction, true);
							} else if (recipe.firstInput().test(secondInputStorage) && recipe.secondInput().test(firstInputStorage)) {
								firstInputStorage.extract(firstInputStorage.getResource(), recipe.secondInput().getAmount(), transaction, true);
								secondInputStorage.extract(secondInputStorage.getResource(), recipe.firstInput().getAmount(), transaction, true);
							}
							
							var outputStorage = fluidStorage.getStorage(OUTPUT_SLOT);
							
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
		return AMConfig.get().blocks.machines.fluidMixer;
	}
	
	public static class Primitive extends FluidMixerBlockEntity {
		public Primitive(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.PRIMITIVE_FLUID_MIXER, blockPos, blockState);
		}
		
		@Override
		public MachineTier getMachineTier() {
			return MachineTier.PRIMITIVE;
		}
	}
	
	public static class Basic extends FluidMixerBlockEntity {
		public Basic(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.BASIC_FLUID_MIXER, blockPos, blockState);
		}
		
		@Override
		public MachineTier getMachineTier() {
			return MachineTier.BASIC;
		}
	}
	
	public static class Advanced extends FluidMixerBlockEntity {
		public Advanced(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.ADVANCED_FLUID_MIXER, blockPos, blockState);
		}
		
		@Override
		public MachineTier getMachineTier() {
			return MachineTier.ADVANCED;
		}
	}
	
	public static class Elite extends FluidMixerBlockEntity {
		public Elite(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.ELITE_FLUID_MIXER, blockPos, blockState);
		}
		
		@Override
		public MachineTier getMachineTier() {
			return MachineTier.ELITE;
		}
	}
}
