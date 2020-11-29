package com.github.chainmailstudios.astromine.common.utilities;

import com.github.chainmailstudios.astromine.AstromineCommon;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class FloatUtilities {
    /** Serializes the given float to a {@link ByteBuf}. */
    public static void toPacket(FriendlyByteBuf buffer, float number) {
        buffer.writeFloat(number);
    }

    /** Deserializes a float from a {@link ByteBuf}. */
    public static double fromPacket(FriendlyByteBuf buffer) {
        return buffer.readFloat();
    }

    /** Serializes the given float to a {@link JsonElement}. */
    public static JsonElement toJson(float number) {
        return new JsonPrimitive(number);
    }

    /** Deserializes a float from a {@link JsonElement}. */
    public static float fromJson(JsonElement json) {
        return AstromineCommon.GSON.fromJson(json, Float.class);
    }
}
