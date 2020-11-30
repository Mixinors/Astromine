package com.github.chainmailstudios.astromine.foundations.datagen.generators.tag;

import net.minecraft.resources.ResourceLocation;

import com.github.chainmailstudios.astromine.datagen.generator.tag.set.GenericSetTagGenerator;
import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;
import com.github.chainmailstudios.astromine.datagen.material.MaterialSet;

public class PiglinArmorTagGenerator extends GenericSetTagGenerator {
	public PiglinArmorTagGenerator(MaterialItemType type) {
		super("piglin_armor", new ResourceLocation("piglib", "piglin_safe_armor"), type);
	}

	@Override
	public boolean shouldGenerate(MaterialSet set) {
		return set.isPiglinLoved() && super.shouldGenerate(set);
	}
}
