package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.fluid.BaseFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.registry.Registry;

public class AstromineFluids {
	public static final Fluid OXYGEN = BaseFluid.builder()
			.fog(0x7e159ef9)
			.tint(0xff159ef9)
			.infinite(false)
			.name("oxygen")
			.build();

	public static void initialize() {

	}

	public static <T extends Fluid> T register(String name, T fluid) {
		return Registry.register(Registry.FLUID, AstromineCommon.identifier(name), fluid);
	}
}
