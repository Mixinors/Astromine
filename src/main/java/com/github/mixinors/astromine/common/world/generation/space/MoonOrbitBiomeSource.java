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

package com.github.mixinors.astromine.common.world.generation.space;

import com.github.mixinors.astromine.registry.common.AMBiomes;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import java.util.stream.Stream;

public class MoonOrbitBiomeSource extends BiomeSource {
	public static final MapCodec<MoonOrbitBiomeSource> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			RegistryOps.retrieveElement(AMBiomes.ASTEROID_BELT_KEY)
	).apply(instance, instance.stable(MoonOrbitBiomeSource::new)));
	
	private final Holder<Biome> asteroidBelt;
	
	public MoonOrbitBiomeSource(Registry<Biome> registry) {
		this(registry.getHolder(AMBiomes.ASTEROID_BELT_KEY).orElseThrow());
	}
	
	public MoonOrbitBiomeSource(Holder<Biome> asteroidBelt) {
		this.asteroidBelt = asteroidBelt;
	}
	
	@Override
	protected MapCodec<? extends BiomeSource> codec() {
		return CODEC;
	}
	
	@Override
	protected Stream<Holder<Biome>> collectPossibleBiomes() {
		return Stream.of(this.asteroidBelt);
	}
	
	@Override
	public Holder<Biome> getNoiseBiome(int x, int y, int z, Climate.Sampler noise) {
		return this.asteroidBelt;
	}
}
