package com.github.chainmailstudios.astromine.common.utilities;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;

import com.google.gson.JsonElement;

public class IngredientUtilities {
	public static Ingredient fromJson(JsonElement jsonElement) {
		return Ingredient.fromJson(jsonElement);
	}

	public static Ingredient fromPacket(PacketByteBuf buffer) {
		return Ingredient.fromPacket(buffer);
	}

	public static void toPacket(PacketByteBuf buffer, Ingredient ingredient) {
		ingredient.write(buffer);
	}
}
