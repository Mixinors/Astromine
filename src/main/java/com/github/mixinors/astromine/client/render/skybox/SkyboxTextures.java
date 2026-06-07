package com.github.mixinors.astromine.client.render.skybox;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

public record SkyboxTextures(ResourceLocation up, ResourceLocation down, ResourceLocation north, ResourceLocation east, ResourceLocation south, ResourceLocation west) {
	public static final Codec<SkyboxTextures> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ResourceLocation.CODEC.fieldOf("up").forGetter(SkyboxTextures::up),
			ResourceLocation.CODEC.fieldOf("down").forGetter(SkyboxTextures::down),
			ResourceLocation.CODEC.fieldOf("north").forGetter(SkyboxTextures::north),
			ResourceLocation.CODEC.fieldOf("east").forGetter(SkyboxTextures::east),
			ResourceLocation.CODEC.fieldOf("south").forGetter(SkyboxTextures::south),
			ResourceLocation.CODEC.fieldOf("west").forGetter(SkyboxTextures::west)
	).apply(instance, SkyboxTextures::new));
}
