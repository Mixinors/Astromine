package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.world.generation.AstromineBiomeSource;
import com.mojang.serialization.Codec;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.source.BiomeSource;

public class AstromineBiomes {
	public static final Codec<AstromineBiomeSource> ASTROMINE = Registry.register(Registry.BIOME_SOURCE, AstromineCommon.identifier(AstromineCommon.MOD_ID), AstromineBiomeSource.CODEC);

	public static void initialize() {
		// Unused.
	}

	public <T extends BiomeSource> Codec<T> register(Identifier id, Codec<BiomeSource> codec) {
		return (Codec<T>) Registry.register(Registry.BIOME_SOURCE, id, codec);
	}
}
