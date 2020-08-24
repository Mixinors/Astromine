package com.github.chainmailstudios.astromine.datagen.generator.worldgen.onetime;

import me.shedaniel.cloth.api.datagen.v1.WorldGenData;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.ConfiguredFeature;

public class GenericFeatureWorldGenGenerator implements OneTimeWorldGenGenerator {
	private final Identifier identifier;
	private final ConfiguredFeature<?, ?> feature;

	public GenericFeatureWorldGenGenerator(Identifier identifier, ConfiguredFeature<?, ?> feature) {
		this.identifier = identifier;
		this.feature = feature;
	}

	@Override
	public String getGeneratorName() {
		return "generic_feature";
	}

	@Override
	public void generate(WorldGenData data) {
		data.addFeature(identifier, feature);
	}
}
