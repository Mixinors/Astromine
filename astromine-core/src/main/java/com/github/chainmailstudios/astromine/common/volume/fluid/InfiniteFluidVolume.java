package com.github.chainmailstudios.astromine.common.volume.fluid;

import com.github.chainmailstudios.astromine.common.volume.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.volume.base.Volume;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.Identifier;

public class InfiniteFluidVolume extends FluidVolume {
	public InfiniteFluidVolume(Fluid fluid) {
		super(Fraction.of(Long.MAX_VALUE), Fraction.of(Long.MAX_VALUE), fluid);
	}

	@Override
	public Fraction getAmount() {
		return Fraction.of(Long.MAX_VALUE);
	}

	@Override
	public Fraction getSize() {
		return Fraction.of(Long.MAX_VALUE);
	}

	public static InfiniteFluidVolume of(Fluid fluid) {
		return new InfiniteFluidVolume(fluid);
	}

	@Override
	public <V extends Volume<Identifier, Fraction>> V copy() {
		return (V) of(getFluid());
	}
}
