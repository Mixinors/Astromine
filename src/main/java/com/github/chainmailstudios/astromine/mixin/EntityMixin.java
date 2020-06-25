package com.github.chainmailstudios.astromine.mixin;

import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import com.github.chainmailstudios.astromine.common.registry.DimensionLayerRegistry;
import com.github.chainmailstudios.astromine.common.registry.GravityRegistry;

import com.google.common.collect.Lists;
import java.util.List;

@Mixin(Entity.class)
public abstract class EntityMixin {
	int lastY = 0;

	@Shadow
	public World world;

	@ModifyVariable(at = @At("HEAD"), method = "handleFallDamage(FF)Z", index = 2)
	float getDamageMultiplier(float damageMultiplier) {
		World world = ((Entity) (Object) this).world;

		return damageMultiplier *= GravityRegistry.INSTANCE.get(world.getDimensionRegistryKey());
	}

	@Inject(at = @At("HEAD"), method = "tick()V")
	void onTick(CallbackInfo callbackInformation) {
		Entity entity = (Entity) (Object) this;

		if ((int) entity.getPos().getY() != lastY && !entity.world.isClient) {
			lastY = (int) entity.getPos().getY();

			int bY = DimensionLayerRegistry.INSTANCE.getLevel(DimensionLayerRegistry.Type.BOTTOM, entity.world.getDimensionRegistryKey());
			int tY = DimensionLayerRegistry.INSTANCE.getLevel(DimensionLayerRegistry.Type.TOP, entity.world.getDimensionRegistryKey());

			if (lastY <= bY && bY != Integer.MIN_VALUE) {
				RegistryKey<World> worldKey = RegistryKey.of(Registry.DIMENSION, DimensionLayerRegistry.INSTANCE.getDimension(DimensionLayerRegistry.Type.BOTTOM, entity.world.getDimensionRegistryKey()).getValue());

				ServerWorld serverWorld = entity.world.getServer().getWorld(worldKey);

				List<Entity> existingPassengers = entity.getPassengerList();
				List<Entity> newPassengers = Lists.newArrayList();

				for (Entity existingEntity : existingPassengers) {
					newPassengers.add(FabricDimensions.teleport(existingEntity, serverWorld, DimensionLayerRegistry.INSTANCE.getPlacer(DimensionLayerRegistry.Type.BOTTOM, entity.world.getDimensionRegistryKey())));
				}

				Entity newEntity = FabricDimensions.teleport(entity, serverWorld, DimensionLayerRegistry.INSTANCE.getPlacer(DimensionLayerRegistry.Type.BOTTOM, entity.world.getDimensionRegistryKey()));

				for (Entity newPassenger : newPassengers) {
					newPassenger.startRiding(newEntity);
				}
			} else if (lastY >= tY && tY != Integer.MIN_VALUE) {
				RegistryKey<World> worldKey = RegistryKey.of(Registry.DIMENSION, DimensionLayerRegistry.INSTANCE.getDimension(DimensionLayerRegistry.Type.TOP, entity.world.getDimensionRegistryKey()).getValue());

				ServerWorld serverWorld = entity.world.getServer().getWorld(worldKey);

				List<Entity> existingPassengers = entity.getPassengerList();
				List<Entity> newPassengers = Lists.newArrayList();

				for (Entity existingEntity : existingPassengers) {
					newPassengers.add(FabricDimensions.teleport(existingEntity, serverWorld, DimensionLayerRegistry.INSTANCE.getPlacer(DimensionLayerRegistry.Type.TOP, entity.world.getDimensionRegistryKey())));
				}

				Entity newEntity = FabricDimensions.teleport(entity, serverWorld, DimensionLayerRegistry.INSTANCE.getPlacer(DimensionLayerRegistry.Type.TOP, entity.world.getDimensionRegistryKey()));

				for (Entity newPassenger : newPassengers) {
					newPassenger.startRiding(newEntity);
				}
			}
		}
	}
}
