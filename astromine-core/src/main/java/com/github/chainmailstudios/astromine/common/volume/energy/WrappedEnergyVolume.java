package com.github.chainmailstudios.astromine.common.volume.energy;

import net.minecraft.util.math.Direction;
import team.reborn.energy.EnergyHandler;

public class WrappedEnergyVolume extends EnergyVolume {
	private final EnergyHandler storage;

	public WrappedEnergyVolume(EnergyHandler storage, Direction direction) {
		this(storage.side(direction));
	}

	public WrappedEnergyVolume(EnergyHandler storage) {
		super(storage.getEnergy(), storage.getMaxStored());
		this.storage = storage;
	}

	public static WrappedEnergyVolume of(EnergyHandler storage) {
		return new WrappedEnergyVolume(storage);
	}

	public static WrappedEnergyVolume of(EnergyHandler storage, Direction direction) {
		return new WrappedEnergyVolume(storage, direction);
	}

	@Override
	public Double getAmount() {
		return storage.getEnergy();
	}

	@Override
	public void setAmount(Double aDouble) {
		storage.set(aDouble);
	}

	@Override
	public Double getSize() {
		return storage.getMaxStored();
	}

	@Override
	public void setSize(Double s) {
		// Not feasible to implement.
	}

	public double getMaximumInput() {
		return storage.getMaxInput();
	}

	public double getMaximumOutput() {
		return storage.getMaxOutput();
	}
}
