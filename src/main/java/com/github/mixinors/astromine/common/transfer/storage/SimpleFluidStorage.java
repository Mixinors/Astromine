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
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.stream.IntStream;

public class SimpleFluidStorage implements IFluidHandler {
	public static final String SIDINGS_KEY = "Sidings";
	public static final String AMOUNT_KEY = "Amount";
	public static final String FLUID_KEY = "Fluid";
	public static final String STORAGES_KEY = "Storages";
	
	private int size;
	private long capacity;
	
	private List<Runnable> listeners;
	private List<SimpleFluidVariantStorage> storages;
	
	private final SimpleFluidStorage proxy;
	
	private BiPredicate<FluidStack, Integer> insertPredicate = null;
	private BiPredicate<FluidStack, Integer> extractPredicate = null;
	
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
	
	private SimpleFluidStorage(int size, long capacity, SimpleFluidStorage proxy) {
		this.size = size;
		this.capacity = Math.max(0L, capacity);
		this.proxy = proxy;
		
		this.listeners = new ArrayList<>();
		this.storages = new ArrayList<>(size);
		
		for (var i = 0; i < size; ++i) {
			this.storages.add(new SimpleFluidVariantStorage(capacity, i, this));
		}
		
		this.sidings = new StorageSiding[6];
		Arrays.fill(sidings, StorageSiding.NONE);
		
		this.insertSlots = IntStream.range(0, size).toArray();
		this.extractSlots = IntStream.range(0, size).toArray();
		
		updateProxies();
	}
	
	public SimpleFluidStorage insertPredicate(BiPredicate<FluidStack, Integer> slotInsertPredicate) {
		this.insertPredicate = slotInsertPredicate;
		updateProxies();
		return this;
	}
	
	public SimpleFluidStorage extractPredicate(BiPredicate<FluidStack, Integer> slotExtractPredicate) {
		this.extractPredicate = slotExtractPredicate;
		updateProxies();
		return this;
	}
	
	public SimpleFluidStorage sidings(StorageSiding[] sidings) {
		this.sidings = sidings;
		updateProxies();
		return this;
	}
	
	public SimpleFluidStorage insertSlots(int[] insertSlots) {
		this.insertSlots = insertSlots;
		updateProxies();
		return this;
	}
	
	public SimpleFluidStorage extractSlots(int[] extractSlots) {
		this.extractSlots = extractSlots;
		updateProxies();
		return this;
	}
	
	public SimpleFluidStorage listener(Runnable listener) {
		this.listeners.add(listener);
		updateProxies();
		return this;
	}
	
	public SimpleFluidStorage getProxy() {
		return proxy;
	}
	
	public SimpleFluidStorage[] getProxies() {
		return new SimpleFluidStorage[] { wildProxy, extractableProxy, insertableProxy };
	}
	
	public SimpleFluidStorage getWildProxy() {
		return wildProxy;
	}
	
	public SimpleFluidStorage getExtractableProxy() {
		return extractableProxy;
	}
	
	public SimpleFluidStorage getInsertableProxy() {
		return insertableProxy;
	}
	
	public SimpleFluidVariantStorage getStorage(int slot) {
		return storages.get(slot);
	}
	
	public FluidStack getVariant(int slot) {
		return getStorage(slot).getResource();
	}
	
	public SimpleFluidVariantStorage[] slice(int... slots) {
		var slicedStorages = new SimpleFluidVariantStorage[slots.length];
		
		for (var i = 0; i < slots.length; ++i) {
			slicedStorages[i] = getStorage(slots[i]);
		}
		
		return slicedStorages;
	}
	
	public int getSize() {
		return size;
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
	
	public boolean canInsert(FluidStack stack, int slot) {
		return (insertPredicate == null || insertPredicate.test(stack, slot)) && allowsInsertion;
	}
	
	public boolean canExtract(FluidStack stack, int slot) {
		return (extractPredicate == null || extractPredicate.test(stack, slot)) && allowsExtraction;
	}
	
	public long insert(FluidStack stack, long maxAmount, boolean force, boolean simulate) {
		if (!allowsInsertion || stack.isEmpty() || maxAmount <= 0L) {
			return 0L;
		}
		
		var amount = 0L;
		
		for (var slot : insertSlots) {
			if (!canInsert(stack, slot) && !force) {
				continue;
			}
			
			amount += storages.get(slot).insert(stack, maxAmount - amount, true, simulate);
			
			if (amount == maxAmount) {
				break;
			}
		}
		
		if (amount > 0L && !simulate) {
			notifyListeners();
			incrementVersion();
		}
		
		return amount;
	}
	
	public long extract(FluidStack stack, long maxAmount, boolean force, boolean simulate) {
		if (!allowsExtraction || stack.isEmpty() || maxAmount <= 0L) {
			return 0L;
		}
		
		var amount = 0L;
		
		for (var slot : extractSlots) {
			if (!canExtract(getStorage(slot).getResource(), slot) && !force) {
				continue;
			}
			
			amount += storages.get(slot).extract(stack, maxAmount - amount, true, simulate);
			
			if (amount == maxAmount) {
				break;
			}
		}
		
		if (amount > 0L && !simulate) {
			notifyListeners();
			incrementVersion();
		}
		
		return amount;
	}
	
	public void writeToNbt(CompoundTag nbt) {
		if (proxy != null) {
			return;
		}
		
		var sidingsNbt = new CompoundTag();
		
		for (var i = 0; i < sidings.length; ++i) {
			sidingsNbt.putInt(String.valueOf(i), sidings[i].ordinal());
		}
		
		nbt.put(SIDINGS_KEY, sidingsNbt);
		
		var storagesNbt = new CompoundTag();
		
		for (var i = 0; i < size; ++i) {
			var stack = storages.get(i).getResource();
			
			if (!stack.isEmpty()) {
				var storageNbt = new CompoundTag();
				
				storageNbt.putString(FLUID_KEY, BuiltInRegistries.FLUID.getKey(stack.getFluid()).toString());
				storageNbt.putLong(AMOUNT_KEY, stack.getAmount());
				
				storagesNbt.put(String.valueOf(i), storageNbt);
			}
		}
		
		nbt.put(STORAGES_KEY, storagesNbt);
	}
	
	public void readFromNbt(CompoundTag nbt) {
		if (proxy != null) {
			return;
		}
		
		var sidingsNbt = nbt.getCompound(SIDINGS_KEY);
		
		for (var i = 0; i < sidings.length; ++i) {
			sidings[i] = StorageSiding.values()[sidingsNbt.getInt(String.valueOf(i))];
		}
		
		var storagesNbt = nbt.getCompound(STORAGES_KEY);
		
		for (var i = 0; i < size; ++i) {
			var storageNbt = storagesNbt.getCompound(String.valueOf(i));
			
			if (storageNbt.contains(FLUID_KEY)) {
				var fluid = BuiltInRegistries.FLUID.get(ResourceLocation.parse(storageNbt.getString(FLUID_KEY)));
				var amount = storageNbt.getLong(AMOUNT_KEY);
				
				storages.get(i).setResource(fluid == Fluids.EMPTY ? FluidStack.EMPTY : new FluidStack(fluid, (int) Math.min(amount, Integer.MAX_VALUE)));
			} else {
				storages.get(i).setResource(FluidStack.EMPTY);
			}
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
	
	private SimpleFluidStorage createProxy(boolean allowsInsertion, boolean allowsExtraction, BiPredicate<FluidStack, Integer> insertPredicate, BiPredicate<FluidStack, Integer> extractPredicate) {
		var createdProxy = new SimpleFluidStorage(size, capacity, this);
		
		createdProxy.allowsInsertion = allowsInsertion;
		createdProxy.allowsExtraction = allowsExtraction;
		createdProxy.size = this.size;
		createdProxy.listeners = this.listeners;
		createdProxy.storages = this.storages;
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
	public int getTanks() {
		return size;
	}
	
	@Override
	public FluidStack getFluidInTank(int tank) {
		return getStorage(tank).getResource();
	}
	
	@Override
	public int getTankCapacity(int tank) {
		return (int) Math.min(getStorage(tank).getCapacity(), Integer.MAX_VALUE);
	}
	
	@Override
	public boolean isFluidValid(int tank, FluidStack stack) {
		return canInsert(stack, tank);
	}
	
	@Override
	public int fill(FluidStack resource, FluidAction action) {
		return (int) Math.min(insert(resource, resource.getAmount(), false, action.simulate()), Integer.MAX_VALUE);
	}
	
	@Override
	public FluidStack drain(FluidStack resource, FluidAction action) {
		var drained = extract(resource, resource.getAmount(), false, action.simulate());
		
		return drained <= 0L ? FluidStack.EMPTY : resource.copyWithAmount((int) Math.min(drained, Integer.MAX_VALUE));
	}
	
	@Override
	public FluidStack drain(int maxDrain, FluidAction action) {
		for (var slot : extractSlots) {
			var stack = getStorage(slot).getResource();
			
			if (!stack.isEmpty()) {
				var drained = extract(stack, maxDrain, false, action.simulate());
				
				if (drained > 0L) {
					return stack.copyWithAmount((int) Math.min(drained, Integer.MAX_VALUE));
				}
			}
		}
		
		return FluidStack.EMPTY;
	}
	
	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		
		if (!(object instanceof SimpleFluidStorage component)) {
			return false;
		}
		
		return Objects.equals(storages, component.storages);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(storages);
	}
}
