package com.github.chainmailstudios.astromine.common.block.entity;

import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineFluids;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Tickable;

public class FuelGeneratorBlockEntity extends AlphaBlockEntity implements Tickable {
	public FuelGeneratorBlockEntity() {
		super(AstromineBlockEntityTypes.FUEL_GENERATOR);

		energyVolume.setSize(new Fraction(16, 1));
		fluidVolume.setSize(new Fraction(16, 1));
	}

	@Override
	public void tick() {
		if (fluidVolume.getFraction().isBiggerThan(Fraction.BOTTLE) && energyVolume.fits(Fraction.BUCKET) && fluidVolume.getFluid() == AstromineFluids.ROCKET_FUEL) {
			fluidVolume.setFraction(Fraction.subtract(fluidVolume.getFraction(), Fraction.BOTTLE));
			energyVolume.setFraction(Fraction.add(energyVolume.getFraction(), Fraction.BUCKET));
		}
	}
}
