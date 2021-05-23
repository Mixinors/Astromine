package com.github.mixinors.astromine.mixin.common;

import com.github.mixinors.astromine.common.component.base.AtmosphereComponent;
import com.github.mixinors.astromine.registry.common.fabric.AMComponentsImpl;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AtmosphereComponent.class)
public class AtmosphereComponentMixin {
	@Inject(at = @At("RETURN"), method = "from", cancellable = true, remap = false)
	private static <V> void astromine_get(V v, CallbackInfoReturnable<@Nullable AtmosphereComponent> cir) {
		if (cir.getReturnValue() == null) {
			if (v != null && AMComponentsImpl.ATMOSPHERE.isProvidedBy(v)) {
				cir.setReturnValue((AtmosphereComponent) AMComponentsImpl.ATMOSPHERE.get(v).peek());
			}
		}
	}
}
