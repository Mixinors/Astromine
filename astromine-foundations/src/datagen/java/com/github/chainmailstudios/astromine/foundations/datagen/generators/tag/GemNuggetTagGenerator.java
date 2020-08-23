package com.github.chainmailstudios.astromine.foundations.datagen.generators.tag;

import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;
import com.github.chainmailstudios.astromine.datagen.material.MaterialSet;
import com.github.chainmailstudios.astromine.datagen.generator.tag.set.SetTagGenerator;
import me.shedaniel.cloth.api.datagen.v1.TagData;
import net.minecraft.util.Identifier;

public class GemNuggetTagGenerator implements SetTagGenerator {
	@Override
	public void generate(TagData tags, MaterialSet set) {
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
