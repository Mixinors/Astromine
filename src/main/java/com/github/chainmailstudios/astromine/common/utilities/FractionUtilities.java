package com.github.chainmailstudios.astromine.common.utilities;

import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.network.PacketByteBuf;

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
