package com.github.chainmailstudios.astromine.common.utilities;

import com.github.chainmailstudios.astromine.common.recipe.BetterIngredient;
import com.google.gson.JsonElement;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;

public class IngredientUtilities {
	public static Ingredient fromJson(JsonElement jsonElement) {
		return Ingredient.fromJson(jsonElement);
	}

	public static BetterIngredient fromBetterJson(JsonElement jsonElement) {
		return BetterIngredient.fromJson(jsonElement);
	}

	public static Ingredient fromPacket(PacketByteBuf buffer) {
		return Ingredient.fromPacket(buffer);
	}

	public static void toPacket(PacketByteBuf buffer, Ingredient ingredient) {
		ingredient.write(buffer);
	}

	public static BetterIngredient fromBetterPacket(PacketByteBuf buffer) {
		return BetterIngredient.fromPacket(buffer);
	}

	public static void toBetterPacket(PacketByteBuf buffer, BetterIngredient ingredient) {
		ingredient.write(buffer);
	}
}
