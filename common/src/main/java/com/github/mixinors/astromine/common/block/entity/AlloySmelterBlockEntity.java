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

import com.github.mixinors.astromine.common.component.general.base.EnergyComponent;
import com.github.mixinors.astromine.common.component.general.base.ItemComponent;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

import com.github.mixinors.astromine.common.block.entity.base.ComponentEnergyItemBlockEntity;
import com.github.mixinors.astromine.common.util.StackUtils;
import com.github.mixinors.astromine.common.volume.energy.EnergyVolume;
import com.github.mixinors.astromine.registry.common.AMConfig;
import com.github.mixinors.astromine.common.block.entity.machine.EnergySizeProvider;
import com.github.mixinors.astromine.common.block.entity.machine.SpeedProvider;
import com.github.mixinors.astromine.common.recipe.AlloySmeltingRecipe;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSets;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Supplier;

import static java.lang.Math.min;

public abstract class AlloySmelterBlockEntity extends ComponentEnergyItemBlockEntity implements EnergySizeProvider, SpeedProvider {
	public double progress = 0;
	public int limit = 100;
	public boolean shouldTry = false;

	private Optional<AlloySmeltingRecipe> optionalRecipe = Optional.empty();

	public AlloySmelterBlockEntity(Supplier<? extends BlockEntityType<?>> type) {
		super(type);
	}

	@Override
	public ItemComponent createItemComponent() {
		return ItemComponent.of(this, 3).withInsertPredicate((direction, stack, slot) -> {
			if (slot != 0 && slot != 1) {
				return false;
			}

			if (!getTransferComponent().getItem(direction).canInsert()) {
				return false;
			}

			if (!StackUtils.test(stack, getItemComponent().getFirst()) && !StackUtils.test(stack, getItemComponent().getSecond())) {
				return false;
			}

			return AlloySmeltingRecipe.allows(world, ItemComponent.of(stack, getItemComponent().getSecond())) || AlloySmeltingRecipe.allows(world, ItemComponent.of(getItemComponent().getFirst(), stack));
		}).withExtractPredicate(((direction, stack, slot) -> {
			if (!getTransferComponent().getItem(direction).canExtract()) {
				return false;
			}

			return slot == 2;
		})).withListener((inventory) -> {
			shouldTry = true;
			optionalRecipe = Optional.empty();
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

		if (world == null || world.isClient || !tickRedstone())
			return;

		var itemComponent = getItemComponent();

		var energyComponent = getEnergyComponent();

		if (itemComponent != null && energyComponent != null) {
			var energyVolume = energyComponent.getVolume();

			if (!optionalRecipe.isPresent() && shouldTry) {
				optionalRecipe = AlloySmeltingRecipe.matching(world, itemComponent);
				shouldTry = false;

				if (!optionalRecipe.isPresent()) {
					progress = 0;
					limit = 100;
				}
			}

			if (optionalRecipe.isPresent()) {
				var recipe = optionalRecipe.get();

				limit = recipe.getTime();

				var speed = min(getMachineSpeed(), limit - progress);
				var consumed = recipe.getEnergyInput() * speed / limit;

				if (energyVolume.hasStored(consumed)) {
					energyVolume.take(consumed);

					if (progress + speed >= limit) {
						optionalRecipe = Optional.empty();

						var first = itemComponent.getFirst();
						var second = itemComponent.getSecond();

						if (recipe.getFirstInput().test(first) && recipe.getSecondInput().test(second)) {
							first.decrement(recipe.getFirstInput().testMatching(first).getCount());
							second.decrement(recipe.getSecondInput().testMatching(second).getCount());
						} else if (recipe.getFirstInput().test(second) && recipe.getSecondInput().test(first)) {
							second.decrement(recipe.getFirstInput().testMatching(second).getCount());
							first.decrement(recipe.getSecondInput().testMatching(first).getCount());
						}

						itemComponent.setThird(StackUtils.into(itemComponent.getThird(), recipe.getFirstOutput()));

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
