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

import com.github.chainmailstudios.astromine.common.block.AlloySmelterBlock;
import com.github.chainmailstudios.astromine.common.block.base.DefaultedBlockWithEntity;
import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedEnergyItemBlockEntity;
import com.github.chainmailstudios.astromine.common.component.inventory.ItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.recipe.AlloySmeltingRecipe;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;
import spinnery.common.inventory.BaseInventory;

import java.util.Optional;

public abstract class AlloySmelterBlockEntity extends DefaultedEnergyItemBlockEntity implements Tickable {
	public double progress = 0;
	public int limit = 100;

	public boolean shouldTry = true;
	public boolean isActive = false;

	public boolean[] activity = {false, false, false, false, false};

	Optional<AlloySmeltingRecipe> recipe = Optional.empty();

	public AlloySmelterBlockEntity(BlockEntityType<?> type) {
		super(type);

		addEnergyListener(() -> shouldTry = true);
	}

	@Override
	protected ItemInventoryComponent createItemComponent() {
		return new SimpleItemInventoryComponent(3).withInsertPredicate((direction, itemStack, slot) -> {
			return slot == 0 || slot == 1;
		}).withExtractPredicate(((direction, stack, slot) -> {
			return slot == 2;
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
			BaseInventory inputInventory = new BaseInventory(2);
			inputInventory.setStack(0, itemComponent.getStack(0));
			inputInventory.setStack(1, itemComponent.getStack(1));
			if (!recipe.isPresent()) {
				if (hasWorld() && !world.isClient) {
					recipe = world.getRecipeManager().getFirstMatch(AlloySmeltingRecipe.Type.INSTANCE, inputInventory, world);
				}
			}
			if (recipe.isPresent() && recipe.get().matches(inputInventory, world)) {
				limit = recipe.get().getTime() * 2;

				ItemStack output = recipe.get().getOutput().copy();

				boolean isEmpty = itemComponent.getStack(2).isEmpty();
				boolean isEqual = ItemStack.areItemsEqual(itemComponent.getStack(2), output) && ItemStack.areTagsEqual(itemComponent.getStack(2), output);

				if (asEnergy().use(recipe.get().getEnergyConsumed()) && (isEmpty || isEqual) && itemComponent.getStack(2).getCount() + output.getCount() <= itemComponent.getStack(2).getMaxCount()) {
					if (progress == limit) {
						ItemStack stack1 = itemComponent.getStack(0);
						ItemStack stack2 = itemComponent.getStack(1);
						if (recipe.get().getFirstInput().test(stack1) || recipe.get().getSecondInput().test(stack2)) {
							stack1.decrement(recipe.get().getFirstInput().testMatching(stack1).getCount());
							stack2.decrement(recipe.get().getSecondInput().testMatching(stack2).getCount());
						} else if (recipe.get().getFirstInput().test(stack2) || recipe.get().getSecondInput().test(stack1)) {
							stack2.decrement(recipe.get().getFirstInput().testMatching(stack2).getCount());
							stack1.decrement(recipe.get().getSecondInput().testMatching(stack1).getCount());
						}

						if (isEmpty) {
							itemComponent.setStack(2, output);
						} else {
							itemComponent.getStack(2).increment(output.getCount());
						}

						progress = 0;
					} else {
						progress += 1 * ((AlloySmelterBlock) this.getCachedState().getBlock()).getMachineSpeed();
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

	public static class Primitive extends AlloySmelterBlockEntity {
		public Primitive() {
			super(AstromineBlockEntityTypes.PRIMITIVE_ALLOY_SMELTER);
		}

		@Override
		protected double getEnergySize() {
			return AstromineConfig.get().primitiveAlloySmelterEnergy;
		}
	}

	public static class Basic extends AlloySmelterBlockEntity {
		public Basic() {
			super(AstromineBlockEntityTypes.BASIC_ALLOY_SMELTER);
		}

		@Override
		protected double getEnergySize() {
			return AstromineConfig.get().basicAlloySmelterEnergy;
		}
	}

	public static class Advanced extends AlloySmelterBlockEntity {
		public Advanced() {
			super(AstromineBlockEntityTypes.ADVANCED_ALLOY_SMELTER);
		}

		@Override
		protected double getEnergySize() {
			return AstromineConfig.get().advancedAlloySmelterEnergy;
		}
	}

	public static class Elite extends AlloySmelterBlockEntity {
		public Elite() {
			super(AstromineBlockEntityTypes.ELITE_ALLOY_SMELTER);
		}

		@Override
		protected double getEnergySize() {
			return AstromineConfig.get().eliteAlloySmelterEnergy;
		}
	}
}
