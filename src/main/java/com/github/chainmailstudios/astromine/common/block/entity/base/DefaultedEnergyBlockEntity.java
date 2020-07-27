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

import com.github.chainmailstudios.astromine.common.block.transfer.TransferType;
import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleEnergyInventoryComponent;
import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import com.google.common.collect.Lists;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import team.reborn.energy.*;

import java.util.List;

public abstract class DefaultedEnergyBlockEntity extends DefaultedBlockEntity implements ComponentProvider, EnergyStorage {
	private final List<Runnable> energyListeners = Lists.newArrayList();
	private final EnergyVolume energyVolume = new EnergyVolume(0, () -> {
		for (Runnable listener : energyListeners) {
			listener.run();
		}
	});

	public DefaultedEnergyBlockEntity(BlockEntityType<?> type) {
		super(type);
		transferComponent.add(AstromineComponentTypes.ENERGY_INVENTORY_COMPONENT);
		addComponent(AstromineComponentTypes.ENERGY_INVENTORY_COMPONENT, new SimpleEnergyInventoryComponent());
		setMaxStoredPower(getEnergySize());
	}

	protected void addEnergyListener(Runnable listener) {
		this.energyListeners.add(listener);
	}

	public EnergyHandler asEnergy() {
		return Energy.of(this);
	}

	protected abstract double getEnergySize();

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
		tag.put("energy", getEnergyVolume().toTag(new CompoundTag()));
		return super.toTag(tag);
	}

	@Override
	public void fromTag(BlockState state, @NotNull CompoundTag tag) {
		super.fromTag(state, tag);
		energyVolume.setAmount(0);
		if (tag.contains("energy")) {
			EnergyVolume energy = EnergyVolume.fromTag(tag.getCompound("energy"));
			energyVolume.setAmount(energy.getAmount());
		}
	}
}
