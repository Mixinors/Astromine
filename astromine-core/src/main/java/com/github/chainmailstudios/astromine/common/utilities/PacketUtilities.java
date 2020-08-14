/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
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

package com.github.chainmailstudios.astromine.common.utilities;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.AstromineCommon;
import org.apache.logging.log4j.Level;

import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class PacketUtilities {
	private static final ImmutableMap<Class<?>, BiConsumer<PacketByteBuf, Object>> WRITERS = ImmutableMap.<Class<?>, BiConsumer<PacketByteBuf, Object>> builder().put(Integer.class, (buffer, object) -> buffer.writeInt((Integer) object)).put(Float.class, (buffer, object) -> buffer
		.writeFloat((Float) object)).put(Double.class, (buffer, object) -> buffer.writeDouble((Double) object)).put(String.class, (buffer, object) -> buffer.writeString((String) object)).put(Identifier.class, (buffer, object) -> buffer.writeIdentifier((Identifier) object)).put(
			Enum.class, (buffer, object) -> buffer.writeEnumConstant((Enum<?>) object)).build();

	private static final ImmutableMap<Class<?>, BiFunction<PacketByteBuf, Class<?>, ?>> READERS = ImmutableMap.<Class<?>, BiFunction<PacketByteBuf, Class<?>, ?>> builder().put(Integer.class, (buffer, object) -> buffer.readInt()).put(Float.class, (buffer, object) -> buffer
		.readFloat()).put(Double.class, (buffer, object) -> buffer.readDouble()).put(String.class, (buffer, object) -> buffer.readString()).put(Identifier.class, (buffer, object) -> buffer.readIdentifier()).put(Enum.class, (buffer, object) -> buffer.readEnumConstant(
			((Enum<?>) (object.getEnumConstants()[0])).getClass())).build();

	public static void toPacket(PacketByteBuf buffer, Object object) {
		writeObject(buffer, object);
	}

	public static void writeObject(PacketByteBuf buffer, Object object) {
		if (WRITERS.containsKey(object.getClass())) {
			WRITERS.get(object.getClass()).accept(buffer, object);
		} else {
			AstromineCommon.LOGGER.log(Level.ERROR, "Packet serialization failed: " + object.getClass().getName() + " is not a valid class!");
		}
	}

	public static void toPacket(PacketByteBuf buffer, HashMap<?, ?> map) {
		writeObject(buffer, map.size());
		for (Map.Entry<?, ?> entry : map.entrySet()) {
			writeObject(buffer, entry.getKey());
			writeObject(buffer, entry.getValue());
		}
	}

	public static <K> K fromPacket(PacketByteBuf buffer, Class<K> k) {
		return (K) readObject(buffer, k);
	}

	public static Object readObject(PacketByteBuf buffer, Class<?> object) {
		if (READERS.containsKey(object)) {
			return READERS.get(object).apply(buffer, object);
		} else {
			AstromineCommon.LOGGER.log(Level.ERROR, "Packet serialization failed: " + object.getClass() + " is not a valid class!");
			return null;
		}
	}

	public static <K, V> HashMap<K, V> fromPacket(PacketByteBuf buffer, Class<K> k, Class<V> v) {
		HashMap<K, V> map = new HashMap<K, V>();
		int size = buffer.readInt();
		for (int i = 0; i < size; ++i) {
			map.put((K) readObject(buffer, k), (V) readObject(buffer, v));
		}
		return map;
	}
}
