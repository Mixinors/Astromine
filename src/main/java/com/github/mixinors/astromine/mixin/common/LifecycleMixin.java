package com.github.mixinors.astromine.mixin.common;

import com.mojang.serialization.Lifecycle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Lifecycle.class, priority = 1)
public class LifecycleMixin {
	
	@Inject(method = "experimental", at = @At("RETURN"), cancellable = true)
	private static void astromine$stableWorldgenSettings(CallbackInfoReturnable<Lifecycle> cir) {
		cir.setReturnValue(Lifecycle.stable());
	}
}
