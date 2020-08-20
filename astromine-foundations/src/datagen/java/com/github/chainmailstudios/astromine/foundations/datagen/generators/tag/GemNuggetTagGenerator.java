package com.github.chainmailstudios.astromine.foundations.datagen.generators.tag;

import com.github.chainmailstudios.astromine.foundations.datagen.MaterialItemType;
import com.github.chainmailstudios.astromine.foundations.datagen.MaterialSet;
import me.shedaniel.cloth.api.datagen.v1.TagData;
import net.minecraft.util.Identifier;

public class GemNuggetTagGenerator implements TagGenerator {
	@Override
	public void generateTag(TagData tags, MaterialSet set) {
		tags.item(new Identifier(set.getItemTagId(MaterialItemType.FRAGMENT).toString().replaceAll("fragment", "nugget"))).appendTag(set.getItemTagId(MaterialItemType.FRAGMENT));
	}

	@Override
	public boolean shouldGenerate(MaterialSet set) {
		return set.hasType(MaterialItemType.FRAGMENT);
	}

	@Override
	public String getGeneratorName() {
		return "gem_nugget";
	}
}
