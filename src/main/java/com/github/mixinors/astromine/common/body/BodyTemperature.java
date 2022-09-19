package com.github.mixinors.astromine.common.body;

import com.github.mixinors.astromine.common.util.extra.Codecs;
import com.mojang.serialization.Codec;
import net.minecraft.text.Text;

public enum BodyTemperature {
	EXTREMELY_COLD(Text.translatable("text.astromine.body.temperature.extremely_cold")),
	COLD(Text.translatable("text.astromine.body.temperature.cold")),
	AVERAGE(Text.translatable("text.astromine.body.temperature.average")),
	HOT(Text.translatable("text.astromine.body.temperature.hot")),
	EXTREMELY_HOT(Text.translatable("text.astromine.body.temperature.extremely_hot"));
	
	public static final Codec<BodyTemperature> CODEC = Codecs.createEnumCodec(BodyTemperature.class);

	private final Text title;
	
	BodyTemperature(Text title) {
		this.title = title;
	}
	
	public Text title() {
		return title;
	}
}
