package com.github.mixinors.astromine.common.util.extra;

import com.mojang.serialization.Codec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;

public class Codecs {
    public static final Codec<Component> LITERAL_TEXT = Codec.STRING.xmap(Component::literal, Component::getString);
	public static final Codec<Component> TRANSLATABLE_TEXT = Codec.STRING.xmap(Component::translatable, (t) -> ((TranslatableContents) t.getContents()).getKey());
	
	public static <T extends Enum<T>> Codec<T> createEnumCodec(Class<T> enumClass) {
		return Codec.STRING.xmap(value -> Enum.valueOf(enumClass, value.toUpperCase()), value -> value.name().toLowerCase());
	}
}
