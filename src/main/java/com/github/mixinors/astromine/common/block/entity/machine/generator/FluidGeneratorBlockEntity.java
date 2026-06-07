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
import com.github.mixinors.astromine.common.config.entry.tiered.FluidStorageMachineConfig;
import com.github.mixinors.astromine.common.provider.config.tiered.FluidStorageMachineConfigProvider;
import com.github.mixinors.astromine.common.recipe.FluidGeneratingRecipe;
import com.github.mixinors.astromine.common.transfer.storage.LongEnergyStorage;
import com.github.mixinors.astromine.common.transfer.storage.SimpleFluidStorage;
import com.github.mixinors.astromine.common.util.data.tier.Tier;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;
import java.util.function.Supplier;

public abstract class FluidGeneratorBlockEntity extends ExtendedBlockEntity implements FluidStorageMachineConfigProvider {
	public static final int INPUT_SLOT = 0;
	
	public static final int[] INSERT_SLOTS = new int[] { INPUT_SLOT };
	
	public static final int[] EXTRACT_SLOTS = new int[] { };
	
	private Optional<FluidGeneratingRecipe> optionalRecipe = Optional.empty();
	
	public FluidGeneratorBlockEntity(Supplier<? extends BlockEntityType<?>> type, BlockPos blockPos, BlockState blockState) {
		super(type, blockPos, blockState);
		
		energyStorage = new LongEnergyStorage(getEnergyStorageSize(), 0L, getMaxTransferRate());
		
		fluidStorage = new SimpleFluidStorage(1, getFluidStorageSize()).extractPredicate((variant, slot) ->
				false
		).insertPredicate((variant, slot) -> {
			if (slot != INPUT_SLOT) {
				return false;
			}
			
			return FluidGeneratingRecipe.allows(level, variant);
		}).listener(() -> {
			if (optionalRecipe.isPresent() && !optionalRecipe.get().matches(fluidStorage.slice(INPUT_SLOT))) {
				optionalRecipe = Optional.empty();
			}
			
			setChanged();
		}).insertSlots(INSERT_SLOTS).extractSlots(EXTRACT_SLOTS);
	}
	
	@Override
	public void tick() {
		super.tick();
		
		if (level == null || level.isClientSide || !shouldRun()) {
			return;
		}
		
		if (fluidStorage != null && energyStorage != null) {
			if (optionalRecipe.isEmpty()) {
				optionalRecipe = FluidGeneratingRecipe.matching(level, fluidStorage.slice(INPUT_SLOT));
			}
			
			if (optionalRecipe.isPresent()) {
				var recipe = optionalRecipe.get();
				
				limit = recipe.time();
				
				var speed = Math.min(getSpeed(), limit - progress);
				var generated = (long) (recipe.energyOutput() * speed / limit);
				
				if (energyStorage.amount + generated <= energyStorage.capacity) {
					energyStorage.amount += generated;
					
					if (progress + speed >= limit) {
						optionalRecipe = Optional.empty();
						
						var inputStorage = fluidStorage.getStorage(INPUT_SLOT);
						
						inputStorage.extract(inputStorage.getResource(), recipe.input().getAmount(), true, false);
						
						progress = 0.0D;
					} else {
						progress += speed;
					}
					
					active = true;
				} else {
					active = false;
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
		return AMConfig.get().blocks.machines.fluidGenerator;
	}
	
	public static class Primitive extends FluidGeneratorBlockEntity {
		public Primitive(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.PRIMITIVE_LIQUID_GENERATOR, blockPos, blockState);
		}
		
		@Override
		public Tier getMachineTier() {
			return Tier.PRIMITIVE;
		}
	}
	
	public static class Basic extends FluidGeneratorBlockEntity {
		public Basic(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.BASIC_LIQUID_GENERATOR, blockPos, blockState);
		}
		
		@Override
		public Tier getMachineTier() {
			return Tier.BASIC;
		}
	}
	
	public static class Advanced extends FluidGeneratorBlockEntity {
		public Advanced(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.ADVANCED_LIQUID_GENERATOR, blockPos, blockState);
		}
		
		@Override
		public Tier getMachineTier() {
			return Tier.ADVANCED;
		}
	}
	
	public static class Elite extends FluidGeneratorBlockEntity {
		public Elite(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.ELITE_LIQUID_GENERATOR, blockPos, blockState);
		}
		
		@Override
		public Tier getMachineTier() {
			return Tier.ELITE;
		}
	}
}
