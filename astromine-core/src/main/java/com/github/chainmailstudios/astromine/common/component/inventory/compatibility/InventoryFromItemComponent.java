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

package com.github.chainmailstudios.astromine.common.component.inventory.compatibility;

import com.github.chainmailstudios.astromine.common.component.inventory.ItemComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

import static java.lang.Integer.min;

/**
 * An {@link Inventory} wrapped over an {@link ItemComponent}.
 */
public class InventoryFromItemComponent implements Inventory {
	ItemComponent itemComponent;

	/** Instantiates an {@link InventoryFromItemComponent} with the given value. */
	public InventoryFromItemComponent(ItemComponent itemComponent) {
		this.itemComponent = itemComponent;
	}

	/** Instantiates an {@link InventoryFromItemComponent} with the given value. */
	public static InventoryFromItemComponent of(ItemComponent itemComponent) {
		return new InventoryFromItemComponent(itemComponent);
	}

	/** Returns this inventory's size. */
	@Override
	public int size() {
		return itemComponent.getSize();
	}

	/** Returns the {@link ItemStack} at the given slot. */
	@Override
	public ItemStack getStack(int slot) {
		return itemComponent.getStack(slot);
	}

	/** Sets the {@link ItemStack} at the given slot to the specified value. */
	@Override
	public void setStack(int slot, ItemStack stack) {
		itemComponent.setStack(slot, stack);
	}

	/** Removes the {@link ItemStack} at the given slot,
	 * or a part of it as per the specified count, and returns it. */
	@Override
	public ItemStack removeStack(int slot, int count) {
		ItemStack removed = itemComponent.removeStack(slot);

		ItemStack returned = removed.copy();

		returned.setCount(min(count, removed.getCount()));

		removed.decrement(count);

		itemComponent.setStack(slot, removed);

		return returned;
	}

	/** Removes the {@link ItemStack} at the given slot, and returns it. */
	@Override
	public ItemStack removeStack(int slot) {
		return itemComponent.removeStack(slot);
	}

	/** Asserts whether this inventory's contents are all empty or not. */
	@Override
	public boolean isEmpty() {
		return itemComponent.isEmpty();
	}

	/** Clears this inventory's contents. */
	@Override
	public void clear() {
		itemComponent.clear();
	}

	/** Marks this inventory as dirt, or, pending update. */
	@Override
	public void markDirty() {
		itemComponent.updateListeners();
	}

	/** Allow the player to use this inventory by default. */
	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return true;
	}
}
