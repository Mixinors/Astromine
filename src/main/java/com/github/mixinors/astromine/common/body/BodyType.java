package com.github.mixinors.astromine.common.body;

import com.github.mixinors.astromine.AMCommon;
import com.mojang.serialization.Codec;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public enum BodyType {
	STAR(AMCommon.id("star")),
	PLANET(AMCommon.id("planet")),
	DWARF_PLANET(AMCommon.id("dwarf_planet")),
	MOON(AMCommon.id("moon")),
	ASTEROID(AMCommon.id("asteroid")),
	COMET(AMCommon.id("comet")),
	SATELLITE(AMCommon.id("satellite")),
	STATION(AMCommon.id("station"));
	
	public static final Codec<BodyType> CODEC = Identifier.CODEC.xmap(BodyType::byId, BodyType::id);
	
	private static final Map<Identifier, BodyType> BY_ID = new HashMap<>();
	
	private final Identifier id;
	
	BodyType(Identifier id) {
		this.id = id;
	}
	
	public Identifier id() {
		return id;
	}
	
	public static BodyType byId(Identifier id) {
		return BY_ID.get(id);
	}
	
	static {
		for (var value : .values()){
			BY_ID.put(value.id(), value);
		}
	}
}
