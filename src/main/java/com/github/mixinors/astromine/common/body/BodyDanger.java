package com.github.mixinors.astromine.common.body;

import com.github.mixinors.astromine.common.util.extra.Codecs;
import com.mojang.serialization.Codec;
import net.minecraft.network.chat.Component;

public enum BodyDanger {
	EXTREMELY_LOW(Component.translatable("text.astromine.body.danger.extremely_low")),
	LOW(Component.translatable("text.astromine.body.danger.low")),
	AVERAGE(Component.translatable("text.astromine.body.danger.average")),
	HIGH(Component.translatable("text.astromine.body.danger.high")),
	EXTREMELY_HIGH(Component.translatable("text.astromine.body.danger.extremely_high"));
	
	public static final Codec<BodyDanger> CODEC = Codecs.createEnumCodec(BodyDanger.class);
	
	private final Component title;
	
	BodyDanger(Component title) {
		this.title = title;
	}
	
	public Component title() {
		return title;
	}
}
