package com.github.chainmailstudios.astromine.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.block.HoneyBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.github.chainmailstudios.astromine.common.registry.GravityRegistry;

@Mixin(HoneyBlock.class)
public class HoneyBlockMixin {
	@ModifyConstant(method = "isSliding(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;)Z", constant = @Constant(doubleValue = -0.08D))
	double getGravity(double original, BlockPos pos, Entity entity) {
		return -GravityRegistry.INSTANCE.get(entity.world.getDimensionRegistryKey());
	}
}
