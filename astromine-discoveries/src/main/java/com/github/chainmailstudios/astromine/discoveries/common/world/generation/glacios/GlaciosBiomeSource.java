package com.github.chainmailstudios.astromine.discoveries.common.world.generation.glacios;

import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesBiomes;

import com.google.common.collect.ImmutableList;

public class GlaciosBiomeSource extends BiomeSource {
	public static final Codec<GlaciosBiomeSource> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			RegistryLookupCodec.of(Registry.BIOME_KEY).forGetter((biomeSource) -> biomeSource.registry),
			Codec.LONG.fieldOf("seed").stable().forGetter((biomeSource) -> biomeSource.seed))
			.apply(instance, instance.stable(GlaciosBiomeSource::new)));
	private final long seed;
	private final Registry<Biome> registry;

	public GlaciosBiomeSource(Registry<Biome> registry, long seed) {
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
		return new GlaciosBiomeSource(registry, seed);
	}

	@Override
	public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
		return registry.get(AstromineDiscoveriesBiomes.GLACIOS);
	}
}