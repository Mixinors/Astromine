package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.world.generation.AstromineBiomeSource;

import net.minecraft.util.registry.Registry;

public class AstromineBiomes {
	public static void initialize() {
		Registry.register(Registry.BIOME_SOURCE, AstromineCommon.identifier(AstromineCommon.MOD_ID), AstromineBiomeSource.CODEC);
	}
}
