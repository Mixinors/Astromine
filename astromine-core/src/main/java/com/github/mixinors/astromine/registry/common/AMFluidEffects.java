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

package com.github.mixinors.astromine.registry.common;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;

import com.github.mixinors.astromine.common.registry.FluidEffectRegistry;
import com.github.mixinors.astromine.common.util.EntityUtils;

public class AMFluidEffects {
	public static void init() {
		/** Makes attempting to breathe lava through a vent or space suit set you on fire. */
		FluidEffectRegistry.INSTANCE.register(Fluids.LAVA, (submerged, entity) -> {
			if(!submerged && !entity.isFireImmune()) {
				entity.setOnFireFor(15);
			}
		});

		/** Makes attempting to breathe water through a vent or space suit cause drowning. */
		FluidEffectRegistry.INSTANCE.register(Fluids.WATER, (submerged, entity) -> {
			if(!submerged) {
				if(!entity.canBreatheInWater() && !StatusEffectUtil.hasWaterBreathing(entity)) {
					entity.setAir(EntityUtils.getNextAirUnderwater(entity, entity.getAir()));
				}
				if(entity.hurtByWater()) {
					entity.damage(DamageSource.DROWN, 1.0F);
				}
			}
		});
		
		/** Makes attempting to walk on oil be dangerous. */
		FluidEffectRegistry.INSTANCE.register(AMFluids.OIL, (submerged, entity) -> {
			if(entity.isAlive() && !entity.isSpectator() && (!(entity instanceof PlayerEntity) || !((PlayerEntity)entity).isCreative())) {
				entity.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 30 * 20, 3));
				entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 30 * 20, 3));
				entity.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 30 * 20, 1));
				entity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 30 * 20, 1));
			}
		});
	}
}
