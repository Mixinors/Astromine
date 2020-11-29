package com.github.chainmailstudios.astromine.common.utilities;

import net.minecraft.network.FriendlyByteBuf;

import com.github.chainmailstudios.astromine.AstromineCommon;
import io.netty.buffer.ByteBuf;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class StringUtilities {
    /** Serializes the given {@link String} to a {@link ByteBuf}. */
    public static void toPacket(FriendlyByteBuf buffer, String string) {
        buffer.writeUtf(string);
    }

    /** Deserializes a {@link String} from a {@link ByteBuf}. */
    public static String fromPacket(FriendlyByteBuf buffer) {
        return buffer.readUtf();
    }

    /** Serializes the given {@link String} to a {@link JsonElement}. */
    public static JsonElement toJson(String string) {
        return new JsonPrimitive(string);
    }

    /** Deserializes a {@link String} from a {@link JsonElement}. */
    public static String fromJson(JsonElement json) {
        return AstromineCommon.GSON.fromJson(json, String.class);
    }
}