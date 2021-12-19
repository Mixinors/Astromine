/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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

import com.github.mixinors.astromine.registry.common.*;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.Tag;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import com.github.mixinors.astromine.common.component.entity.EntityOxygenComponent;
import com.github.mixinors.astromine.common.component.world.ChunkAtmosphereComponent;
import com.github.mixinors.astromine.common.entity.GravityEntity;
import com.github.mixinors.astromine.common.registry.BreathableRegistry;
import com.github.mixinors.astromine.common.volume.fluid.FluidVolume;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin implements GravityEntity {
	@Unique
	private static final ThreadLocal<Boolean> FAKE_BEING_IN_LAVA = ThreadLocal.withInitial(() -> Boolean.FALSE);

	@Inject(at = @At("RETURN"), method = "createLivingAttributes()Lnet/minecraft/entity/attribute/DefaultAttributeContainer$Builder;")
	private static void createLivingAttributesInject(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
		cir.getReturnValue().add(AMAttributes.GRAVITY_MULTIPLIER.get());
	}

	@Shadow
	public abstract Iterable<ItemStack> getArmorItems();

	@ModifyConstant(method = "travel(Lnet/minecraft/util/math/Vec3d;)V", constant = @Constant(doubleValue = 0.08D, ordinal = 0))
	private double modifyGravity(double original) {
		return astromine_getGravity();
	}

	@Override
	@ModifyVariable(at = @At("HEAD"), method = "handleFallDamage(FFLnet/minecraft/entity/damage/DamageSource;)Z", index = 1)
	float getDamageMultiplier(float damageMultiplier) {
		return (float) (damageMultiplier * astromine_getGravity() * astromine_getGravity());
	}

	@Inject(at = @At("HEAD"), method = "tick()V")
	void onTick(CallbackInfo callbackInformation) {
		Entity entity = (Entity) (Object) this;

		if (entity.world.isClient) {
			return;
		}

		if (!entity.getType().isIn(AMTags.DOES_NOT_BREATHE)) {
			ChunkAtmosphereComponent atmosphereComponent = ChunkAtmosphereComponent.get(entity.world.getChunk(entity.getBlockPos()));

			if (atmosphereComponent != null) {
				FluidVolume breathingVolume;

				if (!AMDimensions.isAstromine(entity.world.getRegistryKey())) {
					breathingVolume = atmosphereComponent.get(entity.getBlockPos().offset(Direction.UP));

					if (breathingVolume.isEmpty()) {
						breathingVolume = FluidVolume.of(FluidVolume.BUCKET, AMFluids.OXYGEN);
					}
				} else {
					breathingVolume = atmosphereComponent.get(entity.getBlockPos().offset(Direction.UP));
				}
				
				EntityOxygenComponent oxygenComponent = EntityOxygenComponent.get(entity);
				
				if (oxygenComponent != null) {
					ItemStack helmetStack = ItemStack.EMPTY;
					ItemStack chestplateStack = ItemStack.EMPTY;
					ItemStack leggingsStack = ItemStack.EMPTY;
					ItemStack bootsStack = ItemStack.EMPTY;

					for (ItemStack stack : getArmorItems()) {
						if (stack.getItem() == AMItems.SPACE_SUIT_HELMET.get()) helmetStack = stack;
						if (stack.getItem() == AMItems.SPACE_SUIT_CHESTPLATE.get()) chestplateStack = stack;
						if (stack.getItem() == AMItems.SPACE_SUIT_LEGGINGS.get()) leggingsStack = stack;
						if (stack.getItem() == AMItems.SPACE_SUIT_BOOTS.get()) bootsStack = stack;
					}

					boolean hasSuit = !helmetStack.isEmpty() && !chestplateStack.isEmpty() && !leggingsStack.isEmpty() && !bootsStack.isEmpty();
					
					FluidStore fluidComponent = null;
					
					if (hasSuit) {
						fluidComponent = FluidStore.get(chestplateStack);
						
						if (fluidComponent != null) {
							breathingVolume = fluidComponent.getFirst();
						}
					}
					
					EntityAccessor entityAccessor = (EntityAccessor) this;
					
					if (entityAccessor.getSubmergedFluidTag() != null && breathingVolume.isEmpty()) {
						breathingVolume = FluidVolume.of(FluidVolume.BUCKET, entityAccessor.getSubmergedFluidTag().values().get(0));
					}
					
					boolean isBreathing = BreathableRegistry.INSTANCE.canBreathe(entity.getType(), breathingVolume.getFluid());
					
					if ((!(entity instanceof PlayerEntity) || !entity.isSpectator() && !((PlayerEntity) entity).isCreative()) && isBreathing && fluidComponent != null && age % 5 == 0) {
						fluidComponent.getFirst().take(81L);
					}
					
					oxygenComponent.simulate(isBreathing);
				}
			}
		}
	}

	// A redirect would be the most efficient, but ModifyArg is the only compatible option
	@ModifyArg(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isSubmergedIn(Lnet/minecraft/tag/Tag;)Z"))
	private Tag<Fluid> astromine_tickAirInFluid(Tag<Fluid> tag) {
		if (this.isSubmergedIn(AMTags.INDUSTRIAL_FLUID)) {
			return AMTags.INDUSTRIAL_FLUID;
		}
		return tag;
	}

	/*TODO: Fixin
	@ModifyVariable(method = "tickMovement", slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;shouldSwimInFluids()Z")), at = @At(value = "STORE", ordinal = 0)) // result from "isTouchingWater && l > 0.0"
	private boolean astromine_allowIndustrialFluidSwimming(boolean touchingWater) {
		return touchingWater || this.getFluidHeight(AMTags.INDUSTRIAL_FLUID) > 0;
	}*/

	@Inject(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isInLava()Z"))
	private void astromine_travelInIndustrialFluids(Vec3d movementInput, CallbackInfo ci) {
		FAKE_BEING_IN_LAVA.set(Boolean.TRUE);
	}

	@Override // overrides the inject in EntityMixin
	protected void astromine_fakeLava(CallbackInfoReturnable<Boolean> cir) {
		if (FAKE_BEING_IN_LAVA.get()) {
			FAKE_BEING_IN_LAVA.set(Boolean.FALSE);
			if (!cir.getReturnValueZ() && this.astromine_isInIndustrialFluid()) {
				cir.setReturnValue(true);
			}
		}
	}
}
