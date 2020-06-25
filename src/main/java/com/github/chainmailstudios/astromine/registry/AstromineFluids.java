package com.github.chainmailstudios.astromine.registry;

import net.minecraft.fluid.Fluid;
import net.minecraft.util.registry.Registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.fluid.AdvancedFluid;

public class AstromineFluids {
	public static final Fluid OXYGEN = AdvancedFluid.builder()
			.fog(0x7e159ef9)
			.tint(0xff159ef9)
			.damage(0)
			.toxic(false)
			.infinite(false)
			.name("oxygen")
			.build();

	public static final Fluid ROCKET_FUEL = AdvancedFluid.builder()
			.fog(0x7e9ed5f7)
			.tint(0xff9ed5f7)
			.damage(4)
			.toxic(true)
			.infinite(false)
			.name("rocket_fuel")
			.build();

	public static void initialize() {

	}

	public static <T extends Fluid> T register(String name, T fluid) {
		return Registry.register(Registry.FLUID, AstromineCommon.identifier(name), fluid);
	}
}
