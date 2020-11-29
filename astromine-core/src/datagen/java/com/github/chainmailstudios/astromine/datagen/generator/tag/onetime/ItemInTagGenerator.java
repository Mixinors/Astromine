package com.github.chainmailstudios.astromine.datagen.generator.tag.onetime;

import net.minecraft.resources.ResourceLocation;

import me.shedaniel.cloth.api.datagen.v1.TagData;

public class ItemInTagGenerator implements OneTimeTagGenerator {
	private final ResourceLocation tagId;
	private final ResourceLocation itemId;

	public ItemInTagGenerator(ResourceLocation tagId, ResourceLocation itemId) {
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
