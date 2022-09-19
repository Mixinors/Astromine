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

import dev.vini2003.hammer.core.api.common.util.NumberUtil;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

@SuppressWarnings("unstable")
public class TextUtils {
	public static final char ENERGY_UNIT = 'E';
	public static final char FLUID_UNIT = 'd';
	
	public static MutableText getAstromine() {
		return Text.literal("Astromine").formatted(Formatting.BLUE, Formatting.ITALIC);
	}
	
	public static MutableText getEnergy() {
		return Text.translatable("text.astromine.energy");
	}
	
	public static MutableText getEnergy(long amount, long capacity) {
		return getEnergyAmount(amount).append(Text.literal(" / ").formatted(Formatting.GRAY)).append(getEnergyAmount(capacity));
	}
	
	public static MutableText getRatio(int progress, int limit) {
		return Text.literal((int) ((float) progress / (float) limit * 100) + "%");
	}
	
	public static MutableText getAmount(long amount, char unit) {
		return Text.literal(NumberUtil.getPrettyShortenedString(amount, "" + unit));
	}
	
	public static MutableText getEnergyAmount(long amount) {
		return getAmount(amount, ENERGY_UNIT).formatted(Formatting.GREEN);
	}
}
