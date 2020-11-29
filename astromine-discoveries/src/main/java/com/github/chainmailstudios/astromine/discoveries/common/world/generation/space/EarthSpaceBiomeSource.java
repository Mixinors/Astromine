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

package com.github.chainmailstudios.astromine.discoveries.common.world.generation.space;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryLookupCodec;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesBiomes;

import com.google.common.collect.ImmutableList;

public class EarthSpaceBiomeSource extends BiomeSource {
	public static final Codec<EarthSpaceBiomeSource> CODEC = RecordCodecBuilder.create((instance) -> instance.group(RegistryLookupCodec.create(Registry.BIOME_REGISTRY).forGetter((biomeSource) -> biomeSource.registry), Codec.LONG.fieldOf("seed").stable().forGetter((
		biomeSource) -> biomeSource.seed)).apply(instance, instance.stable(EarthSpaceBiomeSource::new)));
	private final long seed;
	private final Registry<Biome> registry;

	public EarthSpaceBiomeSource(Registry<Biome> registry, long seed) {
		super(ImmutableList.of());
		this.seed = seed;
		this.registry = registry;
	}

	@Override
	protected Codec<? extends BiomeSource> codec() {
		return CODEC;
	}

	@Override
	public BiomeSource withSeed(long seed) {
		return new EarthSpaceBiomeSource(registry, seed);
	}

	@Override
	public Biome getNoiseBiome(int biomeX, int biomeY, int biomeZ) {
		return registry.get(AstromineDiscoveriesBiomes.ASTEROID_BELT);
	}
}
