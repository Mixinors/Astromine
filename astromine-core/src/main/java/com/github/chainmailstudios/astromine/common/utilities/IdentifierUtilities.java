package com.github.chainmailstudios.astromine.common.utilities;

import com.github.chainmailstudios.astromine.AstromineCommon;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class IdentifierUtilities {
    /** Serializes the given {@link ResourceLocation} to a {@link ByteBuf}. */
    public static void toPacket(FriendlyByteBuf buffer, ResourceLocation identifier) {
        buffer.writeResourceLocation(identifier);
    }

    /** Deserializes an {@link ResourceLocation} from a {@link ByteBuf}. */
    public static ResourceLocation fromPacket(FriendlyByteBuf buffer) {
        return buffer.readResourceLocation();
    }

    /** Serializes the given {@link ResourceLocation} to a {@link JsonElement}. */
    public static JsonElement toJson(ResourceLocation identifier) {
        return new JsonPrimitive(identifier.toString());
    }

    /** Deserializes an {@link ResourceLocation} from a {@link JsonElement}. */
    public static ResourceLocation fromJson(JsonElement json) {
        return AstromineCommon.GSON.fromJson(json, ResourceLocation.class);
    }
}
