package com.github.chainmailstudios.astromine.common.registry;

import com.github.chainmailstudios.astromine.common.registry.base.UniRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluid;

import java.util.function.Consumer;

public class FluidEffectRegistry extends UniRegistry<Fluid, Consumer<LivingEntity>> {
	public static final FluidEffectRegistry INSTANCE = new FluidEffectRegistry();

	private FluidEffectRegistry() {

	}
}

