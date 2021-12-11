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
import net.minecraft.nbt.NbtCompound;
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
import com.github.mixinors.astromine.common.recipe.ElectrolyzingRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Supplier;

public abstract class ElectrolyzerBlockEntity extends ComponentEnergyFluidBlockEntity implements EnergySizeProvider, TierProvider, SpeedProvider, FluidSizeProvider {
	public double progress = 0;
	public int limit = 100;
	public boolean shouldTry = false;

	private Optional<ElectrolyzingRecipe> optionalRecipe = Optional.empty();

	public ElectrolyzerBlockEntity(Supplier<? extends BlockEntityType<?>> type) {
		super(type);
	}

	@Override
	public EnergyComponent createEnergyComponent() {
		return SimpleEnergyComponent.of(getEnergySize());
	}

	@Override
	public FluidComponent createFluidComponent() {
		FluidComponent fluidComponent = SimpleDirectionalFluidComponent.of(this, 3).withInsertPredicate((direction, volume, slot) -> {
			if (slot != 0) {
				return false;
			}

			if (!volume.test(getFluidComponent().getFirst()) && !volume.test(getFluidComponent().getSecond())) {
				return false;
			}

			return ElectrolyzingRecipe.allows(world, FluidComponent.of(volume, getFluidComponent().getSecond().copy(), getFluidComponent().getThird().copy()));
		}).withExtractPredicate((direction, volume, slot) -> {
			return slot == 1 || slot == 2;
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
			EnergyVolume volume = energyComponent.getVolume();

			if (!optionalRecipe.isPresent() && shouldTry) {
				optionalRecipe = ElectrolyzingRecipe.matching(world, fluidComponent);
				shouldTry = false;

				if (!optionalRecipe.isPresent()) {
					progress = 0;
					limit = 100;
				}
			}

			if (optionalRecipe.isPresent()) {
				ElectrolyzingRecipe recipe = optionalRecipe.get();

				limit = recipe.getTime();

				double speed = Math.min(getMachineSpeed(), limit - progress);
				double consumed = recipe.getEnergyInput() * speed / limit;

				if (volume.hasStored(consumed)) {
					volume.take(consumed);

					if (progress + speed >= limit) {
						optionalRecipe = Optional.empty();

						fluidComponent.getFirst().take(recipe.getFirstInput().testMatching(fluidComponent.getFirst()).getAmount());
						fluidComponent.getSecond().take(recipe.getFirstOutput());
						fluidComponent.getThird().take(recipe.getSecondOutput());

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
	public NbtCompound writeNbt(NbtCompound tag) {
		tag.putDouble("progress", progress);
		tag.putInt("limit", limit);
		return super.writeNbt(tag);
	}

	@Override
	public void readNbt(BlockState state, @NotNull NbtCompound tag) {
		progress = tag.getDouble("progress");
		limit = tag.getInt("limit");
		super.readNbt(state, tag);
	}

	public static class Primitive extends ElectrolyzerBlockEntity {
		public Primitive() {
			super(AMBlockEntityTypes.PRIMITIVE_ELECTROLYZER);
		}

		@Override
		public long getFluidSize() {
			return AMConfig.get().primitiveElectrolyzerFluid;
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().primitiveElectrolyzerSpeed;
		}

		@Override
		public double getEnergySize() {
			return AMConfig.get().primitiveElectrolyzerEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.PRIMITIVE;
		}
	}

	public static class Basic extends ElectrolyzerBlockEntity {
		public Basic() {
			super(AMBlockEntityTypes.BASIC_ELECTROLYZER);
		}

		@Override
		public long getFluidSize() {
			return AMConfig.get().basicElectrolyzerFluid;
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().basicElectrolyzerSpeed;
		}

		@Override
		public double getEnergySize() {
			return AMConfig.get().basicElectrolyzerEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.BASIC;
		}
	}

	public static class Advanced extends ElectrolyzerBlockEntity {
		public Advanced() {
			super(AMBlockEntityTypes.ADVANCED_ELECTROLYZER);
		}

		@Override
		public long getFluidSize() {
			return AMConfig.get().advancedElectrolyzerFluid;
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().advancedElectrolyzerSpeed;
		}

		@Override
		public double getEnergySize() {
			return AMConfig.get().advancedElectrolyzerEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.ADVANCED;
		}
	}

	public static class Elite extends ElectrolyzerBlockEntity {
		public Elite() {
			super(AMBlockEntityTypes.ELITE_ELECTROLYZER);
		}

		@Override
		public long getFluidSize() {
			return AMConfig.get().eliteElectrolyzerFluid;
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().eliteElectrolyzerSpeed;
		}

		@Override
		public double getEnergySize() {
			return AMConfig.get().eliteElectrolyzerEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.ELITE;
		}
	}
}
