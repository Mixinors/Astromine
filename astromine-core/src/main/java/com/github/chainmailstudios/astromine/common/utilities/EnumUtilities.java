package com.github.chainmailstudios.astromine.common.utilities;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;

public class EnumUtilities {
    /** Serializes the given {@link Enum} to a {@link ByteBuf}. */
    public static void toPacket(FriendlyByteBuf buffer, Enum<?> _enum) {
        buffer.writeEnum(_enum);
    }

    /** Deserializes an {@link Enum} from a {@link ByteBuf}. */
    public static <T extends Enum<T>> Enum<T> fromPacket(FriendlyByteBuf buffer, Class<T> _class) {
        return buffer.readEnum(_class);
    }
}
