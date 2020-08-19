package com.github.chainmailstudios.astromine.foundations.datagen.generators.tag;

import net.minecraft.block.Block;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.foundations.datagen.MaterialItemType;
import com.github.chainmailstudios.astromine.foundations.datagen.MaterialSet;
import com.github.chainmailstudios.astromine.foundations.datagen.MaterialSets;
import me.shedaniel.cloth.api.datagen.v1.TagData;

import java.util.Set;

public class BeaconBaseTagGenerator extends GenericTagGenerator {
	public BeaconBaseTagGenerator() {
		super("beacon_base", new Identifier("beacon_base_blocks"), MaterialItemType.BLOCK);
	}

	@Override
	public void generateTag(TagData tags, MaterialSet set) {
		TagData.TagBuilder<Block> builder = tags.block(new Identifier("beacon_base_blocks"));
		builder.appendTag(set.getItemTagId(MaterialItemType.BLOCK));
	}

	@Override
	public boolean shouldGenerate(MaterialSet set) {
		return super.shouldGenerate(set) && !set.equals(MaterialSets.LAPIS) && !set.equals(MaterialSets.COAL) && !set.equals(MaterialSets.REDSTONE);
	}
}
