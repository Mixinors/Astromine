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

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * A simple {@link Container} with helper methods
 * for a left and a right stack.
 *
 * Originally by {@author Juuz}.
 */
public interface DoubleStackInventory extends Container {
	/** Instantiates a {@link DoubleStackInventory}. */
	static DoubleStackInventory of(NonNullList<ItemStack> items) {
		return new DoubleStackInventoryImpl(items);
	}

	/** Instantiates a {@link DoubleStackInventory}. */
	static DoubleStackInventory ofSize(int size) {
		return of(NonNullList.withSize(size, ItemStack.EMPTY));
	}

	/** Returns this inventory's {@link ItemStack}s. */
	NonNullList<ItemStack> getItems();

	/** Returns this inventory's size. */
	@Override
	default int getContainerSize() {
		return getItems().size();
	}

	/** Asserts whether this inventory's {@link ItemStack}s are all empty or not. */
	@Override
	default boolean isEmpty() {
		for (int i = 0; i < getContainerSize(); i++) {
			ItemStack stack = getItem(i);
			if (!stack.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	/** Returns the {@link ItemStack} at the given slot. */
	@Override
	default ItemStack getItem(int slot) {
		return getItems().get(slot);
	}

	/** Sets the {@link ItemStack} at the given slot to the specified value.
	 * If the count is bigger than this inventory allows, it is set to the maximum allowed. */
	@Override
	default void setItem(int slot, ItemStack stack) {
		getItems().set(slot, stack);

		if (stack.getCount() > getMaxStackSize()) {
			stack.setCount(getMaxStackSize());
		}

		setChanged();
	}
	
	/** Returns the {@link ItemStack} at the first slot. */
	default ItemStack getFirst() {
		return getItem(0);
	}

	/** Sets the {@link ItemStack} at the first slot to the specified value. */
	default void setFirst(ItemStack stack) {
		setItem(0, stack);
	}

	/** Returns the {@link ItemStack} at the second slot. */
	default ItemStack getSecond() {
		return getItem(1);
	}

	/** Sets the {@link ItemStack} at the second slot to the specified value. */
	default void setSecond(ItemStack stack) {
		setItem(1, stack);
	}

	/** Removes the {@link ItemStack} at the given slot,
	 * or a part of it as per the specified count, and returns it. */
	@Override
	default ItemStack removeItem(int slot, int count) {
		ItemStack result = ContainerHelper.removeItem(getItems(), slot, count);
		if (!result.isEmpty()) {
			setChanged();
		}
		return result;
	}

	/** Removes the {@link ItemStack} at the given slot
	 * and returns it. */
	@Override
	default ItemStack removeItemNoUpdate(int slot) {
		ItemStack stack = ContainerHelper.takeItem(getItems(), slot);
		setChanged();
		return stack;
	}

	/** Removes the {@link ItemStack} at the first slot. */
	default ItemStack removeLeftStack() {
		return removeItemNoUpdate(0);
	}

	/** Removes the {@link ItemStack} at the second slot. */
	default ItemStack removeRightStack() {
		return removeItemNoUpdate(1);
	}

	/**
	 * Clears {@linkplain #getItems() the item list}}.
	 */
	@Override
	default void clearContent() {
		getItems().clear();
		setChanged();
	}

	/** Override to do nothing. */
	@Override
	default void setChanged() {}

	/** Allow the player to use this inventory by default. */
	@Override
	default boolean stillValid(Player player) {
		return true;
	}

	class DoubleStackInventoryImpl implements DoubleStackInventory {
		private final NonNullList<ItemStack> items;

		/** Instantiates a {@link DoubleStackInventoryImpl}. */
		private DoubleStackInventoryImpl(NonNullList<ItemStack> items) {
			this.items = items;
		}

		/** Returns this inventory's {@link ItemStack}s. */
		@Override
		public NonNullList<ItemStack> getItems() {
			return items;
		}

		/** Returns this inventory's string representation. */
		@Override
		public String toString() {
			AtomicInteger slot = new AtomicInteger(0);

			return getItems().stream().map(stack -> String.format("%s - [%s]", slot.getAndIncrement(), stack.toString())).collect(Collectors.joining(", "));
		}
	}
}
