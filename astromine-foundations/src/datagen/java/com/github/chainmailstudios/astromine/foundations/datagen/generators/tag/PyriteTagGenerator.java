package com.github.chainmailstudios.astromine.foundations.datagen.generators.tag;

import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.foundations.datagen.MaterialSet;
import com.github.chainmailstudios.astromine.foundations.datagen.MaterialSets;
import me.shedaniel.cloth.api.datagen.v1.TagData;

public class PyriteTagGenerator implements TagGenerator {
	@Override
	public void generateTag(TagData tags, MaterialSet set) {
		set.getItems().forEach((type, entry) -> {
			if(entry.hasItemTag()) {
				tags.item(new Identifier(entry.getItemTagId().toString().replaceAll("fools_gold", "pyrite"))).appendTag(entry.getItemTagId());
				if(entry.isBlock()) {
					tags.block(new Identifier(entry.getItemTagId().toString().replaceAll("fools_gold", "pyrite"))).appendTag(entry.getItemTagId());
				}
			}
		});
	}

	@Override
	public boolean shouldGenerate(MaterialSet set) {
		return set.equals(MaterialSets.FOOLS_GOLD);
	}

	@Override
	public String getGeneratorName() {
		return "pyrite";
	}
}
