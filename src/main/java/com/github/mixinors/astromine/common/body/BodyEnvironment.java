package com.github.mixinors.astromine.common.body;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import org.jetbrains.annotations.Nullable;
import java.util.Optional;

public record BodyEnvironment(
		BodyDanger danger,
		BodyTemperature temperature,
		BodyHumidity humidity,
		@Nullable BodyTerrain terrain,
		float sound,
		float gravity
) {
	public static final Codec<BodyEnvironment> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					BodyDanger.CODEC.fieldOf("danger").forGetter(BodyEnvironment::danger),
					BodyTemperature.CODEC.fieldOf("temperature").forGetter(BodyEnvironment::temperature),
					BodyHumidity.CODEC.fieldOf("humidity").forGetter(BodyEnvironment::humidity),
					BodyTerrain.CODEC.optionalFieldOf("terrain").forGetter(environment -> Optional.ofNullable(environment.terrain)),
					Codec.FLOAT.optionalFieldOf("sound", 1.0F).forGetter(BodyEnvironment::sound),
					Codec.FLOAT.optionalFieldOf("gravity", 0.08F).forGetter(BodyEnvironment::gravity)
			).apply(instance, (danger, temperature, humidity, terrain, sound, gravity) -> new BodyEnvironment(danger, temperature, humidity, terrain.orElse(null), sound, gravity))
	);
}
