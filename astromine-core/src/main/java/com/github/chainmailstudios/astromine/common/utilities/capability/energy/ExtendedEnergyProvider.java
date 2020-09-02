package com.github.chainmailstudios.astromine.common.utilities.capability.energy;

import com.github.chainmailstudios.astromine.common.component.SidedComponentProvider;
import com.github.chainmailstudios.astromine.common.utilities.EnergyUtilities;
import com.github.chainmailstudios.astromine.common.volume.handler.EnergyHandler;
import com.github.chainmailstudios.astromine.common.volume.handler.TransferHandler;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.EnergySide;
import team.reborn.energy.EnergyStorage;
import team.reborn.energy.EnergyTier;

public interface ExtendedEnergyProvider extends EnergyStorage, SidedComponentProvider {
	@Override
	default double getStored(EnergySide side) {
		double[] stored = { 0 };

		EnergyHandler.ofOptional(this).ifPresent(handler -> {
			handler.withVolume(0, (optionalVolume) -> {
				optionalVolume.ifPresent((volume) -> {
					stored[0] = volume.getAmount();
				});
			});
		});

		return stored[0];
	}

	@Override
	default void setStored(double amount) {
		EnergyHandler.ofOptional(this).ifPresent(handler -> {
			handler.withVolume(0, (optionalVolume) -> {
				optionalVolume.ifPresent(volume -> {
					volume.setAmount(amount);
				});
			});
		});
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
		double[] maximum = { 0 };

		EnergyHandler.ofOptional(this).ifPresent(handler -> {
			handler.withVolume(0, (optionalVolume) -> {
				optionalVolume.ifPresent((volume) -> {
					maximum[0] = volume.getSize();
				});
			});
		});

		return maximum[0];
	}

	@Override
	default EnergyTier getTier() {
		return EnergyTier.INSANE;
	}
}
