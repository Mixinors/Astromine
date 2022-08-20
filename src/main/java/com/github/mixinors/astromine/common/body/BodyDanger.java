package com.github.mixinors.astromine.common.body;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.util.extra.Codecs;
import com.mojang.serialization.Codec;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public enum BodyDanger {
	EXTREMELY_LOW(new TranslatableText("text.astromine.body.danger.extremely_low")),
	LOW(new TranslatableText("text.astromine.body.danger.low")),
	AVERAGE(new TranslatableText("text.astromine.body.danger.average")),
	HIGH(new TranslatableText("text.astromine.body.danger.high")),
	EXTREMELY_HIGH(new TranslatableText("text.astromine.body.danger.extremely_high"));
	
	public static final Codec<BodyDanger> CODEC = Codecs.createEnumCodec(BodyDanger.class);
	
	private final Text title;
	
	BodyDanger(Text title) {
		this.title = title;
	}
	
	public Text title() {
		return title;
	}
}
