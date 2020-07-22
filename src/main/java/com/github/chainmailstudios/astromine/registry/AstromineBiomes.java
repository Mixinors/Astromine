package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.world.EarthSpaceBiome;
import com.github.chainmailstudios.astromine.common.world.MarsBiome;
import com.github.chainmailstudios.astromine.common.world.MoonBiome;
import com.github.chainmailstudios.astromine.common.world.generation.EarthSpaceBiomeSource;
import com.github.chainmailstudios.astromine.common.world.generation.MarsBiomeSource;
import com.github.chainmailstudios.astromine.common.world.generation.MoonBiomeSource;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public class AstromineBiomes {
	public static Biome ASTEROID_BELT;
	public static Biome MOON_FLATS;
	public static Biome MOON_HILLS;
	public static Biome MOON_LOWLANDS;
	public static Biome MARS;

	public static void initialize() {
		// Biome Sources
		Registry.register(Registry.BIOME_SOURCE, AstromineCommon.identifier("earth_space"), EarthSpaceBiomeSource.CODEC);
		Registry.register(Registry.BIOME_SOURCE, AstromineCommon.identifier("moon"), MoonBiomeSource.CODEC);
		Registry.register(Registry.BIOME_SOURCE, AstromineCommon.identifier("mars"), MarsBiomeSource.CODEC);

		// Biomes
		ASTEROID_BELT = Registry.register(Registry.BIOME, AstromineCommon.identifier("asteroid_belt"), new EarthSpaceBiome());
		MOON_FLATS = Registry.register(Registry.BIOME, AstromineCommon.identifier("moon_flats"), new MoonBiome(100, 20));
		MOON_HILLS = Registry.register(Registry.BIOME, AstromineCommon.identifier("moon_hills"), new MoonBiome(105, 30));
		MOON_LOWLANDS = Registry.register(Registry.BIOME, AstromineCommon.identifier("moon_lowlands"), new MoonBiome(93, 9));
		MARS = Registry.register(Registry.BIOME, AstromineCommon.identifier("mars"), new MarsBiome());
	}
}
