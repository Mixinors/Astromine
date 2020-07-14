package com.github.chainmailstudios.astromine.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.entity.ai.goal.StepAndDestroyBlockGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;

import com.github.chainmailstudios.astromine.common.registry.GravityRegistry;

@Mixin(StepAndDestroyBlockGoal.class)
public class StepAndDestroyBlockGoalMixin {
	@Shadow @Final private MobEntity stepAndDestroyMob;

	@ModifyConstant(method = "tick()V", constant = @Constant(doubleValue = 0.08D))
	double getGravity(double original) {
		World world = stepAndDestroyMob.world;

		return GravityRegistry.INSTANCE.get(world.getDimensionRegistryKey());
	}
}
