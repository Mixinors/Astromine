package com.github.chainmailstudios.astromine.foundations.datagen.generators.tag;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.generator.material.MaterialItemType;
import com.github.chainmailstudios.astromine.common.generator.material.MaterialSet;

public class PiglinNuggetTagGenerator extends GenericSetTagGenerator {
	public PiglinNuggetTagGenerator(MaterialItemType type) {
		super("piglin_nugget", AstromineCommon.identifier("piglin_loved_nuggets"), type);
	}

	@Override
	public boolean shouldGenerate(MaterialSet set) {
		return set.isPiglinLoved() && super.shouldGenerate(set);
	}
}
