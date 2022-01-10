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

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

/**
 * A simple {@link Inventory} with helper
 * methods for a single stack.
 *
 * Originally by {@author Juuz}.
 */
public interface SingularStackInventory extends Inventory {
	/** Instantiates a {@link SingularStackInventory}. */
	static SingularStackInventory of(DefaultedList<ItemStack> items) {
		return new SingularStackInventoryImpl(items);
	}

	/** Returns this inventory's {@link ItemStack}s. */
	DefaultedList<ItemStack> getItems();

	/** Returns this inventory's size. */
	@Override
	default int size() {
		return 1;
	}

	/** Asserts whether this inventory's {@link ItemStack} is empty. */
	@Override
	default boolean isEmpty() {
		return getStack(0).isEmpty();
	}

	/** Returns the {@link ItemStack} at the given slot. */
	@Override
	default ItemStack getStack(int slot) {
		if (slot != 0) {
			throw new ArrayIndexOutOfBoundsException("Cannot access slot bigger than inventory size");
		} else {
			return getItems().get(0);
		}
	}

	/** Returns this inventory's {@link ItemStack}. */
	default ItemStack getStack() {
		return getStack(0);
	}

	/** Sets this inventory's {@link ItemStack} to the specified value. */
	default void setStack(ItemStack stack) {
		setStack(0, stack);
	}

	/** Removes the {@link ItemStack} at the given slot,
	 * or a part of it as per the specified count, and returns it. */
	@Override
	default ItemStack removeStack(int slot, int count) {
		if (slot != 0) {
			throw new ArrayIndexOutOfBoundsException("Cannot access slot bigger than inventory size");
		}
		
		var stack = getStack();

		if (count > stack.getCount()) {
			setStack(ItemStack.EMPTY);
			return stack;
		} else {
			stack.decrement(count);
			
			var copy = stack.copy();

			copy.setCount(count);

			return copy;
		}
	}

	/** Removes the {@link ItemStack} at the given slot
	 * and returns it. */
	@Override
	default ItemStack removeStack(int slot) {
		if (slot != 0) {
			throw new ArrayIndexOutOfBoundsException("Cannot access slot bigger than inventory size");
		}
		
		var stack = getStack();

		markDirty();

		return stack;
	}

	/** Removes this inventory's {@link ItemStack} and returns it. */
	default ItemStack removeStack() {
		return removeStack(0);
	}

	/** Sets the {@link ItemStack} at the given slot to the specified value.
	 * If the count is bigger than this inventory allows, it is set to the maximum allowed. */
	@Override
	default void setStack(int slot, ItemStack stack) {
		if (slot != 0) {
			throw new ArrayIndexOutOfBoundsException("Cannot access slot bigger than inventory size");
		}

		getItems().set(slot, stack);

		if (stack.getCount() > getMaxCountPerStack()) {
			stack.setCount(getMaxCountPerStack());
		}

		markDirty();
	}

	/** Clear this inventory's content. */
	@Override
	default void clear() {
		setStack(0, ItemStack.EMPTY);

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

	record SingularStackInventoryImpl(
			DefaultedList<ItemStack> items) implements SingularStackInventory {
		/**
		 * Returns this inventory's {@link ItemStack}s.
		 */
		@Override
		public DefaultedList<ItemStack> getItems() {
			return items;
		}

		/**
		 * Returns this inventory's string representation.
		 */
		@Override
		public String toString() {
			var slot = new AtomicInteger(0);

			return getItems().stream().map(stack -> String.format("%s - [%s]", slot.getAndIncrement(), stack.toString())).collect(Collectors.joining(", "));
		}
	}
}
