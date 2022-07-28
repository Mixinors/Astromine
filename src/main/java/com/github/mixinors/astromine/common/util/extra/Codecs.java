package com.github.mixinors.astromine.common.util.extra;

import com.mojang.serialization.Codec;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class Codecs {
    public static final Codec<Text> LITERAL_TEXT = Codec.STRING.xmap(LiteralText::new, Text::asString);
	public static final Codec<Text> TRANSLATABLE_TEXT = Codec.STRING.xmap(TranslatableText::new, (t) -> ((TranslatableText) t).getKey());
}
