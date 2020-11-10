package com.github.chainmailstudios.astromine.common.utilities;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketByteBuf;

public class StringUtilities {
    /** Serializes the given {@link String} to a {@link ByteBuf}. */
    public static void toPacket(PacketByteBuf buffer, String string) {
        buffer.writeString(string);
    }

    /** Deserializes a {@link String} from a {@link ByteBuf}. */
    public static String fromPacket(PacketByteBuf buffer) {
        return buffer.readString();
    }

    /** Serializes the given {@link String} to a {@link JsonElement}. */
    public static JsonElement toJson(String string) {
        return new JsonPrimitive(string);
    }

    /** Deserializes an {@link String} from a {@link JsonElement}. */
    public static String fromJson(JsonElement json) {
        return AstromineCommon.GSON.fromJson(json, String.class);
    }
}