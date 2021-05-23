package com.github.mixinors.astromine.mixin.common;

import com.github.mixinors.astromine.common.component.base.BridgeComponent;
import com.github.mixinors.astromine.registry.common.fabric.AMComponentsImpl;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BridgeComponent.class)
public class BridgeComponentMixin {
	@Inject(at = @At("RETURN"), method = "from", cancellable = true, remap = false)
	private static <V> void astromine_get(V v, CallbackInfoReturnable<@Nullable BridgeComponent> cir) {
		if (cir.getReturnValue() == null) {
			if (v != null && AMComponentsImpl.BRIDGE.isProvidedBy(v)) {
				cir.setReturnValue((BridgeComponent) AMComponentsImpl.BRIDGE.get(v).peek());
			}
		}
	}
}
