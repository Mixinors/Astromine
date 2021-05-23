package com.github.mixinors.astromine.mixin.common;

import com.github.mixinors.astromine.common.component.base.OxygenComponent;
import com.github.mixinors.astromine.registry.common.fabric.AMComponentsImpl;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(OxygenComponent.class)
public class OxygenComponentMixin {
	@Inject(at = @At("RETURN"), method = "from", cancellable = true, remap = false)
	private static <V> void astromine_get(V v, CallbackInfoReturnable<@Nullable OxygenComponent> cir) {
		if (cir.getReturnValue() == null) {
			if (v != null && AMComponentsImpl.OXYGEN.isProvidedBy(v)) {
				cir.setReturnValue((OxygenComponent) AMComponentsImpl.OXYGEN.get(v).peek());
			}
		}
	}
}
