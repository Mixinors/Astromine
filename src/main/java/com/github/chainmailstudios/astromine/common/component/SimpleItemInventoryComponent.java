package com.github.chainmailstudios.astromine.common.component;

import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;

import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Simple implementation of an InventoryComponent for usage anywhere one is required. Size is immutable and therefore defined on instantiation.
 */
public class SimpleItemInventoryComponent implements ItemInventoryComponent {
	private final HashMap<Integer, ItemStack> contents = new HashMap<Integer, ItemStack>() {
		@Override
		public ItemStack get(Object key) {
			return super.getOrDefault(key, ItemStack.EMPTY);
		}
	};

	private final List<Runnable> listeners = new ArrayList<>();

	private final int size;

	public SimpleItemInventoryComponent(int size) {
		this.size = size;
		for (int i = 0; i < size; ++i) {
			contents.put(i, ItemStack.EMPTY);
		}
	}

	@Override
	public Map<Integer, ItemStack> getItemContents() {
		return this.contents;
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
	public int getItemSize() {
		return this.size;
	}

	@Override
	public List<Runnable> getItemListeners() {
		return this.listeners;
	}

	@Override
	public ActionResult canExtract(ItemStack stack) {
		return ActionResult.SUCCESS;
	}
}
