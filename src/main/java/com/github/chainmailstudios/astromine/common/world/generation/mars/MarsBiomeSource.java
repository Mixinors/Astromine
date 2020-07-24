package com.github.chainmailstudios.astromine.common.world.generation.mars;

import java.util.function.LongFunction;

import com.github.chainmailstudios.astromine.common.world.layer.mars.MarsBiomeLayer;
import com.github.chainmailstudios.astromine.common.world.layer.mars.MarsRiverLayer;
import com.github.chainmailstudios.astromine.common.world.layer.util.PlainsOnlyLayer;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.layer.ScaleLayer;
import net.minecraft.world.biome.layer.SimpleLandNoiseLayer;
import net.minecraft.world.biome.layer.util.CachingLayerContext;
import net.minecraft.world.biome.layer.util.LayerFactory;
import net.minecraft.world.biome.layer.util.LayerSampleContext;
import net.minecraft.world.biome.layer.util.LayerSampler;
import net.minecraft.world.biome.source.BiomeLayerSampler;
import net.minecraft.world.biome.source.BiomeSource;

public class MarsBiomeSource extends BiomeSource {
	public static Codec<MarsBiomeSource> CODEC = Codec.LONG.fieldOf("seed").xmap(MarsBiomeSource::new, (source) -> source.seed).stable().codec();
	private final long seed;
	private final BiomeLayerSampler sampler;

	public MarsBiomeSource(long seed) {
		super(ImmutableList.of());
		this.seed = seed;
		this.sampler = build(seed);
	}

	@Override
	protected Codec<? extends BiomeSource> method_28442() {
		return CODEC;
	}

	@Override
	public BiomeSource withSeed(long seed) {
		return new MarsBiomeSource(seed);
	}

	@Override
	public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
		return sampler.sample(biomeX, biomeZ);
	}

	public static BiomeLayerSampler build(long seed) {
		return new BiomeLayerSampler(build((salt) -> new CachingLayerContext(25, seed, salt)));
	}

	private static <T extends LayerSampler, C extends LayerSampleContext<T>> LayerFactory<T> build(LongFunction<C> contextProvider) {
		LayerFactory<T> mainLayer = SimpleLandNoiseLayer.INSTANCE.create(contextProvider.apply(432L), PlainsOnlyLayer.INSTANCE.create(contextProvider.apply(543L)));
		for (int i = 0; i < 7; i++) {
			mainLayer = ScaleLayer.NORMAL.create(contextProvider.apply(43 + i), mainLayer);
		}

		mainLayer = MarsRiverLayer.INSTANCE.create(contextProvider.apply(56L), mainLayer);
		for (int i = 0; i < 2; i++) {
			mainLayer = ScaleLayer.NORMAL.create(contextProvider.apply(473 + i), mainLayer);
		}

		mainLayer = MarsBiomeLayer.INSTANCE.create(contextProvider.apply(721), mainLayer);

		return mainLayer;
	}
}
