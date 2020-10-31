package com.github.chainmailstudios.astromine.mixin;

import com.github.chainmailstudios.astromine.access.EntityAccess;
import net.minecraft.entity.ai.goal.SwimGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = {SwimGoal.class}, targets = {"net.minecraft.entity.passive.FoxEntity$FoxSwimGoal"})
public abstract class SwimGoalMixin implements SwimGoalAccess {
	@Inject(method = "canStart()Z", at = @At("RETURN"), cancellable = true)
	private void shouldRun(CallbackInfoReturnable<Boolean> cir) {
		if (!cir.getReturnValueZ() && ((EntityAccess) this.getMob()).astromine_isInIndustrialFluid()) {
			cir.setReturnValue(true);
		}
	}
}
