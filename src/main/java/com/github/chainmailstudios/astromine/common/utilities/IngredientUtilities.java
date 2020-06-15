package com.github.chainmailstudios.astromine.common.utilities;

import com.google.common.collect.Iterables;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;

public class IngredientUtilities {
    public static Ingredient fromJson(JsonElement jsonElement) {
        List<ItemStack> arrayStacks = new ArrayList<>();
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        jsonArray.forEach((element) -> {
            JsonObject inputObject = element.getAsJsonObject();
            if (inputObject.size() == 1) {
                arrayStacks.add(new ItemStack(Registry.ITEM.getOrEmpty(new Identifier(inputObject.get("item").getAsString())).get()));
            } else {
                arrayStacks.add(new ItemStack(Registry.ITEM.getOrEmpty(new Identifier(inputObject.get("item").getAsString())).get(), inputObject.get("count").getAsInt()));
            }
        });
        return Ingredient.ofStacks(Iterables.toArray(arrayStacks, ItemStack.class));
    }

    public static Ingredient fromPacket(PacketByteBuf buffer) {
        return Ingredient.fromPacket(buffer);
    }

    public static void toPacket(PacketByteBuf buffer, Ingredient ingredient) {
        ingredient.write(buffer);
    }
}
