package com.github.chainmailstudios.astromine.common.utilities;

import net.minecraft.network.PacketByteBuf;

import com.github.chainmailstudios.astromine.AstromineCommon;
import io.netty.buffer.ByteBuf;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class FloatUtilities {
    /** Serializes the given {@link ParsingUtilities.Float} to a {@link ByteBuf}. */
    public static void toPacket(PacketByteBuf buffer, float number) {
        buffer.writeFloat(number);
    }

    /** Deserializes a {@link ParsingUtilities.Float} from a {@link ByteBuf}. */
    public static double fromPacket(PacketByteBuf buffer) {
        return buffer.readFloat();
    }

    /** Serializes the given {@link ParsingUtilities.Float} to a {@link JsonElement}. */
    public static JsonElement toJson(float number) {
        return new JsonPrimitive(number);
    }

    /** Deserializes an {@link ParsingUtilities.Float} from a {@link JsonElement}. */
    public static float fromJson(JsonElement json) {
        return AstromineCommon.GSON.fromJson(json, Float.class);
    }
}
