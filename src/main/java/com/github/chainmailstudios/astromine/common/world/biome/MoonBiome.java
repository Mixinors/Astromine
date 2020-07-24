package com.github.chainmailstudios.astromine.common.world.biome;

import com.github.chainmailstudios.astromine.registry.AstromineFeatures;

import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.CountDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;

public class MoonBiome extends DepthScaleBiome {
	public MoonBiome(float baseHeight, float variation) {
		super(baseHeight, variation);
		this.addFeature(GenerationStep.Feature.RAW_GENERATION, AstromineFeatures.MOON_CRATER.configure(DefaultFeatureConfig.INSTANCE).createDecoratedFeature(Decorator.COUNT_HEIGHTMAP.configure(new CountDecoratorConfig(2))));
	}
}
