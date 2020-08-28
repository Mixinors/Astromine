package com.github.chainmailstudios.astromine.common.volume.fluid;

import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import com.github.chainmailstudios.astromine.common.volume.energy.InfiniteEnergyVolume;

public class InfiniteFluidVolume extends FluidVolume {
	public InfiniteFluidVolume(Runnable listener) {
		super(listener);
	}

	public InfiniteFluidVolume() {
	}

	@Override
	public Fraction getFraction() {
		return Fraction.ofWhole(Integer.MAX_VALUE);
	}

	@Override
	public Fraction getSize() {
		return Fraction.ofWhole(Integer.MAX_VALUE);
	}

	@Override
	public FluidVolume copy() {
		return new InfiniteFluidVolume();
	}
}
