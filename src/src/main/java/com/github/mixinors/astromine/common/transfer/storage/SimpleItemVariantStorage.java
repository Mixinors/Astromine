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

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

/**
 * <p>A {@link SingleVariantStorage} implementation for {@link ItemVariant}s, backed by an {@link Inventory}.</p>
 */
public class SimpleItemVariantStorage extends SnapshotParticipant<ItemStack> implements SingleSlotStorage<ItemVariant> {
	private final Inventory inventory;
	private final int slot;
	
	private SimpleItemStorage outerStorage = null;
	
	public SimpleItemVariantStorage(Inventory inventory, int slot) {
		this.inventory = inventory;
		this.slot = slot;
	}
	
	/**
	 * Returns this storage's stack.
	 */
	public ItemStack getStack() {
		return inventory.getStack(slot);
	}
	
	/**
	 * Sets this storage's stack.
	 *
	 * @param stack the stack to be set.
	 */
	public void setStack(ItemStack stack) {
		inventory.setStack(slot, stack);
	}
	
	/**
	 * Returns this storage's capacity for the given variant.
	 *
	 * @param variant the variant.
	 */
	public int getCapacity(ItemVariant variant) {
		return variant.getItem().getMaxCount();
	}
	
	/**
	 * Asserts whether this storage's variant is blank or not.
	 */
	@Override
	public boolean isResourceBlank() {
		return getResource().isBlank();
	}
	
	/**
	 * Returns this storage's variant.
	 */
	@Override
	public ItemVariant getResource() {
		return ItemVariant.of(getStack());
	}
	
	/**
	 * Returns this storage's amount.
	 */
	@Override
	public long getAmount() {
		return getStack().getCount();
	}
	
	/**
	 * Returns this storage's capacity.
	 */
	@Override
	public final long getCapacity() {
		return getCapacity(getResource());
	}
	
	/**
	 * Returns this storage's {@link #outerStorage}.
	 */
	public SimpleItemStorage getOuterStorage() {
		return outerStorage;
	}
	
	/**
	 * Sets this storage's {@link #outerStorage}
	 *
	 * @param outerStorage the storage to be set.
	 */
	public void setOuterStorage(SimpleItemStorage outerStorage) {
		this.outerStorage = outerStorage;
	}
	
	/**
	 * <p>An implementation of {@link #insert(ItemVariant, long, TransactionContext)}
	 * which allows insertion to ignore this storage's {@link #outerStorage}'s insertion predicate if
	 * <b>force</b> is <code>true</code>.</p>
	 *
	 * <p>See original implementation for detailed documentation.</p>
	 */
	public long insert(ItemVariant variant, long maxAmount, TransactionContext transaction, boolean force) {
		StoragePreconditions.notBlankNotNegative(variant, maxAmount);
		
		transaction.addCloseCallback((($, result) -> {
			if (outerStorage != null && result.wasCommitted()) {
				outerStorage.notifyListeners();
				
				outerStorage.incrementVersion();
			}
		}));
		
		var stack = getStack();
		
		if (variant.matches(stack) || stack.isEmpty()) {
			if (outerStorage != null && !outerStorage.canInsert(variant, slot) && !force) {
				return 0;
			}
			
			var amount = (int) Math.min(maxAmount, getCapacity(variant) - stack.getCount());
			
			if (amount > 0) {
				updateSnapshots(transaction);
				
				stack = getStack();
				
				if (stack.isEmpty()) {
					stack = variant.toStack(amount);
				} else {
					stack.increment(amount);
				}
				
				setStack(stack);
			}
			
			return amount;
		}
		
		return 0;
	}
	
	/**
	 * <p>An implementation of {@link #extract(ItemVariant, long, TransactionContext)}
	 * which allows extraction to ignore this storage's {@link #outerStorage}'s extraction predicate if
	 * <b>force</b> is <code>true</code>.</p>
	 *
	 * <p>See original implementation for detailed documentation.</p>
	 */
	public long extract(ItemVariant variant, long maxAmount, TransactionContext transaction, boolean force) {
		StoragePreconditions.notBlankNotNegative(variant, maxAmount);
		
		transaction.addCloseCallback((($, result) -> {
			if (outerStorage != null && result.wasCommitted()) {
				outerStorage.notifyListeners();
				
				outerStorage.incrementVersion();
			}
		}));
		
		var stack = getStack();
		
		if (variant.matches(stack)) {
			if (outerStorage != null && !outerStorage.canExtract(variant, slot) && !force) {
				return 0;
			}
			
			var amount = (int) Math.min(stack.getCount(), maxAmount);
			
			if (amount > 0) {
				this.updateSnapshots(transaction);
				
				stack = getStack();
				
				stack.decrement(amount);
				
				setStack(stack);
			}
			
			return amount;
		}
		
		return 0;
	}
	
	@Override
	public long insert(ItemVariant variant, long maxAmount, TransactionContext transaction) {
		return insert(variant, maxAmount, transaction, false);
	}
	
	@Override
	public long extract(ItemVariant variant, long maxAmount, TransactionContext transaction) {
		return extract(variant, maxAmount, transaction, false);
	}
	
	public final ItemStack createSnapshot() {
		var original = getStack();
		
		setStack(original.copy());
		
		return original;
	}
	
	public final void readSnapshot(ItemStack snapshot) {
		setStack(snapshot);
	}
}
