package com.github.chainmailstudios.astromine.common.utilities;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketByteBuf;

public class LongUtilities {
    /** Serializes the given {@link ParsingUtilities.Float.Long} to a {@link ByteBuf}. */
    public static void toPacket(PacketByteBuf buffer, long number) {
        buffer.writeLong(number);
    }

    /** Deserializes a {@link ParsingUtilities.Float.Long} from a {@link ByteBuf}. */
    public static long fromPacket(PacketByteBuf buffer) {
        return buffer.readLong();
    }

    /** Serializes the given {@link ParsingUtilities.Float.Long} to a {@link JsonElement}. */
    public static JsonElement toJson(long number) {
        return new JsonPrimitive(number);
    }

    /** Deserializes an {@link ParsingUtilities.Float.Long} from a {@link JsonElement}. */
    public static long fromJson(JsonElement json) {
        return AstromineCommon.GSON.fromJson(json, Long.class);
    }
}
