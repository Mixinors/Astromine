package com.github.mixinors.astromine.common.recipe.result;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public record ItemResult(ItemVariant variant, int amount) {
	public ItemStack toStack() {
		return variant.toStack(amount);
	}

	public boolean equalsAndFitsIn(SingleSlotStorage<ItemVariant> storage) {
		return storage.getCapacity() - storage.getAmount() >= amount && storage.getResource().equals(variant);
	}

	public static JsonObject toJson(ItemResult result) {
		var jsonObject = new JsonObject();

		jsonObject.addProperty("item", Registry.ITEM.getId(result.variant.getItem()).toString());
		jsonObject.addProperty("amount", result.amount);

		return jsonObject;
	}

	public static ItemResult fromJson(JsonElement jsonElement) {
		if (!jsonElement.isJsonObject()) {
			var variantId = new Identifier(jsonElement.getAsString());
			var variantItem = Registry.ITEM.get(variantId);

			var variant = ItemVariant.of(variantItem);

			return new ItemResult(variant, 1);
		} else {
			var jsonObject = jsonElement.getAsJsonObject();

			var variantId = new Identifier(jsonObject.get("item").getAsString());
			var variantItem = Registry.ITEM.get(variantId);

			var variant = ItemVariant.of(variantItem);

			var variantAmount = jsonObject.get("amount").getAsInt();

			return new ItemResult(variant, variantAmount);
		}
	}

	public static void toPacket(PacketByteBuf buf, ItemResult result) {
		buf.writeString(Registry.ITEM.getId(result.variant.getItem()).toString());
		buf.writeLong(result.amount);
	}

	public static ItemResult fromPacket(PacketByteBuf buf) {
		var variantId = new Identifier(buf.readString());
		var variantItem = Registry.ITEM.get(variantId);

		var variant = ItemVariant.of(variantItem);

		var variantAmount = buf.readInt();

		return new ItemResult(variant, variantAmount);
	}
}
