/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.chainmailstudios.astromine.mixin;

import com.github.chainmailstudios.astromine.access.EntityAccess;
import com.github.chainmailstudios.astromine.common.entity.GravityEntity;
import com.github.chainmailstudios.astromine.common.registry.DimensionLayerRegistry;
import com.google.common.collect.Lists;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Entity.class)
public abstract class EntityMixin implements EntityAccess, GravityEntity {
	int lastY = 0;

	@Shadow
	public World world;

	@Unique
	Entity lastVehicle = null;

	@Override
	public Entity astromine_getLastVehicle() {
		return lastVehicle;
	}

	@Override
	public void astromine_setLastVehicle(Entity lastVehicle) {
		this.lastVehicle = lastVehicle;
	}

	@ModifyVariable(at = @At("HEAD"), method = "handleFallDamage(FF)Z", index = 1)
	float getDamageMultiplier(float damageMultiplier) {
		return (float) (damageMultiplier * getGravity() * getGravity());
	}

	@Override
	public double getGravity() {
		World world = ((Entity) (Object) this).world;
		return getGravity(world);
	}

	@Inject(at = @At("HEAD"), method = "tickNetherPortal()V")
	void onTick(CallbackInfo callbackInformation) {
		Entity entity = (Entity) (Object) this;

		if ((int) entity.getPos().getY() != lastY && !entity.world.isClient && entity.getVehicle() == null) {
			lastY = (int) entity.getPos().getY();

			/*
			int bottomPortal = DimensionLayerRegistry.INSTANCE.getLevel(DimensionLayerRegistry.Type.BOTTOM, entity.world.getDimensionRegistryKey());
			int topPortal = DimensionLayerRegistry.INSTANCE.getLevel(DimensionLayerRegistry.Type.TOP, entity.world.getDimensionRegistryKey());

			if (lastY <= bottomPortal && bottomPortal != Integer.MIN_VALUE) {
				RegistryKey<World> worldKey = RegistryKey.of(Registry.DIMENSION, DimensionLayerRegistry.INSTANCE.getDimension(DimensionLayerRegistry.Type.BOTTOM, entity.world.getDimensionRegistryKey()).getValue());

				ServerWorld serverWorld = entity.world.getServer().getWorld(worldKey);

				List<Entity> existingPassengers = Lists.newArrayList(entity.getPassengerList());

				List<DataTracker.Entry<?>> entries = Lists.newArrayList();
				for (DataTracker.Entry<?> entry : entity.getDataTracker().getAllEntries()) {
					entries.add(entry.copy());
				}

				Entity newEntity = FabricDimensions.teleport(entity, serverWorld, DimensionLayerRegistry.INSTANCE.getPlacer(DimensionLayerRegistry.Type.BOTTOM, entity.world.getDimensionRegistryKey()));

				for (DataTracker.Entry entry : entries) {
					newEntity.getDataTracker().set(entry.getData(), entry.get());
				}

				for (Entity existingEntity : existingPassengers) {
					((EntityAccess) existingEntity).astromine_setLastVehicle(newEntity);
				}
			} else if (lastY >= topPortal && topPortal != Integer.MIN_VALUE) {
				RegistryKey<World> worldKey = RegistryKey.of(Registry.DIMENSION, DimensionLayerRegistry.INSTANCE.getDimension(DimensionLayerRegistry.Type.TOP, entity.world.getDimensionRegistryKey()).getValue());

				ServerWorld serverWorld = entity.world.getServer().getWorld(worldKey);

				List<Entity> existingPassengers = Lists.newArrayList(entity.getPassengerList());

				List<DataTracker.Entry<?>> entries = Lists.newArrayList();
				for (DataTracker.Entry<?> entry : entity.getDataTracker().getAllEntries()) {
					entries.add(entry.copy());
				}

				Entity newEntity = FabricDimensions.teleport(entity, serverWorld, DimensionLayerRegistry.INSTANCE.getPlacer(DimensionLayerRegistry.Type.TOP, entity.world.getDimensionRegistryKey()));

				for (DataTracker.Entry entry : entries) {
					newEntity.getDataTracker().set(entry.getData(), entry.get());
				}

				for (Entity existingEntity : existingPassengers) {
					((EntityAccess) existingEntity).astromine_setLastVehicle(newEntity);
				}
			}
			 */
		}

		if (entity.getVehicle() != null)
			lastVehicle = null;
		if (lastVehicle != null) {
			if (lastVehicle.getEntityWorld().getRegistryKey().equals(entity.getEntityWorld().getRegistryKey())) {
				entity.startRiding(lastVehicle);
				lastVehicle = null;
			}
		}
	}
}
