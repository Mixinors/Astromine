package com.github.mixinors.astromine.common.transfer.storage;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.item.base.SingleStackStorage;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public class SimpleItemVariantStorage extends SingleStackStorage {
	private final Inventory inventory;
	private final int slot;
	
	public SimpleItemVariantStorage(Inventory inventory, int slot) {
		this.inventory = inventory;
		this.slot = slot;
	}
	
	@Override
	protected ItemStack getStack() {
		return inventory.getStack(slot);
	}
	
	@Override
	protected void setStack(ItemStack stack) {
		inventory.setStack(slot, stack);
	}
}
