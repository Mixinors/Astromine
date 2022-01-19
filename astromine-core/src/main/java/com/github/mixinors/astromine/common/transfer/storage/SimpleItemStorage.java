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

import com.github.mixinors.astromine.registry.common.AMItems;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.inventory.Inventory;

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
	
	private long version = 0L;
	
	public SimpleItemStorage(int size) {
		this.size = size;
		
		this.listeners = new ArrayList<>();
		this.stacks = new ArrayList<>(size);
		this.storages = new ArrayList<>(size);

		for (var i = 0; i < size; ++i) {
			this.stacks.add(i, ItemStack.EMPTY);
			
			var storage = new SimpleItemVariantStorage(this, i);
			storage.setOuterStorage(this);
			
			this.storages.add(i, storage);
		}
		
		this.sidings = new StorageSiding[6];
		
		Arrays.fill(sidings, StorageSiding.NONE);
		
		this.insertSlots = IntStream.range(0, size).toArray();
		this.extractSlots = IntStream.range(0, size).toArray();
	}
	
	public SimpleItemStorage(ItemStack... stacks) {
		this(stacks.length);

		for (var i = 0; i < stacks.length; ++i) {
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
	
	public SingleSlotStorage<ItemVariant>[] slice(int... slots) {
		var storages = new SingleSlotStorage[slots.length];
		
		for (var i = 0; i < slots.length; ++i) {
			var slot = getStorage(slots[i]);
			storages[i] = slot;
		}
		
		return storages;
	}
	
	@Override
	public long insert(ItemVariant resource, long maxAmount, TransactionContext transaction) {
		StoragePreconditions.notBlank(resource);
		StoragePreconditions.notNegative(maxAmount);
		
		transaction.addCloseCallback((($, result) -> {
			if (result.wasCommitted()) {
				listeners.forEach(Runnable::run);
				
				incrementVersion();
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
				
				incrementVersion();
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
		return (Iterator) storages.iterator();
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
		
		var removedStack = existingStack.copy();
		removedStack.setCount(Math.min(existingStack.getCount(), amount));

		existingStack.setCount(Math.max(0, existingStack.getCount() - amount));
		
		incrementVersion();
		
		listeners.forEach(Runnable::run);
		
		return removedStack;
	}
	
	@Override
	public ItemStack removeStack(int slot) {
		var stack = stacks.get(slot);
		
		stacks.set(slot, ItemStack.EMPTY);
		
		incrementVersion();
		
		listeners.forEach(Runnable::run);
		
		return stack;
	}
	
	@Override
	public void setStack(int slot, ItemStack stack) {
		stacks.set(slot, stack);
		
		incrementVersion();
		
		listeners.forEach(Runnable::run);
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
		for (var i = 0; i < size; ++i) {
			stacks.set(i, ItemStack.EMPTY);
		}
		
		incrementVersion();
		
		listeners.forEach(Runnable::run);
	}
	
	public void writeToNbt(NbtCompound nbt) {
		var sidingsNbt = new NbtCompound();
		
		for (var i = 0; i < sidings.length; ++i) {
			sidingsNbt.putInt(String.valueOf(i), sidings[i].ordinal());
		}
		
		nbt.put("Sidings", sidingsNbt);
		
		var storagesNbt = new NbtCompound();
		
		for (var i = 0; i < size; ++i) {
			var storageNbt = new NbtCompound();
			
			storageNbt.putLong("Amount", getStorage(i).getAmount());
			storageNbt.put("Variant", getStorage(i).getResource().toNbt());
			
			if (getStorage(i).getStack().getItem() == AMItems.ADVANCED_DRILL.get()) {
				var f = 0;
			}
			
			storagesNbt.put(String.valueOf(i), storageNbt);
		}
		
		nbt.put("Storages", storagesNbt);
	}
	
	public void readFromNbt(NbtCompound nbt) {
		var sidingsNbt = nbt.getCompound("Sidings");
		
		for (var i = 0; i < sidings.length; ++i) {
			sidings[i] = StorageSiding.values()[sidingsNbt.getInt(String.valueOf(i))];
		}
		
		var storagesNbt = nbt.getCompound("Storages");
		
		for (var i = 0; i < size; ++i) {
			var storageNbt = storagesNbt.getCompound(String.valueOf(i));
			
			var amount = storageNbt.getLong("Amount");
			var variant = ItemVariant.fromNbt(storageNbt.getCompound("Variant"));
			
			setStack(i, variant.toStack((int) amount));
		}
	}
	
	@Override
	public long getVersion() {
		return version;
	}
	
	public void incrementVersion() {
		version += 1;
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
