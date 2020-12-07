package com.github.chainmailstudios.astromine.datagen.generator.tag.onetime;

import net.minecraft.util.Identifier;

import me.shedaniel.cloth.api.datagen.v1.TagData;

public class ItemInTagGenerator implements OneTimeTagGenerator {
	private final Identifier tagId;
	private final Identifier itemId;

	public ItemInTagGenerator(Identifier tagId, Identifier itemId) {
		this.tagId = tagId;
		this.itemId = itemId;
	}

	@Override
	public void generate(TagData data) {
		data.item(tagId).append(itemId);
	}

	@Override
	public String getGeneratorName() {
		return "item_in_tag";
	}
}
