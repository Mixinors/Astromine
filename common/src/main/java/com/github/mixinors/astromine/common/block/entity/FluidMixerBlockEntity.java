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
import net.minecraft.nbt.CompoundTag;

import com.github.mixinors.astromine.common.block.entity.base.ComponentEnergyFluidBlockEntity;
import com.github.mixinors.astromine.common.component.general.base.EnergyComponent;
import com.github.mixinors.astromine.common.component.general.base.FluidComponent;
import com.github.mixinors.astromine.registry.common.AMConfig;
import com.github.mixinors.astromine.common.block.entity.machine.EnergySizeProvider;
import com.github.mixinors.astromine.common.block.entity.machine.FluidSizeProvider;
import com.github.mixinors.astromine.common.block.entity.machine.SpeedProvider;
import com.github.mixinors.astromine.common.recipe.FluidMixingRecipe;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Supplier;

public abstract class FluidMixerBlockEntity extends ComponentEnergyFluidBlockEntity implements EnergySizeProvider, SpeedProvider, FluidSizeProvider {
	public double progress = 0;
	public int limit = 100;
	public boolean shouldTry = false;

	private Optional<FluidMixingRecipe> recipe = Optional.empty();

	public FluidMixerBlockEntity(Supplier<? extends BlockEntityType<?>> type) {
		super(type);
	}

	@Override
	public EnergyComponent createEnergyComponent() {
		return EnergyComponent.of(getEnergySize());
	}

	@Override
	public FluidComponent createFluidComponent() {
		return FluidComponent.of(this, 3).withInsertPredicate((direction, volume, slot) -> {
			if (slot != 0 && slot != 1) {
				return false;
			}
			
			if (!transfer.getFluid(direction).canInsert()) {
				return false;
			}

			if (!volume.test(getFluidComponent().getFirst()) && !volume.test(getFluidComponent().getSecond())) {
				return false;
			}

			return FluidMixingRecipe.allows(world, FluidComponent.of(volume, getFluidComponent().getSecond().copy(), getFluidComponent().getThird().copy())) || FluidMixingRecipe.allows(world, FluidComponent.of(getFluidComponent().getFirst().copy(), volume, getFluidComponent().getThird().copy()));
		}).withExtractPredicate((direction, volume, slot) -> {
			if (!transfer.getFluid(direction).canExtract()) {
				return false;
			}
			
			return slot == 2;
		}).withListener((inventory) -> {
			shouldTry = true;
			recipe = Optional.empty();
		}).withSizes(getFluidSize());
	}

	@Override
	public void tick() {
		super.tick();
		
		if (!(world instanceof ServerWorld) || !tickRedstone())
			return;
		
		if (recipe.isEmpty() && shouldTry) {
			recipe = FluidMixingRecipe.matching(world, fluids);
			shouldTry = false;

			if (recipe.isEmpty()) {
				progress = 0;
				limit = 100;
			}
		}
		
		if (recipe.isPresent()) {
			var recipe = this.recipe.get();

			limit = recipe.getTime();

			var speed = Math.min(getMachineSpeed(), limit - progress);
			var consumed = recipe.getEnergyInput() * speed / limit;

			if (energy.hasStored(consumed)) {
				energy.take(consumed);

				if (progress + speed >= limit) {
					this.recipe = Optional.empty();
					
					var first = fluids.getFirst();
					var second = fluids.getSecond();
					var third = fluids.getThird();
					
					var firstInput = recipe.getFirstInput();
					var secondInput = recipe.getSecondInput();
					var firstOutput = recipe.getFirstOutput();

					first.take(firstInput.testMatching(first).getAmount());
					second.take(secondInput.testMatching(second).getAmount());
					third.take(firstOutput);

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

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.putDouble("Progress", progress);
		tag.putInt("Limit", limit);
		
		return super.toTag(tag);
	}

	@Override
	public void fromTag(BlockState state, @NotNull CompoundTag tag) {
		progress = tag.getDouble("Progress");
		limit = tag.getInt("Limit");
		
		shouldTry = true;
		
		super.fromTag(state, tag);
	}

	public static class Primitive extends FluidMixerBlockEntity {
		public Primitive() {
			super(AMBlockEntityTypes.PRIMITIVE_FLUID_MIXER);
		}

		@Override
		public long getFluidSize() {
			return AMConfig.get().primitiveFluidMixerFluid;
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().primitiveFluidMixerSpeed;
		}

		@Override
		public double getEnergySize() {
			return AMConfig.get().primitiveFluidMixerEnergy;
		}
	}

	public static class Basic extends FluidMixerBlockEntity {
		public Basic() {
			super(AMBlockEntityTypes.BASIC_FLUID_MIXER);
		}

		@Override
		public long getFluidSize() {
			return AMConfig.get().basicFluidMixerFluid;
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().basicFluidMixerSpeed;
		}

		@Override
		public double getEnergySize() {
			return AMConfig.get().basicFluidMixerEnergy;
		}
	}

	public static class Advanced extends FluidMixerBlockEntity {
		public Advanced() {
			super(AMBlockEntityTypes.ADVANCED_FLUID_MIXER);
		}

		@Override
		public long getFluidSize() {
			return AMConfig.get().advancedFluidMixerFluid;
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().advancedFluidMixerSpeed;
		}

		@Override
		public double getEnergySize() {
			return AMConfig.get().advancedFluidMixerEnergy;
		}
	}

	public static class Elite extends FluidMixerBlockEntity {
		public Elite() {
			super(AMBlockEntityTypes.ELITE_FLUID_MIXER);
		}

		@Override
		public long getFluidSize() {
			return AMConfig.get().eliteFluidMixerFluid;
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().eliteFluidMixerSpeed;
		}

		@Override
		public double getEnergySize() {
			return AMConfig.get().eliteFluidMixerEnergy;
		}
	}
}
