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

import com.github.mixinors.astromine.common.component.base.EnergyComponent;
import com.github.mixinors.astromine.common.component.base.ItemComponent;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;

import com.github.mixinors.astromine.common.block.entity.base.ComponentEnergyItemBlockEntity;
import com.github.mixinors.astromine.common.util.StackUtils;
import com.github.mixinors.astromine.registry.common.AMConfig;
import com.github.mixinors.astromine.common.block.entity.machine.EnergySizeProvider;
import com.github.mixinors.astromine.common.block.entity.machine.SpeedProvider;
import com.github.mixinors.astromine.common.recipe.AlloySmeltingRecipe;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSets;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Supplier;

import static java.lang.Math.min;

public abstract class AlloySmelterBlockEntity extends ComponentEnergyItemBlockEntity implements EnergySizeProvider, SpeedProvider {
	private int progress = 0;
	private int limit = 100;
	private boolean shouldTry = false;

	private Optional<AlloySmeltingRecipe> recipe = Optional.empty();

	public AlloySmelterBlockEntity(Supplier<? extends BlockEntityType<?>> type) {
		super(type);
	}

	@Override
	public ItemComponent createItemComponent() {
		return ItemComponent.of(this, 3).withInsertPredicate((direction, stack, slot) -> {
			if (slot != 0 && slot != 1) {
				return false;
			}

			if (!transfer.getItem(direction).canInsert()) {
				return false;
			}

			if (!StackUtils.test(stack, items.getFirst()) && !StackUtils.test(stack, items.getSecond())) {
				return false;
			}

			return AlloySmeltingRecipe.allows(world, ItemComponent.of(stack, items.getSecond())) || AlloySmeltingRecipe.allows(world, ItemComponent.of(items.getFirst(), stack));
		}).withExtractPredicate(((direction, stack, slot) -> {
			if (!transfer.getItem(direction).canExtract()) {
				return false;
			}

			return slot == 2;
		})).withListener((inventory) -> {
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
		return new IntArraySet(new int[] { 0, 1 });
	}

	@Override
	public IntSet getItemOutputSlots() {
		return IntSets.singleton(2);
	}

	@Override
	public void tick() {
		super.tick();

		if (!(world instanceof ServerWorld) || !tickRedstone())
			return;
		
		if (recipe.isEmpty() && shouldTry) {
			recipe = AlloySmeltingRecipe.matching(world, items);
			shouldTry = false;

			if (recipe.isEmpty()) {
				progress = 0;
				limit = 100;
			}
		}
		
		if (recipe.isPresent()) {
			var recipe = this.recipe.get();

			limit = recipe.getTime();

			var speed = min(getMachineSpeed(), limit - progress);
			var consumed = recipe.getEnergyInput() * speed / limit;

			if (energy.hasStored(consumed)) {
				energy.take(consumed);

				if (progress + speed >= limit) {
					this.recipe = Optional.empty();

					var first = items.getFirst();
					var second = items.getSecond();
					
					var firstInput = recipe.getFirstInput();
					var secondInput = recipe.getSecondInput();

					if (firstInput.test(first) && secondInput.test(second)) {
						first.decrement(firstInput.testMatching(first).getCount());
						second.decrement(secondInput.testMatching(second).getCount());
					} else if (firstInput.test(second) && secondInput.test(first)) {
						second.decrement(firstInput.testMatching(second).getCount());
						first.decrement(secondInput.testMatching(first).getCount());
					}

					items.setThird(StackUtils.into(items.getThird(), recipe.getFirstOutput()));
					
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

	public static class Primitive extends AlloySmelterBlockEntity {
		public Primitive() {
			super(AMBlockEntityTypes.PRIMITIVE_ALLOY_SMELTER);
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().primitiveAlloySmelterSpeed;
		}

		@Override
		public double getEnergySize() {
			return AMConfig.get().primitiveAlloySmelterEnergy;
		}
	}

	public static class Basic extends AlloySmelterBlockEntity {
		public Basic() {
			super(AMBlockEntityTypes.BASIC_ALLOY_SMELTER);
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().basicAlloySmelterSpeed;
		}

		@Override
		public double getEnergySize() {
			return AMConfig.get().basicAlloySmelterEnergy;
		}
	}

	public static class Advanced extends AlloySmelterBlockEntity {
		public Advanced() {
			super(AMBlockEntityTypes.ADVANCED_ALLOY_SMELTER);
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().advancedAlloySmelterSpeed;
		}

		@Override
		public double getEnergySize() {
			return AMConfig.get().advancedAlloySmelterEnergy;
		}
	}

	public static class Elite extends AlloySmelterBlockEntity {
		public Elite() {
			super(AMBlockEntityTypes.ELITE_ALLOY_SMELTER);
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().eliteAlloySmelterSpeed;
		}

		@Override
		public double getEnergySize() {
			return AMConfig.get().eliteAlloySmelterEnergy;
		}
	}
}
