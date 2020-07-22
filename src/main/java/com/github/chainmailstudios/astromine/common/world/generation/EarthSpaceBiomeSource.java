package com.github.chainmailstudios.astromine.common.world.generation;

import com.github.chainmailstudios.astromine.registry.AstromineBiomes;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;

public class EarthSpaceBiomeSource extends BiomeSource {
	public static Codec<EarthSpaceBiomeSource> CODEC = Codec.LONG.fieldOf("seed").xmap(EarthSpaceBiomeSource::new, (source) -> source.seed).stable().codec();
	private final long seed;

	public EarthSpaceBiomeSource(long seed) {
		super(ImmutableList.of());
		this.seed = seed;
	}

	@Override
	protected Codec<? extends BiomeSource> method_28442() {
		return CODEC;
	}

	@Override
	public BiomeSource withSeed(long seed) {
		return new EarthSpaceBiomeSource(seed);
	}

	@Override
	public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
		return AstromineBiomes.ASTEROID_BELT;
	}
}
