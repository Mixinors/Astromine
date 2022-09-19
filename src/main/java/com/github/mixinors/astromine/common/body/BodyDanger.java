package com.github.mixinors.astromine.common.body;

import com.github.mixinors.astromine.common.util.extra.Codecs;
import com.mojang.serialization.Codec;
import net.minecraft.text.Text;

public enum BodyDanger {
	EXTREMELY_LOW(Text.translatable("text.astromine.body.danger.extremely_low")),
	LOW(Text.translatable("text.astromine.body.danger.low")),
	AVERAGE(Text.translatable("text.astromine.body.danger.average")),
	HIGH(Text.translatable("text.astromine.body.danger.high")),
	EXTREMELY_HIGH(Text.translatable("text.astromine.body.danger.extremely_high"));
	
	public static final Codec<BodyDanger> CODEC = Codecs.createEnumCodec(BodyDanger.class);
	
	private final Text title;
	
	BodyDanger(Text title) {
		this.title = title;
	}
	
	public Text title() {
		return title;
	}
}
