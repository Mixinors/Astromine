/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
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

import net.minecraft.util.Mth;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class LongEnergyStorage implements IEnergyStorage {
	public long amount;
	public long capacity;
	public long maxInsertion;
	public long maxExtraction;
	
	public LongEnergyStorage(long capacity) {
		this(capacity, capacity, capacity, 0L);
	}
	
	public LongEnergyStorage(long capacity, long maxTransfer) {
		this(capacity, maxTransfer, maxTransfer, 0L);
	}
	
	public LongEnergyStorage(long capacity, long maxInsertion, long maxExtraction) {
		this(capacity, maxInsertion, maxExtraction, 0L);
	}
	
	public LongEnergyStorage(long capacity, long maxInsertion, long maxExtraction, long amount) {
		this.capacity = Math.max(0L, capacity);
		this.maxInsertion = Math.max(0L, maxInsertion);
		this.maxExtraction = Math.max(0L, maxExtraction);
		this.amount = Mth.clamp(amount, 0L, this.capacity);
	}
	
	public long insert(long maxAmount, boolean simulate) {
		if (maxAmount <= 0L || !supportsInsertion()) {
			return 0L;
		}
		
		var inserted = Math.min(Math.min(maxAmount, maxInsertion), capacity - amount);
		
		if (!simulate) {
			amount += inserted;
		}
		
		return inserted;
	}
	
	public long extract(long maxAmount, boolean simulate) {
		if (maxAmount <= 0L || !supportsExtraction()) {
			return 0L;
		}
		
		var extracted = Math.min(Math.min(maxAmount, maxExtraction), amount);
		
		if (!simulate) {
			amount -= extracted;
		}
		
		return extracted;
	}
	
	public long getAmount() {
		return amount;
	}
	
	public long getCapacity() {
		return capacity;
	}
	
	public boolean supportsInsertion() {
		return maxInsertion > 0L;
	}
	
	public boolean supportsExtraction() {
		return maxExtraction > 0L;
	}
	
	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		return clampToInt(insert(maxReceive, simulate));
	}
	
	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		return clampToInt(extract(maxExtract, simulate));
	}
	
	@Override
	public int getEnergyStored() {
		return clampToInt(amount);
	}
	
	@Override
	public int getMaxEnergyStored() {
		return clampToInt(capacity);
	}
	
	@Override
	public boolean canExtract() {
		return supportsExtraction();
	}
	
	@Override
	public boolean canReceive() {
		return supportsInsertion();
	}
	
	public static long getAmount(IEnergyStorage storage) {
		return storage instanceof LongEnergyStorage longStorage ? longStorage.getAmount() : storage.getEnergyStored();
	}
	
	public static long getCapacity(IEnergyStorage storage) {
		return storage instanceof LongEnergyStorage longStorage ? longStorage.getCapacity() : storage.getMaxEnergyStored();
	}
	
	public static long insert(IEnergyStorage storage, long maxAmount, boolean simulate) {
		if (storage instanceof LongEnergyStorage longStorage) {
			return longStorage.insert(maxAmount, simulate);
		}
		
		if (maxAmount <= 0L || !storage.canReceive()) {
			return 0L;
		}
		
		var accepted = Math.min(maxAmount, Math.max(0L, (long) storage.getMaxEnergyStored() - storage.getEnergyStored()));
		return storage.receiveEnergy(clampToInt(accepted), simulate);
	}
	
	public static long extract(IEnergyStorage storage, long maxAmount, boolean simulate) {
		if (storage instanceof LongEnergyStorage longStorage) {
			return longStorage.extract(maxAmount, simulate);
		}
		
		if (maxAmount <= 0L || !storage.canExtract()) {
			return 0L;
		}
		
		var available = Math.min(maxAmount, Math.max(0L, storage.getEnergyStored()));
		return storage.extractEnergy(clampToInt(available), simulate);
	}
	
	public static long move(IEnergyStorage from, IEnergyStorage to, long maxAmount) {
		if (from == null || to == null || maxAmount <= 0L) {
			return 0L;
		}
		
		var extracted = extract(from, maxAmount, true);
		var inserted = insert(to, extracted, true);
		var moved = extract(from, inserted, false);
		
		return insert(to, moved, false);
	}
	
	private static int clampToInt(long value) {
		return (int) Mth.clamp(value, 0L, Integer.MAX_VALUE);
	}
}
