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
import com.github.mixinors.astromine.common.transfer.storage.LongEnergyStorage;
import com.github.mixinors.astromine.common.transfer.storage.SimpleItemVariantStorage;
import com.github.mixinors.astromine.common.transfer.storage.SimpleItemStorage;
import com.github.mixinors.astromine.common.util.data.tier.Tier;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public abstract class ElectricFurnaceBlockEntity extends ExtendedBlockEntity implements MachineConfigProvider<SimpleMachineConfig> {
	private static final double ENERGY_PER_SMELT = 2000.0D;
	
	public static final int INPUT_SLOT = 0;
	
	public static final int OUTPUT_SLOT = 1;
	
	public static final int[] INSERT_SLOTS = new int[] { INPUT_SLOT };
	
	public static final int[] EXTRACT_SLOTS = new int[] { OUTPUT_SLOT };
	
	private Optional<SmeltingRecipe> optionalRecipe = Optional.empty();
	
	private static final Map<Level, SmeltingRecipe[]> RECIPE_CACHE = new HashMap<>();
	
	public ElectricFurnaceBlockEntity(Supplier<? extends BlockEntityType<?>> type, BlockPos blockPos, BlockState blockState) {
		super(type, blockPos, blockState);
		
		energyStorage = new LongEnergyStorage(getEnergyStorageSize(), getMaxTransferRate(), 0L);
		
		itemStorage = new SimpleItemStorage(2).insertPredicate((variant, slot) -> {
			if (slot != INPUT_SLOT) {
				return false;
			}
			
			var inputInventory = new SingleRecipeInput(variant);
			
			if (level != null) {
				for (var recipe : getSmeltingRecipes(level)) {
					if (recipe.matches(inputInventory, level)) {
						return true;
					}
				}
			}
			
			return false;
		}).extractPredicate((variant, slot) ->
				slot == OUTPUT_SLOT
		).listener(() -> {
			if (level != null && optionalRecipe.isPresent() && !recipeMatches(level, optionalRecipe.get(), itemStorage.slice(INPUT_SLOT, OUTPUT_SLOT))) {
				optionalRecipe = Optional.empty();
			}
			
			setChanged();
		}).insertSlots(INSERT_SLOTS).extractSlots(EXTRACT_SLOTS);
	}
	
	public static boolean recipeMatches(Level world, SmeltingRecipe recipe, SimpleItemVariantStorage... storages) {
		var inputStorage = storages[INPUT_SLOT];
		
		var outputStorage = storages[OUTPUT_SLOT];
		
		var inputInventory = new SingleRecipeInput(inputStorage.getResource().copyWithCount((int) inputStorage.getAmount()));
		
		if (!recipe.matches(inputInventory, world)) {
			return false;
		}
		
		var storageOutput = outputStorage.getResource().copyWithCount((int) outputStorage.getAmount());
		var output = recipe.getResultItem(world.registryAccess());
		
		var isEmpty = outputStorage.isResourceBlank();
		var isEqual = ItemStack.matches(storageOutput, output);
		var canFit = storageOutput.getCount() + output.getCount() <= storageOutput.getMaxStackSize();
		
		return (isEmpty || isEqual) && canFit;
	}
	
	private static SmeltingRecipe[] getSmeltingRecipes(Level world) {
		return RECIPE_CACHE.computeIfAbsent(world, key -> key.getRecipeManager().getAllRecipesFor(RecipeType.SMELTING).stream().map(holder -> holder.value()).toArray(SmeltingRecipe[]::new));
	}
	
	@Override
	public void tick() {
		super.tick();
		
		if (level == null || level.isClientSide || !shouldRun()) {
			return;
		}
		
		if (itemStorage != null && energyStorage != null) {
			var inputInventory = new SingleRecipeInput(itemStorage.getItem(INPUT_SLOT));
			
			if (optionalRecipe.isEmpty()) {
				for (var recipe : getSmeltingRecipes(level)) {
					if (recipe.matches(inputInventory, level)) {
						var output = recipe.getResultItem(level.registryAccess()).copy();
						
						var isEmpty = itemStorage.getItem(OUTPUT_SLOT).isEmpty();
						var isEqual = ItemStack.isSameItemSameComponents(itemStorage.getItem(OUTPUT_SLOT), output);
						var canFit = itemStorage.getItem(OUTPUT_SLOT).getCount() + output.getCount() <= itemStorage.getItem(OUTPUT_SLOT).getMaxStackSize();
						
						if ((isEmpty || isEqual) && canFit) {
							optionalRecipe = Optional.of(recipe);
						}
					}
				}
			}
			
			if (optionalRecipe.isPresent()) {
				var recipe = optionalRecipe.get();
				
				if (recipe.matches(inputInventory, level)) {
					limit = recipe.getCookingTime();
					
					var speed = Math.min(getSpeed() * 2, limit - progress);
					var consumed = (long) (ENERGY_PER_SMELT * speed / limit);
					
					if (energyStorage.amount >= consumed) {
						energyStorage.amount -= consumed;
						
						if (progress + speed >= limit) {
							optionalRecipe = Optional.empty();
							
							var inputStorage = itemStorage.getStorage(INPUT_SLOT);
							
							inputStorage.extract(inputStorage.getResource(), 1, true, false);
							
							var outputStorage = itemStorage.getStorage(OUTPUT_SLOT);
							var output = recipe.getResultItem(level.registryAccess());
							
							outputStorage.insert(output, output.getCount(), true, false);
							
							progress = 0.0D;
						} else {
							progress += speed;
						}
						
						active = true;
					} else {
						active = false;
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
		public Tier getMachineTier() {
			return Tier.PRIMITIVE;
		}
	}
	
	public static class Basic extends ElectricFurnaceBlockEntity {
		public Basic(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.BASIC_ELECTRIC_FURNACE, blockPos, blockState);
		}
		
		@Override
		public Tier getMachineTier() {
			return Tier.BASIC;
		}
	}
	
	public static class Advanced extends ElectricFurnaceBlockEntity {
		public Advanced(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.ADVANCED_ELECTRIC_FURNACE, blockPos, blockState);
		}
		
		@Override
		public Tier getMachineTier() {
			return Tier.ADVANCED;
		}
	}
	
	public static class Elite extends ElectricFurnaceBlockEntity {
		public Elite(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.ELITE_ELECTRIC_FURNACE, blockPos, blockState);
		}
		
		@Override
		public Tier getMachineTier() {
			return Tier.ELITE;
		}
	}
}
