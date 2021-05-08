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

import com.github.mixinors.astromine.common.component.general.*;
import com.github.mixinors.astromine.common.component.general.base.EnergyComponent;
import com.github.mixinors.astromine.common.component.general.base.FluidComponent;
import com.github.mixinors.astromine.common.component.general.base.ItemComponent;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import net.minecraft.block.entity.BlockEntityType;

import com.github.mixinors.astromine.common.block.entity.base.ComponentEnergyFluidItemBlockEntity;
import com.github.mixinors.astromine.common.util.StackUtils;
import com.github.mixinors.astromine.common.util.tier.MachineTier;
import com.github.mixinors.astromine.common.volume.energy.EnergyVolume;
import com.github.mixinors.astromine.registry.common.AMConfig;
import com.github.mixinors.astromine.common.block.entity.machine.EnergySizeProvider;
import com.github.mixinors.astromine.common.block.entity.machine.FluidSizeProvider;
import com.github.mixinors.astromine.common.block.entity.machine.SpeedProvider;
import com.github.mixinors.astromine.common.block.entity.machine.TierProvider;
import com.github.mixinors.astromine.common.recipe.SolidifyingRecipe;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSets;

import java.util.Optional;

public abstract class SolidifierBlockEntity extends ComponentEnergyFluidItemBlockEntity implements TierProvider, EnergySizeProvider, FluidSizeProvider, SpeedProvider {
	public double progress = 0;
	public int limit = 100;
	public boolean shouldTry = true;

	private Optional<SolidifyingRecipe> optionalRecipe = Optional.empty();

	public SolidifierBlockEntity(BlockEntityType<?> type) {
		super(type);
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

			return SolidifyingRecipe.allows(world, FluidComponent.of(volume));
		});

		fluidComponent.getFirst().setSize(getFluidSize());

		return fluidComponent;
	}

	@Override
	public ItemComponent createItemComponent() {
		return SimpleDirectionalItemComponent.of(this, 1).withInsertPredicate((direction, stack, slot) -> {
			return false;
		}).withExtractPredicate((direction, stack, slot) -> {
			return slot == 0;
		}).withListener((inventory) -> {
			shouldTry = true;
			optionalRecipe = Optional.empty();
		});
	}

	@Override
	public EnergyComponent createEnergyComponent() {
		return SimpleEnergyComponent.of(getEnergySize());
	}

	@Override
	public IntSet getItemInputSlots() {
		return IntSets.EMPTY_SET;
	}

	@Override
	public IntSet getItemOutputSlots() {
		return IntSets.singleton(0);
	}

	@Override
	public void tick() {
		super.tick();

		if (world == null || world.isClient || !tickRedstone())
			return;

		ItemComponent itemComponent = getItemComponent();

		FluidComponent fluidComponent = getFluidComponent();

		EnergyComponent energyComponent = getEnergyComponent();

		if (fluidComponent != null) {
			EnergyVolume energyVolume = energyComponent.getVolume();

			if (!optionalRecipe.isPresent() && shouldTry) {
				optionalRecipe = SolidifyingRecipe.matching(world, itemComponent, fluidComponent);
				shouldTry = false;

				if (!optionalRecipe.isPresent()) {
					progress = 0;
					limit = 100;
				}
			}

			if (optionalRecipe.isPresent()) {
				SolidifyingRecipe recipe = optionalRecipe.get();

				limit = recipe.getTime();

				double speed = Math.min(getMachineSpeed(), limit - progress);
				double consumed = recipe.getEnergyInput() * speed / limit;

				if (energyVolume.hasStored(consumed)) {
					energyVolume.take(consumed);

					if (progress + speed >= limit) {
						optionalRecipe = Optional.empty();

						fluidComponent.getFirst().take(recipe.getFirstInput().testMatching(fluidComponent.getFirst()).getAmount());
						itemComponent.setFirst(StackUtils.into(itemComponent.getFirst(), recipe.getFirstOutput()));

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

	public static class Primitive extends SolidifierBlockEntity {
		public Primitive() {
			super(AMBlockEntityTypes.PRIMITIVE_SOLIDIFIER);
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().primitiveSolidifierSpeed;
		}

		@Override
		public double getEnergySize() {
			return AMConfig.get().primitiveSolidifierEnergy;
		}

		@Override
		public long getFluidSize() {
			return AMConfig.get().primitiveSolidifierFluid;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.PRIMITIVE;
		}
	}

	public static class Basic extends SolidifierBlockEntity {
		public Basic() {
			super(AMBlockEntityTypes.BASIC_SOLIDIFIER);
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().basicSolidifierSpeed;
		}

		@Override
		public double getEnergySize() {
			return AMConfig.get().basicSolidifierEnergy;
		}

		@Override
		public long getFluidSize() {
			return AMConfig.get().basicSolidifierFluid;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.BASIC;
		}
	}

	public static class Advanced extends SolidifierBlockEntity {
		public Advanced() {
			super(AMBlockEntityTypes.ADVANCED_SOLIDIFIER);
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().advancedSolidifierSpeed;
		}

		@Override
		public double getEnergySize() {
			return AMConfig.get().advancedSolidifierEnergy;
		}

		@Override
		public long getFluidSize() {
			return AMConfig.get().advancedSolidifierFluid;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.ADVANCED;
		}
	}

	public static class Elite extends SolidifierBlockEntity {
		public Elite() {
			super(AMBlockEntityTypes.ELITE_SOLIDIFIER);
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().eliteSolidifierSpeed;
		}

		@Override
		public double getEnergySize() {
			return AMConfig.get().eliteSolidifierEnergy;
		}

		@Override
		public long getFluidSize() {
			return AMConfig.get().eliteSolidifierFluid;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.ELITE;
		}
	}
}
