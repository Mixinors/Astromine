package com.github.chainmailstudios.astromine.common.volume.energy;

public class CreativeEnergyVolume extends EnergyVolume {
	public CreativeEnergyVolume(Runnable listener) {
		super(0, listener);
	}

	@Override
	public double getAmount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public double getMaxAmount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public EnergyVolume copy() {
		return new CreativeEnergyVolume(null);
	}
}
