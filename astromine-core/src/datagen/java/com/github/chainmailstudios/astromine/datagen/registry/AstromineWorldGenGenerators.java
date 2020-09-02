package com.github.chainmailstudios.astromine.datagen.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.datagen.generator.worldgen.onetime.OneTimeWorldGenGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.worldgen.set.SetWorldGenGenerator;
import com.github.chainmailstudios.astromine.datagen.material.MaterialSet;
import me.shedaniel.cloth.api.datagen.v1.WorldGenData;

import java.util.ArrayList;
import java.util.List;

public abstract class AstromineWorldGenGenerators {
	private final List<SetWorldGenGenerator> SET_GENERATORS = new ArrayList<>();
	private final List<OneTimeWorldGenGenerator> ONE_TIME_GENERATORS = new ArrayList<>();

	public SetWorldGenGenerator register(SetWorldGenGenerator generator) {
		SET_GENERATORS.add(generator);
		return generator;
	}

	public OneTimeWorldGenGenerator register(OneTimeWorldGenGenerator generator) {
		ONE_TIME_GENERATORS.add(generator);
		return generator;
	}

	public void generateWorldGen(WorldGenData worldGen) {
		AstromineMaterialSets.getMaterialSets().forEach((set) -> generateSetWorldGen(worldGen, set));
		generateOneTimeWorldGen(worldGen);
	}

	private void generateSetWorldGen(WorldGenData worldGen, MaterialSet set) {
		SET_GENERATORS.forEach((generator) -> {
			try {
				if (set.shouldGenerate(generator)) {
					generator.generate(worldGen, set);
					AstromineCommon.LOGGER.info("World generation of " + set.getName() + " succeeded, with generator " + generator.getGeneratorName() + ".");
				}
			} catch (Exception exception) {
				AstromineCommon.LOGGER.error("World generation of " + set.getName() + " failed, with generator " + generator.getGeneratorName() + ".");
				AstromineCommon.LOGGER.error(exception.getMessage());
			}
		});
	}

	private void generateOneTimeWorldGen(WorldGenData worldGen) {
		ONE_TIME_GENERATORS.forEach((generator) -> generator.generate(worldGen));
	}
}
