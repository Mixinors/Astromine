package com.github.mixinors.astromine.mixin.common;

import com.github.mixinors.astromine.common.event.DimensionTypeEvents;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DimensionType.class)
public class DimensionTypeMixin {
	@Inject(at = @At("HEAD"), method = "hasSkyLight", cancellable = true)
	private void astromine$hasSkyLight(CallbackInfoReturnable<Boolean> cir) {
		var res = DimensionTypeEvents.SKY_LIGHT.invoker().calculate((DimensionType) (Object) this);
		
		if (res.isPresent()) {
			cir.setReturnValue(res.object());
		}
	}
}
