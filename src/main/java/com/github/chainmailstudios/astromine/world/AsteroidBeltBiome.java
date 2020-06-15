package com.github.chainmailstudios.astromine.world;

import com.github.chainmailstudios.astromine.registry.AstromineFeatures;
import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.ChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;

public class AsteroidBeltBiome extends Biome {

	public AsteroidBeltBiome() {
		super(new Biome.Settings()
				.configureSurfaceBuilder(SurfaceBuilder.NOPE, SurfaceBuilder.STONE_CONFIG)
				.precipitation(Biome.Precipitation.NONE)
				.category(Biome.Category.NONE)
				.depth(0.1F)
				.scale(0.2F)
				.temperature(0.5F)
				.downfall(0.5F)
				.effects(new BiomeEffects.Builder()
						.waterColor(4159204)
						.waterFogColor(329011)
						.fogColor(12638463)
						.moodSound(BiomeMoodSound.CAVE).build())
				.parent(null));
		this.addFeature(GenerationStep.Feature.RAW_GENERATION,
				AstromineFeatures.ASTEROIDS.configure(DefaultFeatureConfig.INSTANCE)
						.createDecoratedFeature(Decorator.CHANCE_HEIGHTMAP.configure(new ChanceDecoratorConfig(16))));
	}
}
