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

import com.github.mixinors.astromine.registry.common.AMAttributes;
import com.github.mixinors.astromine.registry.common.AMTags;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.fluid.Fluid;
import net.minecraft.tag.TagKey;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin {
	@Unique
	private static final ThreadLocal<Boolean> FAKE_BEING_IN_LAVA = ThreadLocal.withInitial(() -> Boolean.FALSE);
	
	@Inject(at = @At("RETURN"), method = "createLivingAttributes()Lnet/minecraft/entity/attribute/DefaultAttributeContainer$Builder;")
	private static void astromine$createLivingAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
		cir.getReturnValue().add(AMAttributes.GRAVITY_MULTIPLIER.get());
	}
	
	@Inject(at = @At("HEAD"), method = "tick()V")
	void astromine$tick(CallbackInfo callbackInformation) {
		// TODO: Rewrite Atmosphere stuff, incl. this.
		
		// Entity entity = (Entity) (Object) this;
//
		// if (entity.world.isClient) {
		// 	return;
		// }
//
		// if (!entity.getType().isIn(AMTags.DOES_NOT_BREATHE)) {
		// 	var atmosphereComponent = ChunkAtmosphereComponent.get(entity.world.getChunk(entity.getBlockPos()));
//
		// 	if (atmosphereComponent != null) {
		// 		FluidVolume breathingVolume;
//
		// 		if (!AMDimensions.isAstromine(entity.world.getRegistryKey())) {
		// 			breathingVolume = atmosphereComponent.get(entity.getBlockPos().offset(Direction.UP));
//
		// 			if (breathingVolume.isEmpty()) {
		// 				breathingVolume = FluidVolume.of(FluidVolume.BUCKET, AMFluids.OXYGEN);
		// 			}
		// 		} else {
		// 			breathingVolume = atmosphereComponent.get(entity.getBlockPos().offset(Direction.UP));
		// 		}
		//
		// 		var oxygenComponent = EntityOxygenComponent.get(entity);
		//
		// 		if (oxygenComponent != null) {
		// 			var helmetStack = ItemStack.EMPTY;
		// 			var chestplateStack = ItemStack.EMPTY;
		// 			var leggingsStack = ItemStack.EMPTY;
		// 			var bootsStack = ItemStack.EMPTY;
//
		// 			for (var stack : getArmorItems()) {
		// 				if (stack.getItem() == AMItems.SPACE_SUIT_HELMET.get()) helmetStack = stack;
		// 				if (stack.getItem() == AMItems.SPACE_SUIT_CHESTPLATE.get()) chestplateStack = stack;
		// 				if (stack.getItem() == AMItems.SPACE_SUIT_LEGGINGS.get()) leggingsStack = stack;
		// 				if (stack.getItem() == AMItems.SPACE_SUIT_BOOTS.get()) bootsStack = stack;
		// 			}
//
		// 			var hasSuit = !helmetStack.isEmpty() && !chestplateStack.isEmpty() && !leggingsStack.isEmpty() && !bootsStack.isEmpty();
		//
		// 			var fluidStorage = null;
		//
		// 			if (hasSuit) {
		// 				fluidStorage = SimpleFluidStorage.get(chestplateStack);
		//
		// 				if (fluidStorage != null) {
		// 					breathingVolume = fluidStorage.getFirst();
		// 				}
		// 			}
		//
		// 			var entityAccessor = (EntityAccessor) this;
		//
		// 			if (entityAccessor.getSubmergedFluidTag() != null && breathingVolume.isEmpty()) {
		// 				breathingVolume = FluidVolume.of(FluidVolume.BUCKET, entityAccessor.getSubmergedFluidTag().values().get(0));
		// 			}
		//
		// 			var isBreathing = BreathableRegistry.INSTANCE.canBreathe(entity.getType(), breathingVolume.getFluid());
		//
		// 			if ((!(entity instanceof PlayerEntity) || !entity.isSpectator() && !((PlayerEntity) entity).isCreative()) && isBreathing && fluidStorage != null && age % 5 == 0) {
		// 				fluidStorage.getFirst().take(81L);
		// 			}
		//
		// 			oxygenComponent.simulate(isBreathing);
		// 		}
		// 	}
		// }
	}
	
	// A redirect would be the most efficient, but ModifyArg is the only compatible option
	@ModifyArg(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isSubmergedIn(Lnet/minecraft/tag/TagKey;)Z"))
	private TagKey<Fluid> astromine$tickAirInFluid(TagKey<Fluid> tag) {
		if (this.isSubmergedIn(AMTags.INDUSTRIAL_FLUID)) {
			return AMTags.INDUSTRIAL_FLUID;
		}
		
		return tag;
	}

	/*TODO: Fixin
	@ModifyVariable(method = "tickMovement", slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;shouldSwimInFluids()Z")), at = @At(value = "STORE", ordinal = 0)) // result from "isTouchingWater && l > 0.0"
	private boolean am_allowIndustrialFluidSwimming(boolean touchingWater) {
		return touchingWater || this.getFluidHeight(AMTags.INDUSTRIAL_FLUID) > 0;
	}*/
	
	@Inject(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isInLava()Z"))
	private void astromine$travel(Vec3d movementInput, CallbackInfo ci) {
		FAKE_BEING_IN_LAVA.set(Boolean.TRUE);
	}
	
	/**
	 * Overrides the inject in {@link EntityMixin}.
	 *
	 * @param cir
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
