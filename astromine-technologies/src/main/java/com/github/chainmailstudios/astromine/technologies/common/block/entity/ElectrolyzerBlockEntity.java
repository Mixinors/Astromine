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
import com.github.chainmailstudios.astromine.technologies.common.recipe.ElectrolyzingRecipe;
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

public abstract class ElectrolyzerBlockEntity extends ComponentEnergyFluidBlockEntity implements EnergySizeProvider, TierProvider, SpeedProvider, FluidSizeProvider {
	public double progress = 0;
	public int limit = 100;
	public boolean shouldTry = false;

	private Optional<ElectrolyzingRecipe> optionalRecipe = Optional.empty();

	public ElectrolyzerBlockEntity(Block energyBlock, BlockEntityType<?> type) {
		super(energyBlock, type);
	}

	@Override
	protected EnergyInventoryComponent createEnergyComponent() {
		return new SimpleEnergyInventoryComponent(getEnergySize());
	}

	@Override
	protected FluidInventoryComponent createFluidComponent() {
		FluidInventoryComponent fluidComponent = new SimpleFluidInventoryComponent(3)
				.withInsertPredicate((direction, volume, slot) -> {
					if (slot != 0) {
						return false;
					}

					Fluid existing = this.fluidComponent.getVolume(0).getFluid();

					Fluid inserting = volume.getFluid();

					return ElectrolyzingRecipe.allows(world, inserting, existing);
				}).withExtractPredicate((direction, volume, slot) -> {
					return slot == 1 || slot == 2;
				}).withListener((inventory) -> {
					shouldTry = true;
					progress = 0;
					limit = 100;
					optionalRecipe = Optional.empty();
				});

		FluidHandler.of(fluidComponent).getFirst().setSize(getFluidSize());
		FluidHandler.of(fluidComponent).getSecond().setSize(getFluidSize());
		FluidHandler.of(fluidComponent).getThird().setSize(getFluidSize());

		return fluidComponent;
	}

	@Override
	public void tick() {
		super.tick();

		if (world == null) return;
		if (world.isClient) return;

		FluidHandler.ofOptional(this).ifPresent(fluids -> {
			EnergyVolume volume = getEnergyComponent().getVolume();
			if (!optionalRecipe.isPresent() && shouldTry) {
				optionalRecipe = (Optional) world.getRecipeManager().getAllOfType(ElectrolyzingRecipe.Type.INSTANCE).values().stream().filter(recipe -> recipe instanceof ElectrolyzingRecipe).filter(recipe -> ((ElectrolyzingRecipe) recipe).matches(fluidComponent)).findFirst();
			}

			if (optionalRecipe.isPresent()) {
				ElectrolyzingRecipe recipe = optionalRecipe.get();

				if (recipe.matches(fluidComponent)) {
					limit = recipe.getTime();

					double speed = Math.min(getMachineSpeed(), limit - progress);
					double consumed = recipe.getEnergyConsumed() * speed / limit;

					if (volume.hasStored(consumed)) {
						volume.minus(consumed);

						if (progress + speed >= limit) {
							optionalRecipe = Optional.empty();

							if (volume.hasAvailable(consumed)) {
								FluidVolume inputVolume = fluids.getFirst();
								FluidVolume firstOutputVolume = fluids.getSecond();
								FluidVolume secondOutputVolume = fluids.getThird();

								inputVolume.minus(recipe.getInputAmount());
								firstOutputVolume.moveFrom(FluidVolume.of(recipe.getFirstOutputAmount(), recipe.getFirstOutputFluid()), recipe.getFirstOutputAmount());
								secondOutputVolume.moveFrom(FluidVolume.of(recipe.getSecondOutputAmount(), recipe.getSecondOutputFluid()), recipe.getSecondOutputAmount());
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

	public static class Primitive extends ElectrolyzerBlockEntity {
		public Primitive() {
			super(AstromineTechnologiesBlocks.PRIMITIVE_ELECTROLYZER, AstromineTechnologiesBlockEntityTypes.PRIMITIVE_ELECTROLYZER);
		}

		@Override
		public Fraction getFluidSize() {
			return Fraction.of(AstromineConfig.get().primitiveElectrolyzerFluid, 1);
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().primitiveElectrolyzerSpeed;
		}

		@Override
		public double getEnergySize() {
			return AstromineConfig.get().primitiveElectrolyzerEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.PRIMITIVE;
		}
	}

	public static class Basic extends ElectrolyzerBlockEntity {
		public Basic() {
			super(AstromineTechnologiesBlocks.BASIC_ELECTROLYZER, AstromineTechnologiesBlockEntityTypes.BASIC_ELECTROLYZER);
		}

		@Override
		public Fraction getFluidSize() {
			return Fraction.of(AstromineConfig.get().basicElectrolyzerFluid, 1);
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().basicElectrolyzerSpeed;
		}

		@Override
		public double getEnergySize() {
			return AstromineConfig.get().basicElectrolyzerEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.BASIC;
		}
	}

	public static class Advanced extends ElectrolyzerBlockEntity {
		public Advanced() {
			super(AstromineTechnologiesBlocks.ADVANCED_ELECTROLYZER, AstromineTechnologiesBlockEntityTypes.ADVANCED_ELECTROLYZER);
		}

		@Override
		public Fraction getFluidSize() {
			return Fraction.of(AstromineConfig.get().advancedElectrolyzerFluid, 1);
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().advancedElectrolyzerSpeed;
		}

		@Override
		public double getEnergySize() {
			return AstromineConfig.get().advancedElectrolyzerEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.ADVANCED;
		}
	}

	public static class Elite extends ElectrolyzerBlockEntity {
		public Elite() {
			super(AstromineTechnologiesBlocks.ELITE_ELECTROLYZER, AstromineTechnologiesBlockEntityTypes.ELITE_ELECTROLYZER);
		}

		@Override
		public Fraction getFluidSize() {
			return Fraction.of(AstromineConfig.get().eliteElectrolyzerFluid, 1);
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().eliteElectrolyzerSpeed;
		}

		@Override
		public double getEnergySize() {
			return AstromineConfig.get().eliteElectrolyzerEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.ELITE;
		}
	}
}
