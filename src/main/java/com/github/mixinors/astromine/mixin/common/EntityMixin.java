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
import com.github.mixinors.astromine.common.registry.DimensionLayerRegistry;
import com.github.mixinors.astromine.registry.common.AMTagKeys;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.Tag;
import net.minecraft.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Set;

@Mixin(Entity.class)
public abstract class EntityMixin implements EntityAccessor {
	@Shadow
	public World world;
	@Shadow
	protected boolean firstUpdate;
	@Shadow
	protected Object2DoubleMap<Tag<Fluid>> fluidHeight;
	
	private int astromine$lastY = 0;
	
	private Entity astromine$lastVehicle = null;
	
	private TeleportTarget astromine$nextTeleportTarget = null;
	
	@Shadow
	public abstract BlockPos getBlockPos();
	
	@Shadow
	public abstract boolean updateMovementInFluid(TagKey<Fluid> tag, double d);
	
	@Shadow
	public abstract boolean isSubmergedIn(TagKey<Fluid> tag);
	
	@Shadow
	public int age;
	
	@Shadow
	public abstract EntityType<?> getType();
	
	@Shadow
	public abstract Iterable<ItemStack> getArmorItems();
	
	@Shadow
	public abstract double getFluidHeight(TagKey<Fluid> fluid);
	
	@Shadow
	@Final
	private Set<TagKey<Fluid>> submergedFluidTag;
	
	@Shadow
	public abstract void setAir(int air);
	
	@Shadow
	public abstract int getMaxAir();
	
	@Override
	public boolean astromine$isInIndustrialFluid() {
		return !this.firstUpdate && this.fluidHeight.getDouble(AMTagKeys.FluidTags.INDUSTRIAL_FLUIDS) > 0.0D;
	}
	
	@Inject(at = @At("HEAD"), method = "tickNetherPortal()V")
	void astromine$tickNetherPortal(CallbackInfo callbackInformation) {
		var entity = (Entity) (Object) this;
		
		if ((int) entity.getPos().getY() != astromine$lastY && !entity.world.isClient && entity.getVehicle() == null) {
			astromine$lastY = (int) entity.getPos().getY();
			
			var bottomPortal = DimensionLayerRegistry.INSTANCE.getLevel(DimensionLayerRegistry.Type.BOTTOM, entity.world.getRegistryKey());
			var topPortal = DimensionLayerRegistry.INSTANCE.getLevel(DimensionLayerRegistry.Type.TOP, entity.world.getRegistryKey());
			
			if (astromine$lastY <= bottomPortal && bottomPortal != Integer.MIN_VALUE) {
				var worldKey = RegistryKey.of(Registry.WORLD_KEY, DimensionLayerRegistry.INSTANCE.getDimension(DimensionLayerRegistry.Type.BOTTOM, entity.world.getRegistryKey()).getValue());
				
				astromine$teleport(entity, worldKey, DimensionLayerRegistry.Type.BOTTOM);
			} else if (astromine$lastY >= topPortal && topPortal != Integer.MIN_VALUE) {
				var worldKey = RegistryKey.of(Registry.WORLD_KEY, DimensionLayerRegistry.INSTANCE.getDimension(DimensionLayerRegistry.Type.TOP, entity.world.getRegistryKey()).getValue());
				
				astromine$teleport(entity, worldKey, DimensionLayerRegistry.Type.TOP);
			}
		}
		
		if (entity.getVehicle() != null) {
			astromine$lastVehicle = null;
		}
		if (astromine$lastVehicle != null) {
			entity.startRiding(astromine$lastVehicle);
			astromine$lastVehicle = null;
		}
	}
	
	void astromine$teleport(Entity entity, RegistryKey<World> destinationKey, DimensionLayerRegistry.Type type) {
		var serverWorld = entity.world.getServer().getWorld(destinationKey);
		
		var existingPassengers = new ArrayList<>(entity.getPassengerList());
		
		var entries = new ArrayList<DataTracker.Entry>();
		for (var entry : entity.getDataTracker().getAllEntries()) {
			entries.add(entry.copy());
		}
		
		astromine$nextTeleportTarget = DimensionLayerRegistry.INSTANCE.getPlacer(type, entity.world.getRegistryKey()).placeEntity(entity);
		var newEntity = entity.moveToWorld(serverWorld);
		
		for (var entry : entries) {
			newEntity.getDataTracker().set(entry.getData(), entry.get());
		}
		
		for (var existingEntity : existingPassengers) {
			((EntityMixin) (Object) existingEntity).astromine$lastVehicle = newEntity;
		}
	}
	
	@Inject(method = "getTeleportTarget", at = @At("HEAD"), cancellable = true)
	protected void astromine$getTeleportTarget(ServerWorld destination, CallbackInfoReturnable<TeleportTarget> cir) {
		if (astromine$nextTeleportTarget != null) {
			cir.setReturnValue(astromine$nextTeleportTarget);
			
			astromine$nextTeleportTarget = null;
		}
	}
	
	@Inject(at = @At("HEAD"), method = "tick()V")
	void astromine$tick(CallbackInfo ci) {
		// TODO: Rewrite Atmosphere stuff, incl. this.
		
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
	private void astromine$updateIndustrialFluidState(CallbackInfoReturnable<Boolean> cir) {
		if (this.updateMovementInFluid(AMTagKeys.FluidTags.INDUSTRIAL_FLUIDS, 0.014)) {
			cir.setReturnValue(true);
		}
	}
	
	@Inject(method = "isInLava", at = @At("RETURN"), cancellable = true)
	protected void astromine$fakeLava(CallbackInfoReturnable<Boolean> cir) {
		// NO-OP
	}
}
