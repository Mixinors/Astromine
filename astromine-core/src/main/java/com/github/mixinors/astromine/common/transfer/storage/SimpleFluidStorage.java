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
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.nbt.NbtCompound;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.IntStream;

public class SimpleFluidStorage implements Storage<FluidVariant> {
	private final int size;
	
	private final List<Runnable> listeners;
	
	private final List<Storage<FluidVariant>> storages;
	
	private BiPredicate<FluidVariant, Integer> insertPredicate = null;
	
	private BiPredicate<FluidVariant, Integer> extractPredicate = null;
	
	private StorageSiding[] sidings;
	
	private int[] insertSlots;
	
	private int[] extractSlots;
	
	private long version = 0L;
	
	public SimpleFluidStorage(int size, long capacity) {
		this.size = size;
		
		this.listeners = new ArrayList<>();
		this.storages = new ArrayList<>(size);

		for (var i = 0; i < size; ++i) {
			var storage = new SimpleFluidVariantStorage(capacity);
			storage.setOuterStorage(this);
			
			this.storages.add(i, storage);
		}
		
		this.sidings = new StorageSiding[6];
		
		Arrays.fill(sidings, StorageSiding.NONE);
		
		this.insertSlots = IntStream.range(0, size).toArray();
		this.extractSlots = IntStream.range(0, size).toArray();
	}
	
	public SimpleFluidStorage insertPredicate(BiPredicate<FluidVariant, Integer> slotInsertPredicate) {
		this.insertPredicate = slotInsertPredicate;
		return this;
	}
	
	public SimpleFluidStorage extractPredicate(BiPredicate<FluidVariant, Integer> slotExtractPredicate) {
		this.extractPredicate = slotExtractPredicate;
		return this;
	}
	
	public SimpleFluidStorage sidings(StorageSiding[] sidings) {
		this.sidings = sidings;
		return this;
	}
	
	public SimpleFluidStorage insertSlots(int[] insertSlots) {
		this.insertSlots = insertSlots;
		return this;
	}
	
	public SimpleFluidStorage extractSlots(int[] extractSlots) {
		this.extractSlots = extractSlots;
		return this;
	}
	
	public SimpleFluidStorage listener(Runnable listener) {
		this.listeners.add(listener);
		return this;
	}
	
	public SingleSlotStorage<FluidVariant>[] slice(int... slots) {
		var storages = new SingleSlotStorage[slots.length];
		
		for (var i = 0; i < slots.length; ++i) {
			var slot = getStorage(slots[i]);
			storages[i] = slot;
		}
		
		return storages;
	}

	@Override
	public long insert(FluidVariant resource, long maxAmount, TransactionContext transaction) {
		return insert(resource, maxAmount, transaction, false);
	}

	public long insert(FluidVariant resource, long maxAmount, TransactionContext transaction, boolean force) {
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
			if (!insertPredicate.test(resource, slot) && !force) continue;
			
			var storage = storages.get(slot);
			
			amount += storage.insert(resource, maxAmount - amount, transaction);
			
			if (amount == maxAmount) break;
		}
		
		return amount;
	}

	@Override
	public long extract(FluidVariant resource, long maxAmount, TransactionContext transaction) {
		return extract(resource, maxAmount, transaction, false);
	}

	public long extract(FluidVariant resource, long maxAmount, TransactionContext transaction, boolean force) {
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
			if (!extractPredicate.test(resource, slot) && !force) continue;
			
			var storage = storages.get(slot);
			
			amount += storage.extract(resource, maxAmount - amount, transaction);
			
			if (amount == maxAmount) break;
		}
		
		return amount;
	}
	
	@Override
	public Iterator<StorageView<FluidVariant>> iterator(TransactionContext transaction) {
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
	
	public SimpleFluidVariantStorage getStorage(int index) {
		return (SimpleFluidVariantStorage) storages.get(index);
	}
	
	public FluidVariant getVariant(int index) {
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
			var variant = FluidVariant.fromNbt(storageNbt.getCompound("Variant"));
			
			getStorage(i).amount = amount;
			getStorage(i).variant = variant;
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

		if (!(object instanceof SimpleFluidStorage component)) return false;
		
		return Objects.equals(storages, component.storages);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(storages);
	}
}
