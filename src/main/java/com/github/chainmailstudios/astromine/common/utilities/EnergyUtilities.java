package com.github.chainmailstudios.astromine.common.utilities;

import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import team.reborn.energy.EnergyHandler;

import java.text.DecimalFormat;

public class EnergyUtilities {
	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("###");

	public static double fromJson(JsonElement element) {
		if (element instanceof JsonPrimitive)
			return element.getAsDouble();
		if (element instanceof JsonObject)
			return ParsingUtilities.fromJson(element, Fraction.class).doubleValue() * EnergyVolume.OLD_NEW_RATIO;
		throw new IllegalArgumentException("Invalid amount: " + element.toString());
	}

	public static double fromPacket(PacketByteBuf buf) {
		return buf.readDouble();
	}

	public static void toPacket(PacketByteBuf buf, double v) {
		buf.writeDouble(v);
	}

	public static boolean hasAvailable(EnergyHandler energyHandler, double v) {
		return energyHandler.getEnergy() + v <= energyHandler.getMaxStored();
	}

	public static String toDecimalString(double v) {
		return DECIMAL_FORMAT.format(v);
	}

	public static String toRoundingString(double v) {
		return String.valueOf((int) v);
	}

	public static MutableText simpleDisplay(double energy) {
		return new TranslatableText("text.astromine.tooltip.energy_value", toRoundingString(energy));
	}

	public static MutableText compoundDisplay(double energy, double maxEnergy) {
		return new TranslatableText("text.astromine.tooltip.compound_energy_value", toRoundingString(energy), toRoundingString(maxEnergy));
	}

	public static MutableText simpleDisplayColored(double energy) {
		return simpleDisplay(energy).formatted(Formatting.GRAY);
	}

	public static MutableText compoundDisplayColored(double energy, double maxEnergy) {
		return compoundDisplay(energy, maxEnergy).formatted(Formatting.GRAY);
	}
}
