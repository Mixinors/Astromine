package com.github.mixinors.astromine.common.util.extra;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Codecs {
    public static final Codec<Text> LITERAL_TEXT = Codec.STRING.xmap(LiteralText::new, Text::asString);
	public static final Codec<Text> TRANSLATABLE_TEXT = Codec.STRING.xmap(TranslatableText::new, (t) -> ((TranslatableText) t).getKey());
	
	public static <T extends Enum<T>> Codec<T> createEnumCodec(Class<T> enumClass) {
		return Codec.STRING.xmap(value -> Enum.valueOf(enumClass, value.toUpperCase()), value -> value.name().toLowerCase());
	}
}
