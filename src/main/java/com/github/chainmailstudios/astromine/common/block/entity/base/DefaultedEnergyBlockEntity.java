/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
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

package com.github.chainmailstudios.astromine.common.block.entity.base;

import net.fabricmc.fabric.api.util.NbtType;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.Direction;

import com.github.chainmailstudios.astromine.common.block.transfer.TransferType;
import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleEnergyInventoryComponent;
import com.github.chainmailstudios.astromine.common.utilities.EnergyCapacityProvider;
import com.github.chainmailstudios.astromine.common.volume.energy.CreativeEnergyVolume;
import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import org.jetbrains.annotations.NotNull;
import team.reborn.energy.Energy;
import team.reborn.energy.EnergyHandler;
import team.reborn.energy.EnergySide;
import team.reborn.energy.EnergyStorage;
import team.reborn.energy.EnergyTier;

import com.google.common.collect.Lists;
import java.util.List;

public abstract class DefaultedEnergyBlockEntity extends DefaultedBlockEntity implements ComponentProvider, EnergyStorage {
	private final EnergyCapacityProvider energyCapacityProvider;
	private final List<Runnable> energyListeners = Lists.newArrayList();
	private final EnergyVolume energyVolume;

	public DefaultedEnergyBlockEntity(Block energyBlock, BlockEntityType<?> type) {
		super(type);
		this.energyCapacityProvider = (EnergyCapacityProvider) energyBlock;
		transferComponent.add(AstromineComponentTypes.ENERGY_INVENTORY_COMPONENT);
		addComponent(AstromineComponentTypes.ENERGY_INVENTORY_COMPONENT, new SimpleEnergyInventoryComponent());
		if (!energyCapacityProvider.isCreative()) {
			energyVolume = new EnergyVolume(0, () -> {
				for (Runnable listener : energyListeners) {
					listener.run();
				}
			});
			setMaxStoredPower(getEnergyCapacity());
		} else {
			energyVolume = new CreativeEnergyVolume(() -> {
				for (Runnable listener : energyListeners) {
					listener.run();
				}
			});
		}
	}

	protected void addEnergyListener(Runnable listener) {
		this.energyListeners.add(listener);
	}

	public EnergyHandler asEnergy() {
		return Energy.of(this);
	}

	protected final double getEnergyCapacity() {
		return energyCapacityProvider.getEnergyCapacity();
	}

	@Override
	public double getStored(EnergySide energySide) {
		return energyVolume.getAmount();
	}

	@Override
	public void setStored(double v) {
		energyVolume.setAmount(v);
	}

	@Override
	public double getMaxStoredPower() {
		return energyVolume.getMaxAmount();
	}

	public void setMaxStoredPower(double v) {
		energyVolume.setMaxAmount(v);
	}

	@Override
	public EnergyTier getTier() {
		return EnergyTier.INSANE;
	}

	@Override
	public double getMaxInput(EnergySide side) {
		if (side != EnergySide.UNKNOWN) {
			TransferType type = transferComponent.get(AstromineComponentTypes.ENERGY_INVENTORY_COMPONENT).get(Direction.byId(side.ordinal()));
			if (type.isDisabled() || (!type.canInsert() && !type.isNone()))
				return 0;
		}
		return EnergyStorage.super.getMaxInput(side);
	}

	@Override
	public double getMaxOutput(EnergySide side) {
		if (side != EnergySide.UNKNOWN) {
			TransferType type = transferComponent.get(AstromineComponentTypes.ENERGY_INVENTORY_COMPONENT).get(Direction.byId(side.ordinal()));
			if (type.isDisabled() || (!type.canExtract() && !type.isNone()))
				return 0;
		}
		return EnergyStorage.super.getMaxOutput(side);
	}

	public final EnergyVolume getEnergyVolume() {
		return energyVolume;
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.putDouble("energy", getEnergyVolume().getAmount());
		return super.toTag(tag);
	}

	@Override
	public void fromTag(BlockState state, @NotNull CompoundTag tag) {
		super.fromTag(state, tag);
		energyVolume.setAmount(0);
		if (tag.contains("energy", NbtType.COMPOUND)) {
			EnergyVolume energy = EnergyVolume.fromTag(tag.getCompound("energy"));
			energyVolume.setAmount(energy.getAmount());
		} else if (tag.contains("energy")) {
			double energy = tag.getDouble("energy");
			energyVolume.setAmount(energy);
		}
	}
}
