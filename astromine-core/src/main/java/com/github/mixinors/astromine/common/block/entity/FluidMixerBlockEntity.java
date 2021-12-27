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
import com.github.mixinors.astromine.common.transfer.storage.SimpleFluidVariantStorage;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import com.github.mixinors.astromine.common.util.tier.MachineTier;
import com.github.mixinors.astromine.registry.common.AMConfig;
import com.github.mixinors.astromine.common.block.entity.machine.EnergySizeProvider;
import com.github.mixinors.astromine.common.block.entity.machine.FluidSizeProvider;
import com.github.mixinors.astromine.common.block.entity.machine.SpeedProvider;
import com.github.mixinors.astromine.common.block.entity.machine.TierProvider;
import com.github.mixinors.astromine.common.recipe.FluidMixingRecipe;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.Optional;
import java.util.function.Supplier;

public abstract class FluidMixerBlockEntity extends ExtendedBlockEntity implements EnergySizeProvider, TierProvider, SpeedProvider, FluidSizeProvider {
	public double progress = 0;
	public int limit = 100;
	public boolean shouldTry = false;
	
	private static final int INPUT_SLOT_1 = 0;
	private static final int INPUT_SLOT_2 = 1;
	
	private static final int OUTPUT_SLOT = 2;
	
	private static final int[] INSERT_SLOTS = new int[] { INPUT_SLOT_1, INPUT_SLOT_2 };
	
	private static final int[] EXTRACT_SLOTS = new int[] { OUTPUT_SLOT };

	private Optional<FluidMixingRecipe> optionalRecipe = Optional.empty();

	public FluidMixerBlockEntity(Supplier<? extends BlockEntityType<?>> type, BlockPos blockPos, BlockState blockState) {
		super(type, blockPos, blockState);
		
		energyStorage = new SimpleEnergyStorage(getEnergySize(), Long.MAX_VALUE, Long.MAX_VALUE);
		
		fluidStorage = new SimpleFluidStorage(3).extractPredicate((variant, slot) -> {
			return slot == OUTPUT_SLOT;
		}).insertPredicate((variant, slot) -> {
			if (slot != INPUT_SLOT_1 && slot != INPUT_SLOT_2) {
				return false;
			}
			
			return FluidMixingRecipe.allows(world, variant, fluidStorage.getVariant(1)) ||
				   FluidMixingRecipe.allows(world, fluidStorage.getVariant(0), variant);
		}).listener(() -> {
			shouldTry = true;
			optionalRecipe = Optional.empty();
		}).insertSlots(INSERT_SLOTS).extractSlots(EXTRACT_SLOTS);
		
		fluidStorage.getStorage(INPUT_SLOT_1).setCapacity(getFluidSize());
		fluidStorage.getStorage(INPUT_SLOT_2).setCapacity(getFluidSize());
		fluidStorage.getStorage(OUTPUT_SLOT).setCapacity(getFluidSize());
	}

	@Override
	public void tick() {
		super.tick();

		if (world == null || world.isClient || !shouldRun())
			return;
		
		if (fluidStorage != null && energyStorage != null) {
			if (optionalRecipe.isEmpty() && shouldTry) {
				optionalRecipe = FluidMixingRecipe.matching(world, fluidStorage.slice(INPUT_SLOT_1, INPUT_SLOT_2));
				shouldTry = false;

				if (optionalRecipe.isEmpty()) {
					progress = 0;
					limit = 100;
				}
			}

			if (optionalRecipe.isPresent()) {
				var recipe = optionalRecipe.get();

				limit = recipe.time();

				var speed = Math.min(getMachineSpeed(), limit - progress);
				var consumed = (long) (recipe.energyInput() * speed / limit);
				
				try (var transaction = Transaction.openOuter()) {
					if (energyStorage.extract(consumed, transaction) == consumed) {
						if (progress + speed >= limit) {
							optionalRecipe = Optional.empty();

							var firstInputStorage = fluidStorage.getStorage(INPUT_SLOT_1);
							var secondInputStorage = fluidStorage.getStorage(INPUT_SLOT_2);
							
							if (recipe.firstInput().test(firstInputStorage) && recipe.secondInput().test(secondInputStorage)) {
								firstInputStorage.extract(firstInputStorage.getResource(), recipe.firstInput().getAmount(), transaction);
								secondInputStorage.extract(secondInputStorage.getResource(), recipe.secondInput().getAmount(), transaction);
							} else if (recipe.firstInput().test(secondInputStorage) && recipe.secondInput().test(firstInputStorage)) {
								firstInputStorage.extract(firstInputStorage.getResource(), recipe.secondInput().getAmount(), transaction);
								secondInputStorage.extract(secondInputStorage.getResource(), recipe.firstInput().getAmount(), transaction);
							}

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

	public static class Primitive extends FluidMixerBlockEntity {
		public Primitive(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.PRIMITIVE_FLUID_MIXER, blockPos, blockState);
		}

		@Override
		public long getFluidSize() {
			return AMConfig.get().primitiveFluidMixerFluid;
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().primitiveFluidMixerSpeed;
		}

		@Override
		public long getEnergySize() {
			return AMConfig.get().primitiveFluidMixerEnergy;
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
		public long getFluidSize() {
			return AMConfig.get().basicFluidMixerFluid;
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().basicFluidMixerSpeed;
		}

		@Override
		public long getEnergySize() {
			return AMConfig.get().basicFluidMixerEnergy;
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
		public long getFluidSize() {
			return AMConfig.get().advancedFluidMixerFluid;
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().advancedFluidMixerSpeed;
		}

		@Override
		public long getEnergySize() {
			return AMConfig.get().advancedFluidMixerEnergy;
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
		public long getFluidSize() {
			return AMConfig.get().eliteFluidMixerFluid;
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().eliteFluidMixerSpeed;
		}

		@Override
		public long getEnergySize() {
			return AMConfig.get().eliteFluidMixerEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.ELITE;
		}
	}
}
