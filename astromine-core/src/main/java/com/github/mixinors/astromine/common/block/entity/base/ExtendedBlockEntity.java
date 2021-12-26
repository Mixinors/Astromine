/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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
import com.github.mixinors.astromine.common.block.entity.TickableBlockEntity;
import com.github.mixinors.astromine.common.transfer.RedstoneControl;
import com.github.mixinors.astromine.common.transfer.storage.SimpleFluidStorage;
import com.github.mixinors.astromine.common.transfer.storage.SimpleItemStorage;
import dev.architectury.hooks.block.BlockEntityHooks;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.function.Supplier;

public abstract class ExtendedBlockEntity extends BlockEntity implements TickableBlockEntity {
	protected boolean isActive = false;

	private final boolean[] activity = { false, false, false, false, false };

	protected boolean skipInventory;
	
	protected RedstoneControl redstoneControl;
	
	protected SimpleEnergyStorage energyStorage = null;
	protected SimpleItemStorage itemStorage = null;
	protected SimpleFluidStorage fluidStorage = null;
	
	public ExtendedBlockEntity(Supplier<? extends BlockEntityType<?>> type, BlockPos blockPos, BlockState blockState) {
		super(type.get(), blockPos, blockState);

		this.skipInventory = true;
		
		this.redstoneControl = RedstoneControl.WORK_ALWAYS;
	}
	
	public void doNotSkipInventory() {
		this.skipInventory = false;
	}
	
	public boolean shouldRun() {
		boolean powered = world.getReceivedRedstonePower(getPos()) > 0;
		
		switch (redstoneControl) {
			case WORK_WHEN_ON -> {
				isActive = powered;
				return powered;
			}
			
			case WORK_WHEN_OFF -> {
				isActive = !powered;
				return !powered;
			}
			
			default -> {
				isActive = true;
				return true;
			}
		}
	}

	public void syncData() {
		BlockEntityHooks.syncData(this);
	}
	
	@Override
	public void tick() {
		if (!hasWorld() || world.isClient) {
			return;
		}

		// TODO: Share items, fluids and energy with neighbors.

		if (world.getBlockState(getPos()).contains(BlockWithEntity.ACTIVE)) {
			if (activity.length - 1 >= 0) {
				System.arraycopy(activity, 1, activity, 0, activity.length - 1);
			}

			activity[4] = isActive;

			if (isActive && !activity[0]) {
				world.setBlockState(getPos(), world.getBlockState(getPos()).with(BlockWithEntity.ACTIVE, true));
			} else if (!isActive && activity[0]) {
				world.setBlockState(getPos(), world.getBlockState(getPos()).with(BlockWithEntity.ACTIVE, false));
			}
		}
	}
	
	@Override
	public void writeNbt(NbtCompound nbt) {
		nbt.putInt("RedstoneControl", redstoneControl.ordinal());
		
		if (energyStorage != null) {
			var energyStorageNbt = new NbtCompound();
			
			energyStorageNbt.putLong("Amount", energyStorage.amount);
			energyStorageNbt.putLong("Capacity", energyStorage.capacity);
			energyStorageNbt.putLong("MaxInsert", energyStorage.maxInsert);
			energyStorageNbt.putLong("MaxExtract", energyStorage.maxExtract);
			
			nbt.put("EnergyStorage", energyStorageNbt);
		}
		
		if (itemStorage != null) {
			var itemStorageNbt = new NbtCompound();
			
			itemStorage.writeToNbt(itemStorageNbt);
			
			nbt.put("ItemStorage", itemStorageNbt);
		}
		
		if (fluidStorage != null) {
			var fluidStorageNbt = new NbtCompound();
			
			fluidStorage.writeToNbt(fluidStorageNbt);
			
			nbt.put("ItemStorage", fluidStorageNbt);
		}

		super.writeNbt(nbt);
	}
	
	@Override
	public void readNbt(@NotNull NbtCompound nbt) {
		redstoneControl = RedstoneControl.values()[nbt.getInt("RedstoneControl")];
		
		if (nbt.contains("EnergyStorage")) {
			var energyStorageNbt = nbt.getCompound("EnergyStorage");
			
			energyStorage.amount = energyStorageNbt.getLong("Amount");
		}
		
		if (nbt.contains("ItemStorage")) {
			var itemStorageNbt = nbt.getCompound("ItemStorage");
			
			itemStorage.readFromNbt(itemStorageNbt);
		}
		
		if (nbt.contains("FluidStorage")) {
			var fluidStorageNbt = nbt.getCompound("FluidStorage");
			
			fluidStorage.readFromNbt(fluidStorageNbt);
		}

		super.readNbt(nbt);
	}
	
	@Override
	public NbtCompound toInitialChunkDataNbt() {
		var nbt = createNbt();
		
		if (skipInventory) {
			nbt.remove("ItemStorage");
		} else {
			skipInventory = true;
		}
		
		return nbt;
	}

	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}

	public RedstoneControl getRedstoneControl() {
		return redstoneControl;
	}
	
	public void setRedstoneControl(RedstoneControl redstoneControl) {
		this.redstoneControl = redstoneControl;
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
	public SimpleFluidStorage getFluidStorage() {
		return fluidStorage;
	}

	@Nullable
	public SimpleItemStorage getItemStorage() {
		return itemStorage;
	}
}
