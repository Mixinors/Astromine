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
import com.github.mixinors.astromine.common.component.base.EnergyComponent;
import com.github.mixinors.astromine.common.component.base.FluidComponent;
import com.github.mixinors.astromine.registry.common.AMConfig;
import com.github.mixinors.astromine.common.block.entity.machine.EnergySizeProvider;
import com.github.mixinors.astromine.common.block.entity.machine.FluidSizeProvider;
import com.github.mixinors.astromine.common.block.entity.machine.SpeedProvider;
import com.github.mixinors.astromine.common.recipe.FluidGeneratingRecipe;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Supplier;

public abstract class FluidGeneratorBlockEntity extends ComponentEnergyFluidBlockEntity implements EnergySizeProvider, SpeedProvider, FluidSizeProvider {
	private int progress = 0;
	private int limit = 100;
	private boolean shouldTry;

	private Optional<FluidGeneratingRecipe> recipe = Optional.empty();

	public FluidGeneratorBlockEntity(Supplier<? extends BlockEntityType<?>> type) {
		super(type);
	}

	@Override
	public EnergyComponent createEnergyComponent() {
		return EnergyComponent.of(getEnergySize());
	}

	@Override
	public FluidComponent createFluidComponent() {
		return FluidComponent.of(this, 1).withInsertPredicate((direction, volume, slot) -> {
			if (slot != 0) {
				return false;
			}
			
			if (!transfer.getFluid(direction).canInsert()) {
				return false;
			}
			
			if (!volume.test(getFluidComponent().getFirst())) {
				return false;
			}
			
			return FluidGeneratingRecipe.allows(world, FluidComponent.of(volume, getFluidComponent().getFirst().copy()));
		}).withExtractPredicate((direction, volume, slot) -> {
			return false;
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
			recipe = FluidGeneratingRecipe.matching(world, fluids);
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
			var generated = recipe.getEnergyOutput() * speed / limit;

			if (energy.hasAvailable(generated) && this.recipe.get().matches(fluids)) {
				if (progress + speed >= limit) {
					this.recipe = Optional.empty();
					
					var first = fluids.getFirst();
					
					var firstInput = recipe.getFirstInput();
					
					first.take(firstInput.testMatching(first).getAmount());

					energy.give(generated);
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
	
	public int getProgress() {
		return progress;
	}
	
	public void setProgress(int progress) {
		this.progress = progress;
	}
	
	public int getLimit() {
		return limit;
	}
	
	public void setLimit(int limit) {
		this.limit = limit;
	}
	
	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.putInt("Progress", progress);
		tag.putInt("Limit", limit);
		
		return super.toTag(tag);
	}
	
	@Override
	public void fromTag(BlockState state, @NotNull CompoundTag tag) {
		progress = tag.getInt("Progress");
		limit = tag.getInt("Limit");
		
		shouldTry = true;
		
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
	}
}
