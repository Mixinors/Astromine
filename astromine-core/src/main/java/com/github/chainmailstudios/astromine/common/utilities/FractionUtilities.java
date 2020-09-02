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

import com.github.chainmailstudios.astromine.common.volume.fraction.Fraction;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class FractionUtilities {
	public static Fraction fromJson(JsonElement element) {
		if (element instanceof JsonPrimitive)
			return new Fraction(element.getAsLong(), 1);
		if (element instanceof JsonObject)
			return ParsingUtilities.fromJson(element, Fraction.class);
		throw new IllegalArgumentException("Invalid fraction: " + element.toString());
	}

	public static Fraction fromPacket(PacketByteBuf buf) {
		long numerator = buf.readLong();
		long denominator = buf.readLong();
		return new Fraction(numerator, denominator);
	}

	public static Fraction fromFloating(double d) {
		String s = Fraction.DECIMAL_FORMAT.format(d);
		int digitsDec = s.length() - 1 - s.indexOf('.');

		int denom = 1;
		for (int i = 0; i < digitsDec; i++) {
			d *= 10;
			denom *= 10;
		}
		int num = (int) Math.round(d);

		return Fraction.simplify(Fraction.of(num, denom));
	}

	public static void toPacket(PacketByteBuf buf, Fraction fraction) {
		buf.writeLong(fraction.getNumerator());
		buf.writeLong(fraction.getDenominator());
	}
}
