package com.github.chainmailstudios.astromine.common.utilities;

import net.minecraft.network.PacketByteBuf;

import com.github.chainmailstudios.astromine.AstromineCommon;
import io.netty.buffer.ByteBuf;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class DoubleUtilities {
    /** Serializes the given double to a {@link ByteBuf}. */
    public static void toPacket(PacketByteBuf buffer, double number) {
        buffer.writeDouble(number);
    }

    /** Deserializes a double from a {@link ByteBuf}. */
    public static double fromPacket(PacketByteBuf buffer) {
        return buffer.readDouble();
    }

    /** Serializes the given double to a {@link JsonElement}. */
    public static JsonElement toJson(double number) {
        return new JsonPrimitive(number);
    }

    /** Deserializes a double from a {@link JsonElement}. */
    public static double fromJson(JsonElement json) {
        return AstromineCommon.GSON.fromJson(json, Double.class);
    }
}