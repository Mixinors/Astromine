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

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

@SuppressWarnings("unstable")
public class TextUtils {
	public static final char ENERGY_UNIT = 'E';
	public static final char FLUID_UNIT = 'd';
	
	public static MutableComponent getAstromine() {
		return Component.literal("Astromine").withStyle(ChatFormatting.BLUE, ChatFormatting.ITALIC);
	}
	
	public static MutableComponent getEnergy() {
		return Component.translatable("text.astromine.energy");
	}
	
	public static MutableComponent getEnergy(long amount, long capacity) {
		return getEnergyAmount(amount).append(Component.literal(" / ").withStyle(ChatFormatting.GRAY)).append(getEnergyAmount(capacity));
	}
	
	public static MutableComponent getRatio(int progress, int limit) {
		return Component.literal((int) ((float) progress / (float) limit * 100) + "%");
	}
	
	public static MutableComponent getAmount(long amount, char unit) {
		return Component.literal(formatAmount(amount, unit));
	}
	
	public static MutableComponent getEnergyAmount(long amount) {
		return getAmount(amount, ENERGY_UNIT).withStyle(ChatFormatting.GREEN);
	}
	
	private static String formatAmount(long amount, char unit) {
		var absolute = Math.abs((double) amount);
		var suffix = "";
		var value = (double) amount;
		
		if (absolute >= 1_000_000_000D) {
			value /= 1_000_000_000D;
			suffix = "G";
		} else if (absolute >= 1_000_000D) {
			value /= 1_000_000D;
			suffix = "M";
		} else if (absolute >= 1_000D) {
			value /= 1_000D;
			suffix = "k";
		}
		
		if (suffix.isEmpty()) {
			return amount + String.valueOf(unit);
		}
		
		return String.format(java.util.Locale.ROOT, "%.1f%s%c", value, suffix, unit);
	}
}
