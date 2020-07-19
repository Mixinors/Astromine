package com.github.chainmailstudios.astromine.common.volume.energy;

import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.MathHelper;

public class EnergyVolume {
	public static final double OLD_NEW_RATIO = 1000;
	private Runnable listener;
	private double amount;
	private double maxAmount = Double.MAX_VALUE;

	public EnergyVolume() {
		this(0);
	}

	public EnergyVolume(double amount) {
		this.amount = amount;
	}

	public EnergyVolume(double amount, Runnable listener) {
		this(amount);
		this.listener = listener;
	}

	public static EnergyVolume empty() {
		return new EnergyVolume();
	}

	public static EnergyVolume of(double amount) {
		return new EnergyVolume(amount);
	}

	public void setAmount(double amount) {
		this.amount = MathHelper.clamp(amount, 0, getMaxAmount());
		if (listener != null) listener.run();
	}

	public double getAmount() {
		return amount;
	}

	public void setMaxAmount(double maxAmount) {
		this.maxAmount = maxAmount;
		if (listener != null) listener.run();
	}

	public double getMaxAmount() {
		return maxAmount;
	}

	/**
	 * Deserializes a Volume from a tag.
	 *
	 * @return a Volume
	 */
	public static EnergyVolume fromTag(CompoundTag tag) {
		// TODO: Null checks.

		EnergyVolume energyVolume = new EnergyVolume();

		if (!tag.contains("fraction")) {
			if (tag.contains("amount"))
				energyVolume.amount = tag.getDouble("amount");
		} else {
			energyVolume.amount = Fraction.fromTag(tag.getCompound("fraction")).doubleValue() * OLD_NEW_RATIO;
		}

		if (!tag.contains("size")) {
			if (tag.contains("maxAmount"))
				energyVolume.maxAmount = tag.getDouble("maxAmount");
		} else {
			energyVolume.maxAmount = Fraction.fromTag(tag.getCompound("size")).doubleValue() * OLD_NEW_RATIO;
		}

		return energyVolume;
	}

	public EnergyVolume copy() {
		return new EnergyVolume(amount);
	}

	public CompoundTag toTag(CompoundTag tag) {
		tag.putDouble("amount", amount);
		tag.putDouble("maxAmount", maxAmount);
		return tag;
	}

	public boolean isEmpty() {
		return amount <= 0.0;
	}

	public boolean isFull() {
		return amount >= maxAmount;
	}

	public boolean hasAvailable(double produced) {
		return maxAmount - amount >= produced;
	}
}
