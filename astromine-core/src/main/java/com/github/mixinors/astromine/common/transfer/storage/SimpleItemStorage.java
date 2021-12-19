/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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

package com.github.mixinors.astromine.common.transfer.storage;

import com.github.mixinors.astromine.common.transfer.StorageSiding;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Direction;
import net.minecraft.inventory.Inventory;

import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.IntStream;

public class SimpleItemStorage implements Storage<ItemVariant>, Inventory {
	private final int size;
	
	private final List<Runnable> listeners;
	
	private final List<ItemStack> stacks;
	
	private final List<Storage<ItemVariant>> storages;
	
	private BiPredicate<ItemVariant, Integer> insertPredicate = null;
	
	private BiPredicate<ItemVariant, Integer> extractPredicate = null;
	
	private StorageSiding[] sidings;
	
	private int[] insertSlots;
	
	private int[] extractSlots;
	
	public SimpleItemStorage(int size) {
		this.size = size;
		
		this.listeners = new ArrayList<>();
		this.stacks = new ArrayList<>(size);
		this.storages = new ArrayList<>(size);

		for (int i = 0; i < size; ++i) {
			this.stacks.set(i, ItemStack.EMPTY);
			this.storages.set(i, new SimpleItemVariantStorage(this, i));
		}
		
		this.sidings = new StorageSiding[6];
		
		Arrays.fill(sidings, StorageSiding.NONE);
		
		this.insertSlots = IntStream.range(0, size).toArray();
		this.extractSlots = IntStream.range(0, size).toArray();
	}
	
	public SimpleItemStorage(ItemStack... stacks) {
		this(stacks.length);

		for (int i = 0; i < stacks.length; ++i) {
			this.stacks.set(i, stacks[i]);
		}
	}
	
	public SimpleItemStorage insertPredicate(BiPredicate<ItemVariant, Integer> slotInsertPredicate) {
		this.insertPredicate = slotInsertPredicate;
		return this;
	}
	
	public SimpleItemStorage extractPredicate(BiPredicate<ItemVariant, Integer> slotExtractPredicate) {
		this.extractPredicate = slotExtractPredicate;
		return this;
	}
	
	public SimpleItemStorage sidings(StorageSiding[] sidings) {
		this.sidings = sidings;
		return this;
	}
	
	public SimpleItemStorage insertSlots(int[] insertSlots) {
		this.insertSlots = insertSlots;
		return this;
	}
	
	public SimpleItemStorage extractSlots(int[] extractSlots) {
		this.extractSlots = extractSlots;
		return this;
	}
	
	public SimpleItemStorage listener(Runnable listener) {
		this.listeners.add(listener);
		return this;
	}
	
	@Override
	public long insert(ItemVariant resource, long maxAmount, TransactionContext transaction) {
		StoragePreconditions.notBlank(resource);
		StoragePreconditions.notNegative(maxAmount);
		
		transaction.addCloseCallback((($, result) -> {
			if (result.wasCommitted()) {
				listeners.forEach(Runnable::run);
			}
		}));
		
		var amount = 0;
		
		for (var slot : insertSlots) {
			if (!insertPredicate.test(resource, slot)) continue;
			
			var storage = storages.get(slot);
			
			amount += storage.insert(resource, maxAmount - amount, transaction);
			
			if (amount == maxAmount) break;
		}
		
		return amount;
	}
	
	@Override
	public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction) {
		StoragePreconditions.notBlank(resource);
		StoragePreconditions.notNegative(maxAmount);
		
		transaction.addCloseCallback((($, result) -> {
			if (result.wasCommitted()) {
				listeners.forEach(Runnable::run);
			}
		}));
		
		var amount = 0;
		
		for (var slot : extractSlots) {
			if (!extractPredicate.test(resource, slot)) continue;
			
			var storage = storages.get(slot);
			
			amount += storage.extract(resource, maxAmount - amount, transaction);
			
			if (amount == maxAmount) break;
		}
		
		return amount;
	}
	
	@Override
	public Iterator<StorageView<ItemVariant>> iterator(TransactionContext transaction) {
		return (Iterator) storages;
	}
	
	@Override
	public boolean supportsInsertion() {
		return insertSlots.length > 0;
	}
	
	@Override
	public boolean supportsExtraction() {
		return extractSlots.length > 0;
	}
	
	public SimpleItemVariantStorage getStorage(int index) {
		return (SimpleItemVariantStorage) storages.get(index);
	}
	
	public ItemVariant getVariant(int index) {
		return getStorage(index).getResource();
	}
	
	public int getSize() {
		return size;
	}
	
	public List<Runnable> getListeners() {
		return listeners;
	}
	
	public StorageSiding[] getSidings() {
		return sidings;
	}
	
	public void setSidings(StorageSiding[] sidings) {
		this.sidings = sidings;
	}
	
	/**
	 * {@link Inventory}
	 */
	
	@Override
	public int size() {
		return size;
	}
	
	@Override
	public boolean isEmpty() {
		for (var stack : stacks) {
			if (!stack.isEmpty()) {
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public ItemStack getStack(int slot) {
		return stacks.get(slot);
	}
	
	@Override
	public ItemStack removeStack(int slot, int amount) {
		var existingStack = stacks.get(slot);
		
		var removedStack = new ItemStack(existingStack.getItem(), Math.min(existingStack.getCount(), amount));
		
		existingStack.setCount(Math.max(0, existingStack.getCount() - amount));
		
		return removedStack;
	}
	
	@Override
	public ItemStack removeStack(int slot) {
		var stack = stacks.get(slot);
		
		stacks.set(slot, ItemStack.EMPTY);
		
		return stack;
	}
	
	@Override
	public void setStack(int slot, ItemStack stack) {
		stacks.set(slot, stack);
	}
	
	@Override
	public void markDirty() {
		// TODO
	}
	
	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return true;
	}
	
	@Override
	public void clear() {
		for (int i = 0; i < size; ++i) {
			stacks.set(i, ItemStack.EMPTY);
		}
	}
	
	public void writeToNbt(NbtCompound nbt) {
		// TODO
	}
	
	public void readFromNbt(NbtCompound nbt) {
		// TODO
	}
	
	/**
	 * {@link Object}
	 */
	
	@Override
	public boolean equals(Object object) {
		if (this == object) return true;

		if (!(object instanceof SimpleItemStorage component)) return false;
		
		return Objects.equals(stacks, component.stacks);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(stacks);
	}
}