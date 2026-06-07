package com.github.mixinors.astromine.common.body;

import com.github.mixinors.astromine.common.util.extra.Codecs;
import com.mojang.serialization.Codec;
import net.minecraft.network.chat.Component;

public enum BodyHumidity {
	EXTREMELY_LOW(Component.translatable("text.astromine.body.humidity.extremely_low")),
	LOW(Component.translatable("text.astromine.body.humidity.low")),
	AVERAGE(Component.translatable("text.astromine.body.humidity.average")),
	HIGH(Component.translatable("text.astromine.body.humidity.high")),
	EXTREMELY_HIGH(Component.translatable("text.astromine.body.humidity.extremely_high"));
	
	public static final Codec<BodyHumidity> CODEC = Codecs.createEnumCodec(BodyHumidity.class);
	
	private final Component title;
	
	BodyHumidity(Component title) {
		this.title = title;
	}
	
	public Component title() {
		return title;
	}
}
