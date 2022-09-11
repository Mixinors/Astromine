package com.github.mixinors.astromine.client.render.skybox;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Identifier;

public record SkyboxTextures(Identifier up, Identifier down, Identifier north, Identifier east, Identifier south, Identifier west) {
	public static final Codec<SkyboxTextures> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Identifier.CODEC.fieldOf("up").forGetter(SkyboxTextures::up),
			Identifier.CODEC.fieldOf("down").forGetter(SkyboxTextures::down),
			Identifier.CODEC.fieldOf("west").forGetter(SkyboxTextures::west),
			Identifier.CODEC.fieldOf("east").forGetter(SkyboxTextures::east),
			Identifier.CODEC.fieldOf("north").forGetter(SkyboxTextures::north),
			Identifier.CODEC.fieldOf("south").forGetter(SkyboxTextures::south)
	).apply(instance, SkyboxTextures::new));
}
