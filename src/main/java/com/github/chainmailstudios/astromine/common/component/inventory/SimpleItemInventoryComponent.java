package com.github.chainmailstudios.astromine.common.component.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

import java.util.*;

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
	public int getItemSize() {
		return this.size;
	}

	@Override
	public List<Runnable> getItemListeners() {
		return this.listeners;
	}

	@Override
	public boolean canExtract(ItemStack stack) {
		return true;
	}

	@Override
	public void fromTag(CompoundTag compoundTag) {
		read(this, compoundTag, Optional.empty(), Optional.empty());
	}

	@Override
	public CompoundTag toTag(CompoundTag compoundTag) {
		write(this, compoundTag, Optional.empty(), Optional.empty());
		return compoundTag;
	}
}
