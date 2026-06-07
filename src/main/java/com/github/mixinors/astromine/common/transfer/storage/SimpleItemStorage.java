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
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.stream.IntStream;

public class SimpleItemStorage implements IItemHandlerModifiable, Container {
	public static final String SIDINGS_KEY = "Sidings";
	public static final String STACK_KEY = "Stack";
	public static final String STORAGES_KEY = "Storages";
	
	private int size;
	
	private List<Runnable> listeners;
	private List<ItemStack> stacks;
	private List<SimpleItemVariantStorage> storages;
	
	private final SimpleItemStorage proxy;
	
	private BiPredicate<ItemStack, Integer> insertPredicate = (stack, slot) -> false;
	private BiPredicate<ItemStack, Integer> extractPredicate = (stack, slot) -> false;
	
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
	
	private SimpleItemStorage(int size, SimpleItemStorage proxy) {
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
	
	public SimpleItemStorage insertPredicate(BiPredicate<ItemStack, Integer> slotInsertPredicate) {
		this.insertPredicate = slotInsertPredicate;
		updateProxies();
		return this;
	}
	
	public SimpleItemStorage extractPredicate(BiPredicate<ItemStack, Integer> slotExtractPredicate) {
		this.extractPredicate = slotExtractPredicate;
		updateProxies();
		return this;
	}
	
	public SimpleItemStorage sidings(StorageSiding[] sidings) {
		this.sidings = sidings;
		updateProxies();
		return this;
	}
	
	public SimpleItemStorage insertSlots(int[] insertSlots) {
		this.insertSlots = insertSlots;
		updateProxies();
		return this;
	}
	
	public SimpleItemStorage extractSlots(int[] extractSlots) {
		this.extractSlots = extractSlots;
		updateProxies();
		return this;
	}
	
	public SimpleItemStorage listener(Runnable listener) {
		this.listeners.add(listener);
		updateProxies();
		return this;
	}
	
	public SimpleItemStorage getProxy() {
		return proxy;
	}
	
	public SimpleItemStorage[] getProxies() {
		return new SimpleItemStorage[] { wildProxy, extractableProxy, insertableProxy };
	}
	
	public SimpleItemStorage getWildProxy() {
		return wildProxy;
	}
	
	public SimpleItemStorage getExtractableProxy() {
		return extractableProxy;
	}
	
	public SimpleItemStorage getInsertableProxy() {
		return insertableProxy;
	}
	
	public SimpleItemVariantStorage getStorage(int slot) {
		return storages.get(slot);
	}
	
	public ItemStack getVariant(int slot) {
		return getStorage(slot).getResource();
	}
	
	public SimpleItemVariantStorage[] slice(int... slots) {
		var slicedStorages = new SimpleItemVariantStorage[slots.length];
		
		for (var i = 0; i < slots.length; ++i) {
			slicedStorages[i] = getStorage(slots[i]);
		}
		
		return slicedStorages;
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
		updateProxies();
	}
	
	public boolean setSiding(Direction direction, StorageSiding siding) {
		var index = direction.ordinal();
		
		if (sidings[index] == siding) {
			return false;
		}
		
		sidings[index] = siding;
		notifyListeners();
		incrementVersion();
		
		return true;
	}
	
	public void incrementVersion() {
		if (proxy != null) {
			proxy.incrementVersion();
		} else {
			version += 1;
		}
	}
	
	public void notifyListeners() {
		if (proxy != null) {
			proxy.notifyListeners();
		} else {
			listeners.forEach(Runnable::run);
		}
	}
	
	public boolean canInsert(ItemStack stack, int slot) {
		return insertPredicate.test(stack, slot) && allowsInsertion;
	}
	
	public boolean canExtract(ItemStack stack, int slot) {
		return extractPredicate.test(stack, slot) && allowsExtraction;
	}
	
	public int insert(ItemStack stack, int maxAmount, boolean force, boolean simulate) {
		if (!allowsInsertion || stack.isEmpty() || maxAmount <= 0) {
			return 0;
		}
		
		var amount = 0;
		
		for (var slot : insertSlots) {
			if (!insertPredicate.test(stack, slot) && !force) {
				continue;
			}
			
			amount += storages.get(slot).insert(stack, maxAmount - amount, force, simulate);
			
			if (amount == maxAmount) {
				break;
			}
		}
		
		if (amount > 0 && !simulate) {
			notifyListeners();
			incrementVersion();
		}
		
		return amount;
	}
	
	public int extract(ItemStack stack, int maxAmount, boolean force, boolean simulate) {
		if (!allowsExtraction || stack.isEmpty() || maxAmount <= 0) {
			return 0;
		}
		
		var amount = 0;
		
		for (var slot : extractSlots) {
			if (!extractPredicate.test(getItem(slot), slot) && !force) {
				continue;
			}
			
			amount += storages.get(slot).extract(stack, maxAmount - amount, force, simulate);
			
			if (amount == maxAmount) {
				break;
			}
		}
		
		if (amount > 0 && !simulate) {
			notifyListeners();
			incrementVersion();
		}
		
		return amount;
	}
	
	public void writeToNbt(CompoundTag nbt) {
		writeToNbt(nbt, RegistryAccess.EMPTY);
	}
	
	public void writeToNbt(CompoundTag nbt, HolderLookup.Provider registries) {
		var sidingsNbt = new CompoundTag();
		
		for (var i = 0; i < sidings.length; ++i) {
			sidingsNbt.putInt(String.valueOf(i), sidings[i].ordinal());
		}
		
		nbt.put(SIDINGS_KEY, sidingsNbt);
		
		var storagesNbt = new CompoundTag();
		
		for (var i = 0; i < size; ++i) {
			var stack = getItem(i);
			
			if (!stack.isEmpty()) {
				var storageNbt = new CompoundTag();
				storageNbt.put(STACK_KEY, stack.save(registries, new CompoundTag()));
				
				storagesNbt.put(String.valueOf(i), storageNbt);
			}
		}
		
		nbt.put(STORAGES_KEY, storagesNbt);
	}
	
	public void readFromNbt(CompoundTag nbt) {
		readFromNbt(nbt, RegistryAccess.EMPTY);
	}
	
	public void readFromNbt(CompoundTag nbt, HolderLookup.Provider registries) {
		var sidingsNbt = nbt.getCompound(SIDINGS_KEY);
		
		for (var i = 0; i < sidings.length; ++i) {
			sidings[i] = StorageSiding.values()[sidingsNbt.getInt(String.valueOf(i))];
		}
		
		var storagesNbt = nbt.getCompound(STORAGES_KEY);
		
		for (var i = 0; i < size; ++i) {
			var storageNbt = storagesNbt.getCompound(String.valueOf(i));
			
			setItem(i, storageNbt.contains(STACK_KEY) ? ItemStack.parseOptional(registries, storageNbt.getCompound(STACK_KEY)) : ItemStack.EMPTY);
		}
	}
	
	private void updateProxies() {
		if (proxy != null) {
			return;
		}
		
		wildProxy = createProxy(true, true, ($, $$) -> true, ($, $$) -> true);
		
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
		
		extractableProxy = createProxy(false, true, insertPredicate, extractPredicate);
		insertableProxy = createProxy(true, false, insertPredicate, extractPredicate);
	}
	
	private SimpleItemStorage createProxy(boolean allowsInsertion, boolean allowsExtraction, BiPredicate<ItemStack, Integer> insertPredicate, BiPredicate<ItemStack, Integer> extractPredicate) {
		var createdProxy = new SimpleItemStorage(size, this);
		
		createdProxy.allowsInsertion = allowsInsertion;
		createdProxy.allowsExtraction = allowsExtraction;
		createdProxy.size = this.size;
		createdProxy.listeners = this.listeners;
		createdProxy.stacks = this.stacks;
		createdProxy.storages = new ArrayList<>();
		
		for (var i = 0; i < storages.size(); ++i) {
			var proxyStorage = new SimpleItemVariantStorage(createdProxy, i);
			proxyStorage.setOuterStorage(createdProxy);
			createdProxy.storages.add(proxyStorage);
		}
		
		createdProxy.insertPredicate = insertPredicate;
		createdProxy.extractPredicate = extractPredicate;
		createdProxy.sidings = this.sidings;
		createdProxy.insertSlots = this.insertSlots;
		createdProxy.extractSlots = this.extractSlots;
		
		return createdProxy;
	}
	
	public boolean supportsInsertion() {
		return insertSlots.length > 0 && allowsInsertion;
	}
	
	public boolean supportsExtraction() {
		return extractSlots.length > 0 && allowsExtraction;
	}
	
	public long getVersion() {
		return proxy != null ? proxy.getVersion() : version;
	}
	
	@Override
	public int getSlots() {
		return size;
	}
	
	@Override
	public ItemStack getStackInSlot(int slot) {
		return getItem(slot);
	}
	
	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		if (stack.isEmpty() || !allowsInsertion || !canInsert(stack, slot)) {
			return stack;
		}
		
		var inserted = storages.get(slot).insert(stack, stack.getCount(), false, simulate);
		
		if (inserted > 0 && !simulate) {
			notifyListeners();
			incrementVersion();
		}
		
		var remainder = stack.copy();
		remainder.shrink(inserted);
		
		return remainder;
	}
	
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		var stack = getItem(slot);
		
		if (stack.isEmpty() || amount <= 0 || !allowsExtraction || !canExtract(stack, slot)) {
			return ItemStack.EMPTY;
		}
		
		var result = stack.copy();
		var extracted = storages.get(slot).extract(stack, amount, false, simulate);
		
		if (extracted > 0 && !simulate) {
			notifyListeners();
			incrementVersion();
		}
		
		result.setCount(extracted);
		
		return result;
	}
	
	@Override
	public int getSlotLimit(int slot) {
		return getStorage(slot).getCapacity(getItem(slot));
	}
	
	@Override
	public boolean isItemValid(int slot, ItemStack stack) {
		return canInsert(stack, slot);
	}
	
	@Override
	public void setStackInSlot(int slot, ItemStack stack) {
		setItem(slot, stack);
	}
	
	@Override
	public int getContainerSize() {
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
	public ItemStack getItem(int slot) {
		return stacks.get(slot);
	}
	
	@Override
	public ItemStack removeItem(int slot, int amount) {
		var existingStack = stacks.get(slot);
		
		if (existingStack.isEmpty()) {
			return ItemStack.EMPTY;
		}
		
		var removedStack = existingStack.copy();
		removedStack.setCount(Math.min(existingStack.getCount(), amount));
		
		existingStack.shrink(removedStack.getCount());
		
		notifyListeners();
		incrementVersion();
		
		return removedStack;
	}
	
	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		var stack = stacks.get(slot);
		stacks.set(slot, ItemStack.EMPTY);
		
		notifyListeners();
		incrementVersion();
		
		return stack;
	}
	
	@Override
	public void setItem(int slot, ItemStack stack) {
		stacks.set(slot, stack);
		
		notifyListeners();
		incrementVersion();
	}
	
	@Override
	public void setChanged() {
		incrementVersion();
	}
	
	@Override
	public boolean stillValid(Player player) {
		return true;
	}
	
	@Override
	public void clearContent() {
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
