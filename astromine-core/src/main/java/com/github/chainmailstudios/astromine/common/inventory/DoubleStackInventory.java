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
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

/**
 * A simple {@code Inventory} implementation with only default methods + an item list getter.
 * <p>
 * Originally by Juuz
 */
public interface DoubleStackInventory extends Inventory {
	/**
	 * Creates an inventory from the item list.
	 */
	static DoubleStackInventory of(DefaultedList<ItemStack> items) {
		return () -> items;
	}
	// Creation

	/**
	 * Creates a new inventory with the size.
	 */
	static DoubleStackInventory ofSize(int size) {
		return of(DefaultedList.ofSize(size, ItemStack.EMPTY));
	}

	/**
	 * Gets the item list of this inventory. Must return the same instance every time it's called.
	 */
	DefaultedList<ItemStack> getItems();
	// Inventory

	/**
	 * Returns the inventory size.
	 */
	@Override
	default int size() {
		return getItems().size();
	}

	/**
	 * @return true if this inventory has only empty stacks, false otherwise
	 */
	default boolean isInvEmpty() {
		for (int i = 0; i < size(); i++) {
			ItemStack stack = getStack(i);
			if (!stack.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	default boolean isEmpty() {
		return isInvEmpty();
	}

	/**
	 * Gets the item in the slot.
	 */
	@Override
	default ItemStack getStack(int slot) {
		return getItems().get(slot);
	}

	default ItemStack getLeftStack() {
		return getStack(0);
	}

	default void setLeftStack(ItemStack stack) {
		setStack(0, stack);
	}

	default ItemStack getRightStack() {
		return getStack(1);
	}

	default void setRightStack(ItemStack stack) {
		setStack(1, stack);
	}

	/**
	 * Takes a stack of the size from the slot.
	 * <p>
	 * (default implementation) If there are less items in the slot than what are requested, takes all items in that
	 * slot.
	 */
	@Override
	default ItemStack removeStack(int slot, int count) {
		ItemStack result = Inventories.splitStack(getItems(), slot, count);
		if (!result.isEmpty()) {
			markDirty();
		}
		return result;
	}

	/**
	 * Removes the current stack in the {@code slot} and returns it.
	 */
	@Override
	default ItemStack removeStack(int slot) {
		ItemStack stack = Inventories.removeStack(getItems(), slot);
		markDirty();
		return stack;
	}

	default ItemStack removeLeftStack() {
		return removeStack(0);
	}

	default ItemStack removeRightStack() {
		return removeStack(1);
	}

	/**
	 * Replaces the current stack in the {@code slot} with the provided stack.
	 * <p>
	 * If the stack is too big for this inventory ({@link Inventory#getMaxCountPerStack()}), it gets resized to this
	 * inventory's maximum amount.
	 */
	@Override
	default void setStack(int slot, ItemStack stack) {
		getItems().set(slot, stack);
		if (stack.getCount() > getMaxCountPerStack()) {
			stack.setCount(getMaxCountPerStack());
		}
		markDirty();
	}

	/**
	 * Clears {@linkplain #getItems() the item list}}.
	 */
	@Override
	default void clear() {
		getItems().clear();
		markDirty();
	}

	@Override
	default void markDirty() {
		// Override if you want behavior.
	}

	@Override
	default boolean canPlayerUse(PlayerEntity player) {
		return true;
	}
}
