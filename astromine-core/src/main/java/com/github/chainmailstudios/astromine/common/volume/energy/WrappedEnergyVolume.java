package com.github.chainmailstudios.astromine.common.volume.energy;

import com.github.chainmailstudios.astromine.common.utilities.EnergyUtilities;
import net.minecraft.util.math.Direction;
import team.reborn.energy.EnergySide;
import team.reborn.energy.EnergyStorage;
import team.reborn.energy.EnergyTier;

public class WrappedEnergyVolume extends EnergyVolume {
	private final EnergyStorage storage;
	private final EnergySide side;

	public WrappedEnergyVolume(EnergyStorage storage, Direction direction) {
		super(storage.getStored(EnergySide.UNKNOWN), storage.getMaxStoredPower());
		this.storage = storage;
		this.side = EnergyUtilities.toSide(direction);
	}

	public static WrappedEnergyVolume of(EnergyStorage storage, Direction direction) {
		return new WrappedEnergyVolume(storage, direction);
	}

	@Override
	public Double getAmount() {
		return storage.getStored(side);
	}

	@Override
	public void setAmount(Double aDouble) {
		storage.setStored(aDouble);
	}

	@Override
	public Double getSize() {
		return storage.getMaxStoredPower();
	}

	@Override
	public void setSize(Double s) {
		// Not feasible to implement.
	}

	public double getMaximumInput() {
		return storage.getMaxInput(side);
	}

	public double getMaximumOutput() {
		return storage.getMaxOutput(side);
	}
}
