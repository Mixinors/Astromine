/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
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

package com.github.chainmailstudios.astromine.common.utilities;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.volume.fraction.Fraction;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketByteBuf;

public class NumberUtilities {
	/**
	 * Shortens the given {@link Double}, appending the specified
	 * unit if necessary.
	 *
	 * < 1,000 has no suffix.
	 * > 1,000 has the "k" suffix.
	 * > 1,000,000 has the "M" suffix.
	 * > 1,000,000,000 has the "G" suffix.
	 * > 1,000,000,000,000 has the "T" suffix.
	 * > 1,000,000,000,000,000 has the "P" suffix.
	 * > 1,000,000,000,000,000,000 has the "E" suffix.
	 * > 1,000,000,000,000,000,000,000 has the "Z" suffix.
	 * > 1,000,000,000,000,000,000,000,000 has the "Y" suffix.
	 * > 1,000,000,000,000,000,000,000,000,000 has the "?" suffix.
	 */
	public static java.lang.String shorten(double value, java.lang.String unit) {
		if (value < 1000) {
			return Fraction.FORMAT.format(value);
		}
		int exponent = (int) (Math.log(value) / Math.log(1000));
		java.lang.String[] units = new java.lang.String[]{ "k" + unit, "M" + unit, "G" + unit, "T" + unit, "P" + unit, "E" + unit, "Z" + unit, "Y" + unit };
		return java.lang.String.format("%.1f%s", value / Math.pow(1000, exponent), exponent - 1 > units.length - 1 ? "?" : units[exponent - 1]);
	}
}
