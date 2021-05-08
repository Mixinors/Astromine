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

package com.github.mixinors.astromine.registry;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;

import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.registry.Registry;

import com.github.mixinors.astromine.AstromineCommon;

public class AstromineParticles {
	public static final DefaultParticleType SPACE_SLIME = register("space_slime", false);
	public static final DefaultParticleType ROCKET_FLAME = register("rocket_flame", true);
	public static final DefaultParticleType MARS_DUST = register("mars_dust", false);
	
	/**
	 * Registers a new {@link DefaultParticleType} instance under the given name.
	 *
	 * @param name
	 *        Name of {@link DefaultParticleType} to register
	 * @param alwaysShow
	 *        Whether or not the com.github.chainmailstudios.astromine.client.particle should always appear visible
	 *
	 * @return Registered {@link DefaultParticleType}
	 */
	public static DefaultParticleType register(String name, boolean alwaysShow) {
		return Registry.register(Registry.PARTICLE_TYPE, AstromineCommon.identifier(name), FabricParticleTypes.simple(alwaysShow));
	}
}
