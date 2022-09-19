package com.github.mixinors.astromine.common.body;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record BodyOrbit(
		@Nullable Identifier orbitedBodyId,
		@Nullable Position orbitedBodyOffset,
		double width, double height, double speed,
		boolean tidalLocked
) {
	public static final Codec<BodyOrbit> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					Identifier.CODEC.optionalFieldOf("orbitedBodyId").forGetter(orbit -> Optional.ofNullable(orbit.orbitedBodyId)),
					Position.CODEC.optionalFieldOf("orbitedBodyOffset").forGetter(orbit -> Optional.ofNullable(orbit.orbitedBodyOffset)),
					Codec.DOUBLE.fieldOf("width").forGetter(BodyOrbit::width),
					Codec.DOUBLE.fieldOf("height").forGetter(BodyOrbit::height),
					Codec.DOUBLE.fieldOf("speed").forGetter(BodyOrbit::speed),
					Codec.BOOL.fieldOf("tidalLocked").forGetter(BodyOrbit::tidalLocked)
			).apply(instance, ((orbitedBodyId, orbitedBodyOffset, width, height, speed, tidalLocked) -> new BodyOrbit(orbitedBodyId.orElse(null), orbitedBodyOffset.orElse(null), width, height, speed, tidalLocked)))
	);
}
