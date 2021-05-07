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

package com.github.mixinors.astromine.common.utilities;

import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.fluid.Fluid;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import com.github.mixinors.astromine.common.volume.base.Volume;

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
        return new LiteralText(NumberUtilities.shorten(volume.getAmount().doubleValue(), "") + "/" + NumberUtilities.shorten(volume.getSize().doubleValue(), "")).formatted(Formatting.GRAY);
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

    /** Returns the "75%" text. */
    public static Text getRatio(int progress, int limit) {
        return new LiteralText("" + (int) ((float) progress / (float) limit * 100) + "%");
    }
}
