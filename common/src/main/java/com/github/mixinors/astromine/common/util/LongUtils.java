/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.mixinors.astromine.common.util;

import net.minecraft.network.PacketByteBuf;

import com.github.mixinors.astromine.AMCommon;
import io.netty.buffer.ByteBuf;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class LongUtils {
    /** Serializes the given long to a {@link ByteBuf}. */
    public static void toPacket(PacketByteBuf buf, long val) {
        buf.writeLong(val);
    }

    /** Deserializes a long from a {@link ByteBuf}. */
    public static long fromPacket(PacketByteBuf buf) {
        return buf.readLong();
    }

    /** Serializes the given long to a {@link JsonElement}. */
    public static JsonElement toJson(long val) {
        return new JsonPrimitive(val);
    }

    /** Deserializes a long from a {@link JsonElement}. */
    public static long fromJson(JsonElement json) {
        return AMCommon.GSON.fromJson(json, Long.class);
    }
}
