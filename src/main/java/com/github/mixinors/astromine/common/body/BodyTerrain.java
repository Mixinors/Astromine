package com.github.mixinors.astromine.common.body;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.util.extra.Codecs;
import com.mojang.serialization.Codec;
import net.minecraft.text.Text;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public enum BodyTerrain {
	SMOOTH(Text.translatable("text.astromine.body.terrain.smooth")),
	RUGGED(Text.translatable("text.astromine.body.terrain.rugged")),
	MISTY(Text.translatable("text.astromine.body.terrain.misty"));
	
	public static final Codec<BodyTerrain> CODEC = Codecs.createEnumCodec(BodyTerrain.class);

	private final Text title;
	
	BodyTerrain(Text title) {
		this.title = title;
	}
	
	public Text title() {
		return title;
	}
}
