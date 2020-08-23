package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.common.generator.material.MaterialSet;
import com.github.chainmailstudios.astromine.common.generator.tag.OneTimeTagGenerator;
import com.github.chainmailstudios.astromine.common.generator.tag.SetTagGenerator;
import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.AstromineCommon;
import me.shedaniel.cloth.api.datagen.v1.TagData;

import java.util.ArrayList;
import java.util.List;

public class AstromineTagGenerators {
	private static final List<SetTagGenerator> SET_GENERATORS = new ArrayList<>();
	private static final List<OneTimeTagGenerator> ONE_TIME_GENERATORS = new ArrayList<>();

	public static SetTagGenerator register(SetTagGenerator generator) {
		SET_GENERATORS.add(generator);
		return generator;
	}

	public static OneTimeTagGenerator register(OneTimeTagGenerator generator) {
		ONE_TIME_GENERATORS.add(generator);
		return generator;
	}

	public static void generateSetTags(TagData tags, MaterialSet set) {
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

	public static void generateOneTimeTags(TagData tags) {
		ONE_TIME_GENERATORS.forEach((generator) -> generator.generate(tags));
	}
}
