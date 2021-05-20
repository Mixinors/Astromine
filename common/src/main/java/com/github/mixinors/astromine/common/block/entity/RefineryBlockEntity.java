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
import com.github.mixinors.astromine.common.recipe.RefiningRecipe;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Supplier;

public abstract class RefineryBlockEntity extends ComponentEnergyFluidBlockEntity implements EnergySizeProvider, SpeedProvider, FluidSizeProvider {
	private double progress = 0;
	private int limit = 100;
	private boolean shouldTry = false;

	private Optional<RefiningRecipe> recipe = Optional.empty();

	public RefineryBlockEntity(Supplier<? extends BlockEntityType<?>> type) {
		super(type);
	}

	@Override
	public EnergyComponent createEnergyComponent() {
		return EnergyComponent.of(getEnergySize());
	}

	@Override
	public FluidComponent createFluidComponent() {
		return FluidComponent.of(this, 2).withInsertPredicate((direction, volume, slot) -> {
			if (slot != 0) {
				return false;
			}
			
			if (!transfer.getFluid(direction).canInsert()) {
				return false;
			}

			if (!volume.test(getFluidComponent().getFirst())) {
				return false;
			}

			return RefiningRecipe.allows(world, FluidComponent.of(volume));
		}).withExtractPredicate((direction, volume, slot) -> {
			if (!transfer.getFluid(direction).canExtract()) {
				return false;
			}
			
			return slot == 1;
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
			recipe = RefiningRecipe.matching(world, fluids);
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

			if (energy.hasStored(consumed) && recipe.matches(fluids)) {
				energy.take(consumed);

				if (progress + speed >= limit) {
					this.recipe = Optional.empty();
					
					var first = fluids.getFirst();
					var second = fluids.getSecond();
					
					var firstInput = recipe.getFirstInput();
					var firstOutput = recipe.getFirstOutput();

					first.take(firstInput.testMatching(first).getAmount());
					second.take(firstOutput);

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

	public static class Primitive extends RefineryBlockEntity {
		public Primitive() {
			super(AMBlockEntityTypes.PRIMITIVE_REFINERY);
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
	}

	public static class Basic extends RefineryBlockEntity {
		public Basic() {
			super(AMBlockEntityTypes.BASIC_REFINERY);
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
	}

	public static class Advanced extends RefineryBlockEntity {
		public Advanced() {
			super(AMBlockEntityTypes.ADVANCED_REFINERY);
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
	}

	public static class Elite extends RefineryBlockEntity {
		public Elite() {
			super(AMBlockEntityTypes.ELITE_REFINERY);
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
	}
}
