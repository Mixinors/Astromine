package com.github.chainmailstudios.astromine.common.utilities;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class IdentifierUtilities {
    /** Serializes the given {@link Identifier} to a {@link ByteBuf}. */
    public static void toPacket(PacketByteBuf buffer, Identifier identifier) {
        buffer.writeIdentifier(identifier);
    }

    /** Deserializes an {@link Identifier} from a {@link ByteBuf}. */
    public static Identifier fromPacket(PacketByteBuf buffer) {
        return buffer.readIdentifier();
    }

    /** Serializes the given {@link Identifier} to a {@link JsonElement}. */
    public static JsonElement toJson(Identifier identifier) {
        return new JsonPrimitive(identifier.toString());
    }

    /** Deserializes an {@link Identifier} from a {@link JsonElement}. */
    public static Identifier fromJson(JsonElement json) {
        return AstromineCommon.GSON.fromJson(json, Identifier.class);
    }
}
