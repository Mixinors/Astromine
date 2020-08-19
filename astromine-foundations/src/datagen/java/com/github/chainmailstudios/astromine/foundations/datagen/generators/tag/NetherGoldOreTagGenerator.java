package com.github.chainmailstudios.astromine.foundations.datagen.generators.tag;

import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.foundations.datagen.MaterialItemType;
import com.github.chainmailstudios.astromine.foundations.datagen.MaterialSet;
import com.github.chainmailstudios.astromine.foundations.datagen.MaterialSets;
import me.shedaniel.cloth.api.datagen.v1.TagData;

public class NetherGoldOreTagGenerator implements TagGenerator {
	@Override
	public void generateTag(TagData tags, MaterialSet set) {
		tags.item(set.getItemTagId(MaterialItemType.ORE)).append(new Identifier("nether_gold_ore"));
		tags.block(set.getItemTagId(MaterialItemType.ORE)).append(new Identifier("nether_gold_ore"));
	}

	@Override
	public boolean shouldGenerate(MaterialSet set) {
		return set.equals(MaterialSets.GOLD);
	}

	@Override
	public String getGeneratorName() {
		return "nether_gold_ore";
	}
}
