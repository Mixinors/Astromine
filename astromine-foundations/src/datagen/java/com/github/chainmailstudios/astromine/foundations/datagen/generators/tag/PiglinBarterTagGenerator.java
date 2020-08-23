package com.github.chainmailstudios.astromine.foundations.datagen.generators.tag;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.generator.material.MaterialItemType;
import com.github.chainmailstudios.astromine.common.generator.material.MaterialSet;

public class PiglinBarterTagGenerator extends GenericSetTagGenerator {
	public PiglinBarterTagGenerator(MaterialItemType type) {
		super("piglin_barter", AstromineCommon.identifier("piglin_bartering_items"), type);
	}

	@Override
	public boolean shouldGenerate(MaterialSet set) {
		return set.isPiglinLoved() && super.shouldGenerate(set);
	}
}
