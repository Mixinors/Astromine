package com.github.chainmailstudios.astromine.common.component.compatibility;

import com.github.chainmailstudios.astromine.common.component.ItemInventoryComponent;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;

import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * An InventoryComponentFromInventory is a wrapper over an Inventory that provides the functions and utilities of an InventoryComponent.
 */
public class ItemInventoryComponentFromItemInventory implements ItemInventoryComponent {
	Inventory inventory;
	List<Runnable> listeners = new ArrayList<>();

	private ItemInventoryComponentFromItemInventory(Inventory inventory) {
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
	public ActionResult canInsert() {
		return ActionResult.SUCCESS;
	}

	@Override
	public ActionResult canExtract() {
		return ActionResult.SUCCESS;
	}

	@Override
	public ActionResult canInsert(int slot) {
		return ActionResult.SUCCESS;
	}

	@Override
	public ActionResult canExtract(int slot) {
		return ActionResult.SUCCESS;
	}

	@Override
	public ActionResult canInsert(ItemStack stack, int slot) {
		return ActionResult.SUCCESS;
	}

	@Override
	public ActionResult canExtract(ItemStack stack, int slot) {
		return ActionResult.SUCCESS;
	}

	@Override
	public ActionResult canInsert(ItemStack stack) {
		return ActionResult.SUCCESS;
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

	@Override
	public ActionResult canExtract(ItemStack stack) {
		return ActionResult.SUCCESS;
	}
}
