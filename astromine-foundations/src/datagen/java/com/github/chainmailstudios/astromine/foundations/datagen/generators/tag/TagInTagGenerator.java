package com.github.chainmailstudios.astromine.foundations.datagen.generators.tag;

import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.foundations.datagen.MaterialSet;
import com.github.chainmailstudios.astromine.foundations.datagen.generators.SetGenerator;
import me.shedaniel.cloth.api.datagen.v1.TagData;

public class TagInTagGenerator implements OneTimeTagGenerator {
	private final Identifier parentTag;
	private final Identifier childTag;

	public TagInTagGenerator(Identifier parentTag, Identifier childTag) {
		this.parentTag = parentTag;
		this.childTag = childTag;
	}

	@Override
	public void generate(TagData data) {
		data.item(parentTag).appendTag(childTag);
	}

	@Override
	public String getGeneratorName() {
		return "tag_in_tag";
	}
}
