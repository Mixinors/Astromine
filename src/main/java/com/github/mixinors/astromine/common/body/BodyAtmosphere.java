package com.github.mixinors.astromine.common.body;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.material.Fluid;

public record BodyAtmosphere(
		ResourceKey<Fluid> content
) {
	public static final Codec<BodyAtmosphere> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					ResourceKey.codec(Registries.FLUID).fieldOf("content").forGetter(BodyAtmosphere::content)
			).apply(instance, BodyAtmosphere::new)
	);
}
