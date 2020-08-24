package com.github.chainmailstudios.astromine.foundations.registry;

import com.github.chainmailstudios.astromine.common.fluid.ExtendedFluid;
import com.github.chainmailstudios.astromine.registry.AstromineFluids;
import net.minecraft.fluid.Fluid;

public class AstromineFoundationsFluids extends AstromineFluids {
	public static final Fluid OXYGEN = ExtendedFluid.builder().fog(0x7e159ef9).tint(0xff159ef9).damage(0).toxic(false).infinite(false).name("oxygen").group(AstromineFoundationsItemGroups.FOUNDATIONS).build();

	public static final Fluid HYDROGEN = ExtendedFluid.builder().fog(0x7eff0019).tint(0xffff0019).damage(0).toxic(false).infinite(false).name("hydrogen").group(AstromineFoundationsItemGroups.FOUNDATIONS).build();

	public static final Fluid ROCKET_FUEL = ExtendedFluid.builder().fog(0x7e9ed5f7).tint(0xff9ed5f7).damage(4).toxic(true).infinite(false).name("rocket_fuel").group(AstromineFoundationsItemGroups.FOUNDATIONS).build();

	public static void initialize() {

	}
}
