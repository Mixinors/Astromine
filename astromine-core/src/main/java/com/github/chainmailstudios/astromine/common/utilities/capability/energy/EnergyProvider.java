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

package com.github.chainmailstudios.astromine.common.utilities.capability.energy;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.block.transfer.TransferType;
import com.github.chainmailstudios.astromine.common.component.block.entity.BlockEntityTransferComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.EnergyComponent;
import com.github.chainmailstudios.astromine.common.utilities.EnergyUtilities;
import team.reborn.energy.EnergySide;
import team.reborn.energy.EnergyStorage;
import team.reborn.energy.EnergyTier;

public interface EnergyProvider extends EnergyStorage {
	@Override
	default double getStored(EnergySide side) {
		return getEnergyComponent().getAmount();
	}

	@Override
	default void setStored(double amount) {
		getEnergyComponent().setAmount(amount);
	}

	@Override
	default double getMaxInput(EnergySide side) {
		boolean allow = false;

		if(side.equals(EnergySide.UNKNOWN)) {
			allow = true;
		} else {
			BlockEntityTransferComponent transferComponent = BlockEntityTransferComponent.get(this);

			if (transferComponent != null) {
				TransferType type = transferComponent.getEnergy(EnergyUtilities.toDirection(side));

				allow = type.canInsert();
			}
		}

		if (!allow) {
			return 0;
		} else {
			return EnergyStorage.super.getMaxInput(side);
		}
	}

	@Override
	default double getMaxOutput(EnergySide side) {
		boolean allow = false;

		if(side.equals(EnergySide.UNKNOWN)) {
			allow = true;
		} else {
			BlockEntityTransferComponent transferComponent = BlockEntityTransferComponent.get(this);

			if (transferComponent != null) {
				TransferType type = transferComponent.getEnergy(EnergyUtilities.toDirection(side));

				allow = type.canExtract();
			}
		}

		if (!allow) {
			return 0;
		} else {
			return EnergyStorage.super.getMaxOutput(side);
		}
	}

	@Override
	default double getMaxStoredPower() {
		return getEnergyComponent().getSize();
	}

	@Override
	default EnergyTier getTier() {
		return EnergyTier.INSANE;
	}

	EnergyComponent getEnergyComponent();
}
