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

package com.github.chainmailstudios.astromine.common.volume.energy;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.MathHelper;

import com.github.chainmailstudios.astromine.common.fraction.Fraction;

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

	/**
	 * Deserializes a Volume from a tag.
	 *
	 * @return a Volume
	 */
	public static EnergyVolume fromTag(CompoundTag tag) {
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

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = MathHelper.clamp(amount, 0, getMaxAmount());
		if (listener != null)
			listener.run();
	}

	public double getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(double maxAmount) {
		this.maxAmount = maxAmount;
		if (listener != null)
			listener.run();
	}

	public EnergyVolume copy() {
		return new EnergyVolume(getMaxAmount());
	}

	public CompoundTag toTag(CompoundTag tag) {
		tag.putDouble("amount", getAmount());
		tag.putDouble("maxAmount", getMaxAmount());
		return tag;
	}

	public boolean isEmpty() {
		return getAmount() <= 0.0;
	}

	public boolean isFull() {
		return getAmount() >= getMaxAmount();
	}

	public boolean hasAvailable(double produced) {
		return getMaxAmount() - getAmount() >= produced;
	}
}
