package com.github.chainmailstudios.astromine.mixin;

import com.github.chainmailstudios.astromine.access.EntityAccess;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityNavigation.class)
public abstract class EntityNavigationMixin {
	@Shadow
	@Final
	protected MobEntity entity;

	@Inject(method = "isInLiquid", at = @At("RETURN"), cancellable = true)
	private void astromine_isInLiquid(CallbackInfoReturnable<Boolean> cir) {
		if (!cir.getReturnValueZ() && ((EntityAccess) this.entity).astromine_isInIndustrialFluid()) {
			cir.setReturnValue(true);
		}
	}
}
