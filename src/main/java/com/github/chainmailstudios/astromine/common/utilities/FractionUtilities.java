package com.github.chainmailstudios.astromine.common.utilities;

import net.minecraft.network.PacketByteBuf;

import com.github.chainmailstudios.astromine.common.fraction.Fraction;

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
	
	public static void toPacket(PacketByteBuf buf, Fraction fraction) {
		buf.writeLong(fraction.getNumerator());
		buf.writeLong(fraction.getDenominator());
	}
}
