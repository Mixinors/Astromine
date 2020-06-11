package com.github.chainmailstudios.astromine.common.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Simple implementation of an InventoryComponent for usage
 * anywhere one is required.
 * Size is immutable and therefore defined on instantiation.
 */
public class SimpleInventoryComponent implements InventoryComponent {
	private final HashMap<Integer, ItemStack> contents = new HashMap<Integer, ItemStack>() {
		@Override
		public ItemStack get(Object key) {
			return super.getOrDefault(key, ItemStack.EMPTY);
		}
	};

	private final List<Runnable> listeners = new ArrayList<>();

	private final int size;

	public SimpleInventoryComponent(int size) {
		this.size = size;
	}

	@Override
	public int getSize() {
		return size;
	}

	@Override
	public List<Runnable> getListeners() {
		return listeners;
	}

	@Override
	public AbstractMap<Integer, ItemStack> getContents() {
		return contents;
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
	public ActionResult canInsert() {
		return ActionResult.SUCCESS;
	}

	@Override
	public ActionResult canExtract() {
		return ActionResult.SUCCESS;
	}

	@Override
	public ActionResult canInsert(ItemStack stack) {
		return ActionResult.SUCCESS;
	}

	@Override
	public ActionResult canExtract(ItemStack stack) {
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
}
