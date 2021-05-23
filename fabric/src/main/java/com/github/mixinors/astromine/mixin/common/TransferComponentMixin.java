package com.github.mixinors.astromine.mixin.common;

import com.github.mixinors.astromine.common.component.base.ItemComponent;
import com.github.mixinors.astromine.common.component.base.TransferComponent;
import com.github.mixinors.astromine.registry.common.fabric.AMComponentsImpl;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TransferComponent.class)
public class TransferComponentMixin {
	@Inject(at = @At("RETURN"), method = "from", cancellable = true, remap = false)
	private static <V> void astromine_get(V v, CallbackInfoReturnable<@Nullable TransferComponent> cir) {
		if (cir.getReturnValue() == null) {
			if (v != null && AMComponentsImpl.TRANSFER.isProvidedBy(v)) {
				cir.setReturnValue((TransferComponent) AMComponentsImpl.TRANSFER.get(v).peek());
			}
		}
	}
}
