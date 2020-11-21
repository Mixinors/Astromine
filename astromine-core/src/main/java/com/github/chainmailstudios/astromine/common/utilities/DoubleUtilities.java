package com.github.chainmailstudios.astromine.common.utilities;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketByteBuf;

public class DoubleUtilities {
    /** Serializes the given {@link Double} to a {@link ByteBuf}. */
    public static void toPacket(PacketByteBuf buffer, double number) {
        buffer.writeDouble(number);
    }

    /** Deserializes a {@link Double} from a {@link ByteBuf}. */
    public static double fromPacket(PacketByteBuf buffer) {
        return buffer.readDouble();
    }

    /** Serializes the given {@link Double} to a {@link JsonElement}. */
    public static JsonElement toJson(double number) {
        return new JsonPrimitive(number);
    }

    /** Deserializes an {@link Double} from a {@link JsonElement}. */
    public static double fromJson(JsonElement json) {
        return AstromineCommon.GSON.fromJson(json, Double.class);
    }
}