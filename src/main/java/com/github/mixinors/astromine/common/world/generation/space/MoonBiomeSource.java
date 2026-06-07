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

import com.github.mixinors.astromine.common.noise.OpenSimplexNoise;
import com.github.mixinors.astromine.registry.common.AMBiomes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import java.util.stream.Stream;

public class MoonBiomeSource extends BiomeSource {
	public static final MapCodec<MoonBiomeSource> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			RegistryOps.retrieveElement(AMBiomes.MOON_LIGHT_SIDE_KEY),
			RegistryOps.retrieveElement(AMBiomes.MOON_DARK_SIDE_KEY),
			RegistryOps.retrieveElement(AMBiomes.MOON_CRATER_FIELD_KEY),
			Codec.LONG.optionalFieldOf("seed", 0L).forGetter(MoonBiomeSource::seed)
	).apply(instance, instance.stable(MoonBiomeSource::new)));
	
	private final Holder<Biome> lightSide;
	private final Holder<Biome> darkSide;
	private final Holder<Biome> craterField;
	private final long seed;
	private final OpenSimplexNoise simplex;
	
	public MoonBiomeSource(Registry<Biome> registry) {
		this(
				registry.getHolder(AMBiomes.MOON_LIGHT_SIDE_KEY).orElseThrow(),
				registry.getHolder(AMBiomes.MOON_DARK_SIDE_KEY).orElseThrow(),
				registry.getHolder(AMBiomes.MOON_CRATER_FIELD_KEY).orElseThrow(),
				0L
		);
	}
	
	public MoonBiomeSource(Holder<Biome> lightSide, Holder<Biome> darkSide, Holder<Biome> craterField) {
		this(lightSide, darkSide, craterField, 0L);
	}
	
	public MoonBiomeSource(Holder<Biome> lightSide, Holder<Biome> darkSide, Holder<Biome> craterField, long seed) {
		this.lightSide = lightSide;
		this.darkSide = darkSide;
		this.craterField = craterField;
		this.seed = seed;
		this.simplex = new OpenSimplexNoise(seed);
	}
	
	@Override
	protected MapCodec<? extends BiomeSource> codec() {
		return CODEC;
	}
	
	@Override
	protected Stream<Holder<Biome>> collectPossibleBiomes() {
		return Stream.of(this.lightSide, this.darkSide, this.craterField);
	}
	
	@Override
	public Holder<Biome> getNoiseBiome(int x, int y, int z, Climate.Sampler noise) {
		// Roughly 50% of the moon's surface should be light side,
		// 30% crater fields, and 20% dark side.
		
		// The noise range is [0.0 .. 1.0], but I've seen it go negative.
		var sample = simplex.sample(x / 512.0F, z / 512.0F);
		
		if (sample > 0.5F) {
			return this.lightSide;
		} else if (sample > 0.2F) {
			return this.craterField;
		} else {
			return this.darkSide;
		}
	}
	
	public long seed() {
		return seed;
	}
}
