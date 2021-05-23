package com.github.mixinors.astromine.mixin.common;

import com.github.mixinors.astromine.common.component.base.NetworkComponent;
import com.github.mixinors.astromine.registry.common.fabric.AMComponentsImpl;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NetworkComponent.class)
public class NetworkComponentMixin {
	@Inject(at = @At("RETURN"), method = "from", cancellable = true, remap = false)
	private static <V> void astromine_get(V v, CallbackInfoReturnable<@Nullable NetworkComponent> cir) {
		if (cir.getReturnValue() == null) {
			if (v != null && AMComponentsImpl.NETWORK.isProvidedBy(v)) {
				cir.setReturnValue((NetworkComponent) AMComponentsImpl.NETWORK.get(v).peek());
			}
		}
	}
}
