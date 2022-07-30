package com.github.mixinors.astromine.common.body;

import com.github.mixinors.astromine.AMCommon;
import com.mojang.serialization.Codec;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public enum BodyHumidity {
	EXTREMELY_LOW(AMCommon.id("extremely_low"), new TranslatableText("text.astromine.body.humidity.extremely_low")),
	LOW(AMCommon.id("low"), new TranslatableText("text.astromine.body.humidity.low")),
	AVERAGE(AMCommon.id("average"), new TranslatableText("text.astromine.body.humidity.average")),
	HIGH(AMCommon.id("high"), new TranslatableText("text.astromine.body.humidity.high")),
	EXTREMELY_HIGH(AMCommon.id("extremely_high"), new TranslatableText("text.astromine.body.humidity.extremely_high"));
	
	public static final Codec<BodyHumidity> CODEC = Identifier.CODEC.xmap(BodyHumidity::byId, BodyHumidity::id);
	
	private static final Map<Identifier, BodyHumidity> BY_ID = new HashMap<>();
	
	private final Identifier id;
	
	private final Text title;
	
	BodyHumidity(Identifier id, Text title) {
		this.id = id;
		
		this.title = title;
	}
	
	public Identifier id() {
		return id;
	}
	
	public Text title() {
		return title;
	}
	
	public static BodyHumidity byId(Identifier id) {
		return BY_ID.get(id);
	}
	
	static {
		for (var value : .values()){
			BY_ID.put(value.id(), value);
		}
	}
}
