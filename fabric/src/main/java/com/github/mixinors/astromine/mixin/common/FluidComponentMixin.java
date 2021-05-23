package com.github.mixinors.astromine.mixin.common;

import com.github.mixinors.astromine.common.component.base.EnergyComponent;
import com.github.mixinors.astromine.common.component.base.FluidComponent;
import com.github.mixinors.astromine.compat.techreborn.common.component.general.TREnergyComponent;
import com.github.mixinors.astromine.registry.common.fabric.AMComponentsImpl;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import team.reborn.energy.EnergyStorage;

@Mixin(FluidComponent.class)
public class FluidComponentMixin {
	@Inject(at = @At("RETURN"), method = "from", cancellable = true, remap = false)
	private static <V> void astromine_get(V v, CallbackInfoReturnable<@Nullable FluidComponent> cir) {
		if (cir.getReturnValue() == null) {
			if (v != null && AMComponentsImpl.FLUID.isProvidedBy(v)) {
				cir.setReturnValue((FluidComponent) AMComponentsImpl.FLUID.get(v).peek());
			}
		}
	}
}
