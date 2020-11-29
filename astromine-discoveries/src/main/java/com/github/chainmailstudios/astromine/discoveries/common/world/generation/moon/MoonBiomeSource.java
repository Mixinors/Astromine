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

package com.github.chainmailstudios.astromine.discoveries.common.world.generation.moon;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import com.github.chainmailstudios.astromine.discoveries.common.world.layer.moon.MoonBiomeLayer;

import com.google.common.collect.ImmutableList;
import java.util.function.LongFunction;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryLookupCodec;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.newbiome.area.Area;
import net.minecraft.world.level.newbiome.area.AreaFactory;
import net.minecraft.world.level.newbiome.context.BigContext;
import net.minecraft.world.level.newbiome.context.LazyAreaContext;
import net.minecraft.world.level.newbiome.layer.Layer;
import net.minecraft.world.level.newbiome.layer.ZoomLayer;

public class MoonBiomeSource extends BiomeSource {
	public static Codec<MoonBiomeSource> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.LONG.fieldOf("seed").stable().forGetter(source -> source.seed), RegistryLookupCodec.create(Registry.BIOME_REGISTRY).forGetter(source -> source.biomeRegistry)).apply(instance,
		instance.stable(MoonBiomeSource::new)));
	private final long seed;
	private final Registry<Biome> biomeRegistry;
	private final Layer sampler;

	public MoonBiomeSource(long seed, Registry<Biome> biomeRegistry) {
		super(ImmutableList.of());
		this.seed = seed;
		this.biomeRegistry = biomeRegistry;
		this.sampler = build(seed);
	}

	@Override
	protected Codec<? extends BiomeSource> codec() {
		return CODEC;
	}

	@Override
	public BiomeSource withSeed(long seed) {
		return new MoonBiomeSource(seed, biomeRegistry);
	}

	@Override
	public Biome getNoiseBiome(int biomeX, int biomeY, int biomeZ) {
		return sampler.get(biomeRegistry, biomeX, biomeZ);
	}

	public Layer build(long seed) {
		return new Layer(build((salt) -> new LazyAreaContext(25, seed, salt)));
	}

	private <T extends Area, C extends BigContext<T>> AreaFactory<T> build(LongFunction<C> contextProvider) {
		AreaFactory<T> mainLayer = new MoonBiomeLayer(biomeRegistry).run(contextProvider.apply(4L));
		for (int i = 0; i < 5; i++) {
			mainLayer = ZoomLayer.FUZZY.run(contextProvider.apply(43 + i), mainLayer);
		}

		return mainLayer;
	}
}
