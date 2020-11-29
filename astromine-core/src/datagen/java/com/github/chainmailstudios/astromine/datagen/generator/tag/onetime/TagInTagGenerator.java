package com.github.chainmailstudios.astromine.datagen.generator.tag.onetime;

import net.minecraft.resources.ResourceLocation;

import me.shedaniel.cloth.api.datagen.v1.TagData;

public class TagInTagGenerator implements OneTimeTagGenerator {
	private final ResourceLocation parentTag;
	private final ResourceLocation childTag;

	public TagInTagGenerator(ResourceLocation parentTag, ResourceLocation childTag) {
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
