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

package com.github.chainmailstudios.astromine.technologies.common.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;

import com.github.chainmailstudios.astromine.common.block.entity.base.ComponentEnergyFluidBlockEntity;
import com.github.chainmailstudios.astromine.common.component.general.base.EnergyComponent;
import com.github.chainmailstudios.astromine.common.component.general.base.FluidComponent;
import com.github.chainmailstudios.astromine.common.component.general.SimpleEnergyComponent;
import com.github.chainmailstudios.astromine.common.component.general.SimpleFluidComponent;
import com.github.chainmailstudios.astromine.common.utilities.tier.MachineTier;
import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import com.github.chainmailstudios.astromine.registry.AstromineConfig;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.machine.EnergySizeProvider;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.machine.FluidSizeProvider;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.machine.SpeedProvider;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.machine.TierProvider;
import com.github.chainmailstudios.astromine.technologies.common.recipe.FluidMixingRecipe;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlockEntityTypes;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class FluidMixerBlockEntity extends ComponentEnergyFluidBlockEntity implements EnergySizeProvider, TierProvider, SpeedProvider, FluidSizeProvider {
	public double progress = 0;
	public int limit = 100;
	public boolean shouldTry = false;

	private Optional<FluidMixingRecipe> optionalRecipe = Optional.empty();

	public FluidMixerBlockEntity(BlockEntityType<?> type) {
		super(type);
		getFluidComponent().addListener(() -> shouldTry = true);
	}

	@Override
	public EnergyComponent createEnergyComponent() {
		return SimpleEnergyComponent.of(getEnergySize());
	}

	@Override
	public FluidComponent createFluidComponent() {
		FluidComponent fluidComponent = SimpleFluidComponent.of(3).withInsertPredicate((direction, volume, slot) -> {
			if (slot != 0 && slot != 1) {
				return false;
			}

			if (!volume.test(getFluidComponent().getFirst()) && !volume.test(getFluidComponent().getSecond())) {
				return false;
			}

			return FluidMixingRecipe.allows(world, FluidComponent.of(volume, getFluidComponent().getSecond().copy(), getFluidComponent().getThird().copy())) || FluidMixingRecipe.allows(world, FluidComponent.of(getFluidComponent().getFirst().copy(), volume, getFluidComponent().getThird().copy()));
		}).withExtractPredicate((direction, volume, slot) -> {
			return slot == 2;
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

		if (world == null || world.isClient || !tickRedstone())
			return;

		FluidComponent fluidComponent = getFluidComponent();

		EnergyComponent energyComponent = getEnergyComponent();

		if (fluidComponent != null && energyComponent != null) {
			EnergyVolume energyVolume = getEnergyComponent().getVolume();

			if (!optionalRecipe.isPresent() && shouldTry) {
				optionalRecipe = FluidMixingRecipe.matching(world, fluidComponent);
				shouldTry = false;

				if (!optionalRecipe.isPresent()) {
					progress = 0;
					limit = 100;
				}
			}

			if (optionalRecipe.isPresent()) {
				FluidMixingRecipe recipe = optionalRecipe.get();

				limit = recipe.getTime();

				double speed = Math.min(getMachineSpeed(), limit - progress);
				double consumed = recipe.getEnergyInput() * speed / limit;

				if (energyVolume.hasStored(consumed)) {
					energyVolume.take(consumed);

					if (progress + speed >= limit) {
						optionalRecipe = Optional.empty();

						fluidComponent.getFirst().take(recipe.getFirstInput().testMatching(fluidComponent.getFirst()).getAmount());
						fluidComponent.getSecond().take(recipe.getSecondInput().testMatching(fluidComponent.getSecond()).getAmount());
						fluidComponent.getThird().take(recipe.getFirstOutput());

						progress = 0;
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

	public static class Primitive extends FluidMixerBlockEntity {
		public Primitive() {
			super(AstromineTechnologiesBlockEntityTypes.PRIMITIVE_FLUID_MIXER);
		}

		@Override
		public long getFluidSize() {
			return AstromineConfig.get().primitiveFluidMixerFluid;
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().primitiveFluidMixerSpeed;
		}

		@Override
		public double getEnergySize() {
			return AstromineConfig.get().primitiveFluidMixerEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.PRIMITIVE;
		}
	}

	public static class Basic extends FluidMixerBlockEntity {
		public Basic() {
			super(AstromineTechnologiesBlockEntityTypes.BASIC_FLUID_MIXER);
		}

		@Override
		public long getFluidSize() {
			return AstromineConfig.get().basicFluidMixerFluid;
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().basicFluidMixerSpeed;
		}

		@Override
		public double getEnergySize() {
			return AstromineConfig.get().basicFluidMixerEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.BASIC;
		}
	}

	public static class Advanced extends FluidMixerBlockEntity {
		public Advanced() {
			super(AstromineTechnologiesBlockEntityTypes.ADVANCED_FLUID_MIXER);
		}

		@Override
		public long getFluidSize() {
			return AstromineConfig.get().advancedFluidMixerFluid;
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().advancedFluidMixerSpeed;
		}

		@Override
		public double getEnergySize() {
			return AstromineConfig.get().advancedFluidMixerEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.ADVANCED;
		}
	}

	public static class Elite extends FluidMixerBlockEntity {
		public Elite() {
			super(AstromineTechnologiesBlockEntityTypes.ELITE_FLUID_MIXER);
		}

		@Override
		public long getFluidSize() {
			return AstromineConfig.get().eliteFluidMixerFluid;
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().eliteFluidMixerSpeed;
		}

		@Override
		public double getEnergySize() {
			return AstromineConfig.get().eliteFluidMixerEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.ELITE;
		}
	}
}
