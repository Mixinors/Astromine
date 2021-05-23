/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.mixinors.astromine.common.util;

import com.github.mixinors.astromine.common.volume.energy.EnergyVolume;
import com.github.mixinors.astromine.common.volume.fluid.FluidVolume;
import me.shedaniel.architectury.platform.Platform;

import net.minecraft.fluid.Fluid;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import com.github.mixinors.astromine.common.volume.base.Volume;

public class TextUtils {
    /** Returns the "Astromine" text, formatted with {@link Formatting#BLUE}
     * and {@link Formatting#ITALIC}. */
    public static Text getAstromine() {
        return new LiteralText("Astromine").formatted(Formatting.BLUE, Formatting.ITALIC);
    }

    /** Returns the "Energy" text. */
    public static Text getEnergy() {
        return new TranslatableText("text.astromine.energy");
    }

    /** Returns the "16k / 32k" text. */
    public static Text getVolume(Volume<?> volume) {
        String unit = volume instanceof EnergyVolume ? "E" : volume instanceof FluidVolume ? "U" : "";
        return new LiteralText(NumberUtils.shorten(volume.getAmount().doubleValue(), unit) + " / " + NumberUtils.shorten(volume.getSize().doubleValue(), unit)).formatted(Formatting.GRAY);
    }
    
    /** Returns the "16kE / 32kE" text. */
    public static Text getEnergyVolume(EnergyVolume volume) {
        return new LiteralText(NumberUtils.shorten(volume.getAmount(), "E")).formatted(Formatting.GREEN).append(new LiteralText(" / ").formatted(Formatting.GRAY)).append(new LiteralText(NumberUtils.shorten(volume.getSize(), "E")).formatted(Formatting.GREEN));
    }
    
    /** Returns the "16kU / 32kU" text. */
    public static Text getFluidVolume(FluidVolume volume) {
        var style = Style.EMPTY.withColor(TextColor.fromRgb(FluidUtils.getColor(ClientUtils.getPlayer(), volume.getFluid())));
        return new LiteralText(NumberUtils.shorten(volume.getAmount(), "U")).fillStyle(style).append(new LiteralText(" / ").formatted(Formatting.GRAY)).append(new LiteralText(NumberUtils.shorten(volume.getSize(), "U")).fillStyle(style));
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
        return new LiteralText(Platform.getMod(identifier.getNamespace()).getName()).formatted(Formatting.BLUE, Formatting.ITALIC);
    }

    /** Returns the "75%" text. */
    public static Text getRatio(int progress, int limit) {
        return new LiteralText("" + (int) ((float) progress / (float) limit * 100.0F) + "%");
    }
}
