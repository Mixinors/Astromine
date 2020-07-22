package com.github.chainmailstudios.astromine.common.world.generation;

import com.github.chainmailstudios.astromine.registry.AstromineBiomes;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;

public class MarsBiomeSource extends BiomeSource {
	public static Codec<MarsBiomeSource> CODEC = Codec.LONG.fieldOf("seed").xmap(MarsBiomeSource::new, (source) -> source.seed).stable().codec();
	private final long seed;

	public MarsBiomeSource(long seed) {
		super(ImmutableList.of());
		this.seed = seed;
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
		return AstromineBiomes.MARS;
	}
}
