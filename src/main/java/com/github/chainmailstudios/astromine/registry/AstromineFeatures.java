package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.world.feature.AsteroidFeature;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

public class AstromineFeatures {
	public static Feature<DefaultFeatureConfig> ASTEROIDS;

	public static void initialize() {
		ASTEROIDS = Registry.register(Registry.FEATURE, AstromineCommon.identifier("asteroids"), new AsteroidFeature(DefaultFeatureConfig.CODEC));
	}
}
