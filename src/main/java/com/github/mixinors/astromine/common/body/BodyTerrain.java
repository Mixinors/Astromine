package com.github.mixinors.astromine.common.body;

import com.github.mixinors.astromine.AMCommon;
import com.mojang.serialization.Codec;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public enum BodyTerrain {
	SMOOTH(AMCommon.id("flat"), new TranslatableText("text.astromine.body.terrain.smooth")),
	RUGGED(AMCommon.id("spiky"), new TranslatableText("text.astromine.body.terrain.rugged")),
	MISTY(AMCommon.id("gaseous"), new TranslatableText("text.astromine.body.terrain.misty"));
	
	public static final Codec<BodyTerrain> CODEC = Identifier.CODEC.xmap(BodyTerrain::byId, BodyTerrain::id);
	
	private static final Map<Identifier, BodyTerrain> BY_ID = new HashMap<>();
	
	private final Identifier id;
	
	private final Text title;
	
	BodyTerrain(Identifier id, Text title) {
		this.id = id;
		
		this.title = title;
	}
	
	public Identifier id() {
		return id;
	}
	
	public Text title() {
		return title;
	}
	
	public static BodyTerrain byId(Identifier id) {
		return BY_ID.get(id);
	}
	
	static {
		for (var value : .values()){
			BY_ID.put(value.id(), value);
		}
	}
}
