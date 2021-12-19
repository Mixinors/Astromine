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
import com.github.mixinors.astromine.common.recipe.RefiningRecipe;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Supplier;

public abstract class RefineryBlockEntity extends ComponentEnergyFluidBlockEntity implements EnergySizeProvider, TierProvider, SpeedProvider, FluidSizeProvider {
	public double progress = 0;
	public int limit = 100;
	public boolean shouldTry = false;

	private Optional<RefiningRecipe> optionalRecipe = Optional.empty();

	public RefineryBlockEntity(Supplier<? extends BlockEntityType<?>> type, BlockPos blockPos, BlockState blockState) {
		super(type, blockPos, blockState);
	}

	@Override
	public EnergyStore createEnergyComponent() {
		return SimpleEnergyComponent.of(getEnergySize());
	}

	@Override
	public FluidStore createFluidComponent() {
		FluidStore fluidComponent = SimpleDirectionalFluidComponent.of(this, 2).withInsertPredicate((direction, volume, slot) -> {
			if (slot != 0) {
				return false;
			}

			if (!volume.test(getFluidComponent().getFirst())) {
				return false;
			}

			return RefiningRecipe.allows(world, FluidStore.of(volume));
		}).withExtractPredicate((direction, volume, slot) -> {
			return slot == 1;
		}).withListener((inventory) -> {
			shouldTry = true;
			optionalRecipe = Optional.empty();
		});

		fluidComponent.forEach(it -> it.setSize(getFluidSize()));

		return fluidComponent;
	}

	@Override
	public void tick() {
		super.tick();

		if (world == null || world.isClient || !shouldRun())
			return;

		FluidStore fluidComponent = getFluidComponent();

		EnergyStore energyComponent = getEnergyComponent();

		if (fluidComponent != null && energyComponent != null) {
			EnergyVolume volume = energyComponent.getVolume();

			if (!optionalRecipe.isPresent() && shouldTry) {
				optionalRecipe = RefiningRecipe.matching(world, fluidComponent);
				shouldTry = false;

				if (!optionalRecipe.isPresent()) {
					progress = 0;
					limit = 100;
				}
			}

			if (optionalRecipe.isPresent()) {
				RefiningRecipe recipe = optionalRecipe.get();

				limit = recipe.getTime();

				double speed = Math.min(getMachineSpeed(), limit - progress);
				double consumed = recipe.getEnergyInput() * speed / limit;

				if (volume.hasStored(consumed) && recipe.matches(fluidComponent)) {
					volume.take(consumed);

					if (progress + speed >= limit) {
						optionalRecipe = Optional.empty();

						fluidComponent.getFirst().take(recipe.getIngredient().testMatching(fluidComponent.getFirst()).getAmount());
						fluidComponent.getSecond().take(recipe.getFirstOutput());

						progress = 0;
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

	public static class Primitive extends RefineryBlockEntity {
		public Primitive(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.PRIMITIVE_REFINERY, blockPos, blockState);
		}

		@Override
		public long getFluidSize() {
			return AMConfig.get().primitiveRefineryFluid;
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().primitiveRefinerySpeed;
		}

		@Override
		public double getEnergySize() {
			return AMConfig.get().primitiveRefineryEnergy;
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
		public long getFluidSize() {
			return AMConfig.get().basicRefineryFluid;
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().basicRefinerySpeed;
		}

		@Override
		public double getEnergySize() {
			return AMConfig.get().basicRefineryEnergy;
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
		public long getFluidSize() {
			return AMConfig.get().advancedRefineryFluid;
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().advancedRefinerySpeed;
		}

		@Override
		public double getEnergySize() {
			return AMConfig.get().advancedRefineryEnergy;
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
		public long getFluidSize() {
			return AMConfig.get().eliteRefineryFluid;
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().eliteRefinerySpeed;
		}

		@Override
		public double getEnergySize() {
			return AMConfig.get().eliteRefineryEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.ELITE;
		}
	}
}
