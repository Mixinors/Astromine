package com.github.chainmailstudios.astromine.common.block.entity;

import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineFluids;
import net.minecraft.util.Tickable;

public class FuelGeneratorBlockEntity extends AlphaBlockEntity implements Tickable {
	public FuelGeneratorBlockEntity() {
		super(AstromineBlockEntityTypes.FUEL_GENERATOR);

		energyComponent.getVolume(0).setSize(new Fraction(16, 1));
		fluidComponent.getVolume(0).setSize(new Fraction(16, 1));
	}

	@Override
	public void tick() {
		if (fluidComponent.getVolume(0).getFraction().isBiggerThan(Fraction.BOTTLE) && energyComponent.getVolume(0).fits(Fraction.BUCKET) && fluidComponent.getVolume(0).getFluid() == AstromineFluids.ROCKET_FUEL) {
			fluidComponent.getVolume(0).setFraction(Fraction.subtract(fluidComponent.getVolume(0).getFraction(), Fraction.BOTTLE));
			energyComponent.getVolume(0).setFraction(Fraction.add(energyComponent.getVolume(0).getFraction(), Fraction.BUCKET));
		}
	}
}
