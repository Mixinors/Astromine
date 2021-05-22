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

import com.github.mixinors.astromine.cardinalcomponents.common.component.base.TransferComponent;
import com.github.mixinors.astromine.cardinalcomponents.common.component.base.EnergyComponent;
import com.github.mixinors.astromine.cardinalcomponents.common.component.base.FluidComponent;
import com.github.mixinors.astromine.cardinalcomponents.common.component.base.ItemComponent;
import com.github.mixinors.astromine.techreborn.common.component.general.provider.*;
import com.github.mixinors.astromine.techreborn.common.volume.fluid.FluidVolume;

import me.shedaniel.architectury.extensions.BlockEntityExtension;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Direction;

import com.github.mixinors.astromine.common.block.base.BlockWithEntity;
import com.github.mixinors.astromine.cardinalcomponents.common.component.base.RedstoneComponent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * A {@link BlockEntity} which is synchronized to the client
 * through {@link BlockEntityExtension}, which is
 * {@link Tickable}, updates its {@link BlockState} based on
 * its activity, and handles redstone behavior.
 */
public abstract class ComponentBlockEntity extends BlockEntity implements BlockEntityExtension, Tickable, TransferComponentProvider, RedstoneComponentProvider {
	protected final TransferComponent transfer = createTransferComponent();

	protected final RedstoneComponent redstone = createRedstoneComponent();

	private boolean isActive = false;

	private final boolean[] activity = { false, false, false, false, false };

	protected boolean skipInventory = true;

	/** Instantiates a {@link ComponentBlockEntity}. */
	public ComponentBlockEntity(Supplier<? extends BlockEntityType<?>> type) {
		super(type.get());
	}

	/** Returns the {@link TransferComponent} to be attached. */
	public TransferComponent createTransferComponent() {
		return TransferComponent.of();
	}

	/** Returns the attached {@link TransferComponent}. */
	@Override
	public TransferComponent getTransferComponent() {
		return transfer;
	}

	/** Returns the {@link RedstoneComponent} to be attached. */
	public RedstoneComponent createRedstoneComponent() {
		return RedstoneComponent.of();
	}

	/** Returns the attached {@link RedstoneComponent}. */
	@Override
	public RedstoneComponent getRedstoneComponent() {
		return redstone;
	}

	/** Signals that this {@link ComponentBlockEntity} should synchronize
	 * its full inventory contents on the next {@link #tick()}. */
	public void doNotSkipInventory() {
		this.skipInventory = false;
	}

	/** Sets this machine as active. */
	public void tickActive() {
		isActive = true;
	}

	/** Sets this machine as inactive. */
	public void tickInactive() {
		isActive = false;
	}

	/** Ticks this {@link ComponentBlockEntity}'s redstone behavior,
	 * returning whether it should work or not. */
	public boolean tickRedstone() {
		var powered = world.getReceivedRedstonePower(getPos()) > 0;
		
		switch (getRedstoneComponent().getType()) {
			case WORK_WHEN_ON -> {
				if (powered) tickActive();
				else tickInactive();
				return powered;
			}
			case WORK_WHEN_OFF -> {
				if (!powered) tickActive();
				else tickInactive();
				return !powered;
			}
			default -> {
				tickActive();
				return true;
			}
		}
	}

	/** Ticks this {@link ComponentBlockEntity},
	 * handling transfer between adjacent {@link BlockEntity}-ies
	 * and updating the machine's {@link BlockState}
	 * based on its activity, or lack thereof. */
	@Override
	public void tick() {
		if (!hasWorld() || world.isClient)
			return;
		
		for (var offsetDirection : Direction.values()) {
			var neighborDirection = offsetDirection.getOpposite();

			var neighborPos = getPos().offset(offsetDirection);

			var neighborBlockEntity = world.getBlockEntity(neighborPos);

			if (getTransferComponent().hasItem()) {
				var ourComponent = ItemComponent.from(this);

				if (ourComponent != null) {
					var theirComponent = ItemComponent.from(neighborBlockEntity);

					if (theirComponent != null) {
						theirComponent.into(ourComponent, 1, neighborDirection, offsetDirection);
						ourComponent.into(theirComponent, 1, offsetDirection, neighborDirection);
					}
				}
			}

			if (getTransferComponent().hasFluid()) {
				var ourComponent = FluidComponent.from(this);

				if (ourComponent != null) {
					var theirComponent = FluidComponent.from(neighborBlockEntity);

					if (theirComponent != null) {
						theirComponent.into(ourComponent, FluidVolume.getTransfer(), neighborDirection, offsetDirection);
						ourComponent.into(theirComponent, FluidVolume.getTransfer(), offsetDirection, neighborDirection);
					}
				}
			}

			if (getTransferComponent().hasEnergy()) {
				var ourComponent = EnergyComponent.from(this);

				if (ourComponent != null) {
					var theirComponent = EnergyComponent.from(neighborBlockEntity);

					if (theirComponent != null) {
						theirComponent.into(ourComponent, 1024D);
						ourComponent.into(theirComponent, 1024D);
					}
				}
			}
		}

		if (world.getBlockState(getPos()).contains(BlockWithEntity.ACTIVE)) {
			if (activity.length - 1 >= 0)
				System.arraycopy(activity, 1, activity, 0, activity.length - 1);

			activity[4] = isActive;

			if (isActive && !activity[0]) {
				world.setBlockState(getPos(), world.getBlockState(getPos()).with(BlockWithEntity.ACTIVE, true));
			} else if (!isActive && activity[0]) {
				world.setBlockState(getPos(), world.getBlockState(getPos()).with(BlockWithEntity.ACTIVE, false));
			}
		}
	}

	/** Serializes this {@link ComponentBlockEntity} to a {@link CompoundTag}. */
	@Override
	public CompoundTag toTag(CompoundTag tag) {
		var transferTag = new CompoundTag();
		getTransferComponent().toTag(transferTag);
		
		tag.put("TransferComponent", transferTag);
		
		var redstoneTag = new CompoundTag();
		getRedstoneComponent().toTag(redstoneTag);
		
		tag.put("RedstoneComponent", redstoneTag);
		
		if (this instanceof EnergyComponentProvider provider) {
			var energyTag = new CompoundTag();
			provider.getEnergyComponent().toTag(energyTag);
			
			tag.put("EnergyComponent", energyTag);
		}
		
		if (this instanceof FluidComponentProvider provider) {
			var fluidTag = new CompoundTag();
			provider.getFluidComponent().toTag(fluidTag);
			
			tag.put("FluidComponent", fluidTag);
		}
		
		if (this instanceof ItemComponentProvider provider) {
			var itemTag = new CompoundTag();
			provider.getItemComponent().toTag(itemTag);
			
			tag.put("ItemComponent", itemTag);
		}
		
		return super.toTag(tag);
	}

	/** Deserializes this {@link ComponentBlockEntity} from a {@link CompoundTag}. */
	@Override
	public void fromTag(BlockState state, @NotNull CompoundTag tag) {
		getTransferComponent().fromTag(tag.getCompound("TransferComponent"));
		getRedstoneComponent().fromTag(tag.getCompound("RedstoneComponent"));
		
		if (this instanceof EnergyComponentProvider provider)
			provider.getEnergyComponent().fromTag(tag.getCompound("EnergyComponent"));
		
		if (this instanceof FluidComponentProvider provider)
			provider.getFluidComponent().fromTag(tag.getCompound("FluidComponent"));
		
		if (this instanceof ItemComponentProvider provider)
			provider.getItemComponent().fromTag(tag.getCompound("ItemComponent"));
		
		super.fromTag(state, tag);
	}

	/** Serializes this {@link ComponentBlockEntity} to a {@link CompoundTag},
	 * for synchronization usage. */
	@Override
	public CompoundTag saveClientData(CompoundTag compoundTag) {
		compoundTag = toTag(compoundTag);
		if (skipInventory) {
			compoundTag.remove("ItemComponent");
		} else {
			skipInventory = true;
		}
		return compoundTag;
	}

	/** Deserializes this {@link ComponentBlockEntity} from a {@link CompoundTag},
	 * for synchronization usage. */
	@Override
	public void loadClientData(BlockState state, CompoundTag compoundTag) {
		fromTag(state, compoundTag);
	}
}
