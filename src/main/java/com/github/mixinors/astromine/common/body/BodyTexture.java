package com.github.mixinors.astromine.common.body;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Identifier;

public record BodyTexture(
		Identifier up,
		Identifier down,
		Identifier north,
		Identifier south,
		Identifier east,
		Identifier west
) {
	public static final Codec<BodyTexture> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					Identifier.CODEC.fieldOf("up").forGetter(BodyTexture::up),
					Identifier.CODEC.fieldOf("down").forGetter(BodyTexture::down),
					Identifier.CODEC.fieldOf("north").forGetter(BodyTexture::north),
					Identifier.CODEC.fieldOf("south").forGetter(BodyTexture::south),
					Identifier.CODEC.fieldOf("east").forGetter(BodyTexture::east),
					Identifier.CODEC.fieldOf("west").forGetter(BodyTexture::west)
			).apply(instance, BodyTexture::new)
	);
}
