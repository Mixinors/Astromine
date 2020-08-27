package com.github.chainmailstudios.astromine.discoveries.registry;

import com.github.chainmailstudios.astromine.discoveries.common.world.generation.mars.MarsBiomeSource;
import com.github.chainmailstudios.astromine.discoveries.common.world.generation.moon.MoonBiomeSource;
import com.github.chainmailstudios.astromine.discoveries.common.world.generation.space.EarthSpaceBiomeSource;
import com.github.chainmailstudios.astromine.discoveries.common.world.generation.vulcan.VulcanBiomeSource;
import com.github.chainmailstudios.astromine.registry.AstromineBiomeSources;
import com.github.chainmailstudios.astromine.registry.AstromineDimensions;
import net.minecraft.util.registry.Registry;

public class AstromineDiscoveriesBiomeSources extends AstromineBiomeSources {
	public static void initialize() {
		Registry.register(Registry.BIOME_SOURCE, AstromineDiscoveriesDimensions.EARTH_SPACE_ID, EarthSpaceBiomeSource.CODEC);
		Registry.register(Registry.BIOME_SOURCE, AstromineDiscoveriesDimensions.MOON_ID, MoonBiomeSource.CODEC);
		Registry.register(Registry.BIOME_SOURCE, AstromineDiscoveriesDimensions.MARS_ID, MarsBiomeSource.CODEC);
		Registry.register(Registry.BIOME_SOURCE, AstromineDiscoveriesDimensions.VULCAN_ID, VulcanBiomeSource.CODEC);
	}
}
