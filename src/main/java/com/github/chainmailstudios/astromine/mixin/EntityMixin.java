package com.github.chainmailstudios.astromine.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import com.github.chainmailstudios.astromine.common.registry.GravityRegistry;

@Mixin (Entity.class)
public abstract class EntityMixin {
	@Shadow public World world;

	@ModifyVariable (at = @At ("HEAD"), method = "handleFallDamage(FF)Z", index = 2)
	float getDamageMultiplier(float damageMultiplier) {
		World world = ((Entity) (Object) this).world;

		return damageMultiplier *= (float) GravityRegistry.INSTANCE.get(world.getDimensionRegistryKey());
	}
}
