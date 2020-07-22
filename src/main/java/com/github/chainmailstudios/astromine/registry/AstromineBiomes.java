package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.world.EarthSpaceBiome;
import com.github.chainmailstudios.astromine.common.world.generation.EarthSpaceBiomeSource;
import com.mojang.serialization.Codec;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;

public class AstromineBiomes {
	public static Biome ASTEROID_BELT;

	public static void initialize() {
		// Biome Sources
		Registry.register(Registry.BIOME_SOURCE, AstromineCommon.identifier("earth_space"), EarthSpaceBiomeSource.CODEC);

		// Biomes
		ASTEROID_BELT = Registry.register(Registry.BIOME, AstromineCommon.identifier("asteroid_belt"), new EarthSpaceBiome());
	}
}
