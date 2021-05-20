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
import net.minecraft.nbt.CompoundTag;

import com.github.mixinors.astromine.common.block.entity.base.ComponentEnergyItemBlockEntity;
import com.github.mixinors.astromine.common.util.StackUtils;
import com.github.mixinors.astromine.registry.common.AMConfig;
import com.github.mixinors.astromine.common.block.entity.machine.EnergySizeProvider;
import com.github.mixinors.astromine.common.block.entity.machine.SpeedProvider;
import com.github.mixinors.astromine.common.recipe.WireMillingRecipe;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSets;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Supplier;

public abstract class WireMillBlockEntity extends ComponentEnergyItemBlockEntity implements EnergySizeProvider, SpeedProvider {
	public double progress = 0;
	public int limit = 100;
	public boolean shouldTry = true;

	private Optional<WireMillingRecipe> recipe = Optional.empty();

	public WireMillBlockEntity(Supplier<? extends BlockEntityType<?>> type) {
		super(type);
	}

	@Override
	public ItemComponent createItemComponent() {
		return ItemComponent.of(this, 2).withInsertPredicate((direction, stack, slot) -> {
			if (slot != 1) {
				return false;
			}
			
			if (!transfer.getItem(direction).canInsert()) {
				return false;
			}

			if (!StackUtils.test(stack, getItemComponent().getSecond())) {
				return false;
			}

			return WireMillingRecipe.allows(world, ItemComponent.of(stack));
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
		return IntSets.singleton(1);
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
			recipe = WireMillingRecipe.matching(world, items);
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
					
					var first = items.getFirst();
					var second = items.getSecond();
					
					var firstInput = recipe.getFirstInput();
					var firstOutput = recipe.getFirstOutput();
					
					second.decrement(firstInput.testMatching(second).getCount());
					items.setFirst(StackUtils.into(first, firstOutput));

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

	public static class Primitive extends WireMillBlockEntity {
		public Primitive() {
			super(AMBlockEntityTypes.PRIMITIVE_WIREMILL);
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().primitiveWireMillSpeed;
		}

		@Override
		public double getEnergySize() {
			return AMConfig.get().primitiveWireMillEnergy;
		}
	}

	public static class Basic extends WireMillBlockEntity {
		public Basic() {
			super(AMBlockEntityTypes.BASIC_WIREMILL);
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().basicWireMillSpeed;
		}

		@Override
		public double getEnergySize() {
			return AMConfig.get().basicWireMillEnergy;
		}
	}

	public static class Advanced extends WireMillBlockEntity {
		public Advanced() {
			super(AMBlockEntityTypes.ADVANCED_WIREMILL);
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().advancedWireMillSpeed;
		}

		@Override
		public double getEnergySize() {
			return AMConfig.get().advancedWireMillEnergy;
		}
	}

	public static class Elite extends WireMillBlockEntity {
		public Elite() {
			super(AMBlockEntityTypes.ELITE_WIREMILL);
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().eliteWireMillSpeed;
		}

		@Override
		public double getEnergySize() {
			return AMConfig.get().eliteWireMillEnergy;
		}
	}
}
