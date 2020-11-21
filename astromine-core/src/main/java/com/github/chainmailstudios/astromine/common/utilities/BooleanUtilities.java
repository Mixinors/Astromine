package com.github.chainmailstudios.astromine.common.utilities;

import net.minecraft.network.PacketByteBuf;

import com.github.chainmailstudios.astromine.AstromineCommon;
import io.netty.buffer.ByteBuf;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class BooleanUtilities {
    /** Serializes the given {@link ParsingUtilities.Float.Long.Boolean} to a {@link ByteBuf}. */
    public static void toPacket(PacketByteBuf buffer, boolean value) {
        buffer.writeBoolean(value);
    }

    /** Deserializes a {@link ParsingUtilities.Float.Long.Boolean} from a {@link ByteBuf}. */
    public static boolean fromPacket(PacketByteBuf buffer) {
        return buffer.readBoolean();
    }

    /** Serializes the given {@link ParsingUtilities.Float.Long.Boolean} to a {@link JsonElement}. */
    public static JsonElement toJson(long number) {
        return new JsonPrimitive(number);
    }

    /** Deserializes an {@link ParsingUtilities.Float.Long.Boolean} from a {@link JsonElement}. */
    public static boolean fromJson(JsonElement json) {
        return AstromineCommon.GSON.fromJson(json, Boolean.class);
    }
}