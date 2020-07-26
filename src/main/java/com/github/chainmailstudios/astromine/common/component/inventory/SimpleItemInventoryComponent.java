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

package com.github.chainmailstudios.astromine.common.component.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import vazkii.patchouli.api.TriPredicate;

import java.util.*;

/**
 * Simple implementation of an InventoryComponent for usage anywhere one is required. Size is immutable and therefore
 * defined on instantiation.
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
	private TriPredicate<@Nullable Direction, ItemStack, Integer> extractPredicate = (direction, stack, integer) -> true;

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

	@Override
	public boolean canExtract(Direction direction, ItemStack stack, int slot) {
		return extractPredicate.test(direction, stack, slot);
	}

	public SimpleItemInventoryComponent withInsertPredicate(TriPredicate<@Nullable Direction, ItemStack, Integer> predicate) {
		TriPredicate<Direction, ItemStack, Integer> triPredicate = this.insertPredicate;
		this.insertPredicate = (direction, itemStack, integer) -> triPredicate.test(direction, itemStack, integer) && predicate.test(direction, itemStack, integer);
		return this;
	}

	public SimpleItemInventoryComponent withExtractPredicate(TriPredicate<@Nullable Direction, ItemStack, Integer> predicate) {
		TriPredicate<Direction, ItemStack, Integer> triPredicate = this.extractPredicate;
		this.extractPredicate = (direction, itemStack, integer) -> triPredicate.test(direction, itemStack, integer) && predicate.test(direction, itemStack, integer);
		return this;
	}

	@Override
	public Map<Integer, ItemStack> getContents() {
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
