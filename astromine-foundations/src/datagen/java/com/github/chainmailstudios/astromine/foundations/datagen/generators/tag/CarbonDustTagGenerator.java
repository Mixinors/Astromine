package com.github.chainmailstudios.astromine.foundations.datagen.generators.tag;

import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.foundations.datagen.MaterialItemType;
import com.github.chainmailstudios.astromine.foundations.datagen.MaterialSet;
import com.github.chainmailstudios.astromine.foundations.datagen.MaterialSets;
import me.shedaniel.cloth.api.datagen.v1.TagData;

public class CarbonDustTagGenerator implements TagGenerator {
	@Override
	public void generateTag(TagData tags, MaterialSet set) {
		tags.item(AstromineCommon.identifier("carbon_dusts")).append(set.getItemTagId(MaterialItemType.DUST));
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
