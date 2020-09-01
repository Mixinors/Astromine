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

package com.github.chainmailstudios.astromine.technologies.common.block.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.util.Tickable;

import com.github.chainmailstudios.astromine.common.block.base.BlockWithEntity;
import com.github.chainmailstudios.astromine.common.block.base.WrenchableHorizontalFacingEnergyTieredBlockWithEntity;
import com.github.chainmailstudios.astromine.common.block.entity.base.ComponentEnergyInventoryBlockEntity;
import com.github.chainmailstudios.astromine.common.component.inventory.ItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.inventory.BaseInventory;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlockEntityTypes;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlocks;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSets;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class ElectricSmelterBlockEntity extends ComponentEnergyInventoryBlockEntity implements Tickable {
	public double progress = 0;
	public int limit = 100;

	public boolean shouldTry = true;
	public boolean isActive = false;

	public boolean[] activity = { false, false, false, false, false };

	Optional<SmeltingRecipe> recipe = Optional.empty();

	public ElectricSmelterBlockEntity(Block energyBlock, BlockEntityType<?> type) {
		super(energyBlock, type);

		addEnergyListener(() -> shouldTry = true);
	}

	@Override
	protected ItemInventoryComponent createItemComponent() {
		return new SimpleItemInventoryComponent(2).withInsertPredicate((direction, itemStack, slot) -> {
			if (slot != 1)
				return false;
			BaseInventory inputInventory = new BaseInventory(1);
			inputInventory.setStack(0, itemStack);
			if (hasWorld()) {
				Optional<SmeltingRecipe> recipe = (Optional<SmeltingRecipe>) world.getRecipeManager().getFirstMatch((RecipeType) RecipeType.SMELTING, inputInventory, world);
				return recipe.isPresent();
			}
			return false;
		}).withExtractPredicate(((direction, stack, slot) -> {
			return slot == 0;
		})).withListener((inv) -> {
			shouldTry = true;
		});
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
	public void fromTag(BlockState state, @NotNull CompoundTag tag) {
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

		if (world.isClient())
			return;
		if (shouldTry) {
			BaseInventory inputInventory = new BaseInventory(1);
			inputInventory.setStack(0, itemComponent.getStack(1));
			if (!recipe.isPresent()) {
				if (hasWorld() && !world.isClient) {
					recipe = (Optional<SmeltingRecipe>) world.getRecipeManager().getFirstMatch((RecipeType) RecipeType.SMELTING, inputInventory, world);
				}
			}
			if (recipe.isPresent() && recipe.get().matches(inputInventory, world)) {
				limit = recipe.get().getCookTime();

				double speed = Math.min(((WrenchableHorizontalFacingEnergyTieredBlockWithEntity) this.getCachedState().getBlock()).getMachineSpeed() * 2, limit - progress);
				ItemStack output = recipe.get().getOutput().copy();

				boolean isEmpty = itemComponent.getStack(0).isEmpty();
				boolean isEqual = ItemStack.areItemsEqual(itemComponent.getStack(0), output) && ItemStack.areTagsEqual(itemComponent.getStack(0), output);

				if ((isEmpty || isEqual) && itemComponent.getStack(0).getCount() + output.getCount() <= itemComponent.getStack(0).getMaxCount() && asEnergy().use(500 / limit * speed)) {
					if (progress + speed >= limit) {
						itemComponent.getStack(1).decrement(1);

						if (isEmpty) {
							itemComponent.setStack(0, output);
						} else {
							itemComponent.getStack(0).increment(output.getCount());
						}

						progress = 0;
					} else {
						progress += speed;
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

		if (activity.length - 1 >= 0)
			System.arraycopy(activity, 1, activity, 0, activity.length - 1);

		activity[4] = isActive;

		if (isActive && !activity[0]) {
			world.setBlockState(getPos(), world.getBlockState(getPos()).with(BlockWithEntity.ACTIVE, true));
		} else if (!isActive && activity[0]) {
			world.setBlockState(getPos(), world.getBlockState(getPos()).with(BlockWithEntity.ACTIVE, false));
		}
	}

	public static class Primitive extends ElectricSmelterBlockEntity {
		public Primitive() {
			super(AstromineTechnologiesBlocks.PRIMITIVE_ELECTRIC_SMELTER, AstromineTechnologiesBlockEntityTypes.PRIMITIVE_ELECTRIC_SMELTER);
		}
	}

	public static class Basic extends ElectricSmelterBlockEntity {
		public Basic() {
			super(AstromineTechnologiesBlocks.BASIC_ELECTRIC_SMELTER, AstromineTechnologiesBlockEntityTypes.BASIC_ELECTRIC_SMELTER);
		}
	}

	public static class Advanced extends ElectricSmelterBlockEntity {
		public Advanced() {
			super(AstromineTechnologiesBlocks.ADVANCED_ELECTRIC_SMELTER, AstromineTechnologiesBlockEntityTypes.ADVANCED_ELECTRIC_SMELTER);
		}
	}

	public static class Elite extends ElectricSmelterBlockEntity {
		public Elite() {
			super(AstromineTechnologiesBlocks.ELITE_ELECTRIC_SMELTER, AstromineTechnologiesBlockEntityTypes.ELITE_ELECTRIC_SMELTER);
		}
	}
}
