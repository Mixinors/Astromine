package com.github.mixinors.astromine.common.body;

import com.github.mixinors.astromine.common.util.extra.Codecs;
import com.mojang.serialization.Codec;
import net.minecraft.network.chat.Component;

public enum BodyTemperature {
	EXTREMELY_COLD(Component.translatable("text.astromine.body.temperature.extremely_cold")),
	COLD(Component.translatable("text.astromine.body.temperature.cold")),
	AVERAGE(Component.translatable("text.astromine.body.temperature.average")),
	HOT(Component.translatable("text.astromine.body.temperature.hot")),
	EXTREMELY_HOT(Component.translatable("text.astromine.body.temperature.extremely_hot"));
	
	public static final Codec<BodyTemperature> CODEC = Codecs.createEnumCodec(BodyTemperature.class);

	private final Component title;
	
	BodyTemperature(Component title) {
		this.title = title;
	}
	
	public Component title() {
		return title;
	}
}
