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

import com.github.mixinors.astromine.common.component.base.*;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;

import com.github.mixinors.astromine.common.block.entity.base.ComponentEnergyFluidItemBlockEntity;
import com.github.mixinors.astromine.common.util.StackUtils;
import com.github.mixinors.astromine.registry.common.AMConfig;
import com.github.mixinors.astromine.common.block.entity.machine.EnergySizeProvider;
import com.github.mixinors.astromine.common.block.entity.machine.FluidSizeProvider;
import com.github.mixinors.astromine.common.block.entity.machine.SpeedProvider;
import com.github.mixinors.astromine.common.recipe.SolidifyingRecipe;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSets;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Supplier;

public abstract class SolidifierBlockEntity extends ComponentEnergyFluidItemBlockEntity implements EnergySizeProvider, FluidSizeProvider, SpeedProvider {
	private int progress = 0;
	private int limit = 100;
	private boolean shouldTry = true;

	private Optional<SolidifyingRecipe> recipe = Optional.empty();

	public SolidifierBlockEntity(Supplier<? extends BlockEntityType<?>> type) {
		super(type);
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

			return SolidifyingRecipe.allows(world, FluidComponent.of(volume));
		}).withSizes(getFluidSize());
	}

	@Override
	public ItemComponent createItemComponent() {
		return ItemComponent.of(this, 1).withInsertPredicate((direction, stack, slot) -> {
			return false;
		}).withExtractPredicate((direction, stack, slot) -> {
			if (!transfer.getItem(direction).canExtract()) {
				return false;
			}
			
			return slot == 0;
		}).withListener((inventory) -> {
			shouldTry = true;
			recipe = Optional.empty();
		});
	}

	@Override
	public EnergyComponent createEnergyComponent() {
		return EnergyComponent.of(getEnergySize());
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
		
		if (!(world instanceof ServerWorld) || !tickRedstone())
			return;
		
		if (recipe.isEmpty() && shouldTry) {
			recipe = SolidifyingRecipe.matching(world, items, fluids);
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

					var firstFluid = fluids.getFirst();
					var firstStack = items.getFirst();
					
					var firstInput = recipe.getFirstInput();
					var firstOutput = recipe.getFirstOutput();
					
					firstFluid.take(firstInput.testMatching(firstFluid).getAmount());
					items.setFirst(StackUtils.into(firstStack, firstOutput));

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
	}
}
