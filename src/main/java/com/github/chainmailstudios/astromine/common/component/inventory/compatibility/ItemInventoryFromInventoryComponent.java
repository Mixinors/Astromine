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

import com.github.chainmailstudios.astromine.common.component.inventory.ItemInventoryComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.TypedActionResult;

/**
* Vanilla wrapper for an InventoryComponent.
*/
public interface ItemInventoryFromInventoryComponent extends Inventory {
	/**
	* Builds an wrapper over the given component for vanilla Inventory usage.
	*
	* @return the requested wrapper.
	*/
	static ItemInventoryFromInventoryComponent of(ItemInventoryComponent component) {
		return () -> component;
	}

	/**
	* Retrieves the inventory's size.
	*
	* @return the requested size.
	*/
	@Override
	default int size() {
		return this.getComponent().getItemSize();
	}

	/**
	* Retrieves the InventoryComponent this wrapper is wrapping.
	*
	* @return the requested component.
	*/
	ItemInventoryComponent getComponent();

	/**
	* Asserts whether inventory is empty or not.
	*
	* @return true if empty; false if not.
	*/
	@Override
	default boolean isEmpty() {
		return this.getComponent().isEmpty();
	}

	/**
	* Retrieves the ItemStack in the specified slot.
	*
	* @param slot the specified slot.
	* @return the requested ItemStack.
	*/
	@Override
	default ItemStack getStack(int slot) {
		return this.getComponent().getStack(slot);
	}

	/**
	* Extracts an ItemStack from the specified slot, the count extracted depending on the specified count.
	*
	* @param slot  the specified slot.
	* @param count the specified count.
	* @return the requested ItemStack.
	*/
	@Override
	default ItemStack removeStack(int slot, int count) {
		if (this.getComponent().getStack(slot).getCount() < count) {
			TypedActionResult<ItemStack> result = this.getComponent().extract(null, slot);
			if (!result.getValue().isEmpty()) {
				this.markDirty();
			}
			return result.getValue();
		} else {
			TypedActionResult<ItemStack> result = this.getComponent().extract(slot, count);
			if (!result.getValue().isEmpty()) {
				this.markDirty();
			}
			return result.getValue();
		}
	}

	/**
	* Removes and retrieves the ItemStack from the specified slot.
	*
	* @param slot the specified slot.
	* @return the retrieved ItemStack.
	*/
	@Override
	default ItemStack removeStack(int slot) {
		return this.getComponent().extract(null, slot).getValue();
	}

	/**
	* Overrides the ItemStack in the specified slot with the specified stack.
	*
	* @param slot  the specified slot.
	* @param stack the specified stack.
	*/
	@Override
	default void setStack(int slot, ItemStack stack) {
		if (this.getComponent().getMaximumCount(slot) < stack.getCount()) {
			stack.setCount(this.getComponent().getMaximumCount(slot));
		}
		this.getComponent().setStack(slot, stack);
	}

	/**
	* Dispatches updates to inventory listeners.
	*/
	@Override
	default void markDirty() {
		this.getComponent().dispatchConsumers();
	}

	/**
	* Asserts whether the specified player can access this inventory.
	*
	* @param player the specified player.
	* @return true if yes; false if no.
	*/
	@Override
	default boolean canPlayerUse(PlayerEntity player) {
		return true;
	}

	@Override
	default boolean isValid(int slot, ItemStack stack) {
		return this.getComponent().canInsert(null, stack, slot);
	}

	/**
	* Clears this inventory.
	*/
	@Override
	default void clear() {
		this.getComponent().clear();
	}
}

