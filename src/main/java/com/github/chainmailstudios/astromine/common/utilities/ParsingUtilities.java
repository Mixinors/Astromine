package com.github.chainmailstudios.astromine.common.utilities;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.google.gson.JsonElement;

public class ParsingUtilities {
	public static <T> T fromJson(JsonElement json, Class<T> classOfT) {
		return AstromineCommon.GSON.fromJson(json, classOfT);
	}
}
