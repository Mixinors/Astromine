package com.github.mixinors.astromine.common.body;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.github.mixinors.astromine.common.util.math.Position;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import net.minecraft.resources.ResourceLocation;

public record BodyOrbit(
		@Nullable ResourceLocation orbitedBodyId,
		@Nullable Position orbitedBodyOffset,
		double width, double height, double speed,
		boolean tidalLocked
) {
	public static final Codec<BodyOrbit> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					ResourceLocation.CODEC.optionalFieldOf("orbitedBodyId").forGetter(orbit -> Optional.ofNullable(orbit.orbitedBodyId)),
					Position.CODEC.optionalFieldOf("orbitedBodyOffset").forGetter(orbit -> Optional.ofNullable(orbit.orbitedBodyOffset)),
					Codec.DOUBLE.fieldOf("width").forGetter(BodyOrbit::width),
					Codec.DOUBLE.fieldOf("height").forGetter(BodyOrbit::height),
					Codec.DOUBLE.fieldOf("speed").forGetter(BodyOrbit::speed),
					Codec.BOOL.fieldOf("tidalLocked").forGetter(BodyOrbit::tidalLocked)
			).apply(instance, ((orbitedBodyId, orbitedBodyOffset, width, height, speed, tidalLocked) -> new BodyOrbit(orbitedBodyId.orElse(null), orbitedBodyOffset.orElse(null), width, height, speed, tidalLocked)))
	);
}
