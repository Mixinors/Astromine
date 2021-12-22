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

import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import com.github.mixinors.astromine.common.util.tier.MachineTier;
import com.github.mixinors.astromine.common.volume.energy.EnergyVolume;
import com.github.mixinors.astromine.registry.common.AMConfig;
import com.github.mixinors.astromine.common.block.entity.machine.EnergySizeProvider;
import com.github.mixinors.astromine.common.block.entity.machine.FluidSizeProvider;
import com.github.mixinors.astromine.common.block.entity.machine.SpeedProvider;
import com.github.mixinors.astromine.common.block.entity.machine.TierProvider;
import com.github.mixinors.astromine.common.recipe.FluidGeneratingRecipe;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Supplier;

public abstract class FluidGeneratorBlockEntity extends ComponentEnergyFluidBlockEntity implements EnergySizeProvider, TierProvider, SpeedProvider, FluidSizeProvider {
	public double progress = 0;
	public int limit = 100;
	public boolean shouldTry;

	private Optional<FluidGeneratingRecipe> optionalRecipe = Optional.empty();

	public FluidGeneratorBlockEntity(Supplier<? extends BlockEntityType<?>> type, BlockPos blockPos, BlockState blockState) {
		super(type, blockPos, blockState);
	}

	@Override
	public EnergyStore createEnergyComponent() {
		return SimpleEnergyComponent.of(getEnergySize());
	}

	@Override
	public SimpleFluidStorage createFluidComponent() {
		SimpleFluidStorage fluidStorage = SimpleDirectionalFluidComponent.of(this, 1).withInsertPredicate((direction, volume, slot) -> {
			if (slot != 0) {
				return false;
			}

			if (!volume.test(getFluidComponent().getFirst())) {
				return false;
			}

			return FluidGeneratingRecipe.allows(world, SimpleFluidStorage.of(volume, getFluidComponent().getFirst().copy()));
		}).withExtractPredicate((direction, volume, slot) -> false).withListener((inventory) -> {
			shouldTry = true;
			optionalRecipe = Optional.empty();
		});

		fluidStorage.getFirst().setSize(getFluidSize());

		return fluidStorage;
	}

	@Override
	public void tick() {
		super.tick();

		if (world == null || world.isClient || !shouldRun())
			return;

		SimpleFluidStorage fluidStorage = getFluidComponent();

		EnergyStore energyComponent = getEnergyComponent();

		if (fluidStorage != null) {
			EnergyVolume energyVolume = energyComponent.getVolume();

			if (!optionalRecipe.isPresent() && shouldTry) {
				optionalRecipe = FluidGeneratingRecipe.matching(world, fluidStorage);
				shouldTry = false;

				if (!optionalRecipe.isPresent()) {
					progress = 0;
					limit = 100;
				}
			}

			if (optionalRecipe.isPresent()) {
				FluidGeneratingRecipe recipe = optionalRecipe.get();

				limit = recipe.getTime();

				double speed = Math.min(getMachineSpeed(), limit - progress);
				double generated = recipe.getEnergyOutput() * speed / limit;

				if (energyVolume.hasAvailable(generated) && optionalRecipe.get().matches(fluidStorage)) {
					if (progress + speed >= limit) {
						optionalRecipe = Optional.empty();

						fluidStorage.getFirst().take(recipe.getInput().testMatching(fluidStorage.getFirst()).getAmount());

						energyVolume.give(generated);
					} else {
						progress += speed;
					}

					isActive = true;
				} else {
					isActive = false;
				}
			} else {
				isActive = false;
			}
		}
	}

	@Override
	public void writeNbt(NbtCompound nbt) {
		nbt.putDouble("progress", progress);
		nbt.putInt("limit", limit);
		super.writeNbt(nbt);
	}

	@Override
	public void readNbt(@NotNull NbtCompound nbt) {
		progress = nbt.getDouble("progress");
		limit = nbt.getInt("limit");
		super.readNbt(nbt);
	}

	public static class Primitive extends FluidGeneratorBlockEntity {
		public Primitive(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.PRIMITIVE_LIQUID_GENERATOR, blockPos, blockState);
		}

		@Override
		public long getFluidSize() {
			return AMConfig.get().primitiveFluidGeneratorFluid;
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().primitiveFluidGeneratorSpeed;
		}

		@Override
		public long getEnergySize() {
			return AMConfig.get().primitiveFluidGeneratorEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.PRIMITIVE;
		}
	}

	public static class Basic extends FluidGeneratorBlockEntity {
		public Basic(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.BASIC_LIQUID_GENERATOR, blockPos, blockState);
		}

		@Override
		public long getFluidSize() {
			return AMConfig.get().basicFluidGeneratorFluid;
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().basicFluidGeneratorSpeed;
		}

		@Override
		public long getEnergySize() {
			return AMConfig.get().basicFluidGeneratorEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.BASIC;
		}
	}

	public static class Advanced extends FluidGeneratorBlockEntity {
		public Advanced(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.ADVANCED_LIQUID_GENERATOR, blockPos, blockState);
		}

		@Override
		public long getFluidSize() {
			return AMConfig.get().advancedFluidGeneratorFluid;
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().advancedFluidGeneratorSpeed;
		}

		@Override
		public long getEnergySize() {
			return AMConfig.get().advancedFluidGeneratorEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.ADVANCED;
		}
	}

	public static class Elite extends FluidGeneratorBlockEntity {
		public Elite(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.ELITE_LIQUID_GENERATOR, blockPos, blockState);
		}

		@Override
		public long getFluidSize() {
			return AMConfig.get().eliteFluidGeneratorFluid;
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().eliteFluidGeneratorSpeed;
		}

		@Override
		public long getEnergySize() {
			return AMConfig.get().eliteFluidGeneratorEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.ELITE;
		}
	}
}
