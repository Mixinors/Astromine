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

import com.github.mixinors.astromine.common.component.general.*;
import com.github.mixinors.astromine.common.component.general.base.EnergyComponent;
import com.github.mixinors.astromine.common.component.general.base.ItemComponent;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import com.github.mixinors.astromine.common.block.entity.base.ComponentEnergyItemBlockEntity;
import com.github.mixinors.astromine.common.util.StackUtils;
import com.github.mixinors.astromine.common.util.tier.MachineTier;
import com.github.mixinors.astromine.common.volume.energy.EnergyVolume;
import com.github.mixinors.astromine.registry.common.AMConfig;
import com.github.mixinors.astromine.common.block.entity.machine.EnergySizeProvider;
import com.github.mixinors.astromine.common.block.entity.machine.SpeedProvider;
import com.github.mixinors.astromine.common.block.entity.machine.TierProvider;
import com.github.mixinors.astromine.common.recipe.WireMillingRecipe;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSets;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Supplier;

public abstract class WireMillBlockEntity extends ComponentEnergyItemBlockEntity implements EnergySizeProvider, TierProvider, SpeedProvider {
	public double progress = 0;
	public int limit = 100;
	public boolean shouldTry = true;

	private Optional<WireMillingRecipe> optionalRecipe = Optional.empty();

	public WireMillBlockEntity(Supplier<? extends BlockEntityType<?>> type, BlockPos blockPos, BlockState blockState) {
		super(type, blockPos, blockState);
	}

	@Override
	public ItemComponent createItemComponent() {
		return SimpleDirectionalItemComponent.of(this, 2).withInsertPredicate((direction, stack, slot) -> {
			if (slot != 1) {
				return false;
			}

			if (!StackUtils.test(stack, getItemComponent().getSecond())) {
				return false;
			}

			return WireMillingRecipe.allows(world, SimpleItemComponent.of(stack));
		}).withExtractPredicate((direction, stack, slot) -> {
			return slot == 0;
		}).withListener((inventory) -> {
			shouldTry = true;
			optionalRecipe = Optional.empty();
		});
	}

	@Override
	public EnergyComponent createEnergyComponent() {
		return SimpleEnergyComponent.of(getEnergySize());
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

		if (world == null || world.isClient || !tickRedstone())
			return;

		ItemComponent itemComponent = getItemComponent();

		EnergyComponent energyComponent = getEnergyComponent();

		if (itemComponent != null) {
			EnergyVolume volume = energyComponent.getVolume();

			if (!optionalRecipe.isPresent() && shouldTry) {
				optionalRecipe = WireMillingRecipe.matching(world, itemComponent);
				shouldTry = false;

				if (!optionalRecipe.isPresent()) {
					progress = 0;
					limit = 100;
				}
			}

			if (optionalRecipe.isPresent()) {
				WireMillingRecipe recipe = optionalRecipe.get();

				limit = recipe.getTime();

				double speed = Math.min(getMachineSpeed(), limit - progress);
				double consumed = recipe.getEnergyInput() * speed / limit;

				if (volume.hasStored(consumed)) {
					volume.take(consumed);

					if (progress + speed >= limit) {
						optionalRecipe = Optional.empty();

						itemComponent.getSecond().decrement(recipe.getInput().testMatching(itemComponent.getSecond()).getCount());
						itemComponent.setFirst(StackUtils.into(itemComponent.getFirst(), recipe.getItemOutput()));

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
	public void writeNbt(NbtCompound tag) {
		tag.putDouble("progress", progress);
		tag.putInt("limit", limit);
		super.writeNbt(tag);
	}

	@Override
	public void readNbt(@NotNull NbtCompound tag) {
		progress = tag.getDouble("progress");
		limit = tag.getInt("limit");
		super.readNbt(tag);
	}

	public static class Primitive extends WireMillBlockEntity {
		public Primitive(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.PRIMITIVE_WIRE_MILL, blockPos, blockState);
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().primitiveWireMillSpeed;
		}

		@Override
		public double getEnergySize() {
			return AMConfig.get().primitiveWireMillEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.PRIMITIVE;
		}
	}

	public static class Basic extends WireMillBlockEntity {
		public Basic(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.BASIC_WIRE_MILL, blockPos, blockState);
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().basicWireMillSpeed;
		}

		@Override
		public double getEnergySize() {
			return AMConfig.get().basicWireMillEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.BASIC;
		}
	}

	public static class Advanced extends WireMillBlockEntity {
		public Advanced(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.ADVANCED_WIRE_MILL, blockPos, blockState);
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().advancedWireMillSpeed;
		}

		@Override
		public double getEnergySize() {
			return AMConfig.get().advancedWireMillEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.ADVANCED;
		}
	}

	public static class Elite extends WireMillBlockEntity {
		public Elite(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.ELITE_WIRE_MILL, blockPos, blockState);
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().eliteWireMillSpeed;
		}

		@Override
		public double getEnergySize() {
			return AMConfig.get().eliteWireMillEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.ELITE;
		}
	}
}
