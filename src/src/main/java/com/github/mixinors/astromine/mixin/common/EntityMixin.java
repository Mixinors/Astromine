/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
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

package com.github.mixinors.astromine.mixin.common;

import com.github.mixinors.astromine.common.access.EntityAccessor;
import com.github.mixinors.astromine.common.entity.GravityEntity;
import com.github.mixinors.astromine.common.registry.DimensionLayerRegistry;
import com.github.mixinors.astromine.registry.common.AMTags;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.fluid.Fluid;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.Tag;
import net.minecraft.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;

@Mixin(Entity.class)
public abstract class EntityMixin implements GravityEntity, EntityAccessor {
	@Shadow
	public World world;
	@Shadow
	protected boolean firstUpdate;
	@Shadow
	protected Object2DoubleMap<Tag<Fluid>> fluidHeight;
	private int am_lastY = 0;
	private Entity am_lastVehicle = null;
	private TeleportTarget am_nextTeleportTarget = null;
	private World am_lastWorld = null;

	@Shadow
	public abstract BlockPos getBlockPos();

	@Shadow
	public abstract boolean updateMovementInFluid(TagKey<Fluid> tag, double d);

	@Shadow
	public abstract double getFluidHeight(TagKey<Fluid> fluid);

	@Shadow
	public abstract boolean isSubmergedIn(TagKey<Fluid> tag);
	
	@Shadow public int age;
	
	@ModifyVariable(at = @At("HEAD"), method = "handleFallDamage(FFLnet/minecraft/entity/damage/DamageSource;)Z", index = 1)
	float getDamageMultiplier(float damageMultiplier) {
		return (float) (damageMultiplier * am_getGravity() * am_getGravity());
	}

	@Override
	public double am_getGravity() {
		var world = ((Entity) (Object) this).world;
		return am_getGravity(world);
	}

	@Override
	public boolean am_isInIndustrialFluid() {
		return !this.firstUpdate && this.fluidHeight.getDouble(AMTags.INDUSTRIAL_FLUID) > 0.0D;
	}

	@Inject(at = @At("HEAD"), method = "tickNetherPortal()V")
	void am_tickNetherPortal(CallbackInfo callbackInformation) {
		var entity = (Entity) (Object) this;

		if ((int) entity.getPos().getY() != am_lastY && !entity.world.isClient && entity.getVehicle() == null) {
			am_lastY = (int) entity.getPos().getY();
			
			var bottomPortal = DimensionLayerRegistry.INSTANCE.getLevel(DimensionLayerRegistry.Type.BOTTOM, entity.world.getRegistryKey());
			var topPortal = DimensionLayerRegistry.INSTANCE.getLevel(DimensionLayerRegistry.Type.TOP, entity.world.getRegistryKey());

			if (am_lastY <= bottomPortal && bottomPortal != Integer.MIN_VALUE) {
				var worldKey = RegistryKey.of(Registry.WORLD_KEY, DimensionLayerRegistry.INSTANCE.getDimension(DimensionLayerRegistry.Type.BOTTOM, entity.world.getRegistryKey()).getValue());

				am_teleport(entity, worldKey, DimensionLayerRegistry.Type.BOTTOM);
			} else if (am_lastY >= topPortal && topPortal != Integer.MIN_VALUE) {
				var worldKey = RegistryKey.of(Registry.WORLD_KEY, DimensionLayerRegistry.INSTANCE.getDimension(DimensionLayerRegistry.Type.TOP, entity.world.getRegistryKey()).getValue());

				am_teleport(entity, worldKey, DimensionLayerRegistry.Type.TOP);
			}
		}

		if (entity.getVehicle() != null)
			am_lastVehicle = null;
		if (am_lastVehicle != null) {
			entity.startRiding(am_lastVehicle);
			am_lastVehicle = null;
		}
	}

	void am_teleport(Entity entity, RegistryKey<World> destinationKey, DimensionLayerRegistry.Type type) {
		var serverWorld = entity.world.getServer().getWorld(destinationKey);
		
		var existingPassengers = new ArrayList<>(entity.getPassengerList());
		
		var entries = new ArrayList<DataTracker.Entry>();
		for (var entry : entity.getDataTracker().getAllEntries()) {
			entries.add(entry.copy());
		}

		am_nextTeleportTarget = DimensionLayerRegistry.INSTANCE.getPlacer(type, entity.world.getRegistryKey()).placeEntity(entity);
		var newEntity = entity.moveToWorld(serverWorld);

		for (var entry : entries) {
			newEntity.getDataTracker().set(entry.getData(), entry.get());
		}

		for (var existingEntity : existingPassengers) {
			((EntityMixin) (Object) existingEntity).am_lastVehicle = newEntity;
		}
	}

	@Inject(method = "getTeleportTarget", at = @At("HEAD"), cancellable = true)
	protected void am_getTeleportTarget(ServerWorld destination, CallbackInfoReturnable<TeleportTarget> cir) {
		if (am_nextTeleportTarget != null) {
			cir.setReturnValue(am_nextTeleportTarget);
			am_nextTeleportTarget = null;
		}
	}

	@Inject(at = @At("HEAD"), method = "tick()V")
	void am_tick(CallbackInfo ci) {
		// TODO: Rewrite Atmosphere stuff, incl. this.
		
		// TODO Make this sync all visible chunks around the player.
		// if (((Entity) (Object) this) instanceof ServerPlayerEntity && world != am_lastWorld) {
		// 	am_lastWorld = world;
		//
		// 	NetworkManager.sendToPlayer((ServerPlayerEntity) (Object) this, AMNetworks.GAS_ERASED, ClientAtmosphereManager.ofGasErased());
//
		// 	var atmosphereComponent = ChunkAtmosphereComponent.get(world.getChunk(getBlockPos()));
//
		// 	atmosphereComponent.getVolumes().forEach(((blockPos, volume) -> {
		// 		NetworkManager.sendToPlayer((ServerPlayerEntity) (Object) this, AMNetworks.GAS_ADDED, ClientAtmosphereManager.ofGasAdded(blockPos, volume));
		// 	}));
		// }
	}

	@Inject(method = "updateWaterState", at = @At("RETURN"), cancellable = true)
	private void am_updateIndustrialFluidState(CallbackInfoReturnable<Boolean> cir) {
		if (this.updateMovementInFluid(AMTags.INDUSTRIAL_FLUID, 0.014)) {
			cir.setReturnValue(true);
		}
	}

	@Inject(method = "isInLava", at = @At("RETURN"), cancellable = true)
	protected void am_fakeLava(CallbackInfoReturnable<Boolean> cir) {
		// NO-OP
	}
}
