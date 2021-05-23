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


import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.world.generation.space.EarthSpaceChunkGenerator;
import me.shedaniel.architectury.registry.RegistrySupplier;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import com.mojang.serialization.Codec;

import java.util.function.Supplier;

public class AMChunkGenerators {
	public static RegistrySupplier<Codec<ChunkGenerator>> EARTH_SPACE = register(AMDimensions.EARTH_SPACE_ID, () -> (Codec) EarthSpaceChunkGenerator.CODEC);
	
	public static void init() {}

	public static <T extends ChunkGenerator> RegistrySupplier<Codec<T>> register(Identifier id, Supplier<Codec<T>> codec) {
		return AMCommon.registry(Registry.CHUNK_GENERATOR_KEY).registerSupplied(id, codec);
	}
}
