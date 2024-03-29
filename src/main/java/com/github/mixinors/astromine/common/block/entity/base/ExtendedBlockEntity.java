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

package com.github.mixinors.astromine.common.block.entity.base;

import com.github.mixinors.astromine.common.block.base.BlockWithEntity;
import com.github.mixinors.astromine.common.tick.Tickable;
import com.github.mixinors.astromine.common.transfer.RedstoneType;
import com.github.mixinors.astromine.common.transfer.StorageSiding;
import com.github.mixinors.astromine.common.transfer.storage.SimpleFluidStorage;
import com.github.mixinors.astromine.common.transfer.storage.SimpleItemStorage;
import com.github.mixinors.astromine.common.util.DirectionUtils;
import dev.architectury.hooks.block.BlockEntityHooks;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.EnergyStorageUtil;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Supplier;

public abstract class ExtendedBlockEntity extends BlockEntity implements Tickable {
	public static final String REDSTONE_TYPE_KEY = "RedstoneType";
	
	public static final String AMOUNT_KEY = "Amount";
	
	public static final String ENERGY_STORAGE_KEY = "EnergyStorage";
	public static final String ITEM_STORAGE_KEY = "ItemStorage";
	public static final String FLUID_STORAGE_KEY = "FluidStorage";
	
	public static final String ITEM_STORAGE_SIDINGS_KEY = "ItemStorageSidings";
	public static final String FLUID_STORAGE_SIDINGS_KEY = "FluidStorageSidings";
	
	public static final String PROGRESS_KEY = "Progress";
	public static final String LIMIT_KEY = "Limit";
	
	private final boolean[] activity = { false, false, false, false, false };
	
	protected boolean active = false;
	
	public double progress = 0.0D;
	public double limit = 0.0D;
	
	protected boolean syncItemStorage = true;
	protected boolean syncFluidStorage = true;
	
	protected RedstoneType redstoneType = RedstoneType.WORK_ALWAYS;
	
	protected SimpleEnergyStorage energyStorage = null;
	protected SimpleItemStorage itemStorage = null;
	protected SimpleFluidStorage fluidStorage = null;
	
	protected StorageSiding[] lastItemStorageSidings = new StorageSiding[6];
	protected StorageSiding[] lastFluidStorageSidings = new StorageSiding[6];
	
	protected long lastItemStorageVersion = 0;
	protected long lastFluidStorageVersion = 0;
	
	public ExtendedBlockEntity(Supplier<? extends BlockEntityType<?>> type, BlockPos blockPos, BlockState blockState) {
		super(type.get(), blockPos, blockState);
	}
	
	@Override
	public void tick() {
		if (!hasWorld() || world.isClient) {
			return;
		}
		
		// Sync with nearby players.
		for (var player : world.getPlayers()) {
			if (player.squaredDistanceTo(getPos().getX(), getPos().getY(), getPos().getZ()) < 8 * 8) {
				syncData();
			}
		}
		
		// Trigger a block update if item sidings have changed.
		if (itemStorage != null) {
			if (!Arrays.equals(lastItemStorageSidings, itemStorage.getSidings())) {
				for (var directions : DirectionUtils.VALUES) {
					world.getBlockState(getPos().offset(directions)).neighborUpdate(world, getPos().offset(directions), getCachedState().getBlock(), getPos(), false);
				}
			}
			
			lastItemStorageSidings = itemStorage.getSidings().clone();
		}
		
		// Trigger a block update if fluid sidings have changed.
		if (fluidStorage != null) {
			if (!Arrays.equals(lastFluidStorageSidings, fluidStorage.getSidings())) {
				for (var directions : DirectionUtils.VALUES) {
					world.getBlockState(getPos().offset(directions)).neighborUpdate(world, getPos().offset(directions), getCachedState().getBlock(), getPos(), false);
				}
			}
			
			lastFluidStorageSidings = fluidStorage.getSidings().clone();
		}
		
		try (var transaction = Transaction.openOuter()) {
			for (var direction : DirectionUtils.VALUES) {
				var theirPos = getPos().offset(direction);
				
				var theirItemStorage = ItemStorage.SIDED.find(world, theirPos, direction.getOpposite());
				var ourItemStorage = ItemStorage.SIDED.find(world, pos, direction);
				
				if (ourItemStorage != null && theirItemStorage != null) {
					StorageUtil.move(ourItemStorage, theirItemStorage, (variant) -> theirItemStorage.exactView(transaction, variant) == null || ourItemStorage.exactView(transaction, variant).getAmount() > theirItemStorage.exactView(transaction, variant).getAmount(), 1, transaction);
				}
				
				var theirFluidStorage = FluidStorage.SIDED.find(world, theirPos, direction.getOpposite());
				var ourFluidStorage = FluidStorage.SIDED.find(world, pos, direction);
				
				if (ourFluidStorage != null && theirFluidStorage != null) {
					StorageUtil.move(ourFluidStorage, theirFluidStorage, (variant) -> theirFluidStorage.exactView(transaction, variant) == null || ourFluidStorage.exactView(transaction, variant).getAmount() > theirFluidStorage.exactView(transaction, variant).getAmount(), FluidConstants.BUCKET, transaction);
				}
			}
			
			moveEnergyAveraged(transaction);
			
			transaction.commit();
		}
		
		if (world.getBlockState(getPos()).contains(BlockWithEntity.ACTIVE)) {
			if (activity.length - 1 >= 0) {
				System.arraycopy(activity, 1, activity, 0, activity.length - 1);
			}
			
			activity[4] = active;
			
			var blockStateActive = world.getBlockState(getPos()).get(BlockWithEntity.ACTIVE);
			
			if (!blockStateActive && active && !activity[0]) {
				world.setBlockState(getPos(), world.getBlockState(getPos()).with(BlockWithEntity.ACTIVE, true));
			} else if (blockStateActive && !active && activity[0]) {
				world.setBlockState(getPos(), world.getBlockState(getPos()).with(BlockWithEntity.ACTIVE, false));
			}
		}
	}
	
	// Transacts energy to the neighboring blocks by averaging the available energy.
	// This will make sure everyone has the same amount of energy.
	private void moveEnergyAveraged(Transaction transaction) {
		record EnergyPair(
				long maxAmount,
				EnergyStorage our,
				EnergyStorage their
		) {}
		
		var list = new ArrayList<EnergyPair>();
		var offering = 0L;
		var requesting = 0L;
		
		for (var direction : DirectionUtils.VALUES) {
			var theirPos = getPos().offset(direction);
			var ourEnergyStorage = EnergyStorage.SIDED.find(world, pos, direction);
			
			if (ourEnergyStorage != null && ourEnergyStorage.supportsExtraction() && ourEnergyStorage.getAmount() > 0) {
				var theirEnergyStorage = EnergyStorage.SIDED.find(world, theirPos, direction.getOpposite());
				
				if (theirEnergyStorage != null && theirEnergyStorage.supportsInsertion()) {
					// We are an output only block entity, so we should transfer all energy to the other storage.
					var maxAmount = !ourEnergyStorage.supportsInsertion() ? Long.MAX_VALUE
							// We should maintain an equilibrium of energy between us.
							: ourEnergyStorage.getAmount() - theirEnergyStorage.getAmount();
					
					if (maxAmount > 0) {
						try (var extractionTestTransaction = Transaction.openNested(transaction)) {
							maxAmount = ourEnergyStorage.extract(maxAmount, extractionTestTransaction);
						}
					}
					
					if (maxAmount > 0) {
						try (var insertionTestTransaction = Transaction.openNested(transaction)) {
							maxAmount = theirEnergyStorage.insert(maxAmount, insertionTestTransaction);
						}
					}
					
					if (maxAmount > 0) {
						offering = Math.max(offering, ourEnergyStorage.getAmount());
						requesting += maxAmount;
						list.add(new EnergyPair(maxAmount, ourEnergyStorage, theirEnergyStorage));
					}
				}
			}
		}
		
		list.sort(Comparator.comparingLong(EnergyPair::maxAmount));
		
		for (var pair : list) {
			var move = (long) Math.ceil(pair.maxAmount * MathHelper.clamp(requesting <= 0 ? 0.0 : (double) offering / requesting, 0.0, 1.0));
			
			EnergyStorageUtil.move(pair.our, pair.their, move, transaction);
		}
	}
	
	@Override
	public void writeNbt(NbtCompound nbt) {
		nbt.putString(REDSTONE_TYPE_KEY, redstoneType.name());
		
		nbt.putDouble(PROGRESS_KEY, progress);
		nbt.putDouble(LIMIT_KEY, limit);
		
		if (energyStorage != null) {
			var energyStorageNbt = new NbtCompound();
			
			energyStorageNbt.putLong(AMOUNT_KEY, energyStorage.amount);
			
			nbt.put(ENERGY_STORAGE_KEY, energyStorageNbt);
		}
		
		if (itemStorage != null) {
			var itemStorageNbt = new NbtCompound();
			
			itemStorage.writeToNbt(itemStorageNbt);
			
			nbt.put(ITEM_STORAGE_KEY, itemStorageNbt);
		}
		
		if (fluidStorage != null) {
			var fluidStorageNbt = new NbtCompound();
			
			fluidStorage.writeToNbt(fluidStorageNbt);
			
			nbt.put(FLUID_STORAGE_KEY, fluidStorageNbt);
		}
		
		super.writeNbt(nbt);
	}
	
	@Override
	public void readNbt(@NotNull NbtCompound nbt) {
		if (nbt.contains(REDSTONE_TYPE_KEY)) {
			redstoneType = RedstoneType.valueOf(nbt.getString(REDSTONE_TYPE_KEY));
		}
		
		if (nbt.contains(PROGRESS_KEY)) {
			progress = nbt.getDouble(PROGRESS_KEY);
		}
		
		if (nbt.contains(LIMIT_KEY)) {
			limit = nbt.getDouble(LIMIT_KEY);
		}
		
		if (nbt.contains(ENERGY_STORAGE_KEY)) {
			var energyStorageNbt = nbt.getCompound(ENERGY_STORAGE_KEY);
			
			energyStorage.amount = energyStorageNbt.getLong(AMOUNT_KEY);
		}
		
		if (nbt.contains(ITEM_STORAGE_KEY)) {
			var itemStorageNbt = nbt.getCompound(ITEM_STORAGE_KEY);
			
			itemStorage.readFromNbt(itemStorageNbt);
		} else if (nbt.contains(ITEM_STORAGE_SIDINGS_KEY)) {
			var sidings = getItemStorage().getSidings();
			
			var sidingsNbt = nbt.getCompound(ITEM_STORAGE_SIDINGS_KEY);
			
			for (var i = 0; i < sidings.length; ++i) {
				sidings[i] = StorageSiding.values()[sidingsNbt.getInt(String.valueOf(i))];
			}
		}
		
		if (nbt.contains(FLUID_STORAGE_KEY)) {
			var fluidStorageNbt = nbt.getCompound(FLUID_STORAGE_KEY);
			
			fluidStorage.readFromNbt(fluidStorageNbt);
		} else if (nbt.contains(FLUID_STORAGE_SIDINGS_KEY)) {
			var sidings = getFluidStorage().getSidings();
			
			var sidingsNbt = nbt.getCompound(FLUID_STORAGE_SIDINGS_KEY);
			
			for (var i = 0; i < sidings.length; ++i) {
				sidings[i] = StorageSiding.values()[sidingsNbt.getInt(String.valueOf(i))];
			}
		}
		
		super.readNbt(nbt);
	}
	
	@Override
	public NbtCompound toInitialChunkDataNbt() {
		var nbt = new NbtCompound();
		
		writeNbt(nbt);
		
		if (hasItemStorage()) {
			var itemStorage = getItemStorage();
			
			if (!syncItemStorage && itemStorage.getVersion() == lastItemStorageVersion) {
				nbt.remove(ITEM_STORAGE_KEY);
				
				var sidings = itemStorage.getSidings();
				
				var sidingsNbt = new NbtCompound();
				
				for (var i = 0; i < sidings.length; ++i) {
					sidingsNbt.putInt(String.valueOf(i), sidings[i].ordinal());
				}
				
				nbt.put(ITEM_STORAGE_SIDINGS_KEY, sidingsNbt);
			} else {
				syncItemStorage = false;
				
				lastItemStorageVersion = itemStorage.getVersion();
			}
		}
		
		if (hasFluidStorage()) {
			var fluidStorage = getFluidStorage();
			
			if (!syncFluidStorage && fluidStorage.getVersion() == lastFluidStorageVersion) {
				nbt.remove(FLUID_STORAGE_KEY);
				
				var sidings = fluidStorage.getSidings();
				
				var sidingsNbt = new NbtCompound();
				
				for (var i = 0; i < sidings.length; ++i) {
					sidingsNbt.putInt(String.valueOf(i), sidings[i].ordinal());
				}
				
				nbt.put(FLUID_STORAGE_SIDINGS_KEY, sidingsNbt);
			} else {
				syncItemStorage = false;
				
				lastFluidStorageVersion = fluidStorage.getVersion();
			}
		}
		
		return nbt;
	}
	
	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
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
	
	/**
	 * Asserts whether this block entity should run or not.
	 */
	public boolean shouldRun() {
		var powered = world.getReceivedRedstonePower(getPos()) > 0;
		var shouldRun = redstoneType.shouldRun(powered);
		
		active = shouldRun;
		
		return shouldRun;
	}
	
	/**
	 * Schedules this block entity for server to client sync.
	 */
	public void syncData() {
		BlockEntityHooks.syncData(this);
	}
	
	/**
	 * Returns this block entity's {@link #redstoneType}.
	 */
	public RedstoneType getRedstoneType() {
		return redstoneType;
	}
	
	/**
	 * Sets this block entity's {@link #redstoneType}.
	 */
	public void setRedstoneControl(RedstoneType redstoneType) {
		this.redstoneType = redstoneType;
	}
	
	/**
	 * Asserts whether this block entity has a {@link SimpleEnergyStorage} or not.
	 */
	public boolean hasEnergyStorage() {
		return getEnergyStorage() != null;
	}
	
	/**
	 * Asserts whether this block entity has a {@link SimpleFluidStorage} or not.
	 */
	public boolean hasFluidStorage() {
		return getFluidStorage() != null;
	}
	
	/**
	 * Asserts whether this block entity has a {@link SimpleItemStorage} or not.
	 */
	public boolean hasItemStorage() {
		return getItemStorage() != null;
	}
	
	/**
	 * Returns this block entity's {@link SimpleEnergyStorage}.
	 */
	@Nullable
	public SimpleEnergyStorage getEnergyStorage() {
		return energyStorage;
	}
	
	/**
	 * Returns this block entity's {@link SimpleFluidStorage}.
	 */
	@Nullable
	public SimpleFluidStorage getFluidStorage() {
		return fluidStorage;
	}
	
	/**
	 * Returns this block entity's {@link SimpleItemStorage}.
	 */
	@Nullable
	public SimpleItemStorage getItemStorage() {
		return itemStorage;
	}
}
