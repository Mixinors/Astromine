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
import com.github.mixinors.astromine.common.recipe.ElectrolyzingRecipe;
import com.github.mixinors.astromine.common.transfer.storage.SimpleFluidStorage;
import com.github.mixinors.astromine.common.util.data.tier.Tier;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.Optional;
import java.util.function.Supplier;

public abstract class ElectrolyzerBlockEntity extends ExtendedBlockEntity implements FluidStorageMachineConfigProvider {
	public static final int INPUT_SLOT = 0;
	
	public static final int OUTPUT_SLOT_1 = 1;
	public static final int OUTPUT_SLOT_2 = 2;
	
	public static final int[] INSERT_SLOTS = new int[] { INPUT_SLOT };
	
	public static final int[] EXTRACT_SLOTS = new int[] { OUTPUT_SLOT_1, OUTPUT_SLOT_2 };
	
	private Optional<ElectrolyzingRecipe> optionalRecipe = Optional.empty();
	
	public ElectrolyzerBlockEntity(Supplier<? extends BlockEntityType<?>> type, BlockPos blockPos, BlockState blockState) {
		super(type, blockPos, blockState);
		
		energyStorage = new SimpleEnergyStorage(getEnergyStorageSize(), getMaxTransferRate(), 0L);
		
		fluidStorage = new SimpleFluidStorage(3, getFluidStorageSize()).insertPredicate((variant, slot) -> {
			if (slot != INPUT_SLOT) {
				return false;
			}
			
			return ElectrolyzingRecipe.allows(world, variant);
		}).extractPredicate((variant, slot) ->
				slot == OUTPUT_SLOT_1 || slot == OUTPUT_SLOT_2
		).listener(() -> {
			if (optionalRecipe.isPresent() && !optionalRecipe.get().matches(fluidStorage.slice(INPUT_SLOT, OUTPUT_SLOT_1, OUTPUT_SLOT_2))) {
				optionalRecipe = Optional.empty();
			}
			
			markDirty();
		}).insertSlots(INSERT_SLOTS).extractSlots(EXTRACT_SLOTS);
	}
	
	@Override
	public void tick() {
		super.tick();
		
		if (world == null || world.isClient || !shouldRun()) {
			return;
		}
		
		if (fluidStorage != null && energyStorage != null) {
			if (optionalRecipe.isEmpty()) {
				optionalRecipe = ElectrolyzingRecipe.matching(world, fluidStorage.slice(INPUT_SLOT, OUTPUT_SLOT_1, OUTPUT_SLOT_2));
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
							
							var inputStorage = fluidStorage.getStorage(INPUT_SLOT);
							
							inputStorage.extract(inputStorage.getResource(), recipe.input().getAmount(), transaction, true);
							
							var firstOutputStorage = fluidStorage.getStorage(OUTPUT_SLOT_1);
							var secondOutputStorage = fluidStorage.getStorage(OUTPUT_SLOT_2);
							
							if (recipe.firstOutput().equalsAndFitsIn(firstOutputStorage) &&
									recipe.secondOutput().equalsAndFitsIn(secondOutputStorage)) {
								
								firstOutputStorage.insert(recipe.firstOutput().variant(), recipe.firstOutput().amount(), transaction, true);
								secondOutputStorage.insert(recipe.secondOutput().variant(), recipe.secondOutput().amount(), transaction, true);
							} else if (recipe.firstOutput().equalsAndFitsIn(secondOutputStorage) &&
									recipe.secondOutput().equalsAndFitsIn(firstOutputStorage)) {
								
								firstOutputStorage.insert(recipe.secondOutput().variant(), recipe.secondOutput().amount(), transaction, true);
								secondOutputStorage.insert(recipe.firstOutput().variant(), recipe.firstOutput().amount(), transaction, true);
							}
							
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
		return AMConfig.get().blocks.machines.electrolyzer;
	}
	
	public static class Primitive extends ElectrolyzerBlockEntity {
		public Primitive(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.PRIMITIVE_ELECTROLYZER, blockPos, blockState);
		}
		
		@Override
		public Tier getMachineTier() {
			return Tier.PRIMITIVE;
		}
	}
	
	public static class Basic extends ElectrolyzerBlockEntity {
		public Basic(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.BASIC_ELECTROLYZER, blockPos, blockState);
		}
		
		@Override
		public Tier getMachineTier() {
			return Tier.BASIC;
		}
	}
	
	public static class Advanced extends ElectrolyzerBlockEntity {
		public Advanced(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.ADVANCED_ELECTROLYZER, blockPos, blockState);
		}
		
		@Override
		public Tier getMachineTier() {
			return Tier.ADVANCED;
		}
	}
	
	public static class Elite extends ElectrolyzerBlockEntity {
		public Elite(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.ELITE_ELECTROLYZER, blockPos, blockState);
		}
		
		@Override
		public Tier getMachineTier() {
			return Tier.ELITE;
		}
	}
}
