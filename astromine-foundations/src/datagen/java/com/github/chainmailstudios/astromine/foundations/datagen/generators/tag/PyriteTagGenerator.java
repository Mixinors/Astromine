package com.github.chainmailstudios.astromine.foundations.datagen.generators.tag;

import com.github.chainmailstudios.astromine.foundations.datagen.MaterialSet;
import com.github.chainmailstudios.astromine.foundations.datagen.MaterialSets;
import me.shedaniel.cloth.api.datagen.v1.TagData;
import net.minecraft.util.Identifier;

public class PyriteTagGenerator implements SetTagGenerator {
	@Override
	public void generate(TagData tags, MaterialSet set) {
		set.getItems().forEach((type, entry) -> {
			if (entry.hasItemTag()) {
				Identifier pyriteTagId = new Identifier(entry.getItemTagId().toString().replaceAll("fools_gold", "pyrite"));
				if (!pyriteTagId.equals(entry.getItemTagId())) {
					tags.item(pyriteTagId).appendTag(entry.getItemTagId());
					if (entry.isBlock()) {
						tags.block(pyriteTagId).appendTag(entry.getItemTagId());
					}
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
