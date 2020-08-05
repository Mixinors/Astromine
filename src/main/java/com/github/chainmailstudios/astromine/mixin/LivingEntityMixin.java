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

import com.github.chainmailstudios.astromine.common.entity.GravityEntity;
import com.github.chainmailstudios.astromine.common.registry.AtmosphereRegistry;
import com.github.chainmailstudios.astromine.common.component.entity.EntityOxygenComponent;
import com.github.chainmailstudios.astromine.common.component.world.WorldAtmosphereComponent;
import com.github.chainmailstudios.astromine.common.fluid.AdvancedFluid;
import com.github.chainmailstudios.astromine.common.item.SpaceSuitItem;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.registry.AstromineAttributes;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import com.github.chainmailstudios.astromine.registry.AstromineTags;
import nerdhub.cardinal.components.api.component.ComponentProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.stream.Collectors;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements GravityEntity {
	@Shadow
	@Final
	private DefaultedList<ItemStack> equippedArmor;

	@Shadow
	public abstract double getAttributeValue(EntityAttribute attribute);

	@ModifyConstant(method = "travel(Lnet/minecraft/util/math/Vec3d;)V", constant = @Constant(doubleValue = 0.08D, ordinal = 0))
	private double modifyGravity(double original) {
		return getGravity();
	}

	public double getGravityMultiplier() {
		return getAttributeValue(AstromineAttributes.GRAVITY_MULTIPLIER);
	}

	@Inject(at = @At("HEAD"), method = "tick()V")
	void onTick(CallbackInfo callbackInformation) {
		Entity entity = (Entity) (Object) this;

		if (AtmosphereRegistry.INSTANCE.containsKey(entity.world.getDimensionRegistryKey()) && !entity.getType().isIn(AstromineTags.DOES_NOT_BREATHE)) {
			ComponentProvider worldProvider = ComponentProvider.fromWorld(entity.world);

			WorldAtmosphereComponent atmosphereComponent = worldProvider.getComponent(AstromineComponentTypes.WORLD_ATMOSPHERE_COMPONENT);

			FluidVolume atmosphereVolume = atmosphereComponent.get(entity.getBlockPos().offset(Direction.UP));

			if (SpaceSuitItem.hasFullArmor(equippedArmor))
				return;

			boolean isSubmerged = false;

			Box collisionBox = entity.getBoundingBox();

			for (BlockPos blockPos : BlockPos.method_29715(collisionBox).collect(Collectors.toList())) {
				BlockState blockState = entity.world.getBlockState(blockPos);

				if (blockState.getBlock() instanceof FluidBlock) {
					isSubmerged = true;

					FluidState fluidState = blockState.getFluidState();
					Fluid collidingFluid = fluidState.getFluid();

					if (collidingFluid instanceof AdvancedFluid) {
						AdvancedFluid advancedFluid = (AdvancedFluid) collidingFluid;

						if (advancedFluid.isToxic()) {
							entity.damage(advancedFluid.getSource(), advancedFluid.getDamage());

							break;
						}
					}
				}
			}

			if (!isSubmerged) {
				ComponentProvider entityProvider = ComponentProvider.fromEntity(entity);

				EntityOxygenComponent oxygenComponent = entityProvider.getComponent(AstromineComponentTypes.ENTITY_OXYGEN_COMPONENT);

				oxygenComponent.simulate(atmosphereVolume);
			}
		}
	}

	@Inject(at = @At("RETURN"), method = "createLivingAttributes()Lnet/minecraft/entity/attribute/DefaultAttributeContainer$Builder;")
	private static void createLivingAttributesInject(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
		cir.getReturnValue().add(AstromineAttributes.GRAVITY_MULTIPLIER);
	}
}
