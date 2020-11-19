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

package com.github.chainmailstudios.astromine.common.volume.fraction;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;

import com.google.common.base.Objects;
import net.minecraft.network.PacketByteBuf;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class representing a fractional value, whose {@link #numerator}
 * and {@link #denominator} and represented through {@link Long}.
 *
 * There is no overflow protection. Values above {@link #MAX_VALUE},
 * and below {@link #MIN_VALUE} will result in incorrect behavior.
 *
 * Serialization and deserialization methods are provided for:
 * - {@link String) - through {@link #toString()}} and {@link #fromString(String)}.
 * - {@link CompoundTag} - through {@link #toTag()} and {@link #fromTag(CompoundTag)}.
 * - {@link JsonElement} - through {@link #toJson()} and {@link #fromJson(JsonElement)}.
 * - {@link ByteBuf} - through {@link #toPacket(PacketByteBuf)} and {@link #fromPacket(PacketByteBuf)}.
 */
public final class Fraction extends Number implements Comparable<Fraction> {
	public static final DecimalFormat FORMAT = new DecimalFormat("###.###");

	public static final Pattern FULL_PATTERN = Pattern.compile("(\\+?\\-?[0-9]+).+?(\\+?\\-?[0-9]+)\\/?([0-9]+)");

	public static final Pattern PARTIAL_PATTERN = Pattern.compile("(\\+?\\-?[0-9]+)\\/?([0-9]+)");

	public static final Fraction EMPTY = of(0, 0);

	public static final Fraction MIN_VALUE = of(1, Long.MAX_VALUE);

	public static final Fraction MAX_VALUE = of(Long.MAX_VALUE, 1);

	public static final Fraction BUCKET = of(1, 1);

	public static final Fraction BOTTLE = of(1, 3);

	private final long numerator;
	private final long denominator;

	/** Instantiates a fraction with the given values. */
	private Fraction(long numerator, long denominator) {
		this.numerator = numerator;
		this.denominator = Math.max(1, denominator);
	}

	/** Returns the numerator of this fraction. */
	public long getNumerator() {
		return this.numerator;
	}

	/** Returns the denominator of this fraction. */
	public long getDenominator() {
		return this.denominator;
	}

	/** Instantiates a fraction from a whole value. */
	public static Fraction of(long whole) {
		return new Fraction(whole, 1);
	}

	/** Instantiates a fraction with the given values. */
	public static Fraction of(long numerator, long denominator) {
		return new Fraction(numerator, denominator);
	}

	/** Instantiates a fraction with a whole and the given values. */
	public static Fraction of(long whole, long numerator, long denominator) {
		return new Fraction(numerator + whole * denominator, denominator);
	}

	/** Instantiates a fraction from a decimal value. */
	public static Fraction ofDecimal(double d) {
		String s = FORMAT.format(d);
		int digitsDec = s.length() - 1 - s.indexOf('.');

		int denom = 1;
		for (int i = 0; i < digitsDec; i++) {
			d *= 10;
			denom *= 10;
		}
		int num = (int) Math.round(d);

		return simplify(of(num, denom));
	}

	/** Adds the given fraction to this fraction, returning the result. */
	public Fraction add(Fraction fractionB) {
		return add(this, fractionB);
	}

	/** Subtracts the given fraction from this fraction, returning the result. */
	public Fraction subtract(Fraction fractionB) {
		return subtract(this, fractionB);
	}

	/** Divides this fraction by the given fraction, returning the result. */
	public Fraction divide(Fraction fractionB) {
		return divide(this, fractionB);
	}

	/** Multiplies this fraction by the given fraction, returning the result. */
	public Fraction multiply(Fraction fractionB) {
		return multiply(this, fractionB);
	}

	/** Inverts this fraction, returning the result. */
	public Fraction inverse() {
		return inverse(this);
	}

	/** Simplifies this fraction, returning the result. */
	public Fraction simplify() {
		return simplify(this);
	}

	/** Returns the smallest between this fraction and the given fraction. */
	public Fraction minimum(Fraction fractionB) {
		return minimum(this, fractionB);
	}

	/** Returns the biggest between this fraction and the given fraction. */
	public Fraction maximum(Fraction fractionB) {
		return maximum(this, fractionB);
	}

	/** Returns a copy of this fraction. */
	public Fraction copy() {
		return of(this.numerator, this.denominator);
	}

	/** Adds the given fractions, returning the result. */
	public static Fraction add(Fraction fractionA, Fraction fractionB) {
		long denominator = lowestCommonDenominator(fractionA.denominator, fractionB.denominator);

		return of(fractionA.numerator * (denominator / fractionA.denominator) + fractionB.numerator * (denominator / fractionB.denominator), denominator);
	}

	/** Subtracts the given fractions, returning the result. */
	public static Fraction subtract(Fraction fractionA, Fraction fractionB) {
		long denominator = lowestCommonDenominator(fractionA.denominator, fractionB.denominator);

		return of(fractionA.numerator * (denominator / fractionA.denominator) - fractionB.numerator * (denominator / fractionB.denominator), denominator);
	}

	/** Divides the given fractions, returning the result. */
	public static Fraction divide(Fraction fractionA, Fraction fractionB) {
		return multiply(fractionA, inverse(fractionB));
	}

	/** Multiplies the given fractions, returning the result. */
	public static Fraction multiply(Fraction fractionA, Fraction fractionB) {
		return of(fractionA.numerator * fractionB.numerator, fractionA.denominator * fractionB.denominator);
	}

	/** Inverts the given fraction, returning the result. */
	public static Fraction inverse(Fraction fraction) {
		return of(fraction.denominator, fraction.numerator);
	}

	/** Simplifies the given fraction. */
	public static Fraction simplify(Fraction fraction) {
		if (fraction.numerator == 0)
			return of(0, 1);
		if ((fraction.numerator <= 0 && fraction.denominator >= 0) || (fraction.numerator >= 0 && fraction.denominator <= 0)) {
			return fraction;
		}

		long divisor = greatestCommonDivisor(fraction.numerator, fraction.denominator);

		return of(fraction.numerator / divisor, fraction.denominator / divisor);
	}

	/** Returns the smallest of the given values. */
	public static Fraction minimum(Fraction fractionA, Fraction fractionB) {
		return (fractionA.smallerThan(fractionB) ? fractionA : fractionB);
	}

	/** Returns the biggest of the given values. */
	public static Fraction maximum(Fraction fractionA, Fraction fractionB) {
		return (fractionA.biggerThan(fractionB) ? fractionA : fractionB);
	}

	/** Returns the lowest common denominator of the given values. */
	private static long lowestCommonDenominator(long a, long b) {
		return a == b ? a : a == 1 ? b : b == 1 ? a : (a * b) / greatestCommonDivisor(a, b);
	}

	/** Returns the greatest common divisor of the given values, through Stein's Algorithm. */
	private static long greatestCommonDivisor(long a, long b) {
		long shift = 0;

		if (a == 0) {
			return b;
		}
		if (b == 0) {
			return a;
		}

		while (((a | b) & 1) == 0) {
			++shift;
			a >>= 1;
			b >>= 1;
		}

		while ((a & 1) == 0) {
			a >>= 1;
		}

		do {
			while ((b & 1) == 0) {
				b >>= 1;
			}
			if (a > b) {
				long t = b;
				b = a;
				a = t;
			}

			b -= a;
		} while (b != 0);

		return a << shift;
	}

	/** Asserts whether this fraction is smaller than the given fraction, or not. */
	public boolean smallerThan(Fraction fraction) {
		return !this.biggerThan(fraction);
	}

	/** Asserts whether this fraction is bigger than the given fraction, or not. */
	public boolean biggerThan(Fraction fraction) {
		if (this.denominator == fraction.denominator) {
			return this.numerator > fraction.numerator;
		}

		long denominator = lowestCommonDenominator(this.denominator, fraction.denominator);

		Fraction fA = of(this.numerator * (this.denominator * (denominator / this.denominator)), this.denominator);
		Fraction fB = of(fraction.numerator * (fraction.denominator * (denominator / fraction.denominator)), fraction.denominator);

		return fA.longValue() > fB.longValue();
	}

	/** Asserts whether this fraction is smaller or equal than the given fraction, or not. */
	public boolean smallerOrEqualThan(Fraction fraction) {
		return smallerThan(fraction) || equals(fraction);
	}

	/** Asserts whether this fraction is bigger or equal than the given fraction, or not. */
	public boolean biggerOrEqualThan(Fraction fraction) {
		return biggerThan(fraction) || equals(fraction);
	}

	/** Compares this fraction to the given fraction. */
	@Override
	public int compareTo(Fraction fraction) {
		return this.biggerThan(fraction) ? 1 : this.smallerThan(fraction) ? -1 : 0;
	}

	/** Asserts the equality of the objects. */
	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof Fraction)) {
			return false;
		}

		Fraction target = (Fraction) object;

		Fraction simplifiedA = simplify(this);
		Fraction simplifiedB = simplify(target);

		return simplifiedA.numerator == simplifiedB.numerator && simplifiedA.denominator == simplifiedB.denominator;
	}

	/** Returns the hash for this volume. */
	@Override
	public int hashCode() {
		return Objects.hashCode(this.numerator, this.denominator);
	}

	/** Returns the value of this fraction as an {@link Integer}. */
	@Override
	public int intValue() {
		return (int) this.longValue();
	}

	/** Returns the value of this fraction as a {@link Long}. */
	@Override
	public long longValue() {
		return this.numerator / this.denominator;
	}

	/** Returns the value of this fraction as a {@link Float}. */
	@Override
	public float floatValue() {
		return (float) this.numerator / (float) this.denominator;
	}

	/** Returns the value of this fraction as a {@link Double}. */
	@Override
	public double doubleValue() {
		return (double) this.numerator / (double) this.denominator;
	}

	/** Deserializes a fraction from a {@link String}. */
	public static Fraction fromString(String string) {
		Matcher fullMatcher = FULL_PATTERN.matcher(string);

		if (fullMatcher.groupCount() == 3) {
			return of(Long.parseLong(fullMatcher.group(0)), Long.parseLong(fullMatcher.group(1)), Long.parseLong(fullMatcher.group(2)));
		} else {
			Matcher partialMatcher = PARTIAL_PATTERN.matcher(string);

			if (partialMatcher.groupCount() == 2) {
				return of(Long.parseLong(fullMatcher.group(0)), Long.parseLong(fullMatcher.group(1)));
			} else {
				return null;
			}
		}
	}

	/** Serializes this fraction to a {@link String}. */
	@Override
	public String toString() {
		return String.format("%s / %s", numerator, denominator);
	}

	/** Deserializes a fraction from a {@link CompoundTag}. */
	public static Fraction fromTag(CompoundTag tag) {
		long[] values = tag.getLongArray("values");

		if (values.length != 2)
			values = new long[]{ 0, 0 };

		return of(values[0], values[1]);
	}

	/** Serializes this fraction to a {@link CompoundTag}. */
	public CompoundTag toTag() {
		CompoundTag tag = new CompoundTag();

		tag.putLongArray("values", new long[]{ this.numerator, this.denominator });

		return tag;
	}

	/** Deserializes a fraction from a {@link CompoundTag}. */
	public static Fraction fromJson(JsonElement element) {
		if (element instanceof JsonPrimitive)
			return of(element.getAsLong(), 1);
		if (element instanceof JsonObject)
			return of(element.getAsJsonObject().get("numerator").getAsLong(), element.getAsJsonObject().get("denominator").getAsLong());
		throw new IllegalArgumentException("Invalid fraction: " + element.toString());
	}

	/** Serializes this fraction to a {@link JsonElement}. */
	public JsonElement toJson() {
		JsonObject object = new JsonObject();
		object.addProperty("numerator", numerator);
		object.addProperty("denominator", denominator);
		return object;
	}

	/** Deserializes a fraction from a {@link ByteBuf}. */
	public static Fraction fromPacket(PacketByteBuf buffer) {
		long numerator = buffer.readLong();
		long denominator = buffer.readLong();
		return of(numerator, denominator);
	}

	/** Serializes this fraction to a {@link ByteBuf}. */
	public void toPacket(PacketByteBuf buffer) {
		buffer.writeLong(numerator);
		buffer.writeLong(denominator);
	}
}
