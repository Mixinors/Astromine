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

package com.github.chainmailstudios.astromine.common.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryChangedListener;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.util.collection.DefaultedList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * A standard implementation of an {@link Inventory}.
 */
public class BaseInventory implements Inventory, RecipeInputProvider {
	private final int size;

	private final DefaultedList<ItemStack> stacks;

	private final List<InventoryChangedListener> listeners = new ArrayList<>();

	/** Instantiates a {@link BaseInventory}. */
	private BaseInventory(int size) {
		this.size = size;
		this.stacks = DefaultedList.ofSize(size, ItemStack.EMPTY);
	}

	/** Instantiates a {@link BaseInventory}. */
	private BaseInventory(ItemStack... items) {
		this.size = items.length;
		this.stacks = DefaultedList.copyOf(ItemStack.EMPTY, items);
	}

	/** Instantiates a {@link BaseInventory}. */
	public static BaseInventory of(int size) {
		return new BaseInventory(size);
	}

	/** Instantiates a {@link BaseInventory}. */
	public static BaseInventory of(ItemStack... items) {
		return new BaseInventory(items);
	}

	/** Adds an {@link InventoryChangedListener} to this inventory. */
	public void addListener(InventoryChangedListener... listeners) {
		this.listeners.addAll(Arrays.asList(listeners));
	}

	/** Removes an {@link InventoryChangedListener} from this inventory. */
	public void removeListener(InventoryChangedListener... listeners) {
		this.listeners.removeAll(Arrays.asList(listeners));
	}

	/** Returns this inventory's size. */
	@Override
	public int size() {
		return this.size;
	}

	/** Asserts whether this inventory's {@link ItemStack}s are all empty or not. */
	@Override
	public boolean isEmpty() {
		return this.stacks.stream().allMatch(ItemStack::isEmpty);
	}

	/** Returns the {@link ItemStack} at the given slot. */
	@Override
	public ItemStack getStack(int slot) {
		if (slot >= 0 && slot < size) {
			return stacks.get(slot);
		} else {
			throw new ArrayIndexOutOfBoundsException("Cannot access slot bigger than inventory size");
		}
	}

	/** Sets the {@link ItemStack} at the given slot to the specified value.
	 * If the count is bigger than this inventory allows, it is set to the maximum allowed. */
	@Override
	public void setStack(int slot, ItemStack stack) {
		stacks.set(slot, stack);

		if (stack.getCount() > getMaxCountPerStack()) {
			stack.setCount(getMaxCountPerStack());
		}

		markDirty();
	}

	/** Removes the {@link ItemStack} at the given slot,
	 * or a part of it as per the specified count, and returns it. */
	@Override
	public ItemStack removeStack(int slot, int amount) {
		ItemStack stack = Inventories.splitStack(this.stacks, slot, amount);

		if (!stack.isEmpty()) {
			this.markDirty();
		}

		return stack;
	}

	/** Removes the {@link ItemStack} at the given slot
	 * and returns it. */
	@Override
	public ItemStack removeStack(int slot) {
		ItemStack stack = stacks.get(slot);

		if (stack.isEmpty()) {
			return ItemStack.EMPTY;
		} else {
			stacks.set(slot, ItemStack.EMPTY);

			markDirty();

			return stack;
		}
	}

	/** Triggers this inventory's {@link InventoryChangedListener}s. */
	@Override
	public void markDirty() {
		for (InventoryChangedListener listener : listeners) {
			listener.onInventoryChanged(this);
		}
	}

	/** Allow the player to use this inventory by default. */
	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return true;
	}

	/** Clear this inventory's contents. */
	@Override
	public void clear() {
		for (int slot = 0; slot < size; ++slot) {
			setStack(slot, ItemStack.EMPTY);
		}

		markDirty();
	}

	/** Override behavior to do nothing. */
	@Override
	public void provideRecipeInputs(RecipeFinder recipeFinder) {}

	/** Returns this inventory's string representation. */
	public String toString() {
		AtomicInteger slot = new AtomicInteger(0);

		return stacks.stream().map(stack -> String.format("%s - [%s]", slot.getAndIncrement(), stack.toString())).collect(Collectors.joining(", "));
	}
}
