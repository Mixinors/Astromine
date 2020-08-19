package com.github.chainmailstudios.astromine.foundations.datagen.generators.tag;

import net.minecraft.item.ItemConvertible;
import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.foundations.datagen.MaterialItemType;
import com.github.chainmailstudios.astromine.foundations.datagen.MaterialSet;
import me.shedaniel.cloth.api.datagen.v1.TagData;

import java.util.Set;

public class PiglinNuggetTagGenerator extends GenericTagGenerator {
	public PiglinNuggetTagGenerator(MaterialItemType type) {
		super("piglin_nugget", AstromineCommon.identifier("piglin_loved_nuggets"), type);
	}

	@Override
	public boolean shouldGenerate(MaterialSet set) {
		return set.isPiglinLoved() && super.shouldGenerate(set);
	}
}
