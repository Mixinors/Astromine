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
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import javax.annotation.Nullable;

public abstract class ExtendedEntity extends Entity implements FluidStorageSizeProvider, EnergyStorageSizeProvider {
	public static final String AMOUNT_KEY = "Amount";
	public static final String SIZE_KEY = "Size";
	
	public static final String ENERGY_STORAGE_KEY = "EnergyStorage";
	public static final String ITEM_STORAGE_KEY = "ItemStorage";
	public static final String FLUID_STORAGE_KEY = "FluidStorage";
	
	protected SimpleEnergyStorage energyStorage = null;
	protected SimpleItemStorage itemStorage = null;
	protected SimpleFluidStorage fluidStorage = null;
	
	public ExtendedEntity(EntityType<?> type, World world) {
		super(type, world);
	}
	
	@Override
	protected void writeCustomDataToNbt(NbtCompound nbt) {
		if (energyStorage != null) {
			var energyStorageNbt = new NbtCompound();
			
			energyStorageNbt.putLong(AMOUNT_KEY, energyStorage.amount);
			
			nbt.put(ENERGY_STORAGE_KEY, energyStorageNbt);
		}
		
		if (itemStorage != null) {
			var itemStorageNbt = new NbtCompound();
			itemStorageNbt.putInt(SIZE_KEY, itemStorage.getSize());
			
			itemStorage.writeToNbt(itemStorageNbt);
			
			nbt.put(ITEM_STORAGE_KEY, itemStorageNbt);
		}
		
		if (fluidStorage != null) {
			var fluidStorageNbt = new NbtCompound();
			fluidStorageNbt.putInt(SIZE_KEY, fluidStorage.getSize());
			
			fluidStorage.writeToNbt(fluidStorageNbt);
			
			nbt.put(FLUID_STORAGE_KEY, fluidStorageNbt);
		}
	}
	
	@Override
	protected void readCustomDataFromNbt(NbtCompound nbt) {
		if (nbt.contains(ENERGY_STORAGE_KEY)) {
			var energyStorageNbt = nbt.getCompound(ENERGY_STORAGE_KEY);
			
			energyStorage.amount = energyStorageNbt.getLong(AMOUNT_KEY);
		}
		
		if (nbt.contains(ITEM_STORAGE_KEY)) {
			var itemStorageNbt = nbt.getCompound(ITEM_STORAGE_KEY);
			
			itemStorage = new SimpleItemStorage(
					itemStorageNbt.getInt(SIZE_KEY)
			);
			
			itemStorage.readFromNbt(itemStorageNbt);
		}
		
		if (nbt.contains(FLUID_STORAGE_KEY)) {
			var fluidStorageNbt = nbt.getCompound(FLUID_STORAGE_KEY);
			
			fluidStorage = new SimpleFluidStorage(
					fluidStorageNbt.getInt(SIZE_KEY),
					getFluidStorageSize()
			);
			
			fluidStorage.readFromNbt(fluidStorageNbt);
		}
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
	public SimpleEnergyStorage getEnergyStorage() {
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
