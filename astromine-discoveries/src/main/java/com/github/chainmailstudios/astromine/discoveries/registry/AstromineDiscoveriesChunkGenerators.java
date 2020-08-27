package com.github.chainmailstudios.astromine.discoveries.registry;

import com.github.chainmailstudios.astromine.discoveries.common.world.generation.mars.MarsChunkGenerator;
import com.github.chainmailstudios.astromine.discoveries.common.world.generation.moon.MoonChunkGenerator;
import com.github.chainmailstudios.astromine.discoveries.common.world.generation.space.EarthSpaceChunkGenerator;
import com.github.chainmailstudios.astromine.discoveries.common.world.generation.vulcan.VulcanChunkGenerator;
import com.github.chainmailstudios.astromine.registry.AstromineChunkGenerators;
import com.github.chainmailstudios.astromine.registry.AstromineDimensions;

public class AstromineDiscoveriesChunkGenerators extends AstromineChunkGenerators {
	public static void initialize() {
		register(AstromineDiscoveriesDimensions.EARTH_SPACE_ID, EarthSpaceChunkGenerator.CODEC);
		register(AstromineDiscoveriesDimensions.MOON_ID, MoonChunkGenerator.CODEC);
		register(AstromineDiscoveriesDimensions.MARS_ID, MarsChunkGenerator.CODEC);
		register(AstromineDiscoveriesDimensions.VULCAN_ID, VulcanChunkGenerator.CODEC);
	}
}
