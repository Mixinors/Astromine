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

package com.github.mixinors.astromine.common.entity.base;

import com.github.mixinors.astromine.common.provider.EnergyStorageSizeProvider;
import com.github.mixinors.astromine.common.provider.FluidStorageSizeProvider;
import com.github.mixinors.astromine.common.transfer.storage.SimpleFluidStorage;
import com.github.mixinors.astromine.common.transfer.storage.SimpleItemStorage;
import com.github.mixinors.astromine.registry.common.AMNetworking;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import com.github.mixinors.astromine.common.transfer.storage.LongEnergyStorage;

public abstract class ExtendedEntity extends Entity implements FluidStorageSizeProvider, EnergyStorageSizeProvider {
	public static final String AMOUNT_KEY = "Amount";
	public static final String SIZE_KEY = "Size";
	
	public static final String ENERGY_STORAGE_KEY = "EnergyStorage";
	public static final String ITEM_STORAGE_KEY = "ItemStorage";
	public static final String FLUID_STORAGE_KEY = "FluidStorage";
	
	protected boolean syncItemStorage = true;
	protected boolean syncFluidStorage = true;
	
	protected LongEnergyStorage energyStorage = null;
	protected SimpleItemStorage itemStorage = null;
	protected SimpleFluidStorage fluidStorage = null;
	
	protected long lastItemStorageVersion = 0;
	protected long lastFluidStorageVersion = 0;
	
	public ExtendedEntity(EntityType<?> type, Level world) {
		super(type, world);
	}
	
	@Override
	protected void addAdditionalSaveData(CompoundTag nbt) {
		writeToNbt(nbt);
	}
	
	public void writeToNbt(CompoundTag nbt) {
		if (energyStorage != null) {
			var energyStorageNbt = new CompoundTag();
			
			energyStorageNbt.putLong(AMOUNT_KEY, energyStorage.amount);
			
			nbt.put(ENERGY_STORAGE_KEY, energyStorageNbt);
		}
		
		if (itemStorage != null) {
			var itemStorageNbt = new CompoundTag();
			itemStorageNbt.putInt(SIZE_KEY, itemStorage.getSize());
			
			itemStorage.writeToNbt(itemStorageNbt, registryAccess());
			
			nbt.put(ITEM_STORAGE_KEY, itemStorageNbt);
		}
		
		if (fluidStorage != null) {
			var fluidStorageNbt = new CompoundTag();
			fluidStorageNbt.putInt(SIZE_KEY, fluidStorage.getSize());
			
			fluidStorage.writeToNbt(fluidStorageNbt);
			
			nbt.put(FLUID_STORAGE_KEY, fluidStorageNbt);
		}
	}
	
	@Override
	protected void readAdditionalSaveData(CompoundTag nbt) {
		readFromNbt(nbt);
	}
	
	public void readFromNbt(CompoundTag nbt) {
		if (nbt.contains(ENERGY_STORAGE_KEY)) {
			var energyStorageNbt = nbt.getCompound(ENERGY_STORAGE_KEY);
			
			energyStorage.amount = energyStorageNbt.getLong(AMOUNT_KEY);
		}
		
		if (nbt.contains(ITEM_STORAGE_KEY)) {
			var itemStorageNbt = nbt.getCompound(ITEM_STORAGE_KEY);
			
			itemStorage.readFromNbt(itemStorageNbt, registryAccess());
		}
		
		if (nbt.contains(FLUID_STORAGE_KEY)) {
			var fluidStorageNbt = nbt.getCompound(FLUID_STORAGE_KEY);
			
			fluidStorage.readFromNbt(fluidStorageNbt);
		}
	}
	
	public void syncData() {
		if (!level().isClientSide) {
			var id = getId();
			
			var nbt = new CompoundTag();
			
			writeToNbt(nbt);
			
			if (hasItemStorage()) {
				var itemStorage = getItemStorage();
				
				if (!syncItemStorage && itemStorage.getVersion() == lastItemStorageVersion) {
					nbt.remove(ITEM_STORAGE_KEY);
				} else {
					syncItemStorage = false;
					
					lastItemStorageVersion = itemStorage.getVersion();
				}
			}
			
			if (hasFluidStorage()) {
				var fluidStorage = getFluidStorage();
				
				if (!syncFluidStorage && fluidStorage.getVersion() == lastFluidStorageVersion) {
					nbt.remove(FLUID_STORAGE_KEY);
				} else {
					syncFluidStorage = false;
					
					lastFluidStorageVersion = fluidStorage.getVersion();
				}
			}
			
			PacketDistributor.sendToPlayersTrackingEntity(this, new AMNetworking.SyncEntityPayload(id, nbt));
		}
	}
	
	@Override
	public void startSeenByPlayer(ServerPlayer player) {
		super.startSeenByPlayer(player);
		
		var id = getId();
		
		var nbt = new CompoundTag();
		
		writeToNbt(nbt);
		
		PacketDistributor.sendToPlayer(player, new AMNetworking.SyncEntityPayload(id, nbt));
	}
	
	/**
	 * Marks this block entity as requiring a full {@link #itemStorage} sync in the next server to client packet.
	 */
	public void setSyncItemStorage(boolean syncItemStorage) {
		this.syncItemStorage = syncItemStorage;
	}
	
	/**
	 * Marks this block entity as requiring a full {@link #fluidStorage} sync in the next server to client packet.
	 */
	public void setSyncFluidStorage(boolean syncFluidStorage) {
		this.syncFluidStorage = syncFluidStorage;
	}
	
	public boolean hasEnergyStorage() {
		return getEnergyStorage() != null;
	}
	
	public boolean hasFluidStorage() {
		return getFluidStorage() != null;
	}
	
	public boolean hasItemStorage() {
		return getItemStorage() != null;
	}
	
	@Nullable
	public LongEnergyStorage getEnergyStorage() {
		return energyStorage;
	}
	
	@Nullable
	public SimpleItemStorage getItemStorage() {
		return itemStorage;
	}
	
	@Nullable
	public SimpleFluidStorage getFluidStorage() {
		return fluidStorage;
	}
	
	@Override
	public long getEnergyStorageSize() {
		return 0;
	}
	
	@Override
	public long getFluidStorageSize() {
		return 0;
	}
}
