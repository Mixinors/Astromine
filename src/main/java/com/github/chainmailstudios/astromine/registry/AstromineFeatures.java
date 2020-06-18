package com.github.chainmailstudios.astromine.registry;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.world.feature.AsteroidOreFeature;

public class AstromineFeatures {
	public static Feature<DefaultFeatureConfig> ASTEROID_ORES;

	public static void initialize() {
		ASTEROID_ORES = Registry.register(Registry.FEATURE, AstromineCommon.identifier("asteroid_ores"), new AsteroidOreFeature(DefaultFeatureConfig.CODEC));
	}
}
