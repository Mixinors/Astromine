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

import net.minecraft.util.math.Direction;

import team.reborn.energy.EnergyHandler;

public class WrappedEnergyVolume extends EnergyVolume {
	private final EnergyHandler storage;

	public WrappedEnergyVolume(EnergyHandler storage, Direction direction) {
		this(storage.side(direction));
	}

	public WrappedEnergyVolume(EnergyHandler storage) {
		super(storage.getEnergy(), storage.getMaxStored());
		this.storage = storage;
	}

	public static WrappedEnergyVolume of(EnergyHandler storage) {
		return new WrappedEnergyVolume(storage);
	}

	public static WrappedEnergyVolume of(EnergyHandler storage, Direction direction) {
		return new WrappedEnergyVolume(storage, direction);
	}

	@Override
	public Double getAmount() {
		return storage.getEnergy();
	}

	@Override
	public void setAmount(Double aDouble) {
		storage.set(aDouble);
	}

	@Override
	public Double getSize() {
		return storage.getMaxStored();
	}

	@Override
	public void setSize(Double s) {
		// Not feasible to implement.
	}

	public double getMaximumInput() {
		return storage.getMaxInput();
	}

	public double getMaximumOutput() {
		return storage.getMaxOutput();
	}
}
