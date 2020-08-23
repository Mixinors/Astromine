package com.github.chainmailstudios.astromine.datagen.registry;

import com.github.chainmailstudios.astromine.datagen.material.MaterialSet;
import com.github.chainmailstudios.astromine.datagen.generator.tag.onetime.OneTimeTagGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.tag.set.SetTagGenerator;

import me.shedaniel.cloth.api.datagen.v1.TagData;

import java.util.ArrayList;
import java.util.List;

public class AstromineTagGenerators {
	private final List<SetTagGenerator> SET_GENERATORS = new ArrayList<>();
	private final List<OneTimeTagGenerator> ONE_TIME_GENERATORS = new ArrayList<>();

	public SetTagGenerator register(SetTagGenerator generator) {
		SET_GENERATORS.add(generator);
		return generator;
	}

	public OneTimeTagGenerator register(OneTimeTagGenerator generator) {
		ONE_TIME_GENERATORS.add(generator);
		return generator;
	}

	public void generateTags(TagData tags) {
		AstromineMaterialSets.getMaterialSets().forEach((set) -> {
			generateSetTags(tags, set);
		});
		generateOneTimeTags(tags);
	}

	private void generateSetTags(TagData tags, MaterialSet set) {
		SET_GENERATORS.forEach((generator) -> {
			try {
				if (set.shouldGenerate(generator)) {
					generator.generate(tags, set);
					System.out.println("generated tag " + generator.getGeneratorName());
				}
			} catch (Exception e) {
				System.out.println("oh fuck tag bronked for " + generator.getGeneratorName());
				System.out.println(e.getMessage());
			}
		});
	}

	private void generateOneTimeTags(TagData tags) {
		ONE_TIME_GENERATORS.forEach((generator) -> generator.generate(tags));
	}
}
