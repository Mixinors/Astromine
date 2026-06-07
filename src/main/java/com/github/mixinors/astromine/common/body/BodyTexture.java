package com.github.mixinors.astromine.common.body;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

public record BodyTexture(
		ResourceLocation up,
		ResourceLocation down,
		ResourceLocation north,
		ResourceLocation south,
		ResourceLocation east,
		ResourceLocation west
) {
	public static final Codec<BodyTexture> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					ResourceLocation.CODEC.fieldOf("up").forGetter(BodyTexture::up),
					ResourceLocation.CODEC.fieldOf("down").forGetter(BodyTexture::down),
					ResourceLocation.CODEC.fieldOf("north").forGetter(BodyTexture::north),
					ResourceLocation.CODEC.fieldOf("south").forGetter(BodyTexture::south),
					ResourceLocation.CODEC.fieldOf("east").forGetter(BodyTexture::east),
					ResourceLocation.CODEC.fieldOf("west").forGetter(BodyTexture::west)
			).apply(instance, BodyTexture::new)
	);
}
