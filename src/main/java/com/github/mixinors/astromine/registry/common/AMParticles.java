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

package com.github.mixinors.astromine.registry.common;

import com.github.mixinors.astromine.AMCommon;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AMParticles {
	private static final DeferredRegister<ParticleType<?>> REGISTRY = DeferredRegister.create(Registries.PARTICLE_TYPE, AMCommon.MOD_ID);
	
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SPACE_SLIME = register("space_slime", false);
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ROCKET_FLAME = register("rocket_flame", true);
	
	public static void init() {
		REGISTRY.register(AMCommon.modEventBus());
	}
	
	/**
	 * Registers a new {@link SimpleParticleType} instance under the given name.
	 *
	 * @param name       Name of {@link SimpleParticleType} to register
	 * @param alwaysShow Whether the particle should always appear visible
	 *
	 * @return Registered {@link SimpleParticleType}
	 */
	public static DeferredHolder<ParticleType<?>, SimpleParticleType> register(String name, boolean alwaysShow) {
		return REGISTRY.register(name, () -> new SimpleParticleType(alwaysShow));
	}
}
