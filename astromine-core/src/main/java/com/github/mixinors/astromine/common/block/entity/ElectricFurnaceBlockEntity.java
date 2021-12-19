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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;

import com.github.mixinors.astromine.common.block.entity.base.ComponentEnergyItemBlockEntity;
import com.github.mixinors.astromine.common.inventory.BaseInventory;
import com.github.mixinors.astromine.common.util.tier.MachineTier;
import com.github.mixinors.astromine.common.volume.energy.EnergyVolume;
import com.github.mixinors.astromine.registry.common.AMConfig;
import com.github.mixinors.astromine.common.block.entity.machine.EnergySizeProvider;
import com.github.mixinors.astromine.common.block.entity.machine.SpeedProvider;
import com.github.mixinors.astromine.common.block.entity.machine.TierProvider;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSets;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public abstract class ElectricFurnaceBlockEntity extends ComponentEnergyItemBlockEntity implements EnergySizeProvider, TierProvider, SpeedProvider {
	public double progress = 0;
	public int limit = 100;
	public boolean shouldTry = true;

	private Optional<SmeltingRecipe> optionalRecipe = Optional.empty();

	private static final Map<World, SmeltingRecipe[]> RECIPE_CACHE = new HashMap<>();

	public ElectricFurnaceBlockEntity(Supplier<? extends BlockEntityType<?>> type, BlockPos blockPos, BlockState blockState) {
		super(type, blockPos, blockState);
	}

	@Override
	public ItemStore createItemComponent() {
		return SimpleDirectionalItemComponent.of(this, 2).withInsertPredicate((direction, stack, slot) -> {
			if (slot != 1) {
				return false;
			}

			BaseInventory inputInventory = BaseInventory.of(stack);

			if (world != null) {
				if (RECIPE_CACHE.get(world) == null) {
					RECIPE_CACHE.put(world, world.getRecipeManager().getAllOfType(RecipeType.SMELTING).values().stream().map(it -> (SmeltingRecipe) it).toArray(SmeltingRecipe[]::new));
				}

				for (SmeltingRecipe recipe : RECIPE_CACHE.get(world)) {
					if (recipe.matches(inputInventory, world)) {
						return true;
					}
				}
			}

			return false;
		}).withExtractPredicate(((direction, stack, slot) -> {
			return slot == 0;
		})).withListener((inventory) -> {
			shouldTry = true;
			optionalRecipe = Optional.empty();
		});
	}

	@Override
	public EnergyStore createEnergyComponent() {
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

		ItemStore itemComponent = getItemComponent();

		EnergyStore energyComponent = getEnergyComponent();

		if (itemComponent != null && energyComponent != null) {
			EnergyVolume energyVolume = energyComponent.getVolume();

			BaseInventory inputInventory = BaseInventory.of(itemComponent.getSecond());

			if (!optionalRecipe.isPresent() && shouldTry) {
				if (RECIPE_CACHE.get(world) == null) {
					RECIPE_CACHE.put(world, world.getRecipeManager().getAllOfType(RecipeType.SMELTING).values().stream().map(it -> (SmeltingRecipe) it).toArray(SmeltingRecipe[]::new));
				}

				for (SmeltingRecipe recipe : RECIPE_CACHE.get(world)) {
					if (recipe.matches(inputInventory, world)) {
						optionalRecipe = Optional.of(recipe);
					}
				}

				shouldTry = false;

				if (!optionalRecipe.isPresent()) {
					progress = 0;
					limit = 100;
				}
			}

			if (optionalRecipe.isPresent()) {
				SmeltingRecipe recipe = optionalRecipe.get();

				if (recipe.matches(inputInventory, world)) {
					limit = recipe.getCookTime();

					double speed = Math.min(getMachineSpeed() * 2, limit - progress);

					ItemStack output = recipe.getOutput().copy();

					boolean isEmpty = itemComponent.getFirst().isEmpty();
					boolean isEqual = ItemStack.areItemsEqual(itemComponent.getFirst(), output) && ItemStack.areNbtEqual(itemComponent.getFirst(), output);

					if ((isEmpty || isEqual) && itemComponent.getFirst().getCount() + output.getCount() <= itemComponent.getFirst().getMaxCount() && energyVolume.hasStored(500.0D / limit * speed)) {
						energyVolume.take(500.0D / limit * speed);

						if (progress + speed >= limit) {
							optionalRecipe = Optional.empty();

							itemComponent.getSecond().decrement(1);

							if (isEmpty) {
								itemComponent.setFirst(output);
							} else {
								itemComponent.getFirst().increment(output.getCount());

								shouldTry = true; // Vanilla is garbage; if we don't do it here, it only triggers the listener on #setStack.
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
		}
	}

	@Override
	public void readNbt(@NotNull NbtCompound tag) {
		super.readNbt(tag);
		progress = tag.getDouble("progress");
		limit = tag.getInt("limit");
		shouldTry = true;
	}

	@Override
	public void writeNbt(NbtCompound tag) {
		tag.putDouble("progress", progress);
		tag.putInt("limit", limit);
		super.writeNbt(tag);
	}

	public static class Primitive extends ElectricFurnaceBlockEntity {
		public Primitive(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.PRIMITIVE_ELECTRIC_FURNACE, blockPos, blockState);
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().primitiveElectricFurnaceSpeed;
		}

		@Override
		public double getEnergySize() {
			return AMConfig.get().primitiveElectricFurnaceEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.PRIMITIVE;
		}
	}

	public static class Basic extends ElectricFurnaceBlockEntity {
		public Basic(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.BASIC_ELECTRIC_FURNACE, blockPos, blockState);
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().basicElectricFurnaceSpeed;
		}

		@Override
		public double getEnergySize() {
			return AMConfig.get().basicElectricFurnaceEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.BASIC;
		}
	}

	public static class Advanced extends ElectricFurnaceBlockEntity {
		public Advanced(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.ADVANCED_ELECTRIC_FURNACE, blockPos, blockState);
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().advancedElectricFurnaceSpeed;
		}

		@Override
		public double getEnergySize() {
			return AMConfig.get().advancedElectricFurnaceEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.ADVANCED;
		}
	}

	public static class Elite extends ElectricFurnaceBlockEntity {
		public Elite(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.ELITE_ELECTRIC_FURNACE, blockPos, blockState);
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().eliteElectricFurnaceSpeed;
		}

		@Override
		public double getEnergySize() {
			return AMConfig.get().eliteElectricFurnaceEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.ELITE;
		}
	}
}
