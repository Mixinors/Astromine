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

import com.google.common.collect.ImmutableList;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;

import java.util.ArrayList;

/**
 * <p>A {@link SingleVariantStorage} implementation for {@link FluidVariant}s.</p>
 */
public class SimpleFluidVariantStorage extends SingleVariantStorage<FluidVariant> {
	private long capacity;
	private int slot;
	
	public SimpleFluidVariantStorage(long capacity, int slot) {
		this.capacity = capacity;
		this.slot = slot;
	}
	
	/**
	 * Sets this storage's amount.
	 *
	 * @param amount the amount to be set.
	 */
	public void setAmount(long amount) {
		this.amount = amount;
	}
	
	/**
	 * Sets this storage's variant.
	 *
	 * @param variant the variant to be set.
	 */
	public void setResource(FluidVariant variant) {
		this.variant = variant;
	}
	
	/**
	 * Returns this storage's slot.
	 */
	public int getSlot() {
		return slot;
	}
	
	/**
	 * Returns this storage's capacity.
	 */
	@Override
	public long getCapacity() {
		return capacity;
	}
	
	/**
	 * Sets this storage's capacity.
	 *
	 * @param capacity the capacity to be set.
	 */
	public void setCapacity(long capacity) {
		this.capacity = capacity;
	}
	
	/**
	 * Returns this storage's default variant.
	 */
	@Override
	protected FluidVariant getBlankVariant() {
		return FluidVariant.blank();
	}
	
	/**
	 * Returns this storage's capacity for the given variant.
	 *
	 * @param variant the variant.
	 */
	@Override
	protected long getCapacity(FluidVariant variant) {
		return capacity;
	}
	
	/**
	 * A proxied {@link SimpleFluidVariantStorage} implementation, backed by a proxy {@link SimpleFluidStorage} and {@link SimpleFluidVariantStorage}.
	 */
	public static class Proxy extends SimpleFluidVariantStorage {
		private final SimpleFluidStorage proxy;
		
		private final SimpleFluidVariantStorage proxyStorage;
		
		public Proxy(SimpleFluidStorage proxy, SimpleFluidVariantStorage proxiedStorage) {
			super(proxiedStorage.getCapacity(), proxiedStorage.getSlot());
			
			this.proxy = proxy;
			this.proxyStorage = proxiedStorage;
			
			this.amount = proxiedStorage.amount;
			this.variant = proxiedStorage.variant;
		}
		
		/**
		 * Returns this storage's proxy.
		 */
		public SimpleFluidStorage getProxy() {
			return proxy;
		}
		
		/**
		 * Returns this storage's proxy storage.
		 */
		public SimpleFluidVariantStorage getProxyStorage() {
			return proxyStorage;
		}
		
		@Override
		protected void onFinalCommit() {
			super.onFinalCommit();
			
			if (proxy != null) {
				proxy.notifyListeners();
				
				proxy.incrementVersion();
				
				var proxies = new ArrayList<SimpleFluidStorage>();
				
				if (proxy.getProxy() != null) {
					proxies.addAll(ImmutableList.copyOf(proxy.getProxy().getProxies()));
					proxies.add(proxy.getProxy());
				} else {
					proxies.addAll(ImmutableList.copyOf(proxy.getProxies()));
					proxies.add(proxy);
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
		
		@Override
		public long insert(FluidVariant insertedVariant, long maxAmount, TransactionContext transaction) {
			return insert(insertedVariant, maxAmount, transaction, false);
		}
		
		public long insert(FluidVariant insertedVariant, long maxAmount, TransactionContext transaction, boolean force) {
			StoragePreconditions.notBlankNotNegative(insertedVariant, maxAmount);
			
			if (proxy != null && !proxy.canInsert(insertedVariant, getSlot()) && !force) {
				return 0;
			}
			
			var inserted = proxyStorage.insert(insertedVariant, maxAmount, transaction);
			
			if (inserted > 0) {
				updateSnapshots(transaction);
			}
			
			return inserted;
		}
		
		@Override
		public long extract(FluidVariant extractedVariant, long maxAmount, TransactionContext transaction) {
			return extract(extractedVariant, maxAmount, transaction, false);
		}
		
		public long extract(FluidVariant extractedVariant, long maxAmount, TransactionContext transaction, boolean force) {
			StoragePreconditions.notBlankNotNegative(extractedVariant, maxAmount);
			
			if (proxy != null && !proxy.canExtract(extractedVariant, getSlot()) && !force) {
				return 0;
			}
			
			var extracted = proxyStorage.extract(extractedVariant, maxAmount, transaction);
			
			if (extracted > 0) {
				updateSnapshots(transaction);
			}
			
			return extracted;
		}
		
		@Override
		public long getAmount() {
			return proxyStorage == null ? 0 : proxyStorage.getAmount();
		}
		
		@Override
		public void setAmount(long amount) {
			this.amount = amount;
			
			if (proxyStorage != null) {
				proxyStorage.setAmount(amount);
			}
		}
		
		@Override
		public void setResource(FluidVariant variant) {
			this.variant = variant;
			
			if (proxy != null) {
				proxyStorage.setResource(variant);
			}
		}
		
		@Override
		public int getSlot() {
			return proxyStorage == null ? 0 : proxyStorage.getSlot();
		}
		
		@Override
		public long getVersion() {
			return proxyStorage == null ? 0 : proxyStorage.getVersion();
		}
		
		@Override
		public long getCapacity() {
			return proxyStorage == null ? 0 : proxyStorage.getCapacity();
		}
		
		@Override
		protected long getCapacity(FluidVariant variant) {
			return proxyStorage == null ? 0 : proxyStorage.getCapacity();
		}
		
		@Override
		public FluidVariant getResource() {
			return proxyStorage == null ? FluidVariant.blank() : proxyStorage.getResource();
		}
		
		@Override
		protected FluidVariant getBlankVariant() {
			return proxyStorage == null ? FluidVariant.blank() : proxyStorage.getBlankVariant();
		}
	}
}
