package com.github.chainmailstudios.astromine.mixin;

import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SwimGoal.class)
public interface SwimGoalAccess {
	@Accessor
	MobEntity getMob();
}
