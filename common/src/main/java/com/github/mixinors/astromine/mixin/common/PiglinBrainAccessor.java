package com.github.mixinors.astromine.mixin.common;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.AbstractPiglinEntity;
import net.minecraft.entity.mob.PiglinBrain;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PiglinBrain.class)
public interface PiglinBrainAccessor {
	@Invoker
	static void callBecomeAngryWith(AbstractPiglinEntity abstractPiglinEntity, LivingEntity livingEntity) {
		throw new UnsupportedOperationException("Cannot invoke @Invoker method!");
	}
}
