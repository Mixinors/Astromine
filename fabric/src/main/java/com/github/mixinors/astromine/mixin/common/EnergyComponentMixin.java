package com.github.mixinors.astromine.mixin.common;

import com.github.mixinors.astromine.compat.techreborn.common.component.general.TREnergyComponent;
import com.github.mixinors.astromine.common.component.base.EnergyComponent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import team.reborn.energy.EnergyStorage;

@Mixin(EnergyComponent.class)
class EnergyComponentMixin {
	private static EnergyComponent of(EnergyStorage storage) {
		return TREnergyComponent.of(storage);
	}
	
	@Inject(at = @At("RETURN"), method = "from", cancellable = true, remap = false)
	private static <V> void astromine_get(V v, CallbackInfoReturnable<@Nullable EnergyComponent> cir) {
		if (cir.getReturnValue() == null) {
			if (v instanceof EnergyStorage storage) {
				cir.setReturnValue(of(storage));
			}
		}
	}
}
