package com.github.chainmailstudios.astromine.common.utilities.capability.energy;

import com.github.chainmailstudios.astromine.common.component.SidedComponentProvider;
import com.github.chainmailstudios.astromine.common.component.inventory.EnergyInventoryComponent;
import com.github.chainmailstudios.astromine.common.utilities.EnergyUtilities;
import com.github.chainmailstudios.astromine.common.volume.handler.TransferHandler;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import team.reborn.energy.EnergySide;
import team.reborn.energy.EnergyStorage;
import team.reborn.energy.EnergyTier;

public interface ExtendedEnergyProvider extends EnergyStorage, SidedComponentProvider {
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
		boolean[] allow = { true };

		TransferHandler.of(this).ifPresent(transferHandler -> {
			transferHandler.withDirection(AstromineComponentTypes.ENERGY_INVENTORY_COMPONENT, EnergyUtilities.toDirection(side), type -> {
				if (type.isDisabled() || (!type.canInsert() && !type.isNone())) {
					allow[0] = false;
				}
			});
		});

		if (!allow[0]) {
			return 0;
		} else {
			return EnergyStorage.super.getMaxInput(side);
		}
	}

	@Override
	default double getMaxOutput(EnergySide side) {
		boolean[] allow = { true };

		TransferHandler.of(this).ifPresent(transferHandler -> {
			transferHandler.withDirection(AstromineComponentTypes.ENERGY_INVENTORY_COMPONENT, EnergyUtilities.toDirection(side), type -> {
				if (type.isDisabled() || (!type.canExtract() && !type.isNone())) {
					allow[0] = false;
				}
			});
		});

		if (!allow[0]) {
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

	EnergyInventoryComponent getEnergyComponent();
}
