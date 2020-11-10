package com.github.chainmailstudios.astromine.common.utilities;

import com.github.chainmailstudios.astromine.common.volume.base.Volume;
import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.fluid.Fluid;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class TextUtilities {
    /** Returns the "Astromine" text, formatted with {@link Formatting#BLUE}
     * and {@link Formatting#ITALIC}. */
    public static Text getAstromine() {
        return new LiteralText("Astromine").formatted(Formatting.BLUE, Formatting.ITALIC);
    }

    /** Returns the "Energy" text. */
    public static Text getEnergy() {
        return new TranslatableText("text.astromine.energy");
    }

    /** Returns the "16k/32k" text. */
    public static Text getVolume(Volume<?> volume) {
        return new LiteralText(NumberUtilities.shorten(volume.getAmount().longValue(), "") + "/" + NumberUtilities.shorten(volume.getSize().longValue(), "")).formatted(Formatting.GRAY);
    }

    /** Returns the "Water" / "Lava" / "Hydrogen" / ... text. */
    public static Text getFluid(Identifier fluidId) {
        return new TranslatableText(String.format("block.%s.%s", fluidId.getNamespace(), fluidId.getPath()));
    }

    /** Returns the "Water" / "Lava" / "Hydrogen" / "... text. */
    public static Text getFluid(Fluid fluid) {
        return getFluid(Registry.FLUID.getId(fluid));
    }

    /** Returns the "astromine:oxygen" / "minecraft:stone" ... text. */
    public static Text getIdentifier(Identifier identifier) {
        return new LiteralText(identifier.toString()).formatted(Formatting.DARK_GRAY);
    }

    /** Returns the "Astromine" / "TechReborn" / "Minecraft" ... text. */
    public static Text getMod(Identifier identifier) {
        return new LiteralText(FabricLoader.getInstance().getModContainer(identifier.getNamespace()).get().getMetadata().getName()).formatted(Formatting.BLUE, Formatting.ITALIC);
    }

    /** Returns the "75 of 200" text. */
    public static Text getRatio(int progress, int limit) {
        return new TranslatableText("text.astromine.tooltip.fractional_bar", progress, limit);
    }
}
