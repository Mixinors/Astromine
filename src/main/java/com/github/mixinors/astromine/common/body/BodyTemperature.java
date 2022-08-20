package com.github.mixinors.astromine.common.body;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.util.extra.Codecs;
import com.mojang.serialization.Codec;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public enum BodyTemperature {
	EXTREMELY_COLD(new TranslatableText("text.astromine.body.temperature.extremely_cold")),
	COLD(new TranslatableText("text.astromine.body.temperature.cold")),
	AVERAGE(new TranslatableText("text.astromine.body.temperature.average")),
	HOT(new TranslatableText("text.astromine.body.temperature.hot")),
	EXTREMELY_HOT(new TranslatableText("text.astromine.body.temperature.extremely_hot"));
	
	public static final Codec<BodyTemperature> CODEC = Codecs.createEnumCodec(BodyTemperature.class);

	private final Text title;
	
	BodyTemperature(Text title) {
		this.title = title;
	}
	
	public Text title() {
		return title;
	}
}
