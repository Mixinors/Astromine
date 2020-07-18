package com.github.chainmailstudios.astromine.common.component.inventory.compatibility;

import com.github.chainmailstudios.astromine.common.component.inventory.SimpleItemInventoryComponent;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An InventoryComponentFromInventory is a wrapper over an Inventory that provides the functions and utilities of an InventoryComponent.
 */
public class ItemInventoryComponentFromItemInventory extends SimpleItemInventoryComponent {
	Inventory inventory;
	List<Runnable> listeners = new ArrayList<>();

	private ItemInventoryComponentFromItemInventory(Inventory inventory) {
		super(inventory.size());
		this.inventory = inventory;
	}

	public static ItemInventoryComponentFromItemInventory of(Inventory inventory) {
		return new ItemInventoryComponentFromItemInventory(inventory);
	}

	@Override
	public Map<Integer, ItemStack> getItemContents() {
		HashMap<Integer, ItemStack> contents = new HashMap<>();
		for (int i = 0; i < this.inventory.size(); ++i) {
			contents.put(i, this.inventory.getStack(i));
		}
		return contents;
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
		this.inventory.setStack(slot, stack);
	}

	@Override
	public int getItemSize() {
		return this.inventory.size();
	}

	@Override
	public List<Runnable> getItemListeners() {
		return this.listeners;
	}

	@Override
	public ItemStack getStack(int slot) {
		return this.inventory.getStack(slot);
	}
}
