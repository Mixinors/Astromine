package com.github.mixinors.astromine.common.body;

import com.github.mixinors.astromine.AMCommon;
import com.mojang.serialization.Codec;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public enum BodyDanger {
	EXTREMELY_LOW(AMCommon.id("extremely_low"), new TranslatableText("text.astromine.body.danger.extremely_low")),
	LOW(AMCommon.id("low"), new TranslatableText("text.astromine.body.danger.low")),
	AVERAGE(AMCommon.id("average"), new TranslatableText("text.astromine.body.danger.average")),
	HIGH(AMCommon.id("high"), new TranslatableText("text.astromine.body.danger.high")),
	EXTREMELY_HIGH(AMCommon.id("extremely_high"), new TranslatableText("text.astromine.body.danger.extremely_high"));
	
	public static final Codec<BodyDanger> CODEC = Identifier.CODEC.xmap(BodyDanger::byId, BodyDanger::id);
	
	private static final Map<Identifier, BodyDanger> BY_ID = new HashMap<>();
	
	private final Identifier id;
	
	private final Text title;
	
	BodyDanger(Identifier id, Text title) {
		this.id = id;
		
		this.title = title;
	}
	
	public Identifier id() {
		return id;
	}
	
	public Text title() {
		return title;
	}
	
	public static BodyDanger byId(Identifier id) {
		return BY_ID.get(id);
	}
	
	static {
		for (var value : .values()){
			BY_ID.put(value.id(), value);
		}
	}
}
