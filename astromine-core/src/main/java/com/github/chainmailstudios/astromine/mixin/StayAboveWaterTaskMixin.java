package com.github.chainmailstudios.astromine.mixin;

import com.github.chainmailstudios.astromine.access.EntityAccess;
import net.minecraft.entity.ai.brain.task.StayAboveWaterTask;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StayAboveWaterTask.class)
public abstract class StayAboveWaterTaskMixin {
	@Inject(method = "shouldRun", at = @At("RETURN"), cancellable = true)
	private void shouldRun(ServerWorld serverWorld, MobEntity mobEntity, CallbackInfoReturnable<Boolean> cir) {
		if (!cir.getReturnValueZ() && ((EntityAccess) mobEntity).astromine_isInIndustrialFluid()) {
			cir.setReturnValue(true);
		}
	}
}
