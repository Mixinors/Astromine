package com.github.mixinors.astromine.common.body;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.util.extra.Codecs;
import com.mojang.serialization.Codec;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public enum BodyHumidity {
	EXTREMELY_LOW(new TranslatableText("text.astromine.body.humidity.extremely_low")),
	LOW(new TranslatableText("text.astromine.body.humidity.low")),
	AVERAGE(new TranslatableText("text.astromine.body.humidity.average")),
	HIGH(new TranslatableText("text.astromine.body.humidity.high")),
	EXTREMELY_HIGH(new TranslatableText("text.astromine.body.humidity.extremely_high"));
	
	public static final Codec<BodyHumidity> CODEC = Codecs.createEnumCodec(BodyHumidity.class);
	
	private final Text title;
	
	BodyHumidity(Text title) {
		this.title = title;
	}
	
	public Text title() {
		return title;
	}
}
