package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.world.AsteroidBeltBiome;
import com.github.chainmailstudios.astromine.common.world.generation.AstromineBiomeSource;
import com.mojang.serialization.Codec;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;

public class AstromineBiomes {
	public static final Codec<AstromineBiomeSource> SPACE = Registry.register(Registry.BIOME_SOURCE, AstromineCommon.identifier("earth_space"), AstromineBiomeSource.CODEC);
	public static Biome ASTEROID_BELT;

	public static void initialize() {
		ASTEROID_BELT = Registry.register(Registry.BIOME, AstromineCommon.identifier("asteroid_belt"), new AsteroidBeltBiome());
	}

	public <T extends BiomeSource> Codec<T> register(Identifier id, Codec<BiomeSource> codec) {
		return (Codec<T>) Registry.register(Registry.BIOME_SOURCE, id, codec);
	}
}
