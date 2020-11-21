/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
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

import com.github.chainmailstudios.astromine.common.block.entity.base.ComponentEnergyFluidItemBlockEntity;
import com.github.chainmailstudios.astromine.common.component.inventory.*;
import com.github.chainmailstudios.astromine.common.utilities.IngredientUtilities;
import com.github.chainmailstudios.astromine.common.utilities.StackUtilities;
import com.github.chainmailstudios.astromine.common.utilities.tier.MachineTier;
import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import com.github.chainmailstudios.astromine.common.volume.fraction.Fraction;
import com.github.chainmailstudios.astromine.registry.AstromineConfig;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.machine.EnergySizeProvider;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.machine.FluidSizeProvider;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.machine.SpeedProvider;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.machine.TierProvider;
import com.github.chainmailstudios.astromine.technologies.common.recipe.MeltingRecipe;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlockEntityTypes;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSets;
import net.minecraft.block.entity.BlockEntityType;

import java.util.Optional;

public abstract class MelterBlockEntity extends ComponentEnergyFluidItemBlockEntity implements TierProvider, EnergySizeProvider, FluidSizeProvider, SpeedProvider {
	public double progress = 0;
	public int limit = 100;
	public boolean shouldTry = true;

	private Optional<MeltingRecipe> optionalRecipe = Optional.empty();

	public MelterBlockEntity(BlockEntityType<?> type) {
		super(type);
	}

	@Override
	public FluidComponent createFluidComponent() {
		FluidComponent fluidComponent = SimpleFluidComponent.of(1).withInsertPredicate((direction, volume, slot) -> {
			return false;
		}).withExtractPredicate((direction, volume, slot) -> {
			return slot == 0;
		});

		fluidComponent.getFirst().setSize(getFluidSize());
		return fluidComponent;
	}

	@Override
	public ItemComponent createItemComponent() {
		return SimpleItemComponent.of(1).withInsertPredicate((direction, stack, slot) -> {
			if (slot != 0) {
				return false;
			}

			if (!StackUtilities.test(stack, getItemComponent().getFirst())) {
				return false;
			}

			return MeltingRecipe.allows(world, ItemComponent.of(stack));
		}).withExtractPredicate((direction, stack, slot) -> {
			return false;
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
		return IntSets.singleton(0);
	}

	@Override
	public IntSet getItemOutputSlots() {
		return IntSets.EMPTY_SET;
	}

	@Override
	public void tick() {
		super.tick();

		if (world == null || world.isClient || !tickRedstone())
			return;

		ItemComponent itemComponent = getItemComponent();

		FluidComponent fluidComponent = getFluidComponent();

		EnergyComponent energyComponent = getEnergyComponent();

		if (itemComponent != null && fluidComponent != null && energyComponent != null) {
			EnergyVolume energyVolume = energyComponent.getVolume();

			if (!optionalRecipe.isPresent() && shouldTry) {
				optionalRecipe = MeltingRecipe.matching(world, itemComponent, fluidComponent, energyComponent);
				shouldTry = false;

				if (!optionalRecipe.isPresent()) {
					progress = 0;
					limit = 100;
				}
			}

			if (optionalRecipe.isPresent()) {
				MeltingRecipe recipe = optionalRecipe.get();

				limit = recipe.getTime();

				double speed = Math.min(getMachineSpeed(), limit - progress);
				double consumed = recipe.getEnergyInput() * speed / limit;

				if (energyVolume.hasStored(consumed)) {
					energyVolume.take(consumed);

					if (progress + speed >= limit) {
						optionalRecipe = Optional.empty();

						fluidComponent.getFirst().take(recipe.getFirstOutput());
						itemComponent.getFirst().decrement(recipe.getFirstInput().testMatching(itemComponent.getFirst()).getCount());

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

	public static class Primitive extends MelterBlockEntity {
		public Primitive() {
			super(AstromineTechnologiesBlockEntityTypes.PRIMITIVE_SOLIDIFIER);
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().primitiveMelterSpeed;
		}

		@Override
		public double getEnergySize() {
			return AstromineConfig.get().primitiveMelterEnergy;
		}

		@Override
		public Fraction getFluidSize() {
			return Fraction.of(AstromineConfig.get().primitiveMelterFluid, 1);
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.PRIMITIVE;
		}
	}

	public static class Basic extends MelterBlockEntity {
		public Basic() {
			super(AstromineTechnologiesBlockEntityTypes.BASIC_SOLIDIFIER);
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().basicMelterSpeed;
		}

		@Override
		public double getEnergySize() {
			return AstromineConfig.get().basicMelterEnergy;
		}

		@Override
		public Fraction getFluidSize() {
			return Fraction.of(AstromineConfig.get().basicMelterFluid, 1);
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.BASIC;
		}
	}

	public static class Advanced extends MelterBlockEntity {
		public Advanced() {
			super(AstromineTechnologiesBlockEntityTypes.ADVANCED_SOLIDIFIER);
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().advancedMelterSpeed;
		}

		@Override
		public double getEnergySize() {
			return AstromineConfig.get().advancedMelterEnergy;
		}

		@Override
		public Fraction getFluidSize() {
			return Fraction.of(AstromineConfig.get().advancedMelterFluid, 1);
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.ADVANCED;
		}
	}

	public static class Elite extends MelterBlockEntity {
		public Elite() {
			super(AstromineTechnologiesBlockEntityTypes.ELITE_SOLIDIFIER);
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().eliteMelterSpeed;
		}

		@Override
		public double getEnergySize() {
			return AstromineConfig.get().eliteMelterEnergy;
		}

		@Override
		public Fraction getFluidSize() {
			return Fraction.of(AstromineConfig.get().eliteMelterFluid, 1);
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.ELITE;
		}
	}
}
