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

import com.github.mixinors.astromine.common.block.entity.base.ExtendedBlockEntity;
import com.github.mixinors.astromine.common.transfer.storage.SimpleItemStorage;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;

import com.github.mixinors.astromine.common.inventory.BaseInventory;
import com.github.mixinors.astromine.common.util.tier.MachineTier;
import com.github.mixinors.astromine.registry.common.AMConfig;
import com.github.mixinors.astromine.common.block.entity.machine.EnergySizeProvider;
import com.github.mixinors.astromine.common.block.entity.machine.SpeedProvider;
import com.github.mixinors.astromine.common.block.entity.machine.TierProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public abstract class ElectricFurnaceBlockEntity extends ExtendedBlockEntity implements EnergySizeProvider, TierProvider, SpeedProvider {
	public double progress = 0;
	public int limit = 100;
	public boolean shouldTry = true;

	private Optional<SmeltingRecipe> optionalRecipe = Optional.empty();

	private static final Map<World, SmeltingRecipe[]> RECIPE_CACHE = new HashMap<>();

	public ElectricFurnaceBlockEntity(Supplier<? extends BlockEntityType<?>> type, BlockPos blockPos, BlockState blockState) {
		super(type, blockPos, blockState);
		
		energyStorage = new SimpleEnergyStorage(getEnergySize(), Long.MAX_VALUE, Long.MAX_VALUE);
		
		itemStorage = new SimpleItemStorage(2).insertPredicate((variant, slot) -> {
			if (slot != 1) {
				return false;
			}
			
			var inputInventory = BaseInventory.of(variant.toStack());
			
			if (world != null) {
				if (RECIPE_CACHE.get(world) == null) {
					RECIPE_CACHE.put(world, world.getRecipeManager().getAllOfType(RecipeType.SMELTING).values().stream().map(it -> (SmeltingRecipe) it).toArray(SmeltingRecipe[]::new));
				}
				
				for (var recipe : RECIPE_CACHE.get(world)) {
					if (recipe.matches(inputInventory, world)) {
						return true;
					}
				}
			}
			
			return false;
		}).extractPredicate((variant, slot) -> {
			return slot == 0;
		}).listener(() -> {
			shouldTry = true;
			optionalRecipe = Optional.empty();
		}).insertSlots(new int[] { 1 }).extractSlots(new int[] { 0 });
	}

	@Override
	public void tick() {
		super.tick();

		if (world == null || world.isClient || !shouldRun())
			return;

		if (itemStorage != null && energyStorage != null) {
			var inputInventory = BaseInventory.of(itemStorage.getStack(1));

			if (!optionalRecipe.isPresent() && shouldTry) {
				if (RECIPE_CACHE.get(world) == null) {
					RECIPE_CACHE.put(world, world.getRecipeManager().getAllOfType(RecipeType.SMELTING).values().stream().map(it -> (SmeltingRecipe) it).toArray(SmeltingRecipe[]::new));
				}

				for (var recipe : RECIPE_CACHE.get(world)) {
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
				var recipe = optionalRecipe.get();

				if (recipe.matches(inputInventory, world)) {
					limit = recipe.getCookTime();

					var speed = Math.min(getMachineSpeed() * 2, limit - progress);

					var output = recipe.getOutput().copy();

					var isEmpty = itemStorage.getStack(0).isEmpty();
					var isEqual = ItemStack.areItemsEqual(itemStorage.getStack(0), output) && ItemStack.areNbtEqual(itemStorage.getStack(0), output);

					if (energyStorage.getAmount() > 500.0D / limit * speed) {
						try (var transaction = Transaction.openOuter()) {
							energyStorage.extract((long) (500.0D / limit * speed), transaction);
							
							if ((isEmpty || isEqual) && itemStorage.getStack(0).getCount() + output.getCount() <= itemStorage.getStack(0).getMaxCount()) {
								if (progress + speed >= limit) {
									optionalRecipe = Optional.empty();
									
									itemStorage.getStack(1).decrement(1);
									
									if (isEmpty) {
										itemStorage.setStack(0, output);
									} else {
										itemStorage.getStack(0).increment(output.getCount());
										
										shouldTry = true; // Vanilla is garbage; if we don't do it here, it only triggers the listener on #setStack.
									}
									
									progress = 0;
								} else {
									progress += speed;
								}
								
								isActive = true;
							} else {
								isActive = false;
							}
						}
					} else {
						isActive = false;
					}
				} else {
					isActive = false;
				}
			} else {
				isActive = false;
			}
		}
	}

	@Override
	public void readNbt(@NotNull NbtCompound nbt) {
		super.readNbt(nbt);
		progress = nbt.getDouble("progress");
		limit = nbt.getInt("limit");
		shouldTry = true;
	}

	@Override
	public void writeNbt(NbtCompound nbt) {
		nbt.putDouble("progress", progress);
		nbt.putInt("limit", limit);
		super.writeNbt(nbt);
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
		public long getEnergySize() {
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
		public long getEnergySize() {
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
		public long getEnergySize() {
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
		public long getEnergySize() {
			return AMConfig.get().eliteElectricFurnaceEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.ELITE;
		}
	}
}
