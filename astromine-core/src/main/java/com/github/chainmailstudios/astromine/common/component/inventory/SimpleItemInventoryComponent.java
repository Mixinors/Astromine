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

import com.github.chainmailstudios.astromine.common.utilities.data.predicate.TriPredicate;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Simple implementation of an InventoryComponent for usage anywhere one is required. Size is immutable and therefore
 * defined on instantiation.
 */
public class SimpleItemInventoryComponent implements ItemInventoryComponent {
	private final Int2ObjectOpenHashMap<ItemStack> contents = new Int2ObjectOpenHashMap<>();

	private final List<Runnable> listeners = new ArrayList<>();

	private TriPredicate<@Nullable Direction, ItemStack, Integer> insertPredicate = (direction, stack, slot) -> true;
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
		this.contents.defaultReturnValue(ItemStack.EMPTY);
	}

	public SimpleItemInventoryComponent(ItemStack... stacks) {
		this(stacks.length);
		for (int i = 0; i < stacks.length; ++i) {
			setStack(i, stacks[i]);
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
		this.insertPredicate = (direction, stack, integer) -> triPredicate.test(direction, stack, integer) && predicate.test(direction, stack, integer);
		return this;
	}

	public SimpleItemInventoryComponent withExtractPredicate(TriPredicate<@Nullable Direction, ItemStack, Integer> predicate) {
		TriPredicate<Direction, ItemStack, Integer> triPredicate = this.extractPredicate;
		this.extractPredicate = (direction, stack, integer) -> triPredicate.test(direction, stack, integer) && predicate.test(direction, stack, integer);
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
