package com.github.chainmailstudios.astromine.common.utilities;

import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import com.github.chainmailstudios.astromine.common.volume.base.Volume;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.List;
import java.util.regex.Pattern;

public class TextUtilities {
    private static final Pattern HEX_PATTERN = Pattern.compile("^<#([a-fA-F0-9]{6})>$");

    /** Returns the "Astromine" text, formatted with {@link ChatFormatting#BLUE}
     * and {@link ChatFormatting#ITALIC}. */
    public static Component getAstromine() {
        return new TextComponent("Astromine").withStyle(ChatFormatting.BLUE, ChatFormatting.ITALIC);
    }

    /** Returns the "Energy" text. */
    public static Component getEnergy() {
        return new TranslatableComponent("text.astromine.energy");
    }

    /** Returns the "16k/32k" text. */
    public static Component getVolume(Volume<?> volume) {
        return new TextComponent(NumberUtilities.shorten(volume.getAmount().doubleValue()) + "/" + NumberUtilities.shorten(volume.getSize().doubleValue())).withStyle(ChatFormatting.GRAY);
    }

    /** Returns the "Water" / "Lava" / "Hydrogen" / ... text. */
    public static Component getFluid(ResourceLocation fluidId) {
        return new TranslatableComponent(String.format("block.%s.%s", fluidId.getNamespace(), fluidId.getPath()));
    }

    /** Returns the "Water" / "Lava" / "Hydrogen" / "... text. */
    public static Component getFluid(Fluid fluid) {
        return getFluid(Registry.FLUID.getKey(fluid));
    }

    /** Returns the "astromine:oxygen" / "minecraft:stone" ... text. */
    public static Component getIdentifier(ResourceLocation identifier) {
        return new TextComponent(identifier.toString()).withStyle(ChatFormatting.DARK_GRAY);
    }

    /** Returns the "Astromine" / "TechReborn" / "Minecraft" ... text. */
    public static Component getMod(ResourceLocation identifier) {
        return new TextComponent(FabricLoader.getInstance().getModContainer(identifier.getNamespace()).get().getMetadata().getName()).withStyle(ChatFormatting.BLUE, ChatFormatting.ITALIC);
    }

    /** Returns the "75%" text. */
    public static Component getRatio(int progress, int limit) {
        return new TextComponent("" + (int) ((float) progress / (float) limit * 100) + "%");
    }
}
