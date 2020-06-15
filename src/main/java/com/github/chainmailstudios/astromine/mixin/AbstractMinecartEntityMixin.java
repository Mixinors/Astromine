package com.github.chainmailstudios.astromine.mixin;

import com.github.chainmailstudios.astromine.common.registry.GravityRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(AbstractMinecartEntity.class)
public class AbstractMinecartEntityMixin {
	@ModifyConstant(method = "tick()V", constant = @Constant(doubleValue = -0.04D))
	double getGravityB(double original) {
		World world = ((Entity) (Object) this).world;

		Identifier dimension = world.getDimensionRegistryKey().getValue();

		return -GravityRegistry.INSTANCE.get(dimension);
	}
}
