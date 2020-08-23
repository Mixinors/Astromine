package com.github.chainmailstudios.astromine.foundations.datagen.generators.tag;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.generator.material.MaterialItemType;
import com.github.chainmailstudios.astromine.common.generator.material.MaterialSet;
import com.github.chainmailstudios.astromine.common.generator.tag.SetTagGenerator;
import com.github.chainmailstudios.astromine.foundations.registry.AstromineFoundationsMaterialSets;
import me.shedaniel.cloth.api.datagen.v1.TagData;

public class CarbonDustTagGenerator implements SetTagGenerator {
	@Override
	public void generate(TagData tags, MaterialSet set) {
		tags.item(AstromineCommon.identifier("carbon_dusts")).appendTag(set.getItemTagId(MaterialItemType.DUST));
	}

	@Override
	public boolean shouldGenerate(MaterialSet set) {
		return (set.equals(AstromineFoundationsMaterialSets.COAL) || set.equals(AstromineFoundationsMaterialSets.CHARCOAL)) && set.hasType(MaterialItemType.DUST);
	}

	@Override
	public String getGeneratorName() {
		return "carbon_dust";
	}
}
