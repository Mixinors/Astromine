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

package com.github.chainmailstudios.astromine.foundations.registry;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.Fluids;

import com.github.chainmailstudios.astromine.common.registry.FluidEffectRegistry;
import com.github.chainmailstudios.astromine.registry.AstromineFluidEffects;

public class AstromineFoundationsFluidEffects extends AstromineFluidEffects {
	public static void initialize() {
		FluidEffectRegistry.INSTANCE.register(Fluids.LAVA, (entity) -> {
			entity.setOnFireFor(15);
		});

		AstromineFoundationsFluids.OIL_DERIVATIVES.forEach(fluid -> {
			FluidEffectRegistry.INSTANCE.register(fluid, (entity) -> {
				entity.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 30 * 20, 3));
				entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 30 * 20, 3));
				entity.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 30 * 20, 1));
				entity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 30 * 20, 1));
			});
		});
	}
}
