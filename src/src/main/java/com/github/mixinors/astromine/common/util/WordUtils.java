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

import java.util.HashMap;
import java.util.Map;

import com.github.mixinors.astromine.AMCommon;

public class WordUtils {
	private static final Map<String, String> PLURALS_CACHE = new HashMap<>();

	public static String pluralize(String singular) {
		return PLURALS_CACHE.computeIfAbsent(singular, WordUtils::findPlural);
	}

	// tons of edge cases not covered but eh
	private static String findPlural(String singular) {
		var base = singular;
		var suffix = "s";
		var lastChar = singular.charAt(singular.length() - 1);
		var secondLastChar = singular.charAt(singular.length() - 2);
		if (lastChar == 's' || lastChar == 'z' || lastChar == 'x' || (lastChar == 'h' && (secondLastChar == 'c' || secondLastChar == 's'))) {
			suffix = "e" + suffix;
		} else {
			var substring = singular.substring(0, singular.length() - 2);
			if (lastChar == 'f' && secondLastChar != 'f') {
				base = substring;
				suffix = "ves";
			} else if (lastChar == 'y' && !isVowel(secondLastChar)) {
				base = substring;
				suffix = "ies";
			}
		}
		var plural = base + suffix;
		AMCommon.LOGGER.debug("Pluralizing " + singular + " produced " + plural);
		return plural;
	}

	public static boolean isVowel(char c) {
		var lc = Character.toLowerCase(c);
		return lc == 'a' || lc == 'e' || lc == 'i' || lc == 'o' || lc == 'u';
	}
}
