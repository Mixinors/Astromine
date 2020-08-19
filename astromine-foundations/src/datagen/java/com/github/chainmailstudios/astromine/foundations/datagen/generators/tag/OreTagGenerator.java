package com.github.chainmailstudios.astromine.foundations.datagen.generators.tag;

import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.foundations.datagen.MaterialItemType;
import com.github.chainmailstudios.astromine.foundations.datagen.MaterialSet;
import me.shedaniel.cloth.api.datagen.v1.TagData;

public class OreTagGenerator implements TagGenerator {
	public final MaterialItemType type;

	public OreTagGenerator(MaterialItemType type) {
		this.type = type;
	}

	@Override
	public void generateTag(TagData tags, MaterialSet set) {
		tags.item(new Identifier("c", set.getName()+"_ores")).appendTag(set.getItemTagId(type));
		tags.block(new Identifier("c", set.getName()+"_ores")).appendTag(set.getItemTagId(type));
	}

	@Override
	public boolean shouldGenerate(MaterialSet set) {
		return set.hasType(type);
	}

	@Override
	public String getGeneratorName() {
		return "ore";
	}
}
