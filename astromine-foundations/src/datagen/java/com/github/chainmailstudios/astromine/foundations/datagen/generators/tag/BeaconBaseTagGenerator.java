package com.github.chainmailstudios.astromine.foundations.datagen.generators.tag;

import net.minecraft.block.Block;
import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.datagen.generator.tag.set.GenericSetTagGenerator;
import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;
import com.github.chainmailstudios.astromine.datagen.material.MaterialSet;
import me.shedaniel.cloth.api.datagen.v1.TagData;

public class BeaconBaseTagGenerator extends GenericSetTagGenerator {
	public BeaconBaseTagGenerator() {
		super("beacon_base", new Identifier("beacon_base_blocks"), MaterialItemType.BLOCK);
	}

	@Override
	public void generate(TagData tags, MaterialSet set) {
		TagData.TagBuilder<Block> builder = tags.block(new Identifier("beacon_base_blocks"));
		builder.appendTag(set.getItemTagId(MaterialItemType.BLOCK));
	}

	@Override
	public boolean shouldGenerate(MaterialSet set) {
		return super.shouldGenerate(set) && !set.isFromVanilla(MaterialItemType.BLOCK);
	}
}
