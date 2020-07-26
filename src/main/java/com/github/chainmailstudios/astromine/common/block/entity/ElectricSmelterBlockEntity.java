/*
 * MIT License
 * 
 * Copyright (c) 2020 Chainmail Studios
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
package com.github.chainmailstudios.astromine.common.block.entity;

import com.github.chainmailstudios.astromine.common.block.ElectricSmelterBlock;
import com.github.chainmailstudios.astromine.common.block.base.DefaultedBlockWithEntity;
import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedEnergyItemBlockEntity;
import com.github.chainmailstudios.astromine.common.component.inventory.ItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleItemInventoryComponent;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.util.Tickable;
import spinnery.common.inventory.BaseInventory;

import java.util.Optional;

public abstract class ElectricSmelterBlockEntity extends DefaultedEnergyItemBlockEntity implements Tickable {
	public double progress = 0;
	public int limit = 100;

	public boolean shouldTry = true;
	public boolean isActive = false;

	public boolean[] activity = {false, false, false, false, false};

	Optional<SmeltingRecipe> recipe = Optional.empty();

	public ElectricSmelterBlockEntity(BlockEntityType<?> type) {
		super(type);

		addEnergyListener(() -> shouldTry = true);
	}

	@Override
	protected ItemInventoryComponent createItemComponent() {
		return new SimpleItemInventoryComponent(2).withInsertPredicate((direction, itemStack, slot) -> {
			return slot == 1;
		}).withExtractPredicate(((direction, stack, slot) -> {
			return slot == 0;
		})).withListener((inv) -> {
			shouldTry = true;
		});
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		progress = tag.getDouble("progress");
		limit = tag.getInt("limit");
		shouldTry = true;
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.putDouble("progress", progress);
		tag.putInt("limit", limit);
		return super.toTag(tag);
	}

	@Override
	public void tick() {
		super.tick();

		if (world.isClient()) return;
		if (shouldTry) {
			BaseInventory inputInventory = new BaseInventory(1);
			inputInventory.setStack(0, itemComponent.getStack(1));
			if (!recipe.isPresent()) {
				if (hasWorld() && !world.isClient) {
					recipe = (Optional<SmeltingRecipe>) world.getRecipeManager().getFirstMatch((RecipeType) RecipeType.SMELTING, inputInventory, world);
				}
			}
			if (recipe.isPresent() && recipe.get().matches(inputInventory, world)) {
				limit = recipe.get().getCookTime() * 2;

				ItemStack output = recipe.get().getOutput().copy();

				boolean isEmpty = itemComponent.getStack(0).isEmpty();
				boolean isEqual = ItemStack.areItemsEqual(itemComponent.getStack(0), output) && ItemStack.areTagsEqual(itemComponent.getStack(0), output);

				if (asEnergy().use(6) && (isEmpty || isEqual) && itemComponent.getStack(0).getCount() + output.getCount() <= itemComponent.getStack(0).getMaxCount()) {
					if (progress == limit) {
						itemComponent.getStack(1).decrement(1);

						if (isEmpty) {
							itemComponent.setStack(0, output);
						} else {
							itemComponent.getStack(0).increment(output.getCount());
						}

						progress = 0;
					} else {
						progress += 1 * ((ElectricSmelterBlock) this.getCachedState().getBlock()).getMachineSpeed();
					}

					isActive = true;
				}
			} else {
				shouldTry = false;
				isActive = false;
				progress = 0;
				recipe = Optional.empty();
			}
		} else {
			progress = 0;
			isActive = false;
		}

		if (activity.length - 1 >= 0) System.arraycopy(activity, 1, activity, 0, activity.length - 1);

		activity[4] = isActive;

		if (isActive && !activity[0]) {
			world.setBlockState(getPos(), world.getBlockState(getPos()).with(DefaultedBlockWithEntity.ACTIVE, true));
		} else if (!isActive && activity[0]) {
			world.setBlockState(getPos(), world.getBlockState(getPos()).with(DefaultedBlockWithEntity.ACTIVE, false));
		}
	}

	public static class Primitive extends ElectricSmelterBlockEntity {
		public Primitive() {
			super(AstromineBlockEntityTypes.PRIMITIVE_ELECTRIC_SMELTER);
		}

		@Override
		protected double getEnergySize() {
			return AstromineConfig.get().primitiveElectricSmelterEnergy;
		}
	}

	public static class Basic extends ElectricSmelterBlockEntity {
		public Basic() {
			super(AstromineBlockEntityTypes.BASIC_ELECTRIC_SMELTER);
		}

		@Override
		protected double getEnergySize() {
			return AstromineConfig.get().basicElectricSmelterEnergy;
		}
	}

	public static class Advanced extends ElectricSmelterBlockEntity {
		public Advanced() {
			super(AstromineBlockEntityTypes.ADVANCED_ELECTRIC_SMELTER);
		}

		@Override
		protected double getEnergySize() {
			return AstromineConfig.get().advancedElectricSmelterEnergy;
		}
	}

	public static class Elite extends ElectricSmelterBlockEntity {
		public Elite() {
			super(AstromineBlockEntityTypes.ELITE_ELECTRIC_SMELTER);
		}

		@Override
		protected double getEnergySize() {
			return AstromineConfig.get().eliteElectricSmelterEnergy;
		}
	}
}
