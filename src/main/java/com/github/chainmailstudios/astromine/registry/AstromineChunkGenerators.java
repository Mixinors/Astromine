package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.world.generation.EarthSpaceChunkGenerator;
import com.github.chainmailstudios.astromine.common.world.generation.MoonChunkGenerator;

import net.minecraft.util.registry.Registry;

public class AstromineChunkGenerators {
	public static void initialize() {
		Registry.register(Registry.CHUNK_GENERATOR, AstromineCommon.identifier("earth_space"), EarthSpaceChunkGenerator.CODEC);
		Registry.register(Registry.CHUNK_GENERATOR, AstromineCommon.identifier("moon"), MoonChunkGenerator.CODEC);
	}
}
