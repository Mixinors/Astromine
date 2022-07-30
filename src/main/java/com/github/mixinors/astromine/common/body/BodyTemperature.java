package com.github.mixinors.astromine.common.body;

import com.github.mixinors.astromine.AMCommon;
import com.mojang.serialization.Codec;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public enum BodyTemperature {
	EXTREMELY_COLD(AMCommon.id("extremely_cold"), new TranslatableText("text.astromine.body.temperature.extremely_cold")),
	COLD(AMCommon.id("cold"), new TranslatableText("text.astromine.body.temperature.cold")),
	AVERAGE(AMCommon.id("average"), new TranslatableText("text.astromine.body.temperature.average")),
	HOT(AMCommon.id("hot"), new TranslatableText("text.astromine.body.temperature.hot")),
	EXTREMELY_HOT(AMCommon.id("extremely_hot"), new TranslatableText("text.astromine.body.temperature.extremely_hot"));
	
	public static final Codec<BodyTemperature> CODEC = Identifier.CODEC.xmap(BodyTemperature::byId, BodyTemperature::id);
	
	private static final Map<Identifier, BodyTemperature> BY_ID = new HashMap<>();
	
	private final Identifier id;
	
	private final Text title;
	
	BodyTemperature(Identifier id, Text title) {
		this.id = id;
		
		this.title = title;
	}
	
	public Identifier id() {
		return id;
	}
	
	public Text title() {
		return title;
	}
	
	public static BodyTemperature byId(Identifier id) {
		return BY_ID.get(id);
	}
	
	static {
		for (var value : .values()){
			BY_ID.put(value.id(), value);
		}
	}
}
