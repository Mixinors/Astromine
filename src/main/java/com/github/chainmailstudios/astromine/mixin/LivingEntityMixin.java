package com.github.chainmailstudios.astromine.mixin;

import com.github.chainmailstudios.astromine.common.registry.DimensionLayerRegistry;
import com.github.chainmailstudios.astromine.common.registry.GravityRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;

@Mixin (LivingEntity.class)
public abstract class LivingEntityMixin {
	@Shadow protected boolean jumping;

	int lastY = 0;

	@ModifyConstant (method = "travel(Lnet/minecraft/util/math/Vec3d;)V", constant = @Constant (doubleValue = 0.08D))
	double getGravity(double original) {
		World world = ((Entity) (Object) this).world;

		return GravityRegistry.INSTANCE.get(world.getDimensionRegistryKey());
	}

	@Inject (at = @At ("HEAD"), method = "tick()V")
	void onTick(CallbackInfo callbackInformation) {
		Entity entity = (Entity) (Object) this;


		if ((int) entity.getPos().getY() != this.lastY && !entity.world.isClient) {
			this.lastY = (int) entity.getPos().getY();

			int bY = DimensionLayerRegistry.INSTANCE.getLevel(DimensionLayerRegistry.Type.BOTTOM, entity.world.getDimensionRegistryKey());
			int tY = DimensionLayerRegistry.INSTANCE.getLevel(DimensionLayerRegistry.Type.TOP, entity.world.getDimensionRegistryKey());

			if (this.lastY <= bY && bY != Integer.MIN_VALUE) {
				RegistryKey<World> worldKey = RegistryKey.of(Registry.DIMENSION, DimensionLayerRegistry.INSTANCE.getDimension(DimensionLayerRegistry.Type.BOTTOM, entity.world.getDimensionRegistryKey()).getValue());

				ServerWorld serverWorld = entity.world.getServer().getWorld(worldKey);

				FabricDimensions.teleport(entity, serverWorld).updatePosition(entity.getPos().getX(), 256, entity.getPos().getZ());
			} else if (this.lastY >= tY && tY != Integer.MIN_VALUE) {
				RegistryKey<World> worldKey = RegistryKey.of(Registry.DIMENSION, DimensionLayerRegistry.INSTANCE.getDimension(DimensionLayerRegistry.Type.TOP, entity.world.getDimensionRegistryKey()).getValue());

				ServerWorld serverWorld = entity.world.getServer().getWorld(worldKey);

				FabricDimensions.teleport(entity, serverWorld).updatePosition(entity.getPos().getX(), 256, entity.getPos().getZ());
			}
		}
	}
}
