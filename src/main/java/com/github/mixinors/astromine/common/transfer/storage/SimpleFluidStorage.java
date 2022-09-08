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
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import net.minecraft.nbt.NbtCompound;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.IntStream;

/**
 * <p>A {@link Storage} implementation for {@link FluidVariant}s, backed
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
public class SimpleFluidStorage extends SnapshotParticipant<SimpleFluidStorage.Snapshot> implements Storage<FluidVariant> {
	public static final String SIDINGS_KEY = "Sidings";
	public static final String AMOUNT_KEY = "Amount";
	public static final String VARIANT_KEY = "Variant";
	public static final String STORAGES_KEY = "Storages";
	
	private int size;
	private long capacity;
	
	private List<Runnable> listeners;
	
	private SimpleFluidStorage proxy;
	
	private List<SimpleFluidVariantStorage.Proxy> proxyStorages;
	
	private List<SimpleFluidVariantStorage> storages;
	
	private BiPredicate<FluidVariant, Integer> insertPredicate = null;
	
	private BiPredicate<FluidVariant, Integer> extractPredicate = null;
	
	private StorageSiding[] sidings;
	
	private int[] insertSlots;
	
	private int[] extractSlots;
	
	private long version = 0L;
	
	private SimpleFluidStorage wildProxy;
	
	private SimpleFluidStorage extractableProxy;
	private SimpleFluidStorage insertableProxy;
	
	private boolean allowsInsertion = true;
	private boolean allowsExtraction = true;
	
	public SimpleFluidStorage(int size, long capacity) {
		this(size, capacity, null);
	}
	
	public SimpleFluidStorage(int size, long capacity, SimpleFluidStorage proxy) {
		this.size = size;
		this.capacity = capacity;
		
		this.listeners = new ArrayList<>();
		this.storages = new ArrayList<>(size);
		this.proxyStorages = new ArrayList<>(size);
		
		this.proxy = proxy;
		
		if (proxy == null) {
			for (var i = 0; i < size; ++i) {
				var storage = new SimpleFluidVariantStorage(capacity, i);
				var storageProxy = new SimpleFluidVariantStorage.Proxy(this, storage);
				
				this.storages.add(i, storage);
				this.proxyStorages.add(i, storageProxy);
			}
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
	public SimpleFluidStorage insertPredicate(BiPredicate<FluidVariant, Integer> slotInsertPredicate) {
		this.insertPredicate = slotInsertPredicate;
		updateProxies();
		return this;
	}
	
	/**
	 * Adds an extraction predicate to this storage, which must be satisfied for insertion of resources.
	 *
	 * @param slotExtractPredicate the predicate to be added.
	 */
	public SimpleFluidStorage extractPredicate(BiPredicate<FluidVariant, Integer> slotExtractPredicate) {
		this.extractPredicate = slotExtractPredicate;
		updateProxies();
		return this;
	}
	
	/**
	 * Adds sidings to this storage.
	 *
	 * @param sidings the sidings to be added.
	 */
	public SimpleFluidStorage sidings(StorageSiding[] sidings) {
		this.sidings = sidings;
		updateProxies();
		return this;
	}
	
	/**
	 * Adds insertion slots to this storage.
	 *
	 * @param insertSlots the slots to be added.
	 */
	public SimpleFluidStorage insertSlots(int[] insertSlots) {
		this.insertSlots = insertSlots;
		updateProxies();
		return this;
	}
	
	/**
	 * Adds extraction slots to this storage.
	 *
	 * @param extractSlots the slots to be added.
	 */
	public SimpleFluidStorage extractSlots(int[] extractSlots) {
		this.extractSlots = extractSlots;
		updateProxies();
		return this;
	}
	
	/**
	 * Adds a listener to this storage.
	 *
	 * @param listener the listener to be added.
	 */
	public SimpleFluidStorage listener(Runnable listener) {
		this.listeners.add(listener);
		updateProxies();
		return this;
	}
	
	/**
	 * Returns this storage's {@link #proxy}.
	 */
	public SimpleFluidStorage getProxy() {
		return proxy;
	}
	
	/**
	 * Returns this storage's proxies.
	 */
	public SimpleFluidStorage[] getProxies() {
		return new SimpleFluidStorage[] { wildProxy, extractableProxy, insertableProxy };
	}
	
	/**
	 * <p>Returns this storage's {@link #wildProxy}.</p>
	 *
	 * <p>This proxy allows <b>insertion</b> and <b>extraction</b>,
	 * regardless of this storage's {@link #insertPredicate} and {@link #extractPredicate}.</p>
	 */
	public SimpleFluidStorage getWildProxy() {
		return wildProxy;
	}
	
	/**
	 * <p>Returns this storage's {@link #extractableProxy}.</p>
	 *
	 * <p>This proxy allows <b>extraction</b> and denies <b>insertion</b>,
	 * regardless of this storage's {@link #extractPredicate}.</p>
	 */
	public SimpleFluidStorage getExtractableProxy() {
		return extractableProxy;
	}
	
	/**
	 * <p>Returns this storage's {@link #insertableProxy}.</p>
	 *
	 * <p>This proxy allows <b>insertion</b> and denies <b>extraction</b>,
	 * regardless of this storage's {@link #insertPredicate}.</p>
	 */
	public SimpleFluidStorage getInsertableProxy() {
		return insertableProxy;
	}
	
	/**
	 * Returns this storage's the storage at the given slot.
	 *
	 * @param slot the slot.
	 */
	public SimpleFluidVariantStorage.Proxy getStorage(int slot) {
		return proxyStorages.get(slot);
	}
	
	/**
	 * Returns this storage's the variant at the given slot.
	 *
	 * @param slot the slot.
	 */
	public FluidVariant getVariant(int slot) {
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
	
	
	public record SnapshotEntry(
			FluidVariant variant,
			long amount
	) {}
	
	public record Snapshot(
			List<SnapshotEntry> entries
	) {
		public Snapshot() {
			this(new ArrayList<>());
		}
	}
	
	@Override
	protected Snapshot createSnapshot() {
		var snapshot = new Snapshot();
		
		for (var storage : storages) {
			snapshot.entries.add(new SnapshotEntry(storage.variant, storage.amount));
		}
		
		return snapshot;
	}
	
	@Override
	protected void readSnapshot(Snapshot snapshot) {
		var index = 0;
		
		for (var entry : snapshot.entries) {
			var storage = storages.get(index);
			
			storage.setResource(entry.variant);
			storage.setAmount(entry.amount);
		}
	}
	
	@Override
	protected void onFinalCommit() {
		if (proxy == null) {
			notifyListeners();
			
			incrementVersion();
		} else {
			var proxies = (SimpleFluidStorage[]) null;
			
			if (proxy.getProxy() != null) {
				proxies = proxy.getProxy().getProxies();
			} else {
				proxies = proxy.getProxies();
			}
			
			for (var proxy : proxies) {
				for (var i = 0; i < proxy.getSize(); ++i) {
					var storage = proxy.getStorage(i);
					
					storage.setAmount(storage.getProxyStorage().getAmount());
					storage.setResource(storage.getProxyStorage().getResource());
				}
			}
		}
	}
	
	/**
	 * Updates this storage's version.
	 */
	public void incrementVersion() {
		if (proxy != null) {
			proxy.incrementVersion();
		} else {
			version += 1;
		}
	}
	
	/**
	 * Notifies this storage's listeners.
	 */
	public void notifyListeners() {
		if (proxy != null) {
			proxy.notifyListeners();
		} else {
			listeners.forEach(Runnable::run);
		}
	}
	
	/**
	 * Asserts whether the given variant can be inserted into the given slot, taking this storage's {@link #insertPredicate} into account.
	 *
	 * @param variant the variant to be inserted.
	 * @param slot    the slot from which the variant is to be extracted.
	 */
	public boolean canInsert(FluidVariant variant, int slot) {
		return (insertPredicate == null || insertPredicate.test(variant, slot)) && allowsInsertion;
	}
	
	/**
	 * Asserts whether the given variant can be extracted from the given slot, taking this storage's {@link #extractPredicate} into account.
	 *
	 * @param variant the variant to be extracted.
	 * @param slot    the slot from which the variant is to be extracted.
	 */
	public boolean canExtract(FluidVariant variant, int slot) {
		return (extractPredicate == null || extractPredicate.test(variant, slot)) && allowsExtraction;
	}
	
	/**
	 * Returns a slice of this storage.
	 *
	 * @param slots the slots from which to create the slice.
	 */
	public SingleSlotStorage<FluidVariant>[] slice(int... slots) {
		var storages = new SingleSlotStorage[slots.length];
		
		for (var i = 0; i < slots.length; ++i) {
			var slot = getStorage(slots[i]);
			storages[i] = slot;
		}
		
		return storages;
	}
	
	/**
	 * <p>An implementation of {@link #insert(FluidVariant, long, TransactionContext)}
	 * which allows insertion to ignore this storage's {@link #insertPredicate} if
	 * <b>force</b> is <code>true</code>.</p>
	 *
	 * <p>See original implementation for detailed documentation.</p>
	 */
	public long insert(FluidVariant resource, long maxAmount, TransactionContext transaction, boolean force) {
		StoragePreconditions.notBlankNotNegative(resource, maxAmount);
		
		if (!allowsInsertion) {
			return 0;
		}
		
		var amount = 0;
		
		for (var slot : insertSlots) {
			if (!insertPredicate.test(resource, slot) && !force) {
				continue;
			}
			
			var storage = proxyStorages.get(slot);
			
			amount += storage.insert(resource, maxAmount - amount, transaction, force);
			
			if (amount == maxAmount) {
				break;
			}
		}
		
		if (amount > 0) {
			updateSnapshots(transaction);
		}
		
		return amount;
	}
	
	/**
	 * <p>An implementation of {@link #extract(FluidVariant, long, TransactionContext)}
	 * which allows extraction to ignore this storage's {@link #extractPredicate} if
	 * <b>force</b> is <code>true</code>.</p>
	 *
	 * <p>See original implementation for detailed documentation.</p>
	 */
	public long extract(FluidVariant resource, long maxAmount, TransactionContext transaction, boolean force) {
		StoragePreconditions.notBlankNotNegative(resource, maxAmount);
		
		if (!allowsExtraction) {
			return 0;
		}
		
		var amount = 0;
		
		for (var slot : extractSlots) {
			if (!extractPredicate.test(resource, slot) && !force) {
				continue;
			}
			
			var storage = proxyStorages.get(slot);
			
			amount += storage.extract(resource, maxAmount - amount, transaction, force);
			
			if (amount == maxAmount) {
				break;
			}
		}
		
		if (amount > 0) {
			updateSnapshots(transaction);
		}
		
		return amount;
	}
	
	/**
	 * Serializes this storage to the given {@link NbtCompound}.
	 *
	 * @param nbt the {@link NbtCompound}.
	 */
	public void writeToNbt(NbtCompound nbt) {
		if (proxy == null) {
			var sidingsNbt = new NbtCompound();
			
			for (var i = 0; i < sidings.length; ++i) {
				sidingsNbt.putInt(String.valueOf(i), sidings[i].ordinal());
			}
			
			nbt.put(SIDINGS_KEY, sidingsNbt);
			
			var storagesNbt = new NbtCompound();
			
			for (var i = 0; i < size; ++i) {
				var storageNbt = new NbtCompound();
				
				storageNbt.putLong(AMOUNT_KEY, storages.get(i).getAmount());
				storageNbt.put(VARIANT_KEY, storages.get(i).getResource().toNbt());
				
				storagesNbt.put(String.valueOf(i), storageNbt);
			}
			
			nbt.put(STORAGES_KEY, storagesNbt);
		}
	}
	
	/**
	 * Deserializes this storage from the given {@link NbtCompound}.
	 *
	 * @param nbt the {@link NbtCompound}.
	 */
	public void readFromNbt(NbtCompound nbt) {
		if (proxy == null) {
			var sidingsNbt = nbt.getCompound(SIDINGS_KEY);
			
			for (var i = 0; i < sidings.length; ++i) {
				sidings[i] = StorageSiding.values()[sidingsNbt.getInt(String.valueOf(i))];
			}
			
			var storagesNbt = nbt.getCompound(STORAGES_KEY);
			
			for (var i = 0; i < size; ++i) {
				var storageNbt = storagesNbt.getCompound(String.valueOf(i));
				
				var amount = storageNbt.getLong(AMOUNT_KEY);
				var variant = FluidVariant.fromNbt(storageNbt.getCompound(VARIANT_KEY));
				
				storages.get(i).setAmount(amount);
				storages.get(i).setResource(variant);
			}
			
			updateProxies();
		}
	}
	
	public void updateProxies() {
		if (proxy != null) {
			return;
		}
		
		for (var i = 0; i < storages.size(); ++i) {
			var storage = storages.get(i);
			var proxyStorage = proxyStorages.get(i);
			
			proxyStorage.setAmount(storage.getAmount());
			proxyStorage.setResource(storage.getResource());
		}
		
		wildProxy = new SimpleFluidStorage(size, capacity, this);
		
		wildProxy.allowsInsertion = true;
		wildProxy.allowsExtraction = true;
		
		wildProxy.size = this.size;
		wildProxy.listeners = this.listeners;
		wildProxy.proxyStorages = new ArrayList<>();
		
		for (var i = 0; i < proxyStorages.size(); ++i) {
			var proxyStorage = new SimpleFluidVariantStorage.Proxy(wildProxy, storages.get(i));
			
			wildProxy.proxyStorages.add(i, proxyStorage);
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
		
		System.arraycopy(extractSlots, 0, wildProxySlots, 0 + insertSlots.length, extractSlots.length);
		
		wildProxy.insertSlots = wildProxySlots;
		wildProxy.extractSlots = wildProxySlots;
		
		extractableProxy = new SimpleFluidStorage(size, capacity, this);
		
		extractableProxy.allowsInsertion = false;
		
		extractableProxy.size = this.size;
		extractableProxy.listeners = this.listeners;
		extractableProxy.proxyStorages = new ArrayList<>();
		
		for (var i = 0; i < proxyStorages.size(); ++i) {
			var proxyStorage = new SimpleFluidVariantStorage.Proxy(extractableProxy, storages.get(i));
			
			extractableProxy.proxyStorages.add(i, proxyStorage);
		}
		
		extractableProxy.insertPredicate = this.insertPredicate;
		extractableProxy.extractPredicate = this.extractPredicate;
		extractableProxy.sidings = this.sidings;
		extractableProxy.insertSlots = this.insertSlots;
		extractableProxy.extractSlots = this.extractSlots;
		
		insertableProxy = new SimpleFluidStorage(size, capacity, this);
		
		insertableProxy.allowsExtraction = false;
		
		insertableProxy.size = this.size;
		insertableProxy.listeners = this.listeners;
		insertableProxy.proxyStorages = new ArrayList<>();
		
		for (var i = 0; i < proxyStorages.size(); ++i) {
			var proxyStorage = new SimpleFluidVariantStorage.Proxy(insertableProxy, storages.get(i));
			
			insertableProxy.proxyStorages.add(i, proxyStorage);
		}
		
		insertableProxy.insertPredicate = this.insertPredicate;
		insertableProxy.extractPredicate = this.extractPredicate;
		insertableProxy.sidings = this.sidings;
		insertableProxy.insertSlots = this.insertSlots;
		insertableProxy.extractSlots = this.extractSlots;
	}
	
	
	@Override
	public long insert(FluidVariant resource, long maxAmount, TransactionContext transaction) {
		return insert(resource, maxAmount, transaction, false);
	}
	
	@Override
	public long extract(FluidVariant resource, long maxAmount, TransactionContext transaction) {
		return extract(resource, maxAmount, transaction, false);
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
		if (proxy != null) {
			return proxy.getVersion();
		} else {
			return version;
		}
	}
	
	@Override
	public Iterator<StorageView<FluidVariant>> iterator(TransactionContext transaction) {
		return (Iterator) proxyStorages.iterator();
	}
	
	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		
		if (!(object instanceof SimpleFluidStorage component)) {
			return false;
		}
		
		return Objects.equals(proxyStorages, component.proxyStorages);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(proxyStorages);
	}
}
