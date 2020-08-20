package com.github.chainmailstudios.astromine.foundations.datagen.generators.tag;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.foundations.datagen.MaterialItemType;
import com.github.chainmailstudios.astromine.foundations.datagen.MaterialSet;
import com.github.chainmailstudios.astromine.foundations.datagen.MaterialSets;
import me.shedaniel.cloth.api.datagen.v1.TagData;

public class CarbonDustTagGenerator implements SetTagGenerator {
	@Override
	public void generate(TagData tags, MaterialSet set) {
		tags.item(AstromineCommon.identifier("carbon_dusts")).appendTag(set.getItemTagId(MaterialItemType.DUST));
	}

	@Override
	public boolean shouldGenerate(MaterialSet set) {
		return (set.equals(MaterialSets.COAL) || set.equals(MaterialSets.CHARCOAL)) && set.hasType(MaterialItemType.DUST);
	}

	@Override
	public String getGeneratorName() {
		return "carbon_dust";
	}
}
