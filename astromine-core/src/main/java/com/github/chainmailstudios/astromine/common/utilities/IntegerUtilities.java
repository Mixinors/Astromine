package com.github.chainmailstudios.astromine.common.utilities;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketByteBuf;

public class IntegerUtilities {
    /** Serializes the given {@link Integer} to a {@link ByteBuf}. */
    public static void toPacket(PacketByteBuf buffer, int number) {
        buffer.writeInt(number);
    }

    /** Deserializes an {@link Integer} from a {@link ByteBuf}. */
    public static int fromPacket(PacketByteBuf buffer) {
        return buffer.readInt();
    }

    /** Serializes the given {@link Integer} to a {@link JsonElement}. */
    public static JsonElement toJson(int number) {
        return new JsonPrimitive(number);
    }

    /** Deserializes an {@link Integer} from a {@link JsonElement}. */
    public static int fromJson(JsonElement json) {
        return AstromineCommon.GSON.fromJson(json, Integer.class);
    }
}