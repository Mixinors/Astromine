package com.github.chainmailstudios.astromine.common.utilities;

import com.github.chainmailstudios.astromine.AstromineCommon;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class BooleanUtilities {
    /** Serializes the given boolean to a {@link ByteBuf}. */
    public static void toPacket(FriendlyByteBuf buffer, boolean value) {
        buffer.writeBoolean(value);
    }

    /** Deserializes a boolean from a {@link ByteBuf}. */
    public static boolean fromPacket(FriendlyByteBuf buffer) {
        return buffer.readBoolean();
    }

    /** Serializes the given boolean to a {@link JsonElement}. */
    public static JsonElement toJson(long number) {
        return new JsonPrimitive(number);
    }

    /** Deserializes a boolean from a {@link JsonElement}. */
    public static boolean fromJson(JsonElement json) {
        return AstromineCommon.GSON.fromJson(json, Boolean.class);
    }
}