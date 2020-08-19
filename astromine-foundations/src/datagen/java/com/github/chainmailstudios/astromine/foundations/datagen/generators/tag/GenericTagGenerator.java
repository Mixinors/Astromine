package com.github.chainmailstudios.astromine.foundations.datagen.generators.tag;

import com.github.chainmailstudios.astromine.foundations.datagen.MaterialItemType;
import com.github.chainmailstudios.astromine.foundations.datagen.MaterialSet;
import me.shedaniel.cloth.api.datagen.v1.TagData;
import net.minecraft.item.ItemConvertible;
import net.minecraft.util.Identifier;

public class GenericTagGenerator implements TagGenerator {
	protected final String name;
	protected final Identifier tagId;
	public final MaterialItemType type;

	public GenericTagGenerator(String name, Identifier tagId, MaterialItemType type) {
		this.name = name;
		this.tagId = tagId;
		this.type = type;
	}

	@Override
	public void generateTag(TagData tags, MaterialSet set) {
		TagData.TagBuilder<ItemConvertible> builder = tags.item(tagId);
		builder.append(set.getEntry(type));
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