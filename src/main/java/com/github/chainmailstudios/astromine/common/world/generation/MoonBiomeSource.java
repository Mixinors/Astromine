package com.github.chainmailstudios.astromine.common.world.generation;

import java.util.function.LongFunction;

import com.github.chainmailstudios.astromine.common.world.layer.MoonBiomeLayer;
import com.github.chainmailstudios.astromine.registry.AstromineBiomes;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.layer.ScaleLayer;
import net.minecraft.world.biome.layer.util.CachingLayerContext;
import net.minecraft.world.biome.layer.util.CachingLayerSampler;
import net.minecraft.world.biome.layer.util.LayerFactory;
import net.minecraft.world.biome.layer.util.LayerSampleContext;
import net.minecraft.world.biome.layer.util.LayerSampler;
import net.minecraft.world.biome.source.BiomeLayerSampler;
import net.minecraft.world.biome.source.BiomeSource;

public class MoonBiomeSource extends BiomeSource {
	public static Codec<MoonBiomeSource> CODEC = Codec.LONG.fieldOf("seed").xmap(MoonBiomeSource::new, (source) -> source.seed).stable().codec();
	private final long seed;
	private final BiomeLayerSampler sampler;

	public MoonBiomeSource(long seed) {
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
		return new MoonBiomeSource(seed);
	}

	@Override
	public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
		return sampler.sample(biomeX, biomeZ);
	}

	public static BiomeLayerSampler build(long seed) {
		return new BiomeLayerSampler(build((salt) -> new CachingLayerContext(25, seed, salt)));
	}

	private static <T extends LayerSampler, C extends LayerSampleContext<T>> LayerFactory<T> build(LongFunction<C> contextProvider) {
		LayerFactory<T> mainLayer = MoonBiomeLayer.INSTANCE.create(contextProvider.apply(4L));
		for (int i = 0; i < 5; i++) {
			mainLayer = ScaleLayer.FUZZY.create(contextProvider.apply(43 + i), mainLayer);
		}

		return mainLayer;
	}
}
