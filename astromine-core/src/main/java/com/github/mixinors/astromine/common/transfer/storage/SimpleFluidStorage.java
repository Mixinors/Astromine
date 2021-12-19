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
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class SimpleFluidStorage implements Storage<FluidVariant> {
	private final int size;
	
	private final List<Storage<FluidVariant>> storages;
	
	private Predicate<@Nullable Direction> insertPredicate;
	
	private Predicate<@Nullable Direction> extractPredicate;
	
	private StorageSiding[] sidings;
	
	private int[] insertSlots;
	
	private int[] extractSlots;
	
	public SimpleFluidStorage(int size) {
		this.size = size;
		
		this.storages = new ArrayList<>(size);

		for (int i = 0; i < size; ++i) {
			this.storages.set(i, new SimpleFluidVariantStorage());
		}
		
		this.insertPredicate = ($) -> true;
		this.extractPredicate = ($) -> true;
		
		this.sidings = new StorageSiding[6];
		
		Arrays.fill(sidings, StorageSiding.NONE);
		
		this.insertSlots = IntStream.range(0, size).toArray();
		this.extractSlots = IntStream.range(0, size).toArray();
	}
	
	public SimpleFluidStorage withInsertPredicate(Predicate<@Nullable Direction> insertPredicate) {
		this.insertPredicate = insertPredicate;
		return this;
	}
	
	public SimpleFluidStorage withExtractPredicate(Predicate<@Nullable Direction> extractPredicate) {
		this.extractPredicate = extractPredicate;
		return this;
	}
	
	public SimpleFluidStorage withSidings(StorageSiding[] sidings) {
		this.sidings = sidings;
		return this;
	}
	
	public SimpleFluidStorage withInsertSlots(int[] insertSlots) {
		this.insertSlots = insertSlots;
		return this;
	}
	
	public SimpleFluidStorage withExtractSlots(int[] extractSlots) {
		this.extractSlots = extractSlots;
		return this;
	}
	
	@Override
	public long insert(FluidVariant resource, long maxAmount, TransactionContext transaction) {
		StoragePreconditions.notBlank(resource);
		StoragePreconditions.notNegative(maxAmount);
		
		var amount = 0;
		
		for (var slot : insertSlots) {
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

		var amount = 0;
		
		for (var slot : extractSlots) {
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
	
	public boolean canInsert(@Nullable Direction direction) {
		return insertPredicate == null || insertPredicate.test(direction);
	}
	
	public boolean canExtract(@Nullable Direction direction) {
		return extractPredicate == null || extractPredicate.test(direction);
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
