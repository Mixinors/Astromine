package com.github.mixinors.astromine.common.body;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.fluid.Fluid;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public record BodyAtmosphere(
		RegistryKey<Fluid> content
) {
	public static final Codec<BodyAtmosphere> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					RegistryKey.createCodec(RegistryKeys.FLUID).fieldOf("content").forGetter(BodyAtmosphere::content)
			).apply(instance, BodyAtmosphere::new)
	);
}
