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
import com.github.chainmailstudios.astromine.client.cca.ClientAtmosphereManager;
import com.github.chainmailstudios.astromine.common.component.world.ChunkAtmosphereComponent;
import com.github.chainmailstudios.astromine.common.entity.GravityEntity;
import com.github.chainmailstudios.astromine.common.registry.DimensionLayerRegistry;
import com.github.chainmailstudios.astromine.registry.AstromineTags;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.portal.PortalInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Entity.class)
public abstract class EntityMixin implements GravityEntity, EntityAccess {
	@Shadow
	public Level level;
	@Shadow
	protected boolean firstTick;
	@Shadow
	protected Object2DoubleMap<Tag<Fluid>> fluidHeight;
	private int astromine_lastY = 0;
	private Entity astromine_lastVehicle = null;
	private PortalInfo astromine_nextTeleportTarget = null;
	private Level astromine_lastWorld = null;

	@Shadow
	public abstract BlockPos blockPosition();

	@Shadow
	public abstract boolean updateFluidHeightAndDoFluidPushing(Tag<Fluid> tag, double d);

	@Shadow
	public abstract double getFluidHeight(Tag<Fluid> fluid);

	@Shadow
	public abstract boolean isEyeInFluid(Tag<Fluid> tag);

	@ModifyVariable(at = @At("HEAD"), method = "causeFallDamage", index = 1)
	float getDamageMultiplier(float damageMultiplier) {
		return (float) (damageMultiplier * astromine_getGravity() * astromine_getGravity());
	}

	@Override
	public double astromine_getGravity() {
		Level world = ((Entity) (Object) this).level;
		return astromine_getGravity(world);
	}

	@Override
	public boolean astromine_isInIndustrialFluid() {
		return !this.firstTick && this.fluidHeight.getDouble(AstromineTags.INDUSTRIAL_FLUID) > 0.0D;
	}

	@Inject(at = @At("HEAD"), method = "handleNetherPortal")
	void astromine_tickNetherPortal(CallbackInfo callbackInformation) {
		Entity entity = (Entity) (Object) this;

		if ((int) entity.position().y() != astromine_lastY && !entity.level.isClientSide && entity.getVehicle() == null) {
			astromine_lastY = (int) entity.position().y();

			int bottomPortal = DimensionLayerRegistry.INSTANCE.getLevel(DimensionLayerRegistry.Type.BOTTOM, entity.level.dimension());
			int topPortal = DimensionLayerRegistry.INSTANCE.getLevel(DimensionLayerRegistry.Type.TOP, entity.level.dimension());

			if (astromine_lastY <= bottomPortal && bottomPortal != Integer.MIN_VALUE) {
				ResourceKey<Level> worldKey = ResourceKey.create(Registry.DIMENSION_REGISTRY, DimensionLayerRegistry.INSTANCE.getDimension(DimensionLayerRegistry.Type.BOTTOM, entity.level.dimension()).location());

				astromine_teleport(entity, worldKey, DimensionLayerRegistry.Type.BOTTOM);
			} else if (astromine_lastY >= topPortal && topPortal != Integer.MIN_VALUE) {
				ResourceKey<Level> worldKey = ResourceKey.create(Registry.DIMENSION_REGISTRY, DimensionLayerRegistry.INSTANCE.getDimension(DimensionLayerRegistry.Type.TOP, entity.level.dimension()).location());

				astromine_teleport(entity, worldKey, DimensionLayerRegistry.Type.TOP);
			}
		}

		if (entity.getVehicle() != null)
			astromine_lastVehicle = null;
		if (astromine_lastVehicle != null) {
			entity.startRiding(astromine_lastVehicle);
			astromine_lastVehicle = null;
		}
	}

	void astromine_teleport(Entity entity, ResourceKey<Level> destinationKey, DimensionLayerRegistry.Type type) {
		ServerLevel serverWorld = entity.level.getServer().getLevel(destinationKey);

		List<Entity> existingPassengers = Lists.newArrayList(entity.getPassengers());

		List<SynchedEntityData.DataItem<?>> entries = Lists.newArrayList();
		for (SynchedEntityData.DataItem<?> entry : entity.getEntityData().getAll()) {
			entries.add(entry.copy());
		}

		astromine_nextTeleportTarget = DimensionLayerRegistry.INSTANCE.getPlacer(type, entity.level.dimension()).placeEntity(entity);
		Entity newEntity = entity.changeDimension(serverWorld);

		for (SynchedEntityData.DataItem entry : entries) {
			newEntity.getEntityData().set(entry.getAccessor(), entry.getValue());
		}

		for (Entity existingEntity : existingPassengers) {
			((EntityMixin) (Object) existingEntity).astromine_lastVehicle = newEntity;
		}
	}

	@Inject(method = "findDimensionEntryPoint", at = @At("HEAD"), cancellable = true)
	protected void astromine_getTeleportTarget(ServerLevel destination, CallbackInfoReturnable<PortalInfo> cir) {
		if (astromine_nextTeleportTarget != null) {
			cir.setReturnValue(astromine_nextTeleportTarget);
			astromine_nextTeleportTarget = null;
		}
	}

	@Inject(at = @At("HEAD"), method = "tick()V")
	void astromine_tick(CallbackInfo ci) {
		// TODO Make this sync all visible chunks around the player.
		if (((Entity) (Object) this) instanceof ServerPlayer && level != astromine_lastWorld) {
			astromine_lastWorld = level;

			ServerSidePacketRegistry.INSTANCE.sendToPlayer(((Player) (Object) this), ClientAtmosphereManager.GAS_ERASED, ClientAtmosphereManager.ofGasErased());

			ChunkAtmosphereComponent atmosphereComponent = ChunkAtmosphereComponent.get(level.getChunk(blockPosition()));

			atmosphereComponent.getVolumes().forEach(((blockPos, volume) -> {
				ServerSidePacketRegistry.INSTANCE.sendToPlayer(((Player) (Object) this), ClientAtmosphereManager.GAS_ADDED, ClientAtmosphereManager.ofGasAdded(blockPos, volume));
			}));
		}
	}

	@Inject(method = "updateInWaterStateAndDoFluidPushing", at = @At("RETURN"), cancellable = true)
	private void astromine_updateIndustrialFluidState(CallbackInfoReturnable<Boolean> cir) {
		if (this.updateFluidHeightAndDoFluidPushing(AstromineTags.INDUSTRIAL_FLUID, 0.014)) {
			cir.setReturnValue(true);
		}
	}

	@Inject(method = "isInLava", at = @At("RETURN"), cancellable = true)
	protected void astromine_fakeLava(CallbackInfoReturnable<Boolean> cir) {
		// NO-OP
	}
}
