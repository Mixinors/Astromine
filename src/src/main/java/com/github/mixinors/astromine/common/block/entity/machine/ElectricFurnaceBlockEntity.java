/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
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

package com.github.mixinors.astromine.common.block.entity.machine;

import com.github.mixinors.astromine.common.block.entity.base.ExtendedBlockEntity;
import com.github.mixinors.astromine.common.config.AMConfig;
import com.github.mixinors.astromine.common.config.entry.tiered.SimpleMachineConfig;
import com.github.mixinors.astromine.common.provider.config.tiered.MachineConfigProvider;
import com.github.mixinors.astromine.common.transfer.storage.SimpleItemStorage;
import com.github.mixinors.astromine.common.util.data.tier.MachineTier;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public abstract class ElectricFurnaceBlockEntity extends ExtendedBlockEntity implements MachineConfigProvider<SimpleMachineConfig> {
	public static final int INPUT_SLOT = 0;
	
	public static final int OUTPUT_SLOT = 1;
	
	public static final int[] INSERT_SLOTS = new int[] { INPUT_SLOT };
	
	public static final int[] EXTRACT_SLOTS = new int[] { OUTPUT_SLOT };
	
	private Optional<SmeltingRecipe> optionalRecipe = Optional.empty();
	
	private static final Map<World, SmeltingRecipe[]> RECIPE_CACHE = new HashMap<>();
	
	public ElectricFurnaceBlockEntity(Supplier<? extends BlockEntityType<?>> type, BlockPos blockPos, BlockState blockState) {
		super(type, blockPos, blockState);
		
		energyStorage = new SimpleEnergyStorage(getEnergyStorageSize(), getMaxTransferRate(), 0L);
		
		itemStorage = new SimpleItemStorage(2).insertPredicate((variant, slot) -> {
			if (slot != INPUT_SLOT) {
				return false;
			}
			
			var inputInventory = new SimpleInventory(variant.toStack());
			
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
		}).extractPredicate((variant, slot) ->
				slot == OUTPUT_SLOT
		).listener(() -> {
			if (optionalRecipe.isPresent() && !recipeMatches(world, optionalRecipe.get(), itemStorage.slice(INPUT_SLOT, OUTPUT_SLOT))) {
				optionalRecipe = Optional.empty();
			}
			
			markDirty();
		}).insertSlots(INSERT_SLOTS).extractSlots(EXTRACT_SLOTS);
	}
	
	public static boolean recipeMatches(World world, SmeltingRecipe recipe, SingleSlotStorage<ItemVariant>... storages) {
		var inputStorage = storages[INPUT_SLOT];
		
		var outputStorage = storages[OUTPUT_SLOT];
		
		var inputInventory = new SimpleInventory(inputStorage.getResource().toStack((int) inputStorage.getAmount()));
		
		if (!recipe.matches(inputInventory, world)) {
			return false;
		}
		
		var storageOutput = outputStorage.getResource().toStack((int) outputStorage.getAmount());
		var output = recipe.getOutput();
		
		var isEmpty = outputStorage.isResourceBlank();
		var isEqual = ItemStack.areItemsEqual(storageOutput, output) && ItemStack.areNbtEqual(storageOutput, output);
		var canFit = storageOutput.getCount() + output.getCount() <= storageOutput.getMaxCount();
		
		return (isEmpty || isEqual) && canFit;
	}
	
	@Override
	public void tick() {
		super.tick();
		
		if (world == null || world.isClient || !shouldRun()) {
			return;
		}
		
		if (itemStorage != null && energyStorage != null) {
			var inputInventory = new SimpleInventory(itemStorage.getStack(INPUT_SLOT));
			
			if (optionalRecipe.isEmpty()) {
				if (RECIPE_CACHE.get(world) == null) {
					RECIPE_CACHE.put(world, world.getRecipeManager().getAllOfType(RecipeType.SMELTING).values().stream().map(it -> (SmeltingRecipe) it).toArray(SmeltingRecipe[]::new));
				}
				
				for (var recipe : RECIPE_CACHE.get(world)) {
					if (recipe.matches(inputInventory, world)) {
						var output = recipe.getOutput().copy();
						
						var isEmpty = itemStorage.getStack(OUTPUT_SLOT).isEmpty();
						var isEqual = ItemStack.areItemsEqual(itemStorage.getStack(OUTPUT_SLOT), output) && ItemStack.areNbtEqual(itemStorage.getStack(OUTPUT_SLOT), output);
						var canFit = itemStorage.getStack(OUTPUT_SLOT).getCount() + output.getCount() <= itemStorage.getStack(OUTPUT_SLOT).getMaxCount();
						
						if ((isEmpty || isEqual) && canFit) {
							optionalRecipe = Optional.of(recipe);
						}
					}
				}
			}
			
			if (optionalRecipe.isPresent()) {
				var recipe = optionalRecipe.get();
				
				if (recipe.matches(inputInventory, world)) {
					limit = recipe.getCookTime();
					
					var speed = Math.min(getSpeed() * 2, limit - progress);
					var consumed = (long) (500.0D * speed / limit);
					
					try (var transaction = Transaction.openOuter()) {
						if (energyStorage.amount >= consumed) {
							energyStorage.amount -= consumed;
							
							if (progress + speed >= limit) {
								optionalRecipe = Optional.empty();
								
								var inputStorage = itemStorage.getStorage(INPUT_SLOT);
								
								inputStorage.extract(inputStorage.getResource(), 1, transaction, true);
								
								var outputStorage = itemStorage.getStorage(OUTPUT_SLOT);
								
								outputStorage.insert(ItemVariant.of(recipe.getOutput()), recipe.getOutput().getCount(), transaction, true);
								
								transaction.commit();
								
								progress = 0.0D;
							} else {
								progress += speed;
							}
							
							active = true;
						} else {
							active = false;
						}
					}
				} else {
					active = false;
				}
			} else {
				progress = 0.0D;
				limit = 100.0D;
				
				active = false;
			}
		}
	}
	
	@Override
	public SimpleMachineConfig getConfig() {
		return AMConfig.get().blocks.machines.electricFurnace;
	}
	
	public static class Primitive extends ElectricFurnaceBlockEntity {
		public Primitive(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.PRIMITIVE_ELECTRIC_FURNACE, blockPos, blockState);
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
		public MachineTier getMachineTier() {
			return MachineTier.BASIC;
		}
	}
	
	public static class Advanced extends ElectricFurnaceBlockEntity {
		public Advanced(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.ADVANCED_ELECTRIC_FURNACE, blockPos, blockState);
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
		public MachineTier getMachineTier() {
			return MachineTier.ELITE;
		}
	}
}
