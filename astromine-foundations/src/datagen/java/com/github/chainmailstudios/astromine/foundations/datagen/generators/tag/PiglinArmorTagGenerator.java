package com.github.chainmailstudios.astromine.foundations.datagen.generators.tag;

import net.minecraft.item.ItemConvertible;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.foundations.datagen.MaterialItemType;
import com.github.chainmailstudios.astromine.foundations.datagen.MaterialSet;
import me.shedaniel.cloth.api.datagen.v1.TagData;

import java.util.Set;

public class PiglinArmorTagGenerator extends GenericTagGenerator {
	public PiglinArmorTagGenerator(MaterialItemType type) {
		super("piglin_armor", AstromineCommon.identifier("piglin_safe_armor"), type);
	}

	@Override
	public boolean shouldGenerate(MaterialSet set) {
		return set.isPiglinLoved() && super.shouldGenerate(set);
	}
}
