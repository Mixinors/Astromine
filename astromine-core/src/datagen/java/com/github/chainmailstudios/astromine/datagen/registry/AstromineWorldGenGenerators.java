package com.github.chainmailstudios.astromine.datagen.registry;

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
				}
			} catch (Exception e) {
				System.out.println("oh fuck worldgen bronked for " + set.getName());
				System.out.println(e.getMessage());
			}
		});
	}

	private void generateOneTimeWorldGen(WorldGenData worldGen) {
		ONE_TIME_GENERATORS.forEach((generator) -> generator.generate(worldGen));
	}
}
