package com.github.chainmailstudios.astromine.common.utilities;

public class HashUtilities {
	public static String firstOf(Object object) {
		return String.valueOf(String.valueOf(object.hashCode()).charAt(0));
	}
}
