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
	
	public SimpleFluidStorage(int size) {
		this.size = size;
		
		this.listeners = new ArrayList<>();
		this.storages = new ArrayList<>(size);

		for (int i = 0; i < size; ++i) {
			this.storages.set(i, new SimpleFluidVariantStorage());
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
	
	@Override
	public long insert(FluidVariant resource, long maxAmount, TransactionContext transaction) {
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
	public long extract(FluidVariant resource, long maxAmount, TransactionContext transaction) {
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
	public Iterator<StorageView<FluidVariant>> iterator(TransactionContext transaction) {
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

		if (!(object instanceof SimpleFluidStorage component)) return false;
		
		return Objects.equals(storages, component.storages);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(storages);
	}
}
