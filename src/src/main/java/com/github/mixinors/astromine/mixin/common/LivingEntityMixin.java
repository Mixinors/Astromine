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

import com.github.mixinors.astromine.common.component.entity.EntityOxygenComponent;
import com.github.mixinors.astromine.common.config.AMConfig;
import com.github.mixinors.astromine.common.item.SpaceSuitItem;
import com.github.mixinors.astromine.common.registry.BreathableRegistry;
import com.github.mixinors.astromine.common.transfer.storage.SimpleFluidItemStorage;
import com.github.mixinors.astromine.registry.common.AMAttributes;
import com.github.mixinors.astromine.registry.common.AMTags;
import com.github.mixinors.astromine.registry.common.AMWorlds;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.TagKey;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import team.reborn.energy.api.EnergyStorage;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin {
	@Shadow
	public abstract Iterable<ItemStack> getArmorItems();
	
	@Shadow
	public abstract ItemStack getEquippedStack(EquipmentSlot var1);
	
	@Unique
	private static final ThreadLocal<Boolean> FAKE_BEING_IN_LAVA = ThreadLocal.withInitial(() -> Boolean.FALSE);
	
	@Inject(at = @At("RETURN"), method = "createLivingAttributes()Lnet/minecraft/entity/attribute/DefaultAttributeContainer$Builder;")
	private static void astromine$createLivingAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
		cir.getReturnValue().add(AMAttributes.GRAVITY_MULTIPLIER.get());
	}
	
	@Inject(at = @At("HEAD"), method = "tick()V")
	void astromine$tick(CallbackInfo callbackInformation) {
		if (AMWorlds.isSpace(world.getRegistryKey())) {
			var entityType = getType();
			
			var entityBreathables = BreathableRegistry.INSTANCE.get(entityType);
			
			var headStack = getEquippedStack(EquipmentSlot.HEAD);
			var chestStack = getEquippedStack(EquipmentSlot.CHEST);
			var legsStack = getEquippedStack(EquipmentSlot.LEGS);
			var feetStack = getEquippedStack(EquipmentSlot.FEET);
			
			var breathing = true;
			
			breathing = breathing && (headStack.getItem() instanceof SpaceSuitItem);
			breathing = breathing && (chestStack.getItem() instanceof SpaceSuitItem);
			breathing = breathing && (legsStack.getItem() instanceof SpaceSuitItem);
			breathing = breathing && (feetStack.getItem() instanceof SpaceSuitItem);
			
			var chestFluidStorage = (SimpleFluidItemStorage) FluidStorage.ITEM.find(chestStack, ContainerItemContext.withInitial(chestStack));
			
			breathing = breathing && chestFluidStorage != null && entityBreathables.stream().anyMatch(tag -> chestFluidStorage.getResource().getFluid().isIn(tag));
			
			var chestEnergyStorage = EnergyStorage.ITEM.find(chestStack, ContainerItemContext.withInitial(chestStack));
			
			breathing = breathing && chestEnergyStorage != null && chestEnergyStorage.getAmount() > 0L;
			
			var component = EntityOxygenComponent.get(this);
			
			component.tick(breathing);
			
			if (breathing && !((Entity) (Object) this).submergedFluidTag.isEmpty()) {
				setAir(getMaxAir());
			}
			
			try (var transaction = Transaction.openOuter()) {
				if (chestFluidStorage != null && !chestFluidStorage.isResourceBlank()) {
					chestFluidStorage.extract(chestFluidStorage.getResource(), AMConfig.get().items.spaceSuitChestplateFluidConsumption, transaction);
				}
				
				if (chestEnergyStorage != null) {
					chestEnergyStorage.extract(AMConfig.get().items.spaceSuitChestplateEnergyConsumption, transaction);
				}
				
				transaction.commit();
			}
		}
	}
	
	// A redirect would be the most efficient, but ModifyArg is the only compatible option
	@ModifyArg(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isSubmergedIn(Lnet/minecraft/tag/TagKey;)Z"))
	private TagKey<Fluid> astromine$tickAirInFluid(TagKey<Fluid> tag) {
		if (this.isSubmergedIn(AMTags.INDUSTRIAL_FLUID)) {
			return AMTags.INDUSTRIAL_FLUID;
		}
		
		return tag;
	}
	
	@ModifyVariable(method = "tickMovement", slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;shouldSwimInFluids()Z")), at = @At(value = "STORE", ordinal = 0)) // result from "isTouchingWater && l > 0.0"
	private boolean astromine$tickMovement$shouldSwimInFluids(boolean touchingWater) {
		return touchingWater || this.getFluidHeight(AMTags.INDUSTRIAL_FLUID) > 0;
	}
	
	@Inject(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isInLava()Z"))
	private void astromine$travel(Vec3d movementInput, CallbackInfo ci) {
		FAKE_BEING_IN_LAVA.set(Boolean.TRUE);
	}
	
	/**
	 * Overrides the injection in {@link EntityMixin}.
	 */
	@Override
	protected void astromine$fakeLava(CallbackInfoReturnable<Boolean> cir) {
		if (FAKE_BEING_IN_LAVA.get()) {
			FAKE_BEING_IN_LAVA.set(Boolean.FALSE);
			
			if (!cir.getReturnValueZ() && this.astromine$isInIndustrialFluid()) {
				cir.setReturnValue(true);
			}
		}
	}
}
