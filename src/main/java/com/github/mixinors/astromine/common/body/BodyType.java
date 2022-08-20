package com.github.mixinors.astromine.common.body;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.util.extra.Codecs;
import com.mojang.serialization.Codec;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public enum BodyType {
	STAR,
	PLANET,
	DWARF_PLANET,
	MOON,
	ASTEROID,
	COMET,
	SATELLITE,
	STATION;
	
	public static final Codec<BodyType> CODEC = Codecs.createEnumCodec(BodyType.class);
}
