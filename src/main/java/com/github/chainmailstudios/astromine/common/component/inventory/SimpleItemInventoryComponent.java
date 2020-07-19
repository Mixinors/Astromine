package com.github.chainmailstudios.astromine.common.component.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import vazkii.patchouli.api.TriPredicate;

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
	private TriPredicate<@Nullable Direction, ItemStack, Integer> insertPredicate = (direction, itemStack, slot) -> true;

	private int size;

	public SimpleItemInventoryComponent() {
		this(0);
	}

	public SimpleItemInventoryComponent(int size) {
		this.size = size;
		for (int i = 0; i < size; ++i) {
			contents.put(i, ItemStack.EMPTY);
		}
	}

	public void resize(int size) {
		this.size = size;
		for (int i = 0; i < size; ++i) {
			contents.putIfAbsent(i, ItemStack.EMPTY);
		}
	}

	@Override
	public boolean canInsert(@Nullable Direction direction, ItemStack stack, int slot) {
		return insertPredicate.test(direction, stack, slot);
	}

	public SimpleItemInventoryComponent withInsertPredicate(TriPredicate<@Nullable Direction, ItemStack, Integer> predicate) {
		TriPredicate<Direction, ItemStack, Integer> triPredicate = this.insertPredicate;
		this.insertPredicate = (direction, itemStack, integer) -> triPredicate.test(direction, itemStack, integer) && predicate.test(direction, itemStack, integer);
		return this;
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
	public void fromTag(CompoundTag compoundTag) {
		read(this, compoundTag, Optional.empty(), Optional.empty());
	}

	@Override
	public CompoundTag toTag(CompoundTag compoundTag) {
		write(this, compoundTag, Optional.empty(), Optional.empty());
		return compoundTag;
	}
}
