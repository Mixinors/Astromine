package com.github.chainmailstudios.astromine.mixin;

import com.github.chainmailstudios.astromine.common.registry.GravityRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {
	@ModifyConstant(method = "tick()V", constant = @Constant(doubleValue = -0.04D))
	double getGravity(double original) {
		World world = ((Entity) (Object) this).world;

		Identifier dimension = world.getDimensionRegistryKey().getValue();

		return -GravityRegistry.INSTANCE.get(dimension);
	}
}
