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

import java.text.DecimalFormat;

public class NumberUtils {
	public static final DecimalFormat FORMAT = new DecimalFormat("#0.00");

	private static final char[] SI_PREFIXES = new char[]{'k', 'M', 'G', 'T', 'P', 'E', 'Z', 'Y'};

	/**
	 * Shortens the given long, appending an SI prefix
	 * if necessary.
	 * <p>
	 * <= 999 has no SI prefix.
	 * >= 1,000 has the "k" prefix.
	 * >= 1,000,000 has the "M" prefix.
	 * >= 1,000,000,000 has the "G" prefix.
	 * >= 1,000,000,000,000 has the "T" prefix.
	 * >= 1,000,000,000,000,000 has the "P" prefix.
	 * >= 1,000,000,000,000,000,000 has the "E" prefix.
	 * >= 1,000,000,000,000,000,000,000 has the "Z" prefix.
	 * >= 1,000,000,000,000,000,000,000,000 has the "Y" prefix.
	 * >= 1,000,000,000,000,000,000,000,000,000 has the "∞" prefix.
	 */
	public static String shorten(long value) {
		boolean negative = false;
		if (value < 0) {
			negative = true;
			value = Math.abs(value);
		}

		if (value < 1000) {
			return (negative ? "-" : "") + value;
		}

		int exponent = 0;
		while (value >= 1000) {
			value /= 1000;
			++exponent;
		}

		return (negative ? "-" : "") + String.format("%d%s", value, exponent - 1 > SI_PREFIXES.length - 1 ? "∞" : SI_PREFIXES[exponent - 1]);
	}

	/**
	 * Shortens the given {@link double}, appending an SI prefix
	 * if necessary.
	 * <p>
	 * <= 999 has no SI prefix.
	 * >= 1,000 has the "k" prefix.
	 * >= 1,000,000 has the "M" prefix.
	 * >= 1,000,000,000 has the "G" prefix.
	 * >= 1,000,000,000,000 has the "T" prefix.
	 * >= 1,000,000,000,000,000 has the "P" prefix.
	 * >= 1,000,000,000,000,000,000 has the "E" prefix.
	 * >= 1,000,000,000,000,000,000,000 has the "Z" prefix.
	 * >= 1,000,000,000,000,000,000,000,000 has the "Y" prefix.
	 * >= 1,000,000,000,000,000,000,000,000,000 has the "∞" prefix.
	 */
	public static String shorten(double value) {
		if (value == Math.round(value)) return shorten((long) value);

		boolean negative = false;
		if (value < 0) {
			negative = true;
			value = Math.abs(value);
		}

		if (value < 1000) {
			return (negative ? "-" : "") + FORMAT.format(value);
		}

		int exponent = 0;
		while (value >= 1000) {
			value /= 1000;
			++exponent;
		}

		return (negative ? "-" : "") + String.format("%.1f%s", value, exponent - 1 > SI_PREFIXES.length - 1 ? "∞" : SI_PREFIXES[exponent - 1]);
	}

	/**
	 * Shortens the given long, appending an SI prefix
	 * if necessary, and a given unit.
	 * <p>
	 * <= 999 has no SI prefix.
	 * >= 1,000 has the "k" prefix.
	 * >= 1,000,000 has the "M" prefix.
	 * >= 1,000,000,000 has the "G" prefix.
	 * >= 1,000,000,000,000 has the "T" prefix.
	 * >= 1,000,000,000,000,000 has the "P" prefix.
	 * >= 1,000,000,000,000,000,000 has the "E" prefix.
	 * >= 1,000,000,000,000,000,000,000 has the "Z" prefix.
	 * >= 1,000,000,000,000,000,000,000,000 has the "Y" prefix.
	 * >= 1,000,000,000,000,000,000,000,000,000 has the "∞" prefix.
	 */
	public static String shorten(long value, char unit) {
		return shorten(value) + unit;
	}

	/**
	 * Shortens the given double, appending an SI prefix
	 * if necessary, and a given unit.
	 * <p>
	 * <= 999 has no SI prefix.
	 * >= 1,000 has the "k" prefix.
	 * >= 1,000,000 has the "M" prefix.
	 * >= 1,000,000,000 has the "G" prefix.
	 * >= 1,000,000,000,000 has the "T" prefix.
	 * >= 1,000,000,000,000,000 has the "P" prefix.
	 * >= 1,000,000,000,000,000,000 has the "E" prefix.
	 * >= 1,000,000,000,000,000,000,000 has the "Z" prefix.
	 * >= 1,000,000,000,000,000,000,000,000 has the "Y" prefix.
	 * >= 1,000,000,000,000,000,000,000,000,000 has the "∞" prefix.
	 */
	public static String shorten(double value, char unit) {
		return shorten(value) + unit;
	}
}
