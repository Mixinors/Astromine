package com.github.chainmailstudios.astromine.mixin;

import com.github.chainmailstudios.astromine.common.registry.GravityRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(FallingBlockEntity.class)
public class FallingBlockEntityMixin {
	@ModifyConstant(method = "tick()V", constant = @Constant(doubleValue = -0.04D))
	double getGravity(double original) {
		World world = ((Entity) (Object) this).world;

		return -GravityRegistry.INSTANCE.get(world.getDimensionRegistryKey());
	}
}
