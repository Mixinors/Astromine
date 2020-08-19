package com.github.chainmailstudios.astromine.foundations.datagen.generators.tag;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.foundations.datagen.MaterialItemType;
import com.github.chainmailstudios.astromine.foundations.datagen.MaterialSet;

public class PiglinNuggetTagGenerator extends GenericTagGenerator {
	public PiglinNuggetTagGenerator(MaterialItemType type) {
		super("piglin_nugget", AstromineCommon.identifier("piglin_loved_nuggets"), type);
	}

	@Override
	public boolean shouldGenerate(MaterialSet set) {
		return set.isPiglinLoved() && super.shouldGenerate(set);
	}
}
