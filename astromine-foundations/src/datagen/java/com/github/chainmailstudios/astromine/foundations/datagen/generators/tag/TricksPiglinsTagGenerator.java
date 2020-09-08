package com.github.chainmailstudios.astromine.foundations.datagen.generators.tag;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.datagen.generator.tag.set.SetTagGenerator;
import com.github.chainmailstudios.astromine.datagen.material.MaterialSet;
import com.github.chainmailstudios.astromine.foundations.datagen.registry.AstromineFoundationsMaterialSets;
import me.shedaniel.cloth.api.datagen.v1.TagData;

public class TricksPiglinsTagGenerator implements SetTagGenerator {
	@Override
	public void generate(TagData tags, MaterialSet set) {
		set.getItems().forEach((type, entry) -> {
			tags.item(AstromineCommon.identifier("tricks_piglins")).append(entry.getItemId());
		});
	}

	@Override
	public boolean shouldGenerate(MaterialSet set) {
		return set.equals(AstromineFoundationsMaterialSets.FOOLS_GOLD);
	}

	@Override
	public String getGeneratorName() {
		return "tricks_piglins";
	}
}