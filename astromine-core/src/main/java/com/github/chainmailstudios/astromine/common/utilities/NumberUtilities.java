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

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.volume.fraction.Fraction;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class NumberUtilities {
	/**
	 * Shortens the given {@link Double}, appending the specified
	 * unit if necessary.
	 *
	 * < 1,000 has no suffix.
	 * > 1,000 has the "k" suffix.
	 * > 1,000,000 has the "M" suffix.
	 * > 1,000,000,000 has the "G" suffix.
	 * > 1,000,000,000,000 has the "T" suffix.
	 * > 1,000,000,000,000,000 has the "P" suffix.
	 * > 1,000,000,000,000,000,000 has the "E" suffix.
	 * > 1,000,000,000,000,000,000,000 has the "Z" suffix.
	 * > 1,000,000,000,000,000,000,000,000 has the "Y" suffix.
	 * > 1,000,000,000,000,000,000,000,000,000 has the "?" suffix.
	 */
	public static java.lang.String shorten(double value, java.lang.String unit) {
		if (value < 1000) {
			return Fraction.FORMAT.format(value);
		}
		int exponent = (int) (Math.log(value) / Math.log(1000));
		java.lang.String[] units = new java.lang.String[]{ "k" + unit, "M" + unit, "G" + unit, "T" + unit, "P" + unit, "E" + unit, "Z" + unit, "Y" + unit };
		return java.lang.String.format("%.1f%s", value / Math.pow(1000, exponent), exponent - 1 > units.length - 1 ? "?" : units[exponent - 1]);
	}

	public static class Integer {
		/** Serializes the given {@link Integer} to a {@link ByteBuf}. */
		public void toPacket(PacketByteBuf buffer, int number) {
			buffer.writeInt(number);
		}

		/** Deserializes an {@link Integer} from a {@link ByteBuf}. */
		public int fromPacket(PacketByteBuf buffer) {
			return buffer.readInt();
		}
		
		/** Serializes the given {@link Integer} to a {@link JsonElement}. */
		public JsonElement toJson(int number) {
			return new JsonPrimitive(number);
		}
		
		/** Deserializes an {@link Integer} from a {@link JsonElement}. */
		public int fromJson(JsonElement json) {
			return AstromineCommon.GSON.fromJson(json, java.lang.Integer.class);
		}
	}

	public static class Double {
		/** Serializes the given {@link Double} to a {@link ByteBuf}. */
		public void toPacket(PacketByteBuf buffer, double number) {
			buffer.writeDouble(number);
		}

		/** Deserializes a {@link Double} from a {@link ByteBuf}. */
		public double fromPacket(PacketByteBuf buffer) {
			return buffer.readDouble();
		}

		/** Serializes the given {@link Double} to a {@link JsonElement}. */
		public JsonElement toJson(double number) {
			return new JsonPrimitive(number);
		}

		/** Deserializes an {@link Double} from a {@link JsonElement}. */
		public double fromJson(JsonElement json) {
			return AstromineCommon.GSON.fromJson(json, java.lang.Double.class);
		}
	}

	public static class Float {
		/** Serializes the given {@link Float} to a {@link ByteBuf}. */
		public void toPacket(PacketByteBuf buffer, float number) {
			buffer.writeFloat(number);
		}

		/** Deserializes a {@link Float} from a {@link ByteBuf}. */
		public double fromPacket(PacketByteBuf buffer) {
			return buffer.readFloat();
		}

		/** Serializes the given {@link Float} to a {@link JsonElement}. */
		public JsonElement toJson(float number) {
			return new JsonPrimitive(number);
		}

		/** Deserializes an {@link Float} from a {@link JsonElement}. */
		public float fromJson(JsonElement json) {
			return AstromineCommon.GSON.fromJson(json, java.lang.Float.class);
		}
	}

	public static class Long {
		/** Serializes the given {@link Long} to a {@link ByteBuf}. */
		public void toPacket(PacketByteBuf buffer, long number) {
			buffer.writeLong(number);
		}

		/** Deserializes a {@link Long} from a {@link ByteBuf}. */
		public long fromPacket(PacketByteBuf buffer) {
			return buffer.readLong();
		}

		/** Serializes the given {@link Long} to a {@link JsonElement}. */
		public JsonElement toJson(long number) {
			return new JsonPrimitive(number);
		}

		/** Deserializes an {@link Long} from a {@link JsonElement}. */
		public long fromJson(JsonElement json) {
			return AstromineCommon.GSON.fromJson(json, java.lang.Long.class);
		}
	}

	public static class Boolean {
		/** Serializes the given {@link Boolean} to a {@link ByteBuf}. */
		public void toPacket(PacketByteBuf buffer, boolean value) {
			buffer.writeBoolean(value);
		}

		/** Deserializes a {@link Boolean} from a {@link ByteBuf}. */
		public boolean fromPacket(PacketByteBuf buffer) {
			return buffer.readBoolean();
		}

		/** Serializes the given {@link Boolean} to a {@link JsonElement}. */
		public JsonElement toJson(long number) {
			return new JsonPrimitive(number);
		}

		/** Deserializes an {@link Boolean} from a {@link JsonElement}. */
		public boolean fromJson(JsonElement json) {
			return AstromineCommon.GSON.fromJson(json, java.lang.Boolean.class);
		}
	}

	public static class String {
		/** Serializes the given {@link String} to a {@link ByteBuf}. */
		public void toPacket(PacketByteBuf buffer, java.lang.String string) {
			buffer.writeString(string);
		}

		/** Deserializes a {@link String} from a {@link ByteBuf}. */
		public java.lang.String fromPacket(PacketByteBuf buffer) {
			return buffer.readString();
		}

		/** Serializes the given {@link String} to a {@link JsonElement}. */
		public JsonElement toJson(java.lang.String string) {
			return new JsonPrimitive(string);
		}

		/** Deserializes an {@link String} from a {@link JsonElement}. */
		public java.lang.String fromJson(JsonElement json) {
			return AstromineCommon.GSON.fromJson(json, java.lang.String.class);
		}
	}

	public static class Identifier {
		/** Serializes the given {@link Identifier} to a {@link ByteBuf}. */
		public void toPacket(PacketByteBuf buffer, net.minecraft.util.Identifier identifier) {
			buffer.writeIdentifier(identifier);
		}

		/** Deserializes an {@link Identifier} from a {@link ByteBuf}. */
		public net.minecraft.util.Identifier fromPacket(PacketByteBuf buffer) {
			return buffer.readIdentifier();
		}

		/** Serializes the given {@link Identifier} to a {@link JsonElement}. */
		public JsonElement toJson(net.minecraft.util.Identifier identifier) {
			return new JsonPrimitive(identifier.toString());
		}

		/** Deserializes an {@link Identifier} from a {@link JsonElement}. */
		public net.minecraft.util.Identifier fromJson(JsonElement json) {
			return AstromineCommon.GSON.fromJson(json, net.minecraft.util.Identifier.class);
		}
	}

	public static class Enum {
		/** Serializes the given {@link Enum} to a {@link ByteBuf}. */
		public void toPacket(PacketByteBuf buffer, java.lang.Enum<?> _enum) {
			buffer.writeEnumConstant(_enum);
		}

		/** Deserializes an {@link Enum} from a {@link ByteBuf}. */
		public <T extends java.lang.Enum<T>> java.lang.Enum<T> fromPacket(PacketByteBuf buffer, Class<T> _class) {
			return buffer.readEnumConstant(_class);
		}
	}
}
