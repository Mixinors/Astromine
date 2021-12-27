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

package com.github.mixinors.astromine.common.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * A simple {@link Inventory} with helper methods
 * for a left and a right stack.
 *
 * Originally by {@author Juuz}.
 */
public interface DoubleStackInventory extends Inventory {
	/** Instantiates a {@link DoubleStackInventory}. */
	static DoubleStackInventory of(DefaultedList<ItemStack> items) {
		return new DoubleStackInventoryImpl(items);
	}

	/** Instantiates a {@link DoubleStackInventory}. */
	static DoubleStackInventory ofSize(int size) {
		return of(DefaultedList.ofSize(size, ItemStack.EMPTY));
	}

	/** Returns this inventory's {@link ItemStack}s. */
	DefaultedList<ItemStack> getItems();

	/** Returns this inventory's size. */
	@Override
	default int size() {
		return getItems().size();
	}

	/** Asserts whether this inventory's {@link ItemStack}s are all empty or not. */
	@Override
	default boolean isEmpty() {
		for (var i = 0; i < size(); i++) {
			var stack = getStack(i);
			if (!stack.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	/** Returns the {@link ItemStack} at the given slot. */
	@Override
	default ItemStack getStack(int slot) {
		return getItems().get(slot);
	}

	/** Sets the {@link ItemStack} at the given slot to the specified value.
	 * If the count is bigger than this inventory allows, it is set to the maximum allowed. */
	@Override
	default void setStack(int slot, ItemStack stack) {
		getItems().set(slot, stack);

		if (stack.getCount() > getMaxCountPerStack()) {
			stack.setCount(getMaxCountPerStack());
		}

		markDirty();
	}
	
	/** Returns the {@link ItemStack} at the first slot. */
	default ItemStack getFirst() {
		return getStack(0);
	}

	/** Sets the {@link ItemStack} at the first slot to the specified value. */
	default void setFirst(ItemStack stack) {
		setStack(0, stack);
	}

	/** Returns the {@link ItemStack} at the second slot. */
	default ItemStack getSecond() {
		return getStack(1);
	}

	/** Sets the {@link ItemStack} at the second slot to the specified value. */
	default void setSecond(ItemStack stack) {
		setStack(1, stack);
	}

	/** Removes the {@link ItemStack} at the given slot,
	 * or a part of it as per the specified count, and returns it. */
	@Override
	default ItemStack removeStack(int slot, int count) {
		var result = Inventories.splitStack(getItems(), slot, count);
		if (!result.isEmpty()) {
			markDirty();
		}
		return result;
	}

	/** Removes the {@link ItemStack} at the given slot
	 * and returns it. */
	@Override
	default ItemStack removeStack(int slot) {
		var stack = Inventories.removeStack(getItems(), slot);
		markDirty();
		return stack;
	}

	/** Removes the {@link ItemStack} at the first slot. */
	default ItemStack removeLeftStack() {
		return removeStack(0);
	}

	/** Removes the {@link ItemStack} at the second slot. */
	default ItemStack removeRightStack() {
		return removeStack(1);
	}

	/**
	 * Clears {@linkplain #getItems() the item list}}.
	 */
	@Override
	default void clear() {
		getItems().clear();
		markDirty();
	}

	/** Override to do nothing. */
	@Override
	default void markDirty() {}

	/** Allow the player to use this inventory by default. */
	@Override
	default boolean canPlayerUse(PlayerEntity player) {
		return true;
	}

	class DoubleStackInventoryImpl implements DoubleStackInventory {
		private final DefaultedList<ItemStack> items;

		/** Instantiates a {@link DoubleStackInventoryImpl}. */
		private DoubleStackInventoryImpl(DefaultedList<ItemStack> items) {
			this.items = items;
		}

		/** Returns this inventory's {@link ItemStack}s. */
		@Override
		public DefaultedList<ItemStack> getItems() {
			return items;
		}

		/** Returns this inventory's string representation. */
		@Override
		public String toString() {
			var slot = new AtomicInteger(0);

			return getItems().stream().map(stack -> String.format("%s - [%s]", slot.getAndIncrement(), stack.toString())).collect(Collectors.joining(", "));
		}
	}
}
