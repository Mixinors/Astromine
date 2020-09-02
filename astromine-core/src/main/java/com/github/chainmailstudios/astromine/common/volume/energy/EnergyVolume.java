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

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleEnergyInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleFluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.volume.base.Volume;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.common.volume.fraction.Fraction;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;

public class EnergyVolume extends Volume<Identifier, Double> {
	public static final Identifier ID = AstromineCommon.identifier("energy");

	public EnergyVolume(double amount, double size) {
		super(ID, amount, size);
	}

	public EnergyVolume(double amount, double size, Runnable runnable) {
		super(ID, amount, size, runnable);
	}

	@Override
	public <V extends Volume<Identifier, Double>> V into(V v, Double doubleA) {
		if (!(v instanceof EnergyVolume)) return (V) this;

		double amount = Math.min(v.getSize() - v.getAmount(), Math.min(getAmount(), doubleA));

		if (amount > 0.0D) {
			v.setAmount(v.getAmount() + amount);
			setAmount(getAmount() - amount);
		}

		return (V) this;
	}

	@Override
	public <V extends Volume<Identifier, Double>> V into(Double aDouble) {
		double amount = Math.min(getSize() - getAmount(), aDouble);

		setAmount(getAmount() + amount);

		return (V) this;
	}

	@Override
	public <V extends Volume<Identifier, Double>> V from(V v, Double doubleA) {
		if (!(v instanceof EnergyVolume)) return (V) this;

		v.into(this, doubleA);

		return (V) this;
	}

	@Override
	public <V extends Volume<Identifier, Double>> V from(Double aDouble) {
		double amount = Math.min(getAmount(), aDouble);

		setAmount(getAmount() - amount);

		return (V) this;
	}

	public static EnergyVolume empty() {
		return new EnergyVolume(0.0D, 0.0D);
	}

	public static EnergyVolume attached(SimpleEnergyInventoryComponent component) {
		return new EnergyVolume(0.0D, 0.0D, component::dispatchConsumers);
	}

	public static EnergyVolume of(double amount) {
		return new EnergyVolume(amount, Long.MAX_VALUE);
	}

	public static EnergyVolume of(double amount, double size) {
		return new EnergyVolume(amount, size);
	}

	@Override
	public CompoundTag toTag() {
		CompoundTag tag = new CompoundTag();
		tag.putDouble("amount", getAmount());
		tag.putDouble("size", getSize());
		return tag;
	}

	public static EnergyVolume fromTag(CompoundTag tag) {
		return of(tag.getDouble("amount"), tag.getDouble("size"));
	}

	@Override
	public <V extends Volume<Identifier, Double>> V copy() {
		return (V) of(getAmount(), getSize());
	}
}
