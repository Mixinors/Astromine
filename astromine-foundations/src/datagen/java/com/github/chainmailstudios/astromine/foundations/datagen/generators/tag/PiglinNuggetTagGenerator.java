package com.github.chainmailstudios.astromine.foundations.datagen.generators.tag;

import net.minecraft.util.ResourceLocation;

import com.github.chainmailstudios.astromine.datagen.generator.tag.set.GenericSetTagGenerator;
import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;
import com.github.chainmailstudios.astromine.datagen.material.MaterialSet;

public class PiglinNuggetTagGenerator extends GenericSetTagGenerator {
	public PiglinNuggetTagGenerator(MaterialItemType type) {
		super("piglin_nugget", new ResourceLocation("piglib", "piglin_loved_nuggets"), type);
	}

	@Override
	public boolean shouldGenerate(MaterialSet set) {
		return set.isPiglinLoved() && super.shouldGenerate(set);
	}
}
