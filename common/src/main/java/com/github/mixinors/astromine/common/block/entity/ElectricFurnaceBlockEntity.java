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
import com.github.mixinors.astromine.mixin.common.RecipeManagerAccessor;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;

import com.github.mixinors.astromine.common.block.entity.base.ComponentEnergyItemBlockEntity;
import com.github.mixinors.astromine.common.inventory.BaseInventory;
import com.github.mixinors.astromine.registry.common.AMConfig;
import com.github.mixinors.astromine.common.block.entity.machine.EnergySizeProvider;
import com.github.mixinors.astromine.common.block.entity.machine.SpeedProvider;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSets;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public abstract class ElectricFurnaceBlockEntity extends ComponentEnergyItemBlockEntity implements EnergySizeProvider, SpeedProvider {
	private double progress = 0;
	private int limit = 100;
	private boolean shouldTry = true;

	private Optional<SmeltingRecipe> recipe = Optional.empty();

	private static final Map<World, SmeltingRecipe[]> RECIPE_CACHE = new HashMap<>();

	public ElectricFurnaceBlockEntity(Supplier<? extends BlockEntityType<?>> type) {
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

			var input = BaseInventory.of(stack);

			if (world != null) {
				if (RECIPE_CACHE.get(world) == null) {
					if (RECIPE_CACHE.get(world) == null && world.getRecipeManager() instanceof RecipeManagerAccessor accessor) {
						RECIPE_CACHE.put(world,
								accessor.getAllOfType(RecipeType.SMELTING)
										.values()
										.stream()
										.map(SmeltingRecipe.class::cast)
										.toArray(SmeltingRecipe[]::new));
					}
				}

				for (var recipe : RECIPE_CACHE.get(world)) {
					if (recipe.matches(input, world)) {
						return true;
					}
				}
			}

			return false;
		}).withExtractPredicate(((direction, stack, slot) -> {
			if (!transfer.getItem(direction).canExtract()) {
				return false;
			}
			
			return slot == 0;
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
		
		var input = BaseInventory.of(items.getSecond());
		
		if (recipe.isEmpty() && shouldTry) {
			if (RECIPE_CACHE.get(world) == null && world.getRecipeManager() instanceof RecipeManagerAccessor accessor) {
				RECIPE_CACHE.put(world,
						accessor.getAllOfType(RecipeType.SMELTING)
								.values()
								.stream()
								.map(SmeltingRecipe.class::cast)
								.toArray(SmeltingRecipe[]::new));
			}

			for (var recipe : RECIPE_CACHE.get(world)) {
				if (recipe.matches(input, world)) {
					this.recipe = Optional.of(recipe);
				}
			}

			shouldTry = false;

			if (recipe.isEmpty()) {
				progress = 0;
				limit = 100;
			}
		}
		
		if (recipe.isPresent()) {
			var recipe = this.recipe.get();

			if (recipe.matches(input, world)) {
				limit = recipe.getCookTime();

				var speed = Math.min(getMachineSpeed() * 2, limit - progress);

				var output = recipe.getOutput().copy();

				var isEmpty = items.getFirst().isEmpty();
				var isEqual = ItemStack.areItemsEqual(items.getFirst(), output) && ItemStack.areTagsEqual(items.getFirst(), output);

				if ((isEmpty || isEqual) && items.getFirst().getCount() + output.getCount() <= items.getFirst().getMaxCount() && energy.hasStored(500.0D / limit * speed)) {
					energy.take(500.0D / limit * speed);

					if (progress + speed >= limit) {
						this.recipe = Optional.empty();

						items.getSecond().decrement(1);

						if (isEmpty) {
							items.setFirst(output);
						} else {
							items.getFirst().increment(output.getCount());

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

	@Override
	public void fromTag(BlockState state, @NotNull CompoundTag tag) {
		super.fromTag(state, tag);
		
		progress = tag.getDouble("Progress");
		limit = tag.getInt("Limit");
		
		shouldTry = true;
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.putDouble("progress", progress);
		tag.putInt("limit", limit);
		
		return super.toTag(tag);
	}

	public static class Primitive extends ElectricFurnaceBlockEntity {
		public Primitive() {
			super(AMBlockEntityTypes.PRIMITIVE_ELECTRIC_FURNACE);
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().primitiveElectricFurnaceSpeed;
		}

		@Override
		public double getEnergySize() {
			return AMConfig.get().primitiveElectricFurnaceEnergy;
		}
	}

	public static class Basic extends ElectricFurnaceBlockEntity {
		public Basic() {
			super(AMBlockEntityTypes.BASIC_ELECTRIC_FURNACE);
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().basicElectricFurnaceSpeed;
		}

		@Override
		public double getEnergySize() {
			return AMConfig.get().basicElectricFurnaceEnergy;
		}
	}

	public static class Advanced extends ElectricFurnaceBlockEntity {
		public Advanced() {
			super(AMBlockEntityTypes.ADVANCED_ELECTRIC_FURNACE);
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().advancedElectricFurnaceSpeed;
		}

		@Override
		public double getEnergySize() {
			return AMConfig.get().advancedElectricFurnaceEnergy;
		}
	}

	public static class Elite extends ElectricFurnaceBlockEntity {
		public Elite() {
			super(AMBlockEntityTypes.ELITE_ELECTRIC_FURNACE);
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().eliteElectricFurnaceSpeed;
		}

		@Override
		public double getEnergySize() {
			return AMConfig.get().eliteElectricFurnaceEnergy;
		}
	}
}
