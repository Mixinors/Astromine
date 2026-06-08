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
import com.github.mixinors.astromine.common.block.network.CableBlock;
import com.github.mixinors.astromine.common.tick.Tickable;
import com.github.mixinors.astromine.common.transfer.RedstoneType;
import com.github.mixinors.astromine.common.transfer.StorageSiding;
import com.github.mixinors.astromine.common.transfer.StorageType;
import com.github.mixinors.astromine.common.transfer.storage.LongEnergyStorage;
import com.github.mixinors.astromine.common.transfer.storage.SimpleFluidStorage;
import com.github.mixinors.astromine.common.transfer.storage.SimpleItemStorage;
import com.github.mixinors.astromine.common.util.DirectionUtils;
import com.github.mixinors.astromine.common.util.NetworkUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

public abstract class ExtendedBlockEntity extends BlockEntity implements Tickable {
	public static final ModelProperty<SidingModelData> SIDING_MODEL_DATA = new ModelProperty<>();

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
	
	protected LongEnergyStorage energyStorage = null;
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
		if (!hasLevel() || level.isClientSide) {
			return;
		}
		
		// Sync with nearby players.
		for (var player : level.players()) {
			if (player.distanceToSqr(getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ()) < 8 * 8) {
				syncData();
			}
		}
		
		// Trigger a block update if item sidings have changed.
		if (itemStorage != null) {
			if (!Arrays.equals(lastItemStorageSidings, itemStorage.getSidings())) {
				notifySidingChanged();
			}
			
			lastItemStorageSidings = itemStorage.getSidings().clone();
		}
		
		// Trigger a block update if fluid sidings have changed.
		if (fluidStorage != null) {
			if (!Arrays.equals(lastFluidStorageSidings, fluidStorage.getSidings())) {
				notifySidingChanged();
			}
			
			lastFluidStorageSidings = fluidStorage.getSidings().clone();
		}
		
		moveItemsAveraged();
		moveFluidsAveraged();
		moveEnergyAveraged();
		
		if (level.getBlockState(getBlockPos()).hasProperty(BlockWithEntity.ACTIVE)) {
			if (activity.length - 1 >= 0) {
				System.arraycopy(activity, 1, activity, 0, activity.length - 1);
			}
			
			activity[4] = active;
			
			var blockStateActive = level.getBlockState(getBlockPos()).getValue(BlockWithEntity.ACTIVE);
			
			if (!blockStateActive && active && !activity[0]) {
				level.setBlockAndUpdate(getBlockPos(), level.getBlockState(getBlockPos()).setValue(BlockWithEntity.ACTIVE, true));
			} else if (blockStateActive && !active && activity[0]) {
				level.setBlockAndUpdate(getBlockPos(), level.getBlockState(getBlockPos()).setValue(BlockWithEntity.ACTIVE, false));
			}
		}
	}
	
	// Transacts energy to the neighboring blocks by averaging the available energy.
	// This will make sure everyone has the same amount of energy.
	private void moveEnergyAveraged() {
		record EnergyPair(
				long maxAmount,
				IEnergyStorage our,
				IEnergyStorage their
		) {}
		
		var list = new ArrayList<EnergyPair>();
		var offering = 0L;
		var requesting = 0L;
		
		for (var direction : DirectionUtils.VALUES) {
			var theirPos = getBlockPos().relative(direction);
			var ourEnergyStorage = level.getCapability(Capabilities.EnergyStorage.BLOCK, worldPosition, direction);
			
			if (ourEnergyStorage != null && ourEnergyStorage.canExtract() && LongEnergyStorage.getAmount(ourEnergyStorage) > 0) {
				var theirEnergyStorage = level.getCapability(Capabilities.EnergyStorage.BLOCK, theirPos, direction.getOpposite());
				
				if (theirEnergyStorage != null && theirEnergyStorage.canReceive()) {
					// We are an output only block entity, so we should transfer all energy to the other storage.
					var maxAmount = !ourEnergyStorage.canReceive() ? Long.MAX_VALUE
							// We should maintain an equilibrium of energy between us.
							: LongEnergyStorage.getAmount(ourEnergyStorage) - LongEnergyStorage.getAmount(theirEnergyStorage);
					
					if (maxAmount > 0) {
						maxAmount = LongEnergyStorage.extract(ourEnergyStorage, maxAmount, true);
					}
					
					if (maxAmount > 0) {
						maxAmount = LongEnergyStorage.insert(theirEnergyStorage, maxAmount, true);
					}
					
					if (maxAmount > 0) {
						offering = Math.max(offering, LongEnergyStorage.getAmount(ourEnergyStorage));
						requesting += maxAmount;
						list.add(new EnergyPair(maxAmount, ourEnergyStorage, theirEnergyStorage));
					}
				}
			}
		}
		
		list.sort(Comparator.comparingLong(EnergyPair::maxAmount));
		
		for (var pair : list) {
			var move = (long) Math.ceil(pair.maxAmount * Mth.clamp(requesting <= 0 ? 0.0 : (double) offering / requesting, 0.0, 1.0));
			
			LongEnergyStorage.move(pair.our, pair.their, move);
		}
	}

	private void moveItemsAveraged() {
		for (var direction : DirectionUtils.VALUES) {
			var theirPos = getBlockPos().relative(direction);
			var ourItemStorage = level.getCapability(Capabilities.ItemHandler.BLOCK, worldPosition, direction);
			var theirItemStorage = level.getCapability(Capabilities.ItemHandler.BLOCK, theirPos, direction.getOpposite());

			if (ourItemStorage == null || theirItemStorage == null) {
				continue;
			}

			moveOneItemIfImbalanced(ourItemStorage, theirItemStorage);
		}
	}

	private static void moveOneItemIfImbalanced(IItemHandler source, IItemHandler destination) {
		for (var slot = 0; slot < source.getSlots(); ++slot) {
			var sourceStack = source.getStackInSlot(slot);

			if (sourceStack.isEmpty() || !destinationHasLess(destination, sourceStack)) {
				continue;
			}

			var extracted = source.extractItem(slot, 1, true);

			if (extracted.isEmpty()) {
				continue;
			}

			var remainder = ItemHandlerHelper.insertItem(destination, extracted, true);
			var accepted = extracted.getCount() - remainder.getCount();

			if (accepted > 0) {
				var moved = source.extractItem(slot, accepted, false);
				ItemHandlerHelper.insertItem(destination, moved, false);
				return;
			}
		}
	}

	private static boolean destinationHasLess(IItemHandler destination, ItemStack sourceStack) {
		var sourceAmount = sourceStack.getCount();
		var destinationAmount = 0;
		var foundMatchingStack = false;

		for (var slot = 0; slot < destination.getSlots(); ++slot) {
			var stack = destination.getStackInSlot(slot);

			if (stack.isEmpty()) {
				return true;
			}

			if (ItemStack.matches(stack, sourceStack)) {
				foundMatchingStack = true;
				destinationAmount += stack.getCount();
			}
		}

		return !foundMatchingStack || sourceAmount > destinationAmount;
	}

	private void moveFluidsAveraged() {
		for (var direction : DirectionUtils.VALUES) {
			var theirPos = getBlockPos().relative(direction);
			var ourFluidStorage = level.getCapability(Capabilities.FluidHandler.BLOCK, worldPosition, direction);
			var theirFluidStorage = level.getCapability(Capabilities.FluidHandler.BLOCK, theirPos, direction.getOpposite());

			if (ourFluidStorage == null || theirFluidStorage == null) {
				continue;
			}

			moveFluidIfImbalanced(ourFluidStorage, theirFluidStorage);
		}
	}

	private static void moveFluidIfImbalanced(IFluidHandler source, IFluidHandler destination) {
		for (var tank = 0; tank < source.getTanks(); ++tank) {
			var sourceStack = source.getFluidInTank(tank);

			if (sourceStack.isEmpty() || !destinationHasLess(destination, sourceStack)) {
				continue;
			}

			var request = sourceStack.copyWithAmount(Math.min(sourceStack.getAmount(), FluidType.BUCKET_VOLUME));
			var drained = source.drain(request, IFluidHandler.FluidAction.SIMULATE);

			if (drained.isEmpty()) {
				continue;
			}

			var accepted = destination.fill(drained, IFluidHandler.FluidAction.SIMULATE);

			if (accepted > 0) {
				var moved = source.drain(drained.copyWithAmount(accepted), IFluidHandler.FluidAction.EXECUTE);
				destination.fill(moved, IFluidHandler.FluidAction.EXECUTE);
				return;
			}
		}
	}

	private static boolean destinationHasLess(IFluidHandler destination, FluidStack sourceStack) {
		var sourceAmount = sourceStack.getAmount();
		var destinationAmount = 0;
		var foundMatchingStack = false;

		for (var tank = 0; tank < destination.getTanks(); ++tank) {
			var stack = destination.getFluidInTank(tank);

			if (stack.isEmpty()) {
				return true;
			}

			if (FluidStack.isSameFluidSameComponents(stack, sourceStack)) {
				foundMatchingStack = true;
				destinationAmount += stack.getAmount();
			}
		}

		return !foundMatchingStack || sourceAmount > destinationAmount;
	}
	
	@Override
	public void saveAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
		nbt.putString(REDSTONE_TYPE_KEY, redstoneType.name());
		
		nbt.putDouble(PROGRESS_KEY, progress);
		nbt.putDouble(LIMIT_KEY, limit);
		
		if (energyStorage != null) {
			var energyStorageNbt = new CompoundTag();
			
			energyStorageNbt.putLong(AMOUNT_KEY, energyStorage.amount);
			
			nbt.put(ENERGY_STORAGE_KEY, energyStorageNbt);
		}
		
		if (itemStorage != null) {
			var itemStorageNbt = new CompoundTag();
			
			itemStorage.writeToNbt(itemStorageNbt, registries);
			
			nbt.put(ITEM_STORAGE_KEY, itemStorageNbt);
		}
		
		if (fluidStorage != null) {
			var fluidStorageNbt = new CompoundTag();
			
			fluidStorage.writeToNbt(fluidStorageNbt);
			
			nbt.put(FLUID_STORAGE_KEY, fluidStorageNbt);
		}
		
		super.saveAdditional(nbt, registries);
	}
	
	@Override
	protected void loadAdditional(@NotNull CompoundTag nbt, HolderLookup.Provider registries) {
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
			
			itemStorage.readFromNbt(itemStorageNbt, registries);
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
		
		super.loadAdditional(nbt, registries);
		refreshClientModelData();
	}

	@Override
	public ModelData getModelData() {
		return ModelData.of(SIDING_MODEL_DATA, SidingModelData.of(itemStorage, fluidStorage));
	}
	
	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		var nbt = new CompoundTag();
		
		saveAdditional(nbt, registries);
		
		if (hasItemStorage()) {
			var itemStorage = getItemStorage();
			
			if (!syncItemStorage && itemStorage.getVersion() == lastItemStorageVersion) {
				nbt.remove(ITEM_STORAGE_KEY);
				
				var sidings = itemStorage.getSidings();
				
				var sidingsNbt = new CompoundTag();
				
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
				
				var sidingsNbt = new CompoundTag();
				
				for (var i = 0; i < sidings.length; ++i) {
					sidingsNbt.putInt(String.valueOf(i), sidings[i].ordinal());
				}
				
				nbt.put(FLUID_STORAGE_SIDINGS_KEY, sidingsNbt);
			} else {
				syncFluidStorage = false;
				
				lastFluidStorageVersion = fluidStorage.getVersion();
			}
		}
		
		return nbt;
	}
	
	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
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
		var powered = level.getBestNeighborSignal(getBlockPos()) > 0;
		var shouldRun = redstoneType.shouldRun(powered);
		
		active = shouldRun;
		
		return shouldRun;
	}
	
	/**
	 * Schedules this block entity for server to client sync.
	 */
	public void syncData() {
		setChanged();

		if (level != null) {
			level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
		}
	}
	
	public void notifySidingChanged() {
		if (level == null) {
			return;
		}

		setChanged();
		requestModelDataUpdate();
		level.invalidateCapabilities(worldPosition);
		
		for (var directions : DirectionUtils.VALUES) {
			var neighborPos = getBlockPos().relative(directions);
			var neighborState = level.getBlockState(neighborPos);
			
			neighborState.handleNeighborChanged(level, neighborPos, getBlockState().getBlock(), getBlockPos(), false);
			
			if (neighborState.getBlock() instanceof CableBlock cableBlock) {
				NetworkUtils.trace(cableBlock.getNetworkType(), level, neighborPos);
				cableBlock.updateConnections(level, neighborPos);
			}
		}
	}
	
	public boolean setStorageSiding(StorageType storageType, Direction direction, StorageSiding siding) {
		var changed = false;
		
		if (storageType == StorageType.ITEM && itemStorage != null) {
			changed = itemStorage.setSiding(direction, siding);
		} else if (storageType == StorageType.FLUID && fluidStorage != null) {
			changed = fluidStorage.setSiding(direction, siding);
		}
		
		if (changed) {
			notifySidingChanged();
			syncData();
		}
		
		return changed;
	}

	private void refreshClientModelData() {
		if (level != null && level.isClientSide) {
			requestModelDataUpdate();
			level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
		}
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
	 * Asserts whether this block entity has a {@link LongEnergyStorage} or not.
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
	 * Returns this block entity's {@link LongEnergyStorage}.
	 */
	@Nullable
	public LongEnergyStorage getEnergyStorage() {
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

	public record SidingModelData(List<StorageSiding> itemSidings, List<StorageSiding> fluidSidings) {
		public static SidingModelData of(@Nullable SimpleItemStorage itemStorage, @Nullable SimpleFluidStorage fluidStorage) {
			return new SidingModelData(sidings(itemStorage == null ? null : itemStorage.getSidings()), sidings(fluidStorage == null ? null : fluidStorage.getSidings()));
		}

		public boolean hasItemSidings() {
			return itemSidings.size() == DirectionUtils.VALUES.length;
		}

		public boolean hasFluidSidings() {
			return fluidSidings.size() == DirectionUtils.VALUES.length;
		}

		public StorageSiding itemSiding(Direction direction) {
			return siding(itemSidings, direction);
		}

		public StorageSiding fluidSiding(Direction direction) {
			return siding(fluidSidings, direction);
		}

		private static List<StorageSiding> sidings(@Nullable StorageSiding[] sidings) {
			return sidings == null ? List.of() : List.copyOf(Arrays.asList(sidings.clone()));
		}

		private static StorageSiding siding(List<StorageSiding> sidings, Direction direction) {
			return sidings.size() == DirectionUtils.VALUES.length ? sidings.get(direction.ordinal()) : StorageSiding.NONE;
		}
	}
}
