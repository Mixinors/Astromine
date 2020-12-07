package com.github.chainmailstudios.astromine.datagen.generator.tag.set;

import net.minecraft.item.ItemConvertible;
import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;
import com.github.chainmailstudios.astromine.datagen.material.MaterialSet;
import me.shedaniel.cloth.api.datagen.v1.TagData;

public class GenericSetTagGenerator implements SetTagGenerator {
	public final MaterialItemType type;
	protected final String name;
	protected final Identifier tagId;

	public GenericSetTagGenerator(String name, Identifier tagId, MaterialItemType type) {
		this.name = name;
		this.tagId = tagId;
		this.type = type;
	}

	@Override
	public void generate(TagData tags, MaterialSet set) {
		TagData.TagBuilder<ItemConvertible> builder = tags.item(tagId);
		builder.append(type.isOptionalInTag(), set.getEntry(type));
	}

	@Override
	public boolean shouldGenerate(MaterialSet set) {
		return set.hasType(type);
	}

	@Override
	public String getGeneratorName() {
		return name;
	}
}