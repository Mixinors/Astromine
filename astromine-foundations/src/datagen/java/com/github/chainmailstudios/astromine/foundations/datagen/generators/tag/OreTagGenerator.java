package com.github.chainmailstudios.astromine.foundations.datagen.generators.tag;

import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;
import com.github.chainmailstudios.astromine.datagen.material.MaterialSet;
import com.github.chainmailstudios.astromine.datagen.generator.tag.set.SetTagGenerator;
import me.shedaniel.cloth.api.datagen.v1.TagData;
import net.minecraft.util.Identifier;

public class OreTagGenerator implements SetTagGenerator {
	public final MaterialItemType type;

	public OreTagGenerator(MaterialItemType type) {
		this.type = type;
	}

	@Override
	public void generate(TagData tags, MaterialSet set) {
		tags.item(new Identifier("c", set.getName() + "_ores")).appendTag(set.getItemTagId(type));
		tags.block(new Identifier("c", set.getName() + "_ores")).appendTag(set.getItemTagId(type));
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
