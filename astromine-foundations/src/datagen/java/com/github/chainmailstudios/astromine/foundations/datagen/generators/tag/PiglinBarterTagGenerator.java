package com.github.chainmailstudios.astromine.foundations.datagen.generators.tag;

import net.minecraft.item.ItemConvertible;
import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.foundations.datagen.MaterialItemType;
import com.github.chainmailstudios.astromine.foundations.datagen.MaterialSet;
import me.shedaniel.cloth.api.datagen.v1.TagData;

import java.util.Set;

public class PiglinBarterTagGenerator extends GenericTagGenerator {
	public PiglinBarterTagGenerator(MaterialItemType type) {
		super("piglin_barter", AstromineCommon.identifier("piglin_bartering_items"), type);
	}

	@Override
	public boolean shouldGenerate(MaterialSet set) {
		return set.isPiglinLoved() && super.shouldGenerate(set);
	}
}
