package com.github.mixinors.astromine.common.body;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

public record BodyAtmosphere(
		RegistryKey<Fluid> content
) {
	public static final Codec<BodyAtmosphere> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					RegistryKey.createCodec(Registry.FLUID_KEY).fieldOf("content").forGetter(BodyAtmosphere::content)
			).apply(instance, BodyAtmosphere::new)
	);
}
