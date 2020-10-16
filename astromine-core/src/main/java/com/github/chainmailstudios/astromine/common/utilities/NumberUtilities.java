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

import com.github.chainmailstudios.astromine.common.volume.fraction.Fraction;

public class NumberUtilities {
	public static String shorten(double value, String unit) {
		if (value < 1000) {
			return Fraction.DECIMAL_FORMAT.format(value);
		}
		int exponent = (int) (Math.log(value) / Math.log(1000));
		String[] units = new String[]{ "k" + unit, "M" + unit, "G" + unit, "T" + unit, "P" + unit, "E" + unit, "Z" + unit, "Y" + unit };
		return String.format("%.1f%s", value / Math.pow(1000, exponent), exponent - 1 > units.length - 1 ? "?" : units[exponent - 1]);
	}
}
