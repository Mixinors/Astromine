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

import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;

public class NumberUtilities {
	private static final String[] units = new String[]{ "k", "M", "G", "T", "P", "E", "Z", "Y" };

	/**
	 * Shortens the given {@link long}, appending the specified
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
	 * > 1,000,000,000,000,000,000,000,000,000 has the "∞" suffix.
	 */
	public static String shorten(long value, String unit) {
		if (value < 1000000) {
			return String.valueOf(value);
		}

		int exponent = 0;
		while (value >= 1000) {
			value /= 1000;
			++exponent;
		}

		return String.format("%d%s", value, exponent - 1 > units.length - 1 ? "∞" : units[exponent - 1] + unit);
	}

	/**
	 * Shortens the given {@link double}, appending the specified
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
	 * > 1,000,000,000,000,000,000,000,000,000 has the "∞" suffix.
	 */
	public static String shorten(double value, String unit) {
		if (value == Math.round(value)) return shorten((long) value, unit);
		if (value < 1000000) {
			return FluidVolume.FORMAT.format(value);
		}

		int exponent = 0;
		while (value >= 1000) {
			value /= 1000;
			++exponent;
		}

		return String.format("%.1f%s", value, exponent - 1 > units.length - 1 ? "∞" : units[exponent - 1] + unit);
	}
}
