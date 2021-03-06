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

import com.github.mixinors.astromine.common.component.general.SimpleDirectionalFluidComponent;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;

import com.github.mixinors.astromine.common.block.entity.base.ComponentEnergyFluidBlockEntity;
import com.github.mixinors.astromine.common.component.general.base.EnergyComponent;
import com.github.mixinors.astromine.common.component.general.base.FluidComponent;
import com.github.mixinors.astromine.common.component.general.SimpleEnergyComponent;
import com.github.mixinors.astromine.common.component.general.SimpleFluidComponent;
import com.github.mixinors.astromine.common.util.tier.MachineTier;
import com.github.mixinors.astromine.common.volume.energy.EnergyVolume;
import com.github.mixinors.astromine.registry.common.AMConfig;
import com.github.mixinors.astromine.common.block.entity.machine.EnergySizeProvider;
import com.github.mixinors.astromine.common.block.entity.machine.FluidSizeProvider;
import com.github.mixinors.astromine.common.block.entity.machine.SpeedProvider;
import com.github.mixinors.astromine.common.block.entity.machine.TierProvider;
import com.github.mixinors.astromine.common.recipe.FluidGeneratingRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Supplier;

public abstract class FluidGeneratorBlockEntity extends ComponentEnergyFluidBlockEntity implements EnergySizeProvider, TierProvider, SpeedProvider, FluidSizeProvider {
	public double progress = 0;
	public int limit = 100;
	public boolean shouldTry;

	private Optional<FluidGeneratingRecipe> optionalRecipe = Optional.empty();

	public FluidGeneratorBlockEntity(Supplier<? extends BlockEntityType<?>> type) {
		super(type);
	}

	@Override
	public EnergyComponent createEnergyComponent() {
		return SimpleEnergyComponent.of(getEnergySize());
	}

	@Override
	public FluidComponent createFluidComponent() {
		FluidComponent fluidComponent = SimpleDirectionalFluidComponent.of(this, 1).withInsertPredicate((direction, volume, slot) -> {
			if (slot != 0) {
				return false;
			}

			if (!volume.test(getFluidComponent().getFirst())) {
				return false;
			}

			return FluidGeneratingRecipe.allows(world, FluidComponent.of(volume, getFluidComponent().getFirst().copy()));
		}).withExtractPredicate((direction, volume, slot) -> false).withListener((inventory) -> {
			shouldTry = true;
			optionalRecipe = Optional.empty();
		});

		fluidComponent.getFirst().setSize(getFluidSize());

		return fluidComponent;
	}

	@Override
	public void tick() {
		super.tick();

		if (world == null || world.isClient || !tickRedstone())
			return;

		FluidComponent fluidComponent = getFluidComponent();

		EnergyComponent energyComponent = getEnergyComponent();

		if (fluidComponent != null) {
			EnergyVolume energyVolume = energyComponent.getVolume();

			if (!optionalRecipe.isPresent() && shouldTry) {
				optionalRecipe = FluidGeneratingRecipe.matching(world, fluidComponent);
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

				if (energyVolume.hasAvailable(generated) && optionalRecipe.get().matches(fluidComponent)) {
					if (progress + speed >= limit) {
						optionalRecipe = Optional.empty();

						fluidComponent.getFirst().take(recipe.getFirstInput().testMatching(fluidComponent.getFirst()).getAmount());

						energyVolume.give(generated);
					} else {
						progress += speed;
					}

					tickActive();
				} else {
					tickInactive();
				}
			} else {
				tickInactive();
			}
		}
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.putDouble("progress", progress);
		tag.putInt("limit", limit);
		return super.toTag(tag);
	}

	@Override
	public void fromTag(BlockState state, @NotNull CompoundTag tag) {
		progress = tag.getDouble("progress");
		limit = tag.getInt("limit");
		super.fromTag(state, tag);
	}

	public static class Primitive extends FluidGeneratorBlockEntity {
		public Primitive() {
			super(AMBlockEntityTypes.PRIMITIVE_LIQUID_GENERATOR);
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
		public double getEnergySize() {
			return AMConfig.get().primitiveFluidGeneratorEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.PRIMITIVE;
		}
	}

	public static class Basic extends FluidGeneratorBlockEntity {
		public Basic() {
			super(AMBlockEntityTypes.BASIC_LIQUID_GENERATOR);
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
		public double getEnergySize() {
			return AMConfig.get().basicFluidGeneratorEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.BASIC;
		}
	}

	public static class Advanced extends FluidGeneratorBlockEntity {
		public Advanced() {
			super(AMBlockEntityTypes.ADVANCED_LIQUID_GENERATOR);
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
		public double getEnergySize() {
			return AMConfig.get().advancedFluidGeneratorEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.ADVANCED;
		}
	}

	public static class Elite extends FluidGeneratorBlockEntity {
		public Elite() {
			super(AMBlockEntityTypes.ELITE_LIQUID_GENERATOR);
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
		public double getEnergySize() {
			return AMConfig.get().eliteFluidGeneratorEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.ELITE;
		}
	}
}
