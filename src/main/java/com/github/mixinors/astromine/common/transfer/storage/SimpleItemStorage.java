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

import com.github.mixinors.astromine.common.transfer.StorageSiding;
import com.github.mixinors.astromine.common.util.DirectionUtils;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.IntStream;

/**
 * <p>A {@link Storage} implementation for {@link ItemVariant}s, backed
 * by a list of storages, with a proxy system.</p>
 * <p>A proxy is defined as a {@link Storage} whose insertion and/or extraction
 * rules are different from this one's, but whose entries should be shared.</p>
 * <ul>
 *     <li>The {@link #wildProxy} is a proxy with both <b>insertion</b> and <b>extraction</b>.</li>
 *     <li>The {@link #extractableProxy} is a proxy with <b>extraction</b> and no <b>insertion</b>.</li>
 *     <li>The {@link #insertableProxy} is a proxy with <b>insertion</b> and no <b>extraction</b>.</li>
 * </ul>
 * <p>Therefore, all insertion and/or extraction rules can be bypassed by using a proxy,
 * which is useful for internal use of Fabric API's storage utilities.</p>
 * <p>Serialization and deserialization methods are provided for:</p>
 * <ul>
 * 		<li>- {@link NbtCompound} - through {@link #writeToNbt(NbtCompound)} and {@link #readFromNbt(NbtCompound)}.</li>
 * </ul>
 */
public class SimpleItemStorage implements Storage<ItemVariant>, Inventory {
	public static final String SIDINGS_KEY = "Sidings";
	public static final String AMOUNT_KEY = "Amount";
	public static final String VARIANT_KEY = "Variant";
	public static final String STORAGES_KEY = "Storages";
	
	private int size;
	
	private List<Runnable> listeners;
	
	private List<ItemStack> stacks;
	
	private final SimpleItemStorage proxy;
	
	private List<SimpleItemVariantStorage> storages;
	
	private BiPredicate<ItemVariant, Integer> insertPredicate = (variant, slot) -> false;
	
	private BiPredicate<ItemVariant, Integer> extractPredicate = (variant, slot) -> false;
	
	private StorageSiding[] sidings;
	
	private int[] insertSlots;
	
	private int[] extractSlots;
	
	private long version = 0L;
	
	private SimpleItemStorage wildProxy;
	
	private SimpleItemStorage extractableProxy;
	private SimpleItemStorage insertableProxy;
	
	private boolean allowsInsertion = true;
	private boolean allowsExtraction = true;
	
	public SimpleItemStorage(int size) {
		this(size, null);
	}
	
	public SimpleItemStorage(ItemStack... stacks) {
		this(stacks.length, null);
		
		for (var i = 0; i < stacks.length; ++i) {
			this.stacks.set(i, stacks[i]);
		}
	}
	
	public SimpleItemStorage(int size, SimpleItemStorage proxy) {
		this.size = size;
		this.proxy = proxy;
		
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
		
		updateProxies();
	}
	
	/**
	 * Adds an insertion predicate to this storage, which must be satisfied for insertion of resources.
	 *
	 * @param slotInsertPredicate the predicate to be added.
	 */
	public SimpleItemStorage insertPredicate(BiPredicate<ItemVariant, Integer> slotInsertPredicate) {
		this.insertPredicate = slotInsertPredicate;
		updateProxies();
		return this;
	}
	
	/**
	 * Adds an extraction predicate to this storage, which must be satisfied for insertion of resources.
	 *
	 * @param slotExtractPredicate the predicate to be added.
	 */
	public SimpleItemStorage extractPredicate(BiPredicate<ItemVariant, Integer> slotExtractPredicate) {
		this.extractPredicate = slotExtractPredicate;
		updateProxies();
		return this;
	}
	
	/**
	 * Adds sidings to this storage.
	 *
	 * @param sidings the sidings to be added.
	 */
	public SimpleItemStorage sidings(StorageSiding[] sidings) {
		this.sidings = sidings;
		updateProxies();
		return this;
	}
	
	/**
	 * Adds insertion slots to this storage.
	 *
	 * @param insertSlots the slots to be added.
	 */
	public SimpleItemStorage insertSlots(int[] insertSlots) {
		this.insertSlots = insertSlots;
		updateProxies();
		return this;
	}
	
	/**
	 * Adds extraction slots to this storage.
	 *
	 * @param extractSlots the slots to be added.
	 */
	public SimpleItemStorage extractSlots(int[] extractSlots) {
		this.extractSlots = extractSlots;
		updateProxies();
		return this;
	}
	
	/**
	 * Adds a listener to this storage.
	 *
	 * @param listener the listener to be added.
	 */
	public SimpleItemStorage listener(Runnable listener) {
		this.listeners.add(listener);
		updateProxies();
		return this;
	}
	
	/**
	 * Returns this storage's {@link #proxy}.
	 */
	public SimpleItemStorage getProxy() {
		return proxy;
	}
	
	/**
	 * Returns this storage's proxies.
	 */
	public SimpleItemStorage[] getProxies() {
		return new SimpleItemStorage[] { wildProxy, extractableProxy, insertableProxy };
	}
	
	/**
	 * <p>Returns this storage's {@link #wildProxy}.</p>
	 *
	 * <p>This proxy allows <b>insertion</b> and <b>extraction</b>,
	 * regardless of this storage's {@link #insertPredicate} and {@link #extractPredicate}.</p>
	 */
	public SimpleItemStorage getWildProxy() {
		return wildProxy;
	}
	
	/**
	 * <p>Returns this storage's {@link #extractableProxy}.</p>
	 *
	 * <p>This proxy allows <b>extraction</b> and denies <b>insertion</b>,
	 * regardless of this storage's {@link #extractPredicate}.</p>
	 */
	public SimpleItemStorage getExtractableProxy() {
		return extractableProxy;
	}
	
	/**
	 * <p>Returns this storage's {@link #insertableProxy}.</p>
	 *
	 * <p>This proxy allows <b>insertion</b> and denies <b>extraction</b>,
	 * regardless of this storage's {@link #insertPredicate}.</p>
	 */
	public SimpleItemStorage getInsertableProxy() {
		return insertableProxy;
	}
	
	/**
	 * Returns this storage's the storage at the given slot.
	 *
	 * @param slot the slot.
	 */
	public SimpleItemVariantStorage getStorage(int slot) {
		return storages.get(slot);
	}
	
	/**
	 * Returns this storage's the variant at the given slot.
	 *
	 * @param slot the slot.
	 */
	public ItemVariant getVariant(int slot) {
		return getStorage(slot).getResource();
	}
	
	/**
	 * Returns this storage's size.
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * Returns this storage's listeners.
	 */
	public List<Runnable> getListeners() {
		return listeners;
	}
	
	/**
	 * Returns this storage's sidings.
	 */
	public StorageSiding[] getSidings() {
		return sidings;
	}
	
	/**
	 * Sets this storage's sidings.
	 *
	 * @param sidings the sidings to be set.
	 */
	public void setSidings(StorageSiding[] sidings) {
		this.sidings = sidings;
	}
	
	/**
	 * Updates this storage's version.
	 */
	public void incrementVersion() {
		version += 1;
	}
	
	/**
	 * Notifies this storage's listeners.
	 */
	public void notifyListeners() {
		listeners.forEach(Runnable::run);
	}
	
	/**
	 * Asserts whether the given variant can be inserted into the given slot, taking this storage's {@link #insertPredicate} into account.
	 *
	 * @param variant the variant to be inserted.
	 * @param slot    the slot from which the variant is to be extracted.
	 */
	public boolean canInsert(ItemVariant variant, int slot) {
		return insertPredicate.test(variant, slot) && allowsInsertion;
	}
	
	/**
	 * Asserts whether the given variant can be extracted from the given slot, taking this storage's {@link #extractPredicate} into account.
	 *
	 * @param variant the variant to be extracted.
	 * @param slot    the slot from which the variant is to be extracted.
	 */
	public boolean canExtract(ItemVariant variant, int slot) {
		return extractPredicate.test(variant, slot) && allowsExtraction;
	}
	
	/**
	 * Returns a slice of this storage.
	 *
	 * @param slots the slots from which to create the slice.
	 */
	public SingleSlotStorage<ItemVariant>[] slice(int... slots) {
		var storages = new SingleSlotStorage[slots.length];
		
		for (var i = 0; i < slots.length; ++i) {
			var slot = getStorage(slots[i]);
			storages[i] = slot;
		}
		
		return storages;
	}
	
	/**
	 * <p>An implementation of {@link #insert(ItemVariant, long, TransactionContext)}
	 * which allows insertion to ignore this storage's {@link #insertPredicate} if
	 * <b>force</b> is <code>true</code>.</p>
	 *
	 * <p>See original implementation for detailed documentation.</p>
	 */
	public long insert(ItemVariant variant, long maxAmount, TransactionContext transaction, boolean force) {
		StoragePreconditions.notBlankNotNegative(variant, maxAmount);
		
		if (!allowsInsertion) {
			return 0;
		}
		
		transaction.addCloseCallback((($, result) -> {
			if (result.wasCommitted()) {
				notifyListeners();
				
				incrementVersion();
			}
		}));
		
		var amount = 0;
		
		for (var slot : insertSlots) {
			if (!insertPredicate.test(variant, slot) && !force) {
				continue;
			}
			
			var storage = storages.get(slot);
			
			amount += storage.insert(variant, maxAmount - amount, transaction, force);
			
			if (amount == maxAmount) {
				break;
			}
		}
		
		return amount;
	}
	
	/**
	 * <p>An implementation of {@link #extract(ItemVariant, long, TransactionContext)}
	 * which allows extraction to ignore this storage's {@link #extractPredicate} if
	 * <b>force</b> is <code>true</code>.</p>
	 *
	 * <p>See original implementation for detailed documentation.</p>
	 */
	public long extract(ItemVariant variant, long maxAmount, TransactionContext transaction, boolean force) {
		StoragePreconditions.notBlankNotNegative(variant, maxAmount);
		
		if (!allowsExtraction) {
			return 0;
		}
		
		transaction.addCloseCallback((($, result) -> {
			if (result.wasCommitted()) {
				notifyListeners();
				
				incrementVersion();
			}
		}));
		
		var amount = 0;
		
		for (var slot : extractSlots) {
			if (!extractPredicate.test(variant, slot) && !force) {
				continue;
			}
			
			var storage = storages.get(slot);
			
			amount += storage.extract(variant, maxAmount - amount, transaction, force);
			
			if (amount == maxAmount) {
				break;
			}
		}
		
		return amount;
	}
	
	/**
	 * Serializes this storage to the given {@link NbtCompound}.
	 *
	 * @param nbt the {@link NbtCompound}.
	 */
	public void writeToNbt(NbtCompound nbt) {
		var sidingsNbt = new NbtCompound();
		
		for (var i = 0; i < sidings.length; ++i) {
			sidingsNbt.putInt(String.valueOf(i), sidings[i].ordinal());
		}
		
		nbt.put(SIDINGS_KEY, sidingsNbt);
		
		var storagesNbt = new NbtCompound();
		
		for (var i = 0; i < size; ++i) {
			var storageNbt = new NbtCompound();
			
			storageNbt.putLong(AMOUNT_KEY, getStorage(i).getAmount());
			storageNbt.put(VARIANT_KEY, getStorage(i).getResource().toNbt());
			
			storagesNbt.put(String.valueOf(i), storageNbt);
		}
		
		nbt.put(STORAGES_KEY, storagesNbt);
	}
	
	/**
	 * Deserializes this storage from the given {@link NbtCompound}.
	 *
	 * @param nbt the {@link NbtCompound}.
	 */
	public void readFromNbt(NbtCompound nbt) {
		var sidingsNbt = nbt.getCompound(SIDINGS_KEY);
		
		for (var i = 0; i < sidings.length; ++i) {
			sidings[i] = StorageSiding.values()[sidingsNbt.getInt(String.valueOf(i))];
		}
		
		var storagesNbt = nbt.getCompound(STORAGES_KEY);
		
		for (var i = 0; i < size; ++i) {
			var storageNbt = storagesNbt.getCompound(String.valueOf(i));
			
			var amount = storageNbt.getLong(AMOUNT_KEY);
			var variant = ItemVariant.fromNbt(storageNbt.getCompound(VARIANT_KEY));
			
			setStack(i, variant.toStack((int) amount));
		}
	}
	
	private void updateProxies() {
		if (proxy != null) {
			return;
		}
		
		wildProxy = new SimpleItemStorage(size, this);
		
		wildProxy.allowsInsertion = true;
		wildProxy.allowsExtraction = true;
		
		wildProxy.size = this.size;
		wildProxy.listeners = this.listeners;
		wildProxy.stacks = this.stacks;
		wildProxy.storages = new ArrayList<>();
		
		for (var i = 0; i < storages.size(); ++i) {
			var proxyStorage = new SimpleItemVariantStorage(this, i);
			proxyStorage.setOuterStorage(wildProxy);
			
			wildProxy.storages.add(proxyStorage);
		}
		
		wildProxy.insertPredicate = ($, $$) -> true;
		wildProxy.extractPredicate = ($, $$) -> true;
		
		var wildProxySidings = new StorageSiding[6];
		
		for (var direction : DirectionUtils.VALUES) {
			wildProxySidings[direction.ordinal()] = StorageSiding.INSERT_EXTRACT;
		}
		
		wildProxy.sidings = wildProxySidings;
		
		var wildProxySlots = new int[insertSlots.length + extractSlots.length];
		
		System.arraycopy(insertSlots, 0, wildProxySlots, 0, insertSlots.length);
		
		System.arraycopy(extractSlots, 0, wildProxySlots, insertSlots.length, extractSlots.length);
		
		wildProxy.insertSlots = wildProxySlots;
		wildProxy.extractSlots = wildProxySlots;
		
		extractableProxy = new SimpleItemStorage(size, this);
		
		extractableProxy.allowsInsertion = false;
		
		extractableProxy.size = this.size;
		extractableProxy.listeners = this.listeners;
		extractableProxy.stacks = this.stacks;
		extractableProxy.storages = new ArrayList<>();
		
		for (var i = 0; i < storages.size(); ++i) {
			var proxyStorage = new SimpleItemVariantStorage(this, i);
			proxyStorage.setOuterStorage(extractableProxy);
			
			extractableProxy.storages.add(proxyStorage);
		}
		
		extractableProxy.insertPredicate = this.insertPredicate;
		extractableProxy.extractPredicate = this.extractPredicate;
		extractableProxy.sidings = this.sidings;
		extractableProxy.insertSlots = this.insertSlots;
		extractableProxy.extractSlots = this.extractSlots;
		
		insertableProxy = new SimpleItemStorage(size, this);
		
		insertableProxy.allowsExtraction = false;
		
		insertableProxy.size = this.size;
		insertableProxy.listeners = this.listeners;
		insertableProxy.stacks = this.stacks;
		insertableProxy.storages = new ArrayList<>();
		
		for (var i = 0; i < storages.size(); ++i) {
			var proxyStorage = new SimpleItemVariantStorage(this, i);
			proxyStorage.setOuterStorage(insertableProxy);
			
			insertableProxy.storages.add(proxyStorage);
		}
		
		insertableProxy.insertPredicate = this.insertPredicate;
		insertableProxy.extractPredicate = this.extractPredicate;
		insertableProxy.sidings = this.sidings;
		insertableProxy.insertSlots = this.insertSlots;
		insertableProxy.extractSlots = this.extractSlots;
	}
	
	@Override
	public long insert(ItemVariant variant, long maxAmount, TransactionContext transaction) {
		return insert(variant, maxAmount, transaction, false);
	}
	
	@Override
	public long extract(ItemVariant variant, long maxAmount, TransactionContext transaction) {
		return extract(variant, maxAmount, transaction, false);
	}
	
	@Override
	public boolean supportsInsertion() {
		return insertSlots.length > 0 && allowsInsertion;
	}
	
	@Override
	public boolean supportsExtraction() {
		return extractSlots.length > 0 && allowsExtraction;
	}
	
	@Override
	public long getVersion() {
		return version;
	}
	
	@Override
	public Iterator<StorageView<ItemVariant>> iterator(TransactionContext transaction) {
		return (Iterator) storages.iterator();
	}
	
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
		
		notifyListeners();
		
		incrementVersion();
		
		return removedStack;
	}
	
	@Override
	public ItemStack removeStack(int slot) {
		var stack = stacks.get(slot);
		
		stacks.set(slot, ItemStack.EMPTY);
		
		notifyListeners();
		
		incrementVersion();
		
		return stack;
	}
	
	@Override
	public void setStack(int slot, ItemStack stack) {
		stacks.set(slot, stack);
		
		notifyListeners();
		
		incrementVersion();
	}
	
	@Override
	public void markDirty() {
		// This should suffice, though it has not been tested.
		incrementVersion();
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
		
		notifyListeners();
		
		incrementVersion();
	}
	
	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		
		if (!(object instanceof SimpleItemStorage component)) {
			return false;
		}
		
		return Objects.equals(stacks, component.stacks);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(stacks);
	}
}
