package com.github.chainmailstudios.astromine.registry;

import net.minecraft.util.registry.Registry;

import com.github.chainmailstudios.astromine.common.world.generation.mars.MarsBiomeSource;
import com.github.chainmailstudios.astromine.common.world.generation.moon.MoonBiomeSource;
import com.github.chainmailstudios.astromine.common.world.generation.space.EarthSpaceBiomeSource;
import com.github.chainmailstudios.astromine.common.world.generation.vulcan.VulcanBiomeSource;

public class AstromineBiomeSources {
	public static void initialize() {
		Registry.register(Registry.BIOME_SOURCE, AstromineDimensions.EARTH_SPACE_ID, EarthSpaceBiomeSource.CODEC);
		Registry.register(Registry.BIOME_SOURCE, AstromineDimensions.MOON_ID, MoonBiomeSource.CODEC);
		Registry.register(Registry.BIOME_SOURCE, AstromineDimensions.MARS_ID, MarsBiomeSource.CODEC);
		Registry.register(Registry.BIOME_SOURCE, AstromineDimensions.VULCAN_ID, VulcanBiomeSource.CODEC);
	}
}
