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

// import com.github.mixinors.astromine.common.world.generation.glacios.GlaciosChunkGenerator;
// import com.github.mixinors.astromine.common.world.generation.mars.MarsChunkGenerator;
// import com.github.mixinors.astromine.common.world.generation.moon.MoonChunkGenerator;
// import com.github.mixinors.astromine.common.world.generation.vulcan.VulcanChunkGenerator;

import com.github.mixinors.astromine.common.world.generation.space.EarthSpaceChunkGenerator;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import com.mojang.serialization.Codec;

public class AMChunkGenerators {
	public static void init() {
		register(AMDimensions.EARTH_SPACE_ID, EarthSpaceChunkGenerator.CODEC);
		
		// register(AMDimensions.MOON_ID, MoonChunkGenerator.CODEC);
		// register(AMDimensions.MARS_ID, MarsChunkGenerator.CODEC);
		// register(AMDimensions.VULCAN_ID, VulcanChunkGenerator.CODEC);
		// register(AMDimensions.GLACIOS_ID, GlaciosChunkGenerator.CODEC);
	}

	public static void register(Identifier id, Codec<? extends ChunkGenerator> codec) {
		Registry.register(Registry.CHUNK_GENERATOR, id, codec);
	}
}
