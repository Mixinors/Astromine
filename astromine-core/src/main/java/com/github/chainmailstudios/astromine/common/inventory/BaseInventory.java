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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;

/**
 * A standard implementation of an {@link Container}.
 */
public class BaseInventory implements Container, StackedContentsCompatible {
	private final int size;

	private final NonNullList<ItemStack> stacks;

	private final List<ContainerListener> listeners = new ArrayList<>();

	/** Instantiates a {@link BaseInventory}. */
	private BaseInventory(int size) {
		this.size = size;
		this.stacks = NonNullList.withSize(size, ItemStack.EMPTY);
	}

	/** Instantiates a {@link BaseInventory}. */
	private BaseInventory(ItemStack... items) {
		this.size = items.length;
		this.stacks = NonNullList.of(ItemStack.EMPTY, items);
	}

	/** Instantiates a {@link BaseInventory}. */
	public static BaseInventory of(int size) {
		return new BaseInventory(size);
	}

	/** Instantiates a {@link BaseInventory}. */
	public static BaseInventory of(ItemStack... items) {
		return new BaseInventory(items);
	}

	/** Adds an {@link ContainerListener} to this inventory. */
	public void addListener(ContainerListener... listeners) {
		this.listeners.addAll(Arrays.asList(listeners));
	}

	/** Removes an {@link ContainerListener} from this inventory. */
	public void removeListener(ContainerListener... listeners) {
		this.listeners.removeAll(Arrays.asList(listeners));
	}

	/** Returns this inventory's size. */
	@Override
	public int getContainerSize() {
		return this.size;
	}

	/** Asserts whether this inventory's {@link ItemStack}s are all empty or not. */
	@Override
	public boolean isEmpty() {
		return this.stacks.stream().allMatch(ItemStack::isEmpty);
	}

	/** Returns the {@link ItemStack} at the given slot. */
	@Override
	public ItemStack getItem(int slot) {
		if (slot >= 0 && slot < size) {
			return stacks.get(slot);
		} else {
			throw new ArrayIndexOutOfBoundsException("Cannot access slot bigger than inventory size");
		}
	}

	/** Sets the {@link ItemStack} at the given slot to the specified value.
	 * If the count is bigger than this inventory allows, it is set to the maximum allowed. */
	@Override
	public void setItem(int slot, ItemStack stack) {
		stacks.set(slot, stack);

		if (stack.getCount() > getMaxStackSize()) {
			stack.setCount(getMaxStackSize());
		}

		setChanged();
	}

	/** Removes the {@link ItemStack} at the given slot,
	 * or a part of it as per the specified count, and returns it. */
	@Override
	public ItemStack removeItem(int slot, int amount) {
		ItemStack stack = ContainerHelper.removeItem(this.stacks, slot, amount);

		if (!stack.isEmpty()) {
			this.setChanged();
		}

		return stack;
	}

	/** Removes the {@link ItemStack} at the given slot
	 * and returns it. */
	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		ItemStack stack = stacks.get(slot);

		if (stack.isEmpty()) {
			return ItemStack.EMPTY;
		} else {
			stacks.set(slot, ItemStack.EMPTY);

			setChanged();

			return stack;
		}
	}

	/** Triggers this inventory's {@link ContainerListener}s. */
	@Override
	public void setChanged() {
		for (ContainerListener listener : listeners) {
			listener.containerChanged(this);
		}
	}

	/** Allow the player to use this inventory by default. */
	@Override
	public boolean stillValid(Player player) {
		return true;
	}

	/** Clear this inventory's contents. */
	@Override
	public void clearContent() {
		for (int slot = 0; slot < size; ++slot) {
			setItem(slot, ItemStack.EMPTY);
		}

		setChanged();
	}

	/** Override behavior to do nothing. */
	@Override
	public void fillStackedContents(StackedContents recipeFinder) {}

	/** Returns this inventory's string representation. */
	public String toString() {
		AtomicInteger slot = new AtomicInteger(0);

		return stacks.stream().map(stack -> String.format("%s - [%s]", slot.getAndIncrement(), stack.toString())).collect(Collectors.joining(", "));
	}
}
