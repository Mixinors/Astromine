package com.github.chainmailstudios.astromine.common.utilities;

import net.minecraft.network.PacketByteBuf;

import com.github.chainmailstudios.astromine.AstromineCommon;
import io.netty.buffer.ByteBuf;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class LongUtilities {
    /** Serializes the given long to a {@link ByteBuf}. */
    public static void toPacket(PacketByteBuf buffer, long number) {
        buffer.writeLong(number);
    }

    /** Deserializes a long from a {@link ByteBuf}. */
    public static long fromPacket(PacketByteBuf buffer) {
        return buffer.readLong();
    }

    /** Serializes the given long to a {@link JsonElement}. */
    public static JsonElement toJson(long number) {
        return new JsonPrimitive(number);
    }

    /** Deserializes a long from a {@link JsonElement}. */
    public static long fromJson(JsonElement json) {
        return AstromineCommon.GSON.fromJson(json, Long.class);
    }
}
