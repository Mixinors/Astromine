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

package com.github.mixinors.astromine.common.util.capability.energy;

import com.github.mixinors.astromine.common.block.transfer.TransferType;
import com.github.mixinors.astromine.common.component.block.entity.TransferComponent;
import com.github.mixinors.astromine.common.component.general.base.EnergyComponent;
import com.github.mixinors.astromine.common.util.EnergyUtils;
import team.reborn.energy.EnergySide;
import team.reborn.energy.EnergyStorage;
import team.reborn.energy.EnergyTier;

public interface
ComponentEnergyProvider extends EnergyStorage {
	/**
	 * Override behavior to redirect calls to this provider's {@link EnergyComponent}.
	 */
	@Override
	default double getStored(EnergySide side) {
		return getEnergyComponent().getAmount();
	}

	/**
	 * Override behavior to redirect calls to this provider's {@link EnergyComponent}.
	 */
	@Override
	default void setStored(double amount) {
		getEnergyComponent().setAmount(amount);
	}

	/**
	 * Override behavior to redirect calls to this provider's {@link EnergyComponent}.
	 */
	@Override
	default double getMaxStoredPower() {
		return getEnergyComponent().getSize();
	}

	/**
	 * Override behavior to ignore TechReborn's energy tiers.
	 */
	@Override
	default EnergyTier getTier() {
		return EnergyTier.INSANE;
	}

	/**
	 * Override behavior to redirect calls to this provider's {@link EnergyComponent}
	 * and {@link TransferComponent}, if present.
	 */
	@Override
	default double getMaxInput(EnergySide side) {
		boolean allow = false;

		if (side.equals(EnergySide.UNKNOWN)) {
			allow = true;
		} else {
			TransferComponent transferComponent = TransferComponent.get(this);

			if (transferComponent != null) {
				TransferType type = transferComponent.getEnergy(EnergyUtils.toDirection(side));

				allow = type.canInsert();
			}
		}

		if (!allow) {
			return 0;
		} else {
			return EnergyStorage.super.getMaxInput(side);
		}
	}

	/**
	 * Override behavior to redirect calls to this provider's {@link EnergyComponent}
	 * and {@link TransferComponent}, if present.
	 */
	@Override
	default double getMaxOutput(EnergySide side) {
		boolean allow = false;

		if (side.equals(EnergySide.UNKNOWN)) {
			allow = true;
		} else {
			TransferComponent transferComponent = TransferComponent.get(this);

			if (transferComponent != null) {
				TransferType type = transferComponent.getEnergy(EnergyUtils.toDirection(side));

				allow = type.canExtract();
			}
		}

		if (!allow) {
			return 0;
		} else {
			return EnergyStorage.super.getMaxOutput(side);
		}
	}

	/**
	 * Override behavior to return this as an {@link EnergyComponent}.
	 */
	default EnergyComponent getEnergyComponent() {
		return EnergyComponent.get(this);
	}
}
