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

package com.github.chainmailstudios.astromine.discoveries.common.world.generation.vulcan;

import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesBiomes;

import com.google.common.collect.ImmutableList;

public class VulcanBiomeSource extends BiomeSource {
	public static final Codec<VulcanBiomeSource> CODEC = RecordCodecBuilder.create(instance -> instance.group(RegistryLookupCodec.of(Registry.BIOME_KEY).forGetter((biomeSource) -> biomeSource.registry), Codec.LONG.fieldOf("seed").stable().forGetter((
		biomeSource) -> biomeSource.seed)).apply(instance, instance.stable(VulcanBiomeSource::new)));
	private final long seed;
	private final Registry<Biome> registry;

	public VulcanBiomeSource(Registry<Biome> registry, long seed) {
		super(ImmutableList.of());
		this.seed = seed;
		this.registry = registry;
	}

	@Override
	protected Codec<? extends BiomeSource> getCodec() {
		return CODEC;
	}

	@Override
	public BiomeSource withSeed(long seed) {
		return new VulcanBiomeSource(registry, seed);
	}

	@Override
	public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
		return registry.get(AstromineDiscoveriesBiomes.VULCAN_PLAINS);
	}
}
