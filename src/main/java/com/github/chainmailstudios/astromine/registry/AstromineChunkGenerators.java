package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.world.generation.AstromineChunkGenerator;

import net.minecraft.util.registry.Registry;

public class AstromineChunkGenerators {
	public static void initialize() {
		Registry.register(Registry.CHUNK_GENERATOR, AstromineCommon.identifier(AstromineCommon.MOD_ID), AstromineChunkGenerator.CODEC);
	}
}
