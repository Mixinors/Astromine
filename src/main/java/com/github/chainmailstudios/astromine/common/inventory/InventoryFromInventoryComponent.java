package com.github.chainmailstudios.astromine.common.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.TypedActionResult;

/**
 * Vanilla wrapper for an InventoryComponent.
 */
public interface InventoryFromInventoryComponent extends Inventory {
	/**
	 * Retrieves the InventoryComponent this wrapper
	 * is wrapping.
	 *
	 * @return the requested component.
	 */
	InventoryComponent getComponent();

	/**
	 * Builds an wrapper over the given component
	 * for vanilla Inventory usage.
	 *
	 * @return the requested wrapper.
	 */
	static InventoryFromInventoryComponent of(InventoryComponent component) {
		return () -> component;
	}

	/**
	 * Retrieves the inventory's size.
	 *
	 * @return the requested size.
	 */
	@Override
	default int size() {
		return getComponent().getSize();
	}

	/**
	 * Asserts whether inventory is empty or not.
	 *
	 * @return true if empty; false if not.
	 */
	@Override
	default boolean isEmpty() {
		return getComponent().isEmpty();
	}

	/**
	 * Retrieves the ItemStack in the specified slot.
	 *
	 * @param slot the specified slot.
	 * @return the requested ItemStack.
	 */
	@Override
	default ItemStack getStack(int slot) {
		return getComponent().getStack(slot);
	}

	/**
	 * Extracts an ItemStack from the specified slot,
	 * the count extracted depending on the specified
	 * count.
	 *
	 * @param slot  the specified slot.
	 * @param count the specified count.
	 * @return the requested ItemStack.
	 */
	@Override
	default ItemStack removeStack(int slot, int count) {
		if (getComponent().getStack(slot).getCount() < count) {
			TypedActionResult<ItemStack> result = getComponent().extract(slot);
			if (!result.getValue().isEmpty()) markDirty();
			return result.getValue();
		} else {
			TypedActionResult<ItemStack> result = getComponent().extract(slot, count);
			if (!result.getValue().isEmpty()) markDirty();
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
		return getComponent().extract(slot).getValue();
	}

	/**
	 * Overrides the ItemStack in the specified slot
	 * with the specified stack.
	 *
	 * @param slot  the specified slot.
	 * @param stack the specified stack.
	 */
	@Override
	default void setStack(int slot, ItemStack stack) {
		if (getComponent().getMaximumCount(slot) < stack.getCount()) {
			stack.setCount(getComponent().getMaximumCount(slot));
		}
		getComponent().setStack(slot, stack);
	}

	/**
	 * Clears this inventory.
	 */
	@Override
	default void clear() {
		getComponent().clear();
	}

	/**
	 * Dispatches updates to inventory listeners.
	 */
	@Override
	default void markDirty() {
		getComponent().dispatchConsumers();
	}

	/**
	 * Asserts whether the specified player
	 * can access this inventory.
	 *
	 * @param player the specified player.
	 * @return true if yes; false if no.
	 */
	@Override
	default boolean canPlayerUse(PlayerEntity player) {
		return true;
	}
}

