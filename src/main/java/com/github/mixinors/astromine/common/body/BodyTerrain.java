package com.github.mixinors.astromine.common.body;

import com.github.mixinors.astromine.common.util.extra.Codecs;
import com.mojang.serialization.Codec;
import net.minecraft.network.chat.Component;

public enum BodyTerrain {
	SMOOTH(Component.translatable("text.astromine.body.terrain.smooth")),
	RUGGED(Component.translatable("text.astromine.body.terrain.rugged")),
	MISTY(Component.translatable("text.astromine.body.terrain.misty"));
	
	public static final Codec<BodyTerrain> CODEC = Codecs.createEnumCodec(BodyTerrain.class);

	private final Component title;
	
	BodyTerrain(Component title) {
		this.title = title;
	}
	
	public Component title() {
		return title;
	}
}
