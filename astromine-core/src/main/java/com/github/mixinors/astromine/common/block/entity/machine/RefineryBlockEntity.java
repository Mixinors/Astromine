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

package com.github.mixinors.astromine.common.block.entity.machine;

import com.github.mixinors.astromine.common.block.entity.base.ExtendedBlockEntity;
import com.github.mixinors.astromine.common.provider.config.tiered.FluidStorageMachineConfigProvider;
import com.github.mixinors.astromine.common.config.entry.tiered.FluidStorageMachineConfig;
import com.github.mixinors.astromine.common.transfer.storage.SimpleFluidStorage;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import com.github.mixinors.astromine.common.util.tier.MachineTier;
import com.github.mixinors.astromine.common.config.AMConfig;
import com.github.mixinors.astromine.common.recipe.RefiningRecipe;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.Optional;
import java.util.function.Supplier;

public abstract class RefineryBlockEntity extends ExtendedBlockEntity implements FluidStorageMachineConfigProvider {
	public double progress = 0;
	public int limit = 100;
	public boolean shouldTry = false;
	
	private static final int INPUT_SLOT = 0;
	
	private static final int OUTPUT_SLOT = 1;
	
	private static final int[] INSERT_SLOTS = new int[] { INPUT_SLOT };
	
	private static final int[] EXTRACT_SLOTS = new int[] { OUTPUT_SLOT };

	private Optional<RefiningRecipe> optionalRecipe = Optional.empty();

	public RefineryBlockEntity(Supplier<? extends BlockEntityType<?>> type, BlockPos blockPos, BlockState blockState) {
		super(type, blockPos, blockState);
		
		energyStorage = new SimpleEnergyStorage(getEnergyStorageSize(), Long.MAX_VALUE, Long.MAX_VALUE);
		
		fluidStorage = new SimpleFluidStorage(2, getFluidStorageSize()).extractPredicate((variant, slot) ->
			slot == OUTPUT_SLOT
		).insertPredicate((variant, slot) -> {
			if (slot != INPUT_SLOT) {
				return false;
			}
			
			return RefiningRecipe.allows(world, variant);
		}).listener(() -> {
			shouldTry = true;
			optionalRecipe = Optional.empty();
		}).insertSlots(INSERT_SLOTS).extractSlots(EXTRACT_SLOTS);
	}

	@Override
	public void tick() {
		super.tick();

		if (world == null || world.isClient || !shouldRun())
			return;
		
		if (fluidStorage != null && energyStorage != null) {
			if (optionalRecipe.isEmpty() && shouldTry) {
				optionalRecipe = RefiningRecipe.matching(world, fluidStorage.slice(INPUT_SLOT, OUTPUT_SLOT));
				shouldTry = false;

				if (optionalRecipe.isEmpty()) {
					progress = 0;
					limit = 100;
				}
			}

			if (optionalRecipe.isPresent()) {
				var recipe = optionalRecipe.get();

				limit = recipe.time();
				
				var speed = Math.min(getSpeed(), limit - progress);
				var consumed = (long) (recipe.energyInput() * speed / limit);

				try (var transaction = Transaction.openOuter()) {
					if (energyStorage.extract(consumed, transaction) == consumed) {
						if (progress + speed >= limit) {
							optionalRecipe = Optional.empty();
							
							var inputStorage = fluidStorage.getStorage(INPUT_SLOT);
							
							inputStorage.extract(inputStorage.getResource(), recipe.input().getAmount(), transaction);
							
							var outputStorage = fluidStorage.getStorage(OUTPUT_SLOT);
							
							outputStorage.insert(recipe.output().variant(), recipe.output().amount(), transaction);
							
							transaction.commit();
							
							progress = 0;
						} else {
							progress += speed;
						}
						
						isActive = true;
					} else {
						isActive = false;
					}
				}
			} else {
				isActive = false;
			}
		}
	}
	
	@Override
	public void writeNbt(NbtCompound nbt) {
		nbt.putDouble("Progress", progress);
		nbt.putInt("Limit", limit);
		
		super.writeNbt(nbt);
	}
	
	@Override
	public void readNbt(@NotNull NbtCompound nbt) {
		progress = nbt.getDouble("Progress");
		limit = nbt.getInt("Limit");
		
		super.readNbt(nbt);
	}

	@Override
	public FluidStorageMachineConfig getConfig() {
		return AMConfig.get().machines.refinery;
	}

	public static class Primitive extends RefineryBlockEntity {
		public Primitive(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.PRIMITIVE_REFINERY, blockPos, blockState);
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.PRIMITIVE;
		}
	}

	public static class Basic extends RefineryBlockEntity {
		public Basic(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.BASIC_REFINERY, blockPos, blockState);
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.BASIC;
		}
	}

	public static class Advanced extends RefineryBlockEntity {
		public Advanced(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.ADVANCED_REFINERY, blockPos, blockState);
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.ADVANCED;
		}
	}

	public static class Elite extends RefineryBlockEntity {
		public Elite(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.ELITE_REFINERY, blockPos, blockState);
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.ELITE;
		}
	}
}
