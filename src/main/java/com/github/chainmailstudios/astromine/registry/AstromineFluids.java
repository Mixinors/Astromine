package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.fluid.BaseFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.registry.Registry;

public class AstromineFluids {
	public static void initialize() {
		BaseFluid.builder()
				.fog(0xffffffff)
				.tint(0x00ff0000)
				.infinite(false)
				.name("oxygen")
				.build();
	}

	public static <T extends Fluid> T register(String name, T fluid) {
		return Registry.register(Registry.FLUID, AstromineCommon.identifier(name), fluid);
	}
}
