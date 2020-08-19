package com.github.chainmailstudios.astromine.foundations.datagen.generators.tag;

import com.github.chainmailstudios.astromine.foundations.datagen.MaterialItemType;
import com.github.chainmailstudios.astromine.foundations.datagen.MaterialSet;
import net.minecraft.util.Identifier;

public class PiglinLovedTagGenerator extends GenericTagGenerator {
	public PiglinLovedTagGenerator(MaterialItemType type) {
		super("piglin_loved", new Identifier("piglin_loved"), type);
	}

	@Override
	public boolean shouldGenerate(MaterialSet set) {
		return set.isPiglinLoved() && super.shouldGenerate(set);
	}
}
