package com.github.mixinors.astromine.mixin.common;

import com.github.mixinors.astromine.common.component.base.FluidComponent;
import com.github.mixinors.astromine.common.component.base.ItemComponent;
import com.github.mixinors.astromine.registry.common.fabric.AMComponentsImpl;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemComponent.class)
public class ItemComponentMixin {
	@Inject(at = @At("RETURN"), method = "from", cancellable = true, remap = false)
	private static <V> void astromine_get(V v, CallbackInfoReturnable<@Nullable ItemComponent> cir) {
		if (cir.getReturnValue() == null) {
			if (v != null && AMComponentsImpl.ITEM.isProvidedBy(v)) {
				cir.setReturnValue((ItemComponent) AMComponentsImpl.ITEM.get(v).peek());
			}
		}
	}
}
