package com.github.chainmailstudios.astromine.foundations.registry;

import com.github.chainmailstudios.astromine.common.registry.FluidEffectRegistry;
import com.github.chainmailstudios.astromine.registry.AstromineFluidEffects;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.Fluids;

public class AstromineFoundationsFluidEffects extends AstromineFluidEffects {
	public static void initialize() {
		FluidEffectRegistry.INSTANCE.register(AstromineFoundationsFluids.ROCKET_FUEL, (entity) -> {
			entity.setOnFireFor(1024);
		});

		FluidEffectRegistry.INSTANCE.register(Fluids.LAVA, (entity) -> {
			entity.setOnFireFor(1024);
		});
	}
}
