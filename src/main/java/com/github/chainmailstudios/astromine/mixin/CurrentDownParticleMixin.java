package com.github.chainmailstudios.astromine.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.client.particle.CurrentDownParticle;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import com.github.chainmailstudios.astromine.common.registry.GravityRegistry;

@Mixin (CurrentDownParticle.class)
public class CurrentDownParticleMixin {
	@ModifyConstant (method = "tick()V", constant = @Constant (doubleValue = 0.08D))
	double getGravity(double original) {
		World world = ((Entity) (Object) this).world;

		return GravityRegistry.INSTANCE.get(world.getDimensionRegistryKey());
	}
}
