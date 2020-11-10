package com.github.chainmailstudios.astromine.common.utilities;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketByteBuf;

public class EnumUtilities {
    /** Serializes the given {@link Enum} to a {@link ByteBuf}. */
    public static void toPacket(PacketByteBuf buffer, Enum<?> _enum) {
        buffer.writeEnumConstant(_enum);
    }

    /** Deserializes an {@link Enum} from a {@link ByteBuf}. */
    public static <T extends Enum<T>> Enum<T> fromPacket(PacketByteBuf buffer, Class<T> _class) {
        return buffer.readEnumConstant(_class);
    }
}
