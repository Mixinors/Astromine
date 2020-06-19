package com.github.chainmailstudios.astromine.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.world.World;

import com.github.chainmailstudios.astromine.common.registry.GravityRegistry;

@Mixin (FishingBobberEntity.class)
public class FishingBobberEntityMixin {
	@ModifyConstant (method = "use(Lnet/minecraft/item/ItemStack;)I", constant = @Constant (doubleValue = 0.08D))
	double getGravityA(double original) {
		World world = ((Entity) (Object) this).world;

		return GravityRegistry.INSTANCE.get(world.getDimensionRegistryKey());
	}
}
