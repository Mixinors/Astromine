package com.github.chainmailstudios.astromine.registry;

import net.minecraft.util.registry.Registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.world.generation.AstromineChunkGenerator;

public class AstromineChunkGenerators {
	public static void initialize() {
		Registry.register(Registry.CHUNK_GENERATOR, AstromineCommon.identifier("space"), AstromineChunkGenerator.CODEC);
	}
}
