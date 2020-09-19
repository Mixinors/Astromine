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

import com.github.chainmailstudios.astromine.common.block.entity.base.ComponentEnergyFluidBlockEntity;
import com.github.chainmailstudios.astromine.common.component.inventory.EnergyInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.FluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleEnergyInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleFluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.utilities.tier.MachineTier;
import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.common.volume.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.volume.handler.FluidHandler;
import com.github.chainmailstudios.astromine.registry.AstromineConfig;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.machine.EnergySizeProvider;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.machine.FluidSizeProvider;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.machine.SpeedProvider;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.machine.TierProvider;
import com.github.chainmailstudios.astromine.technologies.common.recipe.FluidMixingRecipe;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlockEntityTypes;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class FluidMixerBlockEntity extends ComponentEnergyFluidBlockEntity implements EnergySizeProvider, TierProvider, SpeedProvider, FluidSizeProvider {
	public double progress = 0;
	public int limit = 100;
	public boolean shouldTry = false;

	private Optional<FluidMixingRecipe> optionalRecipe = Optional.empty();

	public FluidMixerBlockEntity(Block energyBlock, BlockEntityType<?> type) {
		super(energyBlock, type);
		fluidComponent.addListener(() -> shouldTry = true);
	}

	@Override
	protected EnergyInventoryComponent createEnergyComponent() {
		return new SimpleEnergyInventoryComponent(getEnergySize());
	}

	/**
	 * Determine whether the recipe matches the provided fluids.
	 * The existing fluid may be null, indicating that the inserting fluid is the only one present in the system
	 *
	 * @param r
	 *        the recipe to match against
	 * @param existing
	 *        the fluid already present in the machine. may be null.
	 * @param inserting
	 *        the newly inserted fluid.
	 * @return truthy if a recipe matching the provided fluids exists, falsy if no such recipe exists
	 */
	private static boolean isRecipeValid(FluidMixingRecipe r, Fluid existing, Fluid inserting) {
		if (r.getFirstInputFluid() == inserting)
			return true;
		else if (r.getFirstInputFluid() != existing)
			return false;

		if (r.getSecondInputFluid() == inserting)
			return true;
		else if (r.getSecondInputFluid() != existing)
			return false;
		return false;
	}

	/**
	 * Determine whether a recipe exists such that its input criteria are fulfilled by the provided fluids.
	 * The existing fluid may be null, indicating that the inserting fluid is the only one present in the system
	 *
	 * @param existing
	 *        the fluid already present in the machine. may be null.
	 * @param inserting
	 *        the newly inserted fluid.
	 * @return truthy if a recipe matching the provided fluids exists, falsy if no such recipe exists
	 */
	private boolean hasMatchingRecipe(Fluid existing, Fluid inserting) {
		return world.getRecipeManager().getAllOfType(FluidMixingRecipe.Type.INSTANCE).values().stream().filter(recipe -> recipe instanceof FluidMixingRecipe).anyMatch(recipe -> isRecipeValid((FluidMixingRecipe) recipe, existing, inserting));
	}

	/**
	 * Find a slot already filled with the matching fluid
	 *
	 * @param target
	 *        the fluid to find
	 * @return -1 if no such slot exists, otherwise the 0-based index of the slot.
	 */
	private int findMergeableSlot(Fluid target) {
		for (int i = 0; i < 2; i++) {
			FluidVolume v = fluidComponent.getVolume(i);
			if (v == null)
				continue;
			if (v.getFluid() == target) {
				return i;
			}
		}
		return -1;
	}

	@Override
	protected FluidInventoryComponent createFluidComponent() {
		FluidInventoryComponent fC = new SimpleFluidInventoryComponent(3).withInsertPredicate((direction, volume, slot) -> {
			if (slot != 0 && slot != 1) {
				return false;
			}

			Fluid insertedFluid = volume.getFluid();
			int existingSlot = findMergeableSlot(insertedFluid);

			// Allow merging into existing fluids, assuming a valid recipe
			if (existingSlot == slot) {
				return true;
			}

			// Don't allow overflow in the second input, if the first input is full
			if (existingSlot != -1) {
				return false;
			}

			Fluid otherFluid = null;
			for (int i = 0; i < 2; i++) {
				FluidVolume v = fluidComponent.getVolume(i);
				if (v == null)
					continue;
				if (!v.isEmpty()) {
					// No space left
					if (otherFluid != null)
						return false;
					otherFluid = v.getFluid();
				}
			}

			return hasMatchingRecipe(otherFluid, insertedFluid);
		}).withExtractPredicate((direction, volume, slot) -> {
			return slot == 2;
		}).withListener((inventory) -> {
			shouldTry = true;
			progress = 0;
			limit = 100;
			optionalRecipe = Optional.empty();
		});

		FluidHandler.of(fC).getFirst().setSize(getFluidSize());
		FluidHandler.of(fC).getSecond().setSize(getFluidSize());
		FluidHandler.of(fC).getThird().setSize(getFluidSize());

		return fC;
	}

	@Override
	public void tick() {
		super.tick();

		if (world == null)
			return;
		if (world.isClient)
			return;

		FluidHandler.ofOptional(this).ifPresent(fluids -> {
			EnergyVolume energyVolume = getEnergyComponent().getVolume();
			if (!optionalRecipe.isPresent() && shouldTry) {
				optionalRecipe = (Optional) world.getRecipeManager().getAllOfType(FluidMixingRecipe.Type.INSTANCE).values().stream().filter(recipe -> recipe instanceof FluidMixingRecipe).filter(recipe -> ((FluidMixingRecipe) recipe).matches(fluidComponent)).findFirst();
				shouldTry = false;
			}

			if (optionalRecipe.isPresent()) {
				FluidMixingRecipe recipe = optionalRecipe.get();

				if (recipe.matches(fluidComponent)) {
					limit = recipe.getTime();

					double speed = Math.min(getMachineSpeed(), limit - progress);
					double consumed = recipe.getEnergyConsumed() * speed / limit;

					if (energyVolume.hasStored(consumed)) {
						energyVolume.minus(consumed);

						if (progress + speed >= limit) {
							optionalRecipe = Optional.empty();

							if (energyVolume.hasAvailable(consumed)) {
								FluidVolume firstInputFluidVolume = fluids.getFirst();
								FluidVolume secondInputFluidVolume = fluids.getSecond();
								FluidVolume outputVolume = fluids.getThird();

								firstInputFluidVolume.minus(recipe.getFirstInputAmount());
								secondInputFluidVolume.minus(recipe.getSecondInputAmount());
								outputVolume.moveFrom(FluidVolume.of(recipe.getOutputAmount(), recipe.getOutputFluid()), recipe.getOutputAmount());
							}

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
			} else {
				tickInactive();
			}
		});
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
			super(AstromineTechnologiesBlocks.PRIMITIVE_FLUID_MIXER, AstromineTechnologiesBlockEntityTypes.PRIMITIVE_FLUID_MIXER);
		}

		@Override
		public Fraction getFluidSize() {
			return Fraction.of(AstromineConfig.get().primitiveFluidMixerFluid, 1);
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
			super(AstromineTechnologiesBlocks.BASIC_FLUID_MIXER, AstromineTechnologiesBlockEntityTypes.BASIC_FLUID_MIXER);
		}

		@Override
		public Fraction getFluidSize() {
			return Fraction.of(AstromineConfig.get().basicFluidMixerFluid, 1);
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
			super(AstromineTechnologiesBlocks.ADVANCED_FLUID_MIXER, AstromineTechnologiesBlockEntityTypes.ADVANCED_FLUID_MIXER);
		}

		@Override
		public Fraction getFluidSize() {
			return Fraction.of(AstromineConfig.get().advancedFluidMixerFluid, 1);
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
			super(AstromineTechnologiesBlocks.ELITE_FLUID_MIXER, AstromineTechnologiesBlockEntityTypes.ELITE_FLUID_MIXER);
		}

		@Override
		public Fraction getFluidSize() {
			return Fraction.of(AstromineConfig.get().eliteFluidMixerFluid, 1);
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
