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
import com.github.mixinors.astromine.common.world.generation.space.EarthOrbitChunkGenerator;
import com.github.mixinors.astromine.common.world.generation.space.MoonChunkGenerator;
import com.github.mixinors.astromine.common.world.generation.space.MoonOrbitChunkGenerator;
import com.github.mixinors.astromine.common.world.generation.space.RocketInteriorsChunkGenerator;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AMChunkGenerators {
	private static final DeferredRegister<MapCodec<? extends ChunkGenerator>> REGISTRY = DeferredRegister.create(Registries.CHUNK_GENERATOR, AMCommon.MOD_ID);
	
	public static void init() {
		REGISTRY.register("earth_orbit", () -> EarthOrbitChunkGenerator.CODEC);
		REGISTRY.register("moon", () -> MoonChunkGenerator.CODEC);
		REGISTRY.register("moon_orbit", () -> MoonOrbitChunkGenerator.CODEC);
		REGISTRY.register("rocket_interiors", () -> RocketInteriorsChunkGenerator.CODEC);
		REGISTRY.register(AMCommon.modEventBus());
	}
}
