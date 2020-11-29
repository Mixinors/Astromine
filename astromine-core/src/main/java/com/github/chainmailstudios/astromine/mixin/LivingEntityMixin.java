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

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.component.entity.EntityOxygenComponent;
import com.github.chainmailstudios.astromine.common.component.general.base.FluidComponent;
import com.github.chainmailstudios.astromine.common.component.world.ChunkAtmosphereComponent;
import com.github.chainmailstudios.astromine.common.entity.GravityEntity;
import com.github.chainmailstudios.astromine.common.registry.BreathableRegistry;
import com.github.chainmailstudios.astromine.common.registry.FluidEffectRegistry;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.common.volume.fraction.Fraction;
import com.github.chainmailstudios.astromine. registry.AstromineAttributes;
import com.github.chainmailstudios.astromine. registry.AstromineDimensions;
import com.github.chainmailstudios.astromine. registry.AstromineTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.tags.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin implements GravityEntity {
	@Unique
	private static final ThreadLocal<Boolean> FAKE_BEING_IN_LAVA = ThreadLocal.withInitial(() -> Boolean.FALSE);

	@Inject(at = @At("RETURN"), method = "createLivingAttributes")
	private static void createLivingAttributesInject(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
		cir.getReturnValue().add(AstromineAttributes.GRAVITY_MULTIPLIER);
	}

	@Shadow
	public abstract double getAttributeValue(Attribute attribute);

	@Shadow
	public abstract Iterable<ItemStack> getArmorSlots();

	@ModifyConstant(method = "travel", constant = @Constant(doubleValue = 0.08D, ordinal = 0))
	private double modifyGravity(double original) {
		return astromine_getGravity();
	}

	public double getGravityMultiplier() {
		return getAttributeValue(AstromineAttributes.GRAVITY_MULTIPLIER);
	}

	@Inject(at = @At("HEAD"), method = "tick()V")
	void onTick(CallbackInfo callbackInformation) {
		Entity entity = (Entity) (Object) this;
		if (entity.level.isClientSide)
			return;

		if (!entity.getType().is(AstromineTags.DOES_NOT_BREATHE)) {
			ChunkAtmosphereComponent atmosphereComponent = ChunkAtmosphereComponent.get(entity.level.getChunk(entity.blockPosition()));

			if (atmosphereComponent != null) {
				FluidVolume atmosphereVolume;

				if (!AstromineDimensions.isAstromine(entity.level.dimension())) {
					atmosphereVolume = atmosphereComponent.get(entity.blockPosition().relative(Direction.UP));

					if (atmosphereVolume.isEmpty()) {
						atmosphereVolume = FluidVolume.of(Fraction.BUCKET, Registry.FLUID.get(AstromineCommon.identifier("oxygen")));
					}
				} else {
					atmosphereVolume = atmosphereComponent.get(entity.blockPosition().relative(Direction.UP));
				}

				boolean isSubmerged = false;

				AABB collisionBox = entity.getBoundingBox();

				for (BlockPos blockPos : (Iterable<BlockPos>) () -> BlockPos.betweenClosedStream(collisionBox).iterator()) {
					BlockState blockState = entity.level.getBlockState(blockPos);

					if (blockState.getBlock() instanceof LiquidBlock) {
						isSubmerged = true;

						Optional.ofNullable(FluidEffectRegistry.INSTANCE.get(blockState.getFluidState().getType())).ifPresent(it -> it.accept(true, (LivingEntity) (Object) this));
					}
				}

				if (!isSubmerged) {
					boolean isBreathing = true;

					EntityOxygenComponent oxygenComponent = EntityOxygenComponent.get(entity);

					if (oxygenComponent != null) {
						boolean hasHelmet = false;
						boolean hasChestplate = false;
						boolean hasLeggings = false;
						boolean hasBoots = false;

						for (ItemStack stack : getArmorSlots()) {
							if (!stack.isEmpty()) {
								if (Registry.ITEM.getKey(stack.getItem()).toString().equals("astromine:space_suit_helmet")) {
									hasHelmet = true;
								}
								if (Registry.ITEM.getKey(stack.getItem()).toString().equals("astromine:space_suit_chestplate")) {
									hasChestplate = true;
								}
								if (Registry.ITEM.getKey(stack.getItem()).toString().equals("astromine:space_suit_leggings")) {
									hasLeggings = true;
								}
								if (Registry.ITEM.getKey(stack.getItem()).toString().equals("astromine:space_suit_boots")) {
									hasBoots = true;
								}
							}
						}

						boolean hasSuit = hasHelmet && hasChestplate && hasLeggings && hasBoots;

						for (ItemStack stack : getArmorSlots()) {
							if (!stack.isEmpty()) {
								if (Registry.ITEM.getKey(stack.getItem()).toString().equals("astromine:space_suit_chestplate")) { // TODO: Properly verify for Space Suit.
									FluidComponent fluidComponent = FluidComponent.get(stack);

									if (fluidComponent != null) {
										FluidVolume volume = fluidComponent.getFirst();

										if (volume != null) {
											boolean canBreathe = BreathableRegistry.INSTANCE.canBreathe(entity.getType(), volume.getFluid());

											if ((volume.isEmpty() || !canBreathe) && hasSuit) { // TODO: Check if can breathe!
												isBreathing = false;
											}

											if (!canBreathe) {
												if (FluidEffectRegistry.INSTANCE.containsKey(volume.getFluid())) {
													FluidEffectRegistry.INSTANCE.get(volume.getFluid()).accept(false, (LivingEntity) entity);
												}
											}
										}
									}
								}
							}
						}

						if (!hasSuit && FluidEffectRegistry.INSTANCE.containsKey(atmosphereVolume.getFluid())) {
							FluidEffectRegistry.INSTANCE.get(atmosphereVolume.getFluid()).accept(false, (LivingEntity) entity);
						}

						if (!isBreathing) {
							oxygenComponent.simulate(false);
						} else {
							if (!hasSuit && BreathableRegistry.INSTANCE.containsKey(entity.getType())) {
								if (!BreathableRegistry.INSTANCE.canBreathe(entity.getType(), atmosphereVolume.getFluid())) {
									isBreathing = false;
								}
							}

							oxygenComponent.simulate(isBreathing);
						}
					}
				}
			}
		}
	}

	// A redirect would be the most efficient, but ModifyArg is the only compatible option
	@ModifyArg(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isEyeInFluid(Lnet/minecraft/tags/Tag;)Z"))
	private Tag<Fluid> astromine_tickAirInFluid(Tag<Fluid> tag) {
		if (this.isEyeInFluid(AstromineTags.INDUSTRIAL_FLUID)) {
			return AstromineTags.INDUSTRIAL_FLUID;
		}
		return tag;
	}

	@ModifyVariable(method = "aiStep", slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isAffectedByFluids()Z")), at = @At(value = "STORE", ordinal = 0) // result from "isTouchingWater && l > 0.0"
	)
	private boolean astromine_allowIndustrialFluidSwimming(boolean touchingWater) {
		return touchingWater || this.getFluidHeight(AstromineTags.INDUSTRIAL_FLUID) > 0;
	}

	@Inject(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isInLava()Z"))
	private void astromine_travelInIndustrialFluids(Vec3 movementInput, CallbackInfo ci) {
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
