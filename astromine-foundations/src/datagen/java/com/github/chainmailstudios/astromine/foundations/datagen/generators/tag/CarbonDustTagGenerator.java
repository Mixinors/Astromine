package com.github.chainmailstudios.astromine.foundations.datagen.generators.tag;

import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.datagen.generator.tag.set.SetTagGenerator;
import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;
import com.github.chainmailstudios.astromine.datagen.material.MaterialSet;
import com.github.chainmailstudios.astromine.foundations.datagen.registry.AstromineFoundationsMaterialSets;
import me.shedaniel.cloth.api.datagen.v1.TagData;

public class CarbonDustTagGenerator implements SetTagGenerator {
	@Override
	public void generate(TagData tags, MaterialSet set) {
		tags.item(new Identifier("c", "carbon_dusts")).appendTag(set.getItemTagId(MaterialItemType.DUST));
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
