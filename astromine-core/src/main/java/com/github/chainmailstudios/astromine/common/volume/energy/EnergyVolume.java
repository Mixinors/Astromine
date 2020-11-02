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
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleEnergyComponent;
import com.github.chainmailstudios.astromine.common.volume.base.Volume;

import com.google.common.base.Objects;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class EnergyVolume extends Volume<Identifier, Double> {
	public static final Identifier ID = AstromineCommon.identifier("energy");

	protected EnergyVolume(double amount, double size) {
		super(ID, amount, size);
	}

	protected EnergyVolume(double amount, double size, Runnable runnable) {
		super(ID, amount, size, runnable);
	}

	public static EnergyVolume empty() {
		return new EnergyVolume(0.0D, 0.0D);
	}

	public static EnergyVolume of(SimpleEnergyComponent component) {
		return new EnergyVolume(0.0D, 0.0D, component::updateListeners);
	}

	public static EnergyVolume of(double size, SimpleEnergyComponent component) {
		return new EnergyVolume(0.0D, size, component::updateListeners);
	}

	public static EnergyVolume of(double amount) {
		return new EnergyVolume(amount, Long.MAX_VALUE);
	}

	public static EnergyVolume of(double amount, double size) {
		return new EnergyVolume(amount, size);
	}

	public static EnergyVolume fromTag(CompoundTag tag) {
		return of(tag.getDouble("amount"), tag.getDouble("size"));
	}

	public static EnergyVolume fromJson(JsonElement jsonElement) {
		if (!jsonElement.isJsonObject())
			return EnergyVolume.of(jsonElement.getAsDouble());
		else {
			JsonObject jsonObject = jsonElement.getAsJsonObject();

			if (!jsonObject.has("amount")) {
				return null;
			} else {
				if (!jsonObject.has("size")) {
					return EnergyVolume.of(jsonObject.get("amount").getAsDouble());
				} else {
					return EnergyVolume.of(jsonObject.get("amount").getAsDouble(), jsonObject.get("size").getAsDouble());
				}
			}
		}
	}

	public static EnergyVolume fromPacket(PacketByteBuf buffer) {
		if (!buffer.readBoolean()) {
			return empty();
		} else {
			return EnergyVolume.of(buffer.readDouble(), buffer.readDouble());
		}
	}

	@Override
	public <V extends Volume<Identifier, Double>> V add(V v, Double doubleA) {
		if (!(v instanceof EnergyVolume))
			return (V) this;

		double amount = Math.min(v.getSize() - v.getAmount(), Math.min(getAmount(), doubleA));

		if (amount > 0.0D) {
			v.setAmount(v.getAmount() + amount);
			setAmount(getAmount() - amount);
		}

		return (V) this;
	}

	@Override
	public <V extends Volume<Identifier, Double>> V add(Double aDouble) {
		double amount = Math.min(getSize() - getAmount(), aDouble);

		setAmount(getAmount() + amount);

		return (V) this;
	}

	@Override
	public <V extends Volume<Identifier, Double>> V minus(Double aDouble) {
		double amount = Math.min(getAmount(), aDouble);

		setAmount(getAmount() - amount);

		return (V) this;
	}

	@Override
	public <V extends Volume<Identifier, Double>> V moveFrom(V v, Double doubleA) {
		if (!(v instanceof EnergyVolume))
			return (V) this;

		v.add(this, doubleA);

		return (V) this;
	}

	@Override
	public <V extends Volume<Identifier, Double>> V moveFrom(V v) {
		return moveFrom(v, v.getAmount());
	}

	@Override
	public <V extends Volume<Identifier, Double>> V copy() {
		return (V) of(getAmount(), getSize());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(super.hashCode(), getAmount(), getSize());
	}

	@Override
	public String toString() {
		return getAmount() + " / " + getSize();
	}

	@Override
	public CompoundTag toTag() {
		CompoundTag tag = new CompoundTag();
		tag.putDouble("amount", getAmount());
		tag.putDouble("size", getSize());
		return tag;
	}

	public PacketByteBuf toPacket(PacketByteBuf buffer) {
		if (this.isEmpty()) {
			buffer.writeBoolean(false);
		} else {
			buffer.writeBoolean(true);

			buffer.writeDouble(getAmount());
			buffer.writeDouble(getSize());
		}

		return buffer;
	}
}
