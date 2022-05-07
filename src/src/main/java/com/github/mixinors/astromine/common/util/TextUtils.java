/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
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

import dev.architectury.platform.Platform;
import dev.vini2003.hammer.core.api.common.util.NumberUtils;
import team.reborn.energy.api.EnergyStorage;

import net.minecraft.fluid.Fluid;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;

@SuppressWarnings("unstable")
public class TextUtils {
	public static final char ENERGY_UNIT = 'E';
	public static final char FLUID_UNIT = 'd';
	
	public static Text getAstromine() {
		return new LiteralText("Astromine").formatted(Formatting.BLUE, Formatting.ITALIC);
	}
	
	public static Text getEnergy() {
		return new TranslatableText("text.astromine.energy");
	}
	
	public static Text getEnergy(long amount, long capacity) {
		return getEnergyAmount(amount).append(new LiteralText(" / ").formatted(Formatting.GRAY)).append(getEnergyAmount(capacity));
	}

	public static Text getRatio(int progress, int limit) {
		return new LiteralText((int) ((float) progress / (float) limit * 100) + "%");
	}
	
	public static LiteralText getAmount(long amount, char unit) {
		return new LiteralText(NumberUtils.getPrettyShortenedString(amount, "" + unit));
	}

	public static MutableText getEnergyAmount(long amount) {
		return getAmount(amount, ENERGY_UNIT).formatted(Formatting.GREEN);
	}
}
