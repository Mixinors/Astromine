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

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.CompoundTag;

import com.github.chainmailstudios.astromine.common.block.entity.base.ComponentEnergyFluidBlockEntity;
import com.github.chainmailstudios.astromine.common.component.inventory.EnergyComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.FluidComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleEnergyComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleFluidComponent;
import com.github.chainmailstudios.astromine.common.utilities.tier.MachineTier;
import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import com.github.chainmailstudios.astromine.common.volume.fraction.Fraction;
import com.github.chainmailstudios.astromine.registry.AstromineConfig;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.machine.EnergySizeProvider;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.machine.FluidSizeProvider;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.machine.SpeedProvider;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.machine.TierProvider;
import com.github.chainmailstudios.astromine.technologies.common.recipe.LiquidGeneratingRecipe;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlockEntityTypes;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlocks;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class LiquidGeneratorBlockEntity extends ComponentEnergyFluidBlockEntity implements EnergySizeProvider, TierProvider, SpeedProvider, FluidSizeProvider {
	public double progress = 0;
	public int limit = 100;
	public boolean shouldTry;

	private Optional<LiquidGeneratingRecipe> optionalRecipe = Optional.empty();

	public LiquidGeneratorBlockEntity(Block energyBlock, BlockEntityType<?> type) {
		super(energyBlock, type);
	}

	@Override
	public EnergyComponent createEnergyComponent() {
		return SimpleEnergyComponent.of(getEnergySize());
	}

	@Override
	public FluidComponent createFluidComponent() {
		FluidComponent fluidComponent = SimpleFluidComponent.of(1).withInsertPredicate((direction, volume, slot) -> {
			if (slot != 0) {
				return false;
			}

			Fluid existing = getFluidComponent().getFirst().getFluid();

			Fluid inserting = volume.getFluid();

			return LiquidGeneratingRecipe.allows(world, inserting, existing);
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

		if (fluidComponent != null) {
			EnergyVolume energyVolume = getEnergyComponent().getVolume();
			if (!optionalRecipe.isPresent() && shouldTry) {
				optionalRecipe = (Optional) world.getRecipeManager().getAllOfType(LiquidGeneratingRecipe.Type.INSTANCE).values().stream().filter(recipe -> recipe instanceof LiquidGeneratingRecipe).filter(recipe -> ((LiquidGeneratingRecipe) recipe).matches(fluidComponent))
					.findFirst();
				shouldTry = false;

				if (!optionalRecipe.isPresent()) {
					progress = 0;
					limit = 100;
				}
			}

			if (optionalRecipe.isPresent()) {
				LiquidGeneratingRecipe recipe = optionalRecipe.get();

				if (recipe.matches(fluidComponent)) {
					limit = recipe.getTime();

					double speed = Math.min(getMachineSpeed(), limit - progress);
					double generated = recipe.getEnergyGenerated() * speed / limit;

					if (energyVolume.hasAvailable(generated)) {
						if (progress + speed >= limit) {
							optionalRecipe = Optional.empty();

							fluidComponent.getFirst().minus(recipe.getIngredient().getFirstMatchingVolume().getAmount());

							energyVolume.add(generated);
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

	public static class Primitive extends LiquidGeneratorBlockEntity {
		public Primitive() {
			super(AstromineTechnologiesBlocks.PRIMITIVE_LIQUID_GENERATOR, AstromineTechnologiesBlockEntityTypes.PRIMITIVE_LIQUID_GENERATOR);
		}

		@Override
		public Fraction getFluidSize() {
			return Fraction.of(AstromineConfig.get().primitiveLiquidGeneratorFluid, 1);
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().primitiveLiquidGeneratorSpeed;
		}

		@Override
		public double getEnergySize() {
			return AstromineConfig.get().primitiveLiquidGeneratorEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.PRIMITIVE;
		}
	}

	public static class Basic extends LiquidGeneratorBlockEntity {
		public Basic() {
			super(AstromineTechnologiesBlocks.BASIC_LIQUID_GENERATOR, AstromineTechnologiesBlockEntityTypes.BASIC_LIQUID_GENERATOR);
		}

		@Override
		public Fraction getFluidSize() {
			return Fraction.of(AstromineConfig.get().basicLiquidGeneratorFluid, 1);
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().basicLiquidGeneratorSpeed;
		}

		@Override
		public double getEnergySize() {
			return AstromineConfig.get().basicLiquidGeneratorEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.BASIC;
		}
	}

	public static class Advanced extends LiquidGeneratorBlockEntity {
		public Advanced() {
			super(AstromineTechnologiesBlocks.ADVANCED_LIQUID_GENERATOR, AstromineTechnologiesBlockEntityTypes.ADVANCED_LIQUID_GENERATOR);
		}

		@Override
		public Fraction getFluidSize() {
			return Fraction.of(AstromineConfig.get().advancedLiquidGeneratorFluid, 1);
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().advancedLiquidGeneratorSpeed;
		}

		@Override
		public double getEnergySize() {
			return AstromineConfig.get().advancedLiquidGeneratorEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.ADVANCED;
		}
	}

	public static class Elite extends LiquidGeneratorBlockEntity {
		public Elite() {
			super(AstromineTechnologiesBlocks.ELITE_LIQUID_GENERATOR, AstromineTechnologiesBlockEntityTypes.ELITE_LIQUID_GENERATOR);
		}

		@Override
		public Fraction getFluidSize() {
			return Fraction.of(AstromineConfig.get().eliteLiquidGeneratorFluid, 1);
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().eliteLiquidGeneratorSpeed;
		}

		@Override
		public double getEnergySize() {
			return AstromineConfig.get().eliteLiquidGeneratorEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.ELITE;
		}
	}
}
