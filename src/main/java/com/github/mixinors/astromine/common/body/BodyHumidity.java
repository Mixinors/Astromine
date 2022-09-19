package com.github.mixinors.astromine.common.body;

import com.github.mixinors.astromine.common.util.extra.Codecs;
import com.mojang.serialization.Codec;
import net.minecraft.text.Text;

public enum BodyHumidity {
	EXTREMELY_LOW(Text.translatable("text.astromine.body.humidity.extremely_low")),
	LOW(Text.translatable("text.astromine.body.humidity.low")),
	AVERAGE(Text.translatable("text.astromine.body.humidity.average")),
	HIGH(Text.translatable("text.astromine.body.humidity.high")),
	EXTREMELY_HIGH(Text.translatable("text.astromine.body.humidity.extremely_high"));
	
	public static final Codec<BodyHumidity> CODEC = Codecs.createEnumCodec(BodyHumidity.class);
	
	private final Text title;
	
	BodyHumidity(Text title) {
		this.title = title;
	}
	
	public Text title() {
		return title;
	}
}
