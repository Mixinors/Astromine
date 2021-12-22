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
		String base = singular;
		String suffix = "s";
		char lastChar = singular.charAt(singular.length() - 1);
		char secondLastChar = singular.charAt(singular.length() - 2);
		if (lastChar == 's' || lastChar == 'z' || lastChar == 'x' || (lastChar == 'h' && (secondLastChar == 'c' || secondLastChar == 's'))) {
			suffix = "e" + suffix;
		} else {
			String substring = singular.substring(0, singular.length() - 2);
			if (lastChar == 'f' && secondLastChar != 'f') {
				base = substring;
				suffix = "ves";
			} else if (lastChar == 'y' && !isVowel(secondLastChar)) {
				base = substring;
				suffix = "ies";
			}
		}
		String plural = base + suffix;
		AMCommon.LOGGER.debug("Pluralizing " + singular + " produced " + plural);
		return plural;
	}

	public static boolean isVowel(char c) {
		char lc = Character.toLowerCase(c);
		return lc == 'a' || lc == 'e' || lc == 'i' || lc == 'o' || lc == 'u';
	}
}
